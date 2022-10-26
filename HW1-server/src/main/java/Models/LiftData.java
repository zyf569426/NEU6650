package Models;


public class LiftData {

    /**
     skierID - between 1 and 100000
     resortID - between 1 and 10
     liftID - between 1 and 40
     seasonID - 2022
     dayID - 1
     time - between 1 and 360
     **/

    private int resortID;
    private int seasonID;
    private int dayID;
    private int skierID;
    private LiftRide liftRide;

    public LiftData(int resortID, int seasonID, int dayID, int skierID, LiftRide liftRide) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.liftRide = liftRide;
    }

    @Override
    public String toString() {
        return "LiftData{" +
                "resortID=" + resortID +
                ", seasonID=" + seasonID +
                ", dayID=" + dayID +
                ", skierID=" + skierID +
                ", liftRide=" + liftRide.toString() +
                '}';
    }
}
