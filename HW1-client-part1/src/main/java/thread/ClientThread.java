package thread;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import model.Record;
import model.SkierLiftRide;

public class ClientThread implements Runnable{

    private BlockingQueue<SkierLiftRide> blockingQueue;
    private int reqCount;
    private CountDownLatch latch;
    private CountDownLatch reqLatch;
    private static final int RETRY_NUM = 5;
    private AtomicInteger successCounter;
    private AtomicInteger failCounter;

    private String BASE_PATH = "http://35.88.162.187:8080/HW1-Server_war/";
//    private static final String BASE_PATH = "http://localhost:8080/HW1-Server_war_exploded/";

    private Queue<Record> records;


    public ClientThread(BlockingQueue<SkierLiftRide> blockingQueue, int reqCount, CountDownLatch latch,
        AtomicInteger successCounter, AtomicInteger failCounter, Queue<Record> records) {
        this.blockingQueue = blockingQueue;
        this.reqCount = reqCount;
        this.latch = latch;
        this.successCounter = successCounter;
        this.failCounter = failCounter;
        this.records = records;
        this.reqLatch = new CountDownLatch(this.reqCount);
    }

    @Override
    public void run() {
        while (reqLatch.getCount() > 0) {
            try {
                SkierLiftRide value = blockingQueue.take();
                sendRequest(value);
                reqLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        latch.countDown();
    }

    private void sendRequest(SkierLiftRide liftData) {
        if (liftData != null) {
            ApiClient client = new ApiClient();
            client.setBasePath(BASE_PATH);
            SkiersApi api = new SkiersApi();
            api.setApiClient(client);
            for (int i = 1; i <= RETRY_NUM; i++) {
                try {
                    long startTime = System.currentTimeMillis();
                    ApiResponse<Void> res = api.writeNewLiftRideWithHttpInfo(liftData.getLiftRide(),
                            liftData.getResortID(), liftData.getSeasonID(), liftData.getDayID(), liftData.getSkierID());
                    long endTime = System.currentTimeMillis();
                    long latency = endTime - startTime;
                    if (res.getStatusCode() == 201 || res.getStatusCode() == 200) {
                        successCounter.getAndIncrement();
                        this.records.offer(new Record(startTime, "POST",
                            latency, res.getStatusCode()));
                        break;
                    }
                    if (i == RETRY_NUM) {
                        failCounter.getAndDecrement();
                        this.records.offer(new Record(startTime, "POST",
                            latency, res.getStatusCode()));
                    }
                } catch (ApiException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
