package Game.Engine.Buildings.Projects;

import Game.Engine.Buildings.Building;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class BuildingProject {
    private String name;
    private int requiredWork;

    private Inventory partition;

    private int priority;

    private Inventory requiredResources;
    private boolean completed;

    private Progress progress;
    private Building resultingBuilding;
    private int maxBuilders;

    private int remBuilders;

    private int workProg;
    private List<Colonist> builders = new ArrayList<>();

    private Tile target;

    public BuildingProject(String name, Building resultingBuilding,Tile target) {

        this.name = name;
        this.requiredResources = resultingBuilding.getNeededResources();
        this.progress = Progress.COLLECTING;
        this.completed = false;
        this.partition = new Inventory(resultingBuilding.getNeededResourcesWeight());
        this.resultingBuilding = resultingBuilding;
        this.maxBuilders = Math.max(1, (int) (resultingBuilding.getNeededResourcesWeight() / 50));
        this.target = target;
        this.workProg = 0;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() { return name; }
    public Progress getProgress() { return progress; }
    public boolean isCompleted() { return completed; }




    public Inventory getPartition() {
        return partition;
    }

    public boolean addBuilder(Colonist colonist) {
        if(builders.size() + 1 <= maxBuilders) {
            builders.add(colonist);
            return true;
        }
        return false;
    }

    public Tile getTargetTile() {
        return target;
    }

    public boolean doWork() {
        return false;
    }

    public int getWorkProgress() {
        return workProg;
    }



    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public List<ItemStack> getStillNeeded() {
        List<ItemStack> needed = new ArrayList<>();
        for (ItemStack required : requiredResources.getStacks()) {
            int have = partition.getByType(required.getItem().getType()).stream()
                    .mapToInt(ItemStack::getQuantity).sum();
            int deficit = required.getQuantity() - have;
            if (deficit > 0) {
                needed.add(new ItemStack(required.getItem(), deficit));
            }
        }
        return needed;
    }

    public ItemStack claimMaterial(Class<? extends Item> itemClass) {
        // Find the deficit for this type
        List<ItemStack> needed = getStillNeeded();
        return needed.stream()
                .filter(s -> s.getItem().getClass() == itemClass)
                .findFirst()
                .orElse(null); // null = nothing needed or already fully claimed
    }

    public void depositMaterial(Item item, int quantity) {
        // Add to partition and reduce from requiredResources
        partition.add(item, quantity);
        for (ItemStack required : requiredResources.getStacks()) {
            if (required.getItem().getType() == item.getType()) {
                required.remove(Math.min(quantity, required.getQuantity()));
                break;
            }
        }
    }

    public boolean isFullyStocked() {
        return requiredResources.getStacks().stream().allMatch(s -> s.getQuantity() <= 0);
    }

    public Inventory getReqRes() {
        return requiredResources;
    }
}
