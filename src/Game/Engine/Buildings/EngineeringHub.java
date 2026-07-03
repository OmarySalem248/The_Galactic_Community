package Game.Engine.Buildings;

import Game.Engine.Buildings.Projects.BuildingProject;
import Game.Engine.Buildings.Projects.Progress;
import Game.Engine.Colonist.Career.Profession.Builder;

import java.util.*;

public class EngineeringHub extends Building {

    private final Map<Progress, List<BuildingProject>> projects = new HashMap<>();
    private final List<BuildingProject> completedProjects = new ArrayList<>();

    public EngineeringHub() {
        super("Engineering Hub", 4, Builder.class, 1000, BuildingType.WORKPLACE);
        for (Progress p : Progress.values()) {
            projects.put(p, new ArrayList<>());
        }
    }

    public void addProject(BuildingProject project) {
        projects.get(Progress.COLLECTING).add(project);
    }

    public void advanceProject(BuildingProject project, Progress next) {
        // Remove from current state
        for (List<BuildingProject> list : projects.values()) {
            list.remove(project);
        }
        if (next == Progress.DONE) {
            completeProject(project);
        } else {
            project.setProgress(next);
            projects.get(next).add(project);
        }
    }

    public void completeProject(BuildingProject project) {
        for (List<BuildingProject> list : projects.values()) {
            list.remove(project);
        }
        project.setProgress(Progress.DONE);
        completedProjects.add(project);
    }

    public boolean hasProjects() {
        return projects.values().stream().anyMatch(l -> !l.isEmpty());
    }

    public boolean isRecruting() {
        return !projects.get(Progress.RECRUITING).isEmpty();
    }

    public BuildingProject getHighPriorityProject(Progress state) {
        List<BuildingProject> list = projects.get(state);
        if (list == null || list.isEmpty()) return null;
        return list.stream()
                .max(Comparator.comparingInt(BuildingProject::getPriority))
                .orElse(null);
    }

    public List<BuildingProject> getProjects(Progress state) {
        return projects.getOrDefault(state, Collections.emptyList());
    }

    public List<BuildingProject> getCompletedProjects() {
        return completedProjects;
    }
}