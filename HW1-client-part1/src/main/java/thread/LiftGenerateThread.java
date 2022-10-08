package thread;

import io.swagger.client.model.LiftRide;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import model.SkierLiftRide;

public class LiftGenerateThread implements Runnable{

    BlockingQueue<SkierLiftRide> blockingQueue;
    private int totalCount;

    public LiftGenerateThread(BlockingQueue<SkierLiftRide> blockingQueue, int totalCount) {
        this.blockingQueue = blockingQueue;
        this.totalCount = totalCount;
    }

    @Override
    public void run() {
        int count = 0;
        while (count < totalCount) {
            SkierLiftRide cur = generateRandom();
            try {
                blockingQueue.put(cur);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private SkierLiftRide generateRandom() {
        int skierID = ThreadLocalRandom.current().nextInt(1, 100001);
        int resortID = ThreadLocalRandom.current().nextInt(1, 11);
        int liftID = ThreadLocalRandom.current().nextInt(1, 41);
        int seasonID = 2022;
        int dayID = 1;
        int time = ThreadLocalRandom.current().nextInt(1, 361);
        LiftRide liftRide = new LiftRide();
        liftRide.setTime(time);
        liftRide.setLiftID(liftID);
        return new SkierLiftRide(resortID, String.valueOf(seasonID), String.valueOf(dayID), skierID, liftRide);
    }
}
