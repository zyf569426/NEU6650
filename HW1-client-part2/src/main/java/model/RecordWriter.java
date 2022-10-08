package model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class RecordWriter {

    private Queue<Record> records;
    private long throughPut;

    public RecordWriter(Queue<Record> records, long throughPut) {
        this.records = records;
        this.throughPut = throughPut;
    }

    public void writeToCSV(){
        try {
            FileWriter csvWriter = new FileWriter( "records.csv");
            csvWriter.append("startTime,requestType,latency,responseCode\n");
            for (Record record : records) {
                csvWriter.append(record.toCsvString());
            }
            csvWriter.flush();
            csvWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printPart2Results() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Record record: records){
            stats.addValue(record.getLatency());
        }
        System.out.println("------------------Part2 output------------------");
        System.out.println("Mean response time: " + stats.getMean() + " (ms)");
        System.out.println("Median response time: " + stats.getPercentile(50) + " (ms)");
        System.out.println("Throughput: " + throughPut + " (requests/second)");
        System.out.println("p99 response time: " + stats.getPercentile(99) + " (ms)");
        System.out.println("Min: " + stats.getMin() + " (ms), Max: " + stats.getMax() + " (ms)");
    }

}
