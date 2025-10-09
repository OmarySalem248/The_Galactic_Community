package Game.Buildings;

import Game.Resources;

public class BuildingProject {
    private String name;
    private int requiredWork;
    private int progress;
    private Resources requiredResources;
    private boolean completed;
    private Building resultingBuilding;

    public BuildingProject(String name, int requiredWork, Resources requiredResources, Building resultingBuilding) {
        this.name = name;
        this.requiredWork = requiredWork;
        this.requiredResources = requiredResources;
        this.progress = 0;
        this.completed = false;
        this.resultingBuilding = resultingBuilding;
    }

    public String getName() { return name; }
    public int getProgress() { return progress; }
    public boolean isCompleted() { return completed; }

    public void contributeWork(int amount) {
        if (completed) return;
        progress += amount;
        if (progress >= requiredWork) {
            completed = true;
            progress = requiredWork;
        }
    }

    public Building completeProject() {
        return completed ? resultingBuilding : null;
    }
}
