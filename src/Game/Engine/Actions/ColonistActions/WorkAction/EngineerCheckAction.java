package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Buildings.EngineeringHub;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Buildings.Projects.Progress;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Career.Job.BuildJob;
import Game.Engine.Colonist.Memory.Search.ClassSearch;
import Game.Engine.Inventory.Delivery;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.SearchTile;
import Game.Engine.Map.Tile;

import java.util.List;

public class EngineerCheckAction extends WorkAction {

    public EngineerCheckAction(ActionManager colonist) {
        super("Check EngHub", colonist);
    }

    @Override
    public boolean execute() {
        BuildJob job = (BuildJob) colonist.getJob();
        BuildingProject topProject = job.getTopProj();

        // If assigned to a building-phase project, head to build site
        if (topProject != null && topProject.getProgress() == Progress.BUILDING) {
            setDes(topProject.getTargetTile());
            if (colonistam.getCurrentTile() == topProject.getTargetTile()) {
                boolean done = topProject.doWork();
                colonist.setStatus("Building " + topProject.getName()
                        + " (" + topProject.getWorkProgress() + "%)");
                if (done) finishProject(topProject, getHub());
            }
            return false;
        }

        // If project is done, clean up
        if (topProject != null && topProject.getProgress() == Progress.DONE) {
            finishProject(topProject, getHub());
            return false;
        }

        // All other states require being at the hub
        EngineeringHub engHub = getHub();
        if (engHub == null) {
            // Not at hub yet — set destination to hub tile
            Tile hubTile = colonistam.getFirstTile(colonist.getAssignedBuilding());
            setDes(hubTile);
            return false;
        }

        // At hub
        setDes(colonistam.getCurrentTile()); // stay here

        if (!engHub.hasProjects()) return false;

        if (topProject != null) {
            setName("hey a project!!!!!!!!");
            checkProj(topProject, engHub);
            return false;
        }

        // Haul for any collecting project even if unassigned
        BuildingProject collecting = engHub.getHighPriorityProject(Progress.COLLECTING);
        if (collecting != null) {
            setName("collectin");
            haulMaterials(collecting, engHub);
            return false;
        }

        // Claim a recruiting project
        BuildingProject recruiting = engHub.getHighPriorityProject(Progress.RECRUITING);
        if (recruiting != null) {
            assignToProject(recruiting, job);
        }

        return false;
    }

    @Override
    public void updateQueue() {

    }

    /** Returns the EngineeringHub if the colonist is currently standing on it, else null. */
    private EngineeringHub getHub() {
        if (colonistam.getCurrentTile() == null) return null;
        if (colonistam.getCurrentTile().getBuilding() instanceof EngineeringHub hub) return hub;
        return null;
    }

    private void checkProj(BuildingProject project, EngineeringHub engHub) {
        switch (project.getProgress()) {
            case COLLECTING -> haulMaterials(project, engHub);
            case RECRUITING -> colonist.setStatus("Waiting for project to start: " + project.getName());
            case BUILDING   -> {
                setDes(project.getTargetTile());
                if (colonistam.getCurrentTile() == project.getTargetTile()) {
                    boolean done = project.doWork();
                    colonist.setStatus("Building " + project.getName()
                            + " (" + project.getWorkProgress() + "%)");
                    if (done) finishProject(project, engHub);
                }
            }
            case DONE -> finishProject(project, engHub);
        }
    }

    private void haulMaterials(BuildingProject project, EngineeringHub engHub) {
        // Already carrying a delivery — head to hub to deposit
        Delivery pending = colonist.getInventory().getDeliveries().stream()
                .filter(d -> d.getDestination() == project.getPartition())
                .findFirst().orElse(null);

        if (pending != null) {
            setDes(colonistam.getFirstTile(engHub));
            if (colonistam.getCurrentTile().getBuilding() == engHub) {
                for (ItemStack stack : pending.getItems()) {
                    project.depositMaterial(stack.getItem(), stack.getQuantity());
                }
                colonist.getInventory().removeDelivery(pending);
                pending.getSource().releaseTransportClaim();
                if (project.isFullyStocked()) {
                    engHub.advanceProject(project, Progress.RECRUITING);
                }
            }
            return;
        }

        // Nothing carrying — claim and search
        List<ItemStack> needed = project.getStillNeeded();
        if (needed.isEmpty()) return;

        Item item = needed.get(0).getItem();
        Class<? extends Item> itemClass = item.getClass();
        long currentTick = colonistam.getTime().tick();

        // Check cooldown — give up until reassigned
        if (colonistam.getMemory().isClassOnCooldown(itemClass, currentTick)) {
            colonist.setStatus("Waiting — recent search for " + item.getName() + " failed");
            return;
        }

        ItemStack claimed = project.claimMaterial(itemClass);
        if (claimed == null) return; // another builder already claimed this

        // Check memory for a known location first
        Tile known = colonistam.getMemory().recallClassLocation(itemClass);
        if (known != null) {
            // We remember where this resource was — go straight there
            setDes(known);
            Delivery delivery = new Delivery(
                    List.of(new ItemStack(item, claimed.getQuantity())),
                    null, project.getPartition()
            );
            colonist.getInventory().addDelivery(delivery);
        } else {
            // No memory — create a SearchTile to find it
            SearchTile searchTile = new SearchTile(new ClassSearch(needed.get(0).getQuantity(),itemClass));
            setDes(searchTile);
            colonist.setStatus("Searching for " + item.getName() + " for " + project.getName());
        }
    }
    private boolean assignToProject(BuildingProject project, BuildJob job) {
        if (project.addBuilder(colonist)) {
            job.addProject(project);
            colonist.setStatus("Assigned to " + project.getName());
            return true;
        }
        return false;
    }

    private void finishProject(BuildingProject project, EngineeringHub engHub) {
        ((BuildJob) colonist.getJob()).removeProject(project);
        if (engHub != null) engHub.completeProject(project);
        colonist.setStatus("Completed " + project.getName());
    }
}