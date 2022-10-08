package model;

import io.swagger.client.model.LiftRide;

public class SkierLiftRide {

    private int resortID;
    private String seasonID;
    private String dayID;
    private int skierID;
    private LiftRide liftRide;

    public SkierLiftRide(int resortID, String seasonID, String dayID, int skierID, LiftRide liftRide) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.liftRide = liftRide;
    }

    public int getResortID() {
        return resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public int getSkierID() {
        return skierID;
    }

    public LiftRide getLiftRide() {
        return liftRide;
    }

}
