package Game.Engine.Colonist.Career.Job;

import Game.Engine.Buildings.Projects.BuildingProject;

import java.util.ArrayList;

public class BuildJob extends Job {
    private ArrayList<BuildingProject> projectList = new ArrayList<>();

    public void addProject(BuildingProject project){
        projectList.add(project);

    }
    public void removeProject(BuildingProject project){
        projectList.remove(project);

    }

    public boolean isAssigned() {
        return !projectList.isEmpty();
    }
    public BuildingProject getTopProj() {
        if (projectList.isEmpty()) return null;
        return projectList.get(0);
    }
}
