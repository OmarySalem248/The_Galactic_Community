package Game.Engine.Actions.ColonistActions.WorkAction;

import Game.Engine.Buildings.EngineeringHub;
import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Buildings.Projects.Progress;
import Game.Engine.Colonist.ActionManager;
import Game.Engine.Colonist.Career.Job.BuildJob;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;
import Game.Engine.Buildings.BuildingType;
import Game.Engine.Inventory.Delivery;

import java.util.List;

public class EngineerCheckAction extends WorkAction {

    public EngineerCheckAction(ActionManager colonist) {
        super("Check EngHub", colonist);
    }

    @Override
    public boolean execute() {
        BuildJob job = (BuildJob) colonist.getJob();

        if (!(colonistam.getCurrentTile().getBuilding() instanceof EngineeringHub engHub)) {
            return true;
        }

        if (!engHub.hasProjects()) return false;

        // Handle existing assigned project first
        BuildingProject topProject = job.getTopProj();
        if (topProject != null) {
            checkProj(topProject, engHub);
            return false;
        }

        // Try to haul materials for any COLLECTING project even if not assigned
        BuildingProject collecting = engHub.getHighPriorityProject(Progress.COLLECTING);
        if (collecting != null) {
            haulMaterials(collecting, engHub);
            return false;
        }

        // Try to claim a RECRUITING project
        BuildingProject recruiting = engHub.getHighPriorityProject(Progress.RECRUITING);
        if (recruiting != null) {
            assignToProject(recruiting, job);
        }

        return false;
    }

    /** Handle an already-assigned project based on its current progress state. */
    private void checkProj(BuildingProject project, EngineeringHub engHub) {
        switch (project.getProgress()) {
            case COLLECTING -> haulMaterials(project, engHub);
            case RECRUITING -> {
                // Fully stocked, waiting at hub — nothing to do yet
                colonist.setStatus("Waiting for project to start: " + project.getName());
            }
            case BUILDING -> {
                // Head to build site and work
                if (colonistam.getCurrentTile() == project.getTargetTile()) {
                    boolean done = project.doWork();
                    colonist.setStatus("Building " + project.getName()
                            + " (" + project.getWorkProgress() + "%)");
                    if (done) {
                        finishProject(project, engHub);
                    }
                } else {
                    colonistam.setTileDestination(project.getTargetTile());
                }
            }
            case DONE -> finishProject(project, engHub);
        }
    }

    /** Haul still-needed materials from storage to the hub partition. */
    private void haulMaterials(BuildingProject project, EngineeringHub engHub) {
        // If already carrying a delivery for this project, deposit it into the partition
        Delivery pending = colonist.getInventory().getDeliveries().stream()
                .filter(d -> d.getDestination() == project.getPartition())
                .findFirst().orElse(null);

        if (pending != null) {
            if (colonistam.getCurrentTile().getBuilding() == engHub) {
                // At hub — deposit into partition
                for (ItemStack stack : pending.getItems()) {
                    project.getPartition().add(stack.getItem(), stack.getQuantity());
                }
                colonist.getInventory().removeDelivery(pending);
                pending.getSource().releaseTransportClaim();

                // Check if project is now fully stocked
                if (project.isFullyStocked()) {
                    engHub.advanceProject(project, Progress.RECRUITING);
                }
            } else {
                // Head to hub to deposit
                colonistam.setDeliveryTarget(engHub);
            }
            return;
        }

        // No pending delivery — claim materials and head to storage
        List<ItemStack> needed = project.getStillNeeded();
        if (needed.isEmpty()) return;

        ItemStack target = needed.get(0);
        ItemStack claimed = project.claimMaterial(target.getItem().getType());
        if (claimed == null) return; // already claimed by another builder

        colonist.setStatus("Hauling materials for " + project.getName());
        colonistam.searchFor(claimed.getItem().getType(), BuildingType.STORAGE);
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
        engHub.completeProject(project);
        colonist.setStatus("Completed " + project.getName());
    }
}