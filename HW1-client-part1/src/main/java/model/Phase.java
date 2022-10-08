package model;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import thread.ClientThread;


public class Phase {

    private int threadNum;
    private int reqNum;
    private BlockingQueue<SkierLiftRide> queue;
    private CountDownLatch latch;

    private AtomicInteger successCounter;
    private AtomicInteger failCounter;

    private Queue<Record> records;


    public Phase(int threadNum, BlockingQueue<SkierLiftRide> queue, int phaseReqNum, CountDownLatch latch,
        AtomicInteger successCounter, AtomicInteger failCounter, Queue<Record> records) {
        this.threadNum = threadNum;
        this.queue = queue;
        this.reqNum = phaseReqNum;
        this.latch = latch;
        this.successCounter = successCounter;
        this.failCounter = failCounter;
        this.records = records;
    }

    public void run() throws InterruptedException{
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new ClientThread(queue, reqNum, latch, successCounter, failCounter, records));
            thread.start();
        }
    }

    public void await() throws InterruptedException {
        latch.await();
    }
}
