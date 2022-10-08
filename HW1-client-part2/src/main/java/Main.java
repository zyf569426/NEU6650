import java.util.concurrent.atomic.AtomicInteger;
import model.*;
import model.Record;
import thread.LiftGenerateThread;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    private static final int QUEUE_SIZE = 400;
    private static final int PHASE1_THREAD_NUM = 32;
    private static final int PHASE2_THREAD_NUM = 168;
    private static final int PHASE1_REQUEST_NUM = 1000;
    private static final int PHASE2_REQUEST_NUM = 1000;
    private static final int TOTAL_NUM = 200000;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        BlockingQueue<SkierLiftRide> blockingQueue = new LinkedBlockingDeque<>(QUEUE_SIZE);

        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger failCounter = new AtomicInteger(0);

        LiftGenerateThread liftGenerateThread = new LiftGenerateThread(blockingQueue, TOTAL_NUM);
        Thread thread = new Thread(liftGenerateThread);
        thread.start();

        Queue<Record> records = new ConcurrentLinkedDeque<>();

        runPhase(PHASE1_THREAD_NUM, blockingQueue, PHASE1_REQUEST_NUM, successCounter, failCounter, records);
        runPhase(PHASE2_THREAD_NUM, blockingQueue, PHASE2_REQUEST_NUM, successCounter, failCounter, records);

        // Part1
        long endTime = System.currentTimeMillis();
        System.out.println("------------------Part1 output------------------");
        long runtime = endTime - startTime;
        int successNum = successCounter.get();
        int failNum = failCounter.get();
        long totalThroughput = 1000L * (successNum + failNum) / runtime;
        System.out.println("Time takes: " + (endTime - startTime) + "ms");
        System.out.println("Number of successful requests sent: " + successNum);
        System.out.println("Number of unsuccessful requests: " + failNum);
        System.out.println("The total run time(wall time): " + runtime + " ms");
        System.out.println("The total throughput in requests per second " + totalThroughput);

        // part 2
        RecordWriter recordWriter = new RecordWriter(records, totalThroughput);
        recordWriter.writeToCSV();
        recordWriter.printPart2Results();
    }

    private static void runPhase(int threadCount, BlockingQueue<SkierLiftRide> queue, int requestCount,
        AtomicInteger successCounter, AtomicInteger failCounter, Queue<Record> records)
            throws InterruptedException {
        CountDownLatch phase1Latch = new CountDownLatch(threadCount);
        Phase phase = new Phase(threadCount, queue, requestCount, phase1Latch, successCounter, failCounter, records);
        phase.run();
        phase.await();
    }
}
