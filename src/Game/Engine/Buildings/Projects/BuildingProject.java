package Game.Engine.Buildings.Projects;

import Game.Engine.Buildings.Building;
import Game.Engine.Colonist.Colonist;
import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Resources;
import Game.Engine.Map.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuildingProject {
    private String name;
    private int requiredWork;

    private Inventory partition;

    private int priority;

    private ArrayList<ItemStack> requiredResources;
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

    public boolean isFullyStocked() {
        return  partition.isFull();
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public List<ItemStack> getStillNeeded() {
    }
}
