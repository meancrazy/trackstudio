package ru.sang.trackstudio.restapi;


public class Task {
    private String name;
    private String priorityName;
    private String resolutionName;
    private String workflowName;
    private boolean finishedState;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getResolutionName() {
        return resolutionName;
    }

    public void setResolutionName(String resolutionName) {
        this.resolutionName = resolutionName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public boolean isFinishedState() {
        return finishedState;
    }

    public void setFinishedState(boolean finishedState) {
        this.finishedState = finishedState;
    }
}
