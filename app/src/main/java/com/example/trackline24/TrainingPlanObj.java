package com.example.trackline24;

public class TrainingPlanObj {
    private String experienceLevel;
    private String finishDate;
    private int idActivity;
    private int idTrainingPlan;
    private String objective;
    private String startDate;

    public TrainingPlanObj(String experienceLevel, String finishDate, int idActivity, int idTrainingPlan, String objective, String startDate) {
        this.experienceLevel = experienceLevel;
        this.finishDate = finishDate;
        this.idActivity = idActivity;
        this.idTrainingPlan = idTrainingPlan;
        this.objective = objective;
        this.startDate = startDate;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public int getIdTrainingPlan() {
        return idTrainingPlan;
    }

    public String getObjective() {
        return objective;
    }

    public String getStartDate() {
        return startDate;
    }
}

