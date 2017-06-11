package dtu.alto.linkload;

import java.util.Calendar;

/**
 * Created by s150924 on 6/5/17.
 */
public class PortStats {

    private long firstTimestamp;
    private long firstSample;

    private int numSamples;
    private double avgRate;
    private double rate;
    private long lastTimestamp;
    private long lastSample; // bytes received in the switch port


    public PortStats() {
        this.firstSample = 0;
        this.firstTimestamp = 0;
        this.numSamples = 0;
        this.avgRate = 0;
        this.rate = 0;
        this.lastTimestamp = 0;
        this.lastSample = 0;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(int numSamples) {
        this.numSamples = numSamples;
    }

    public double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(double avgRate) {
        this.avgRate = avgRate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public long getLastSample() {
        return lastSample;
    }

    public void setLastSample(long latestSample) {
        this.lastSample = latestSample;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }

    public long getFirstSample() {
        return firstSample;
    }

    public void setFirstSample(long firstSample) {
        this.firstSample = firstSample;
    }
}
