package data;

/**
 * Created by Alex on 31.03.2017.
 */
public class Dynamic {
    private double RMS;
    private double maxDeltaRMS;
    private double averageDeltaRMS;
    private double[] RMSdinamics;
    private double[] RMSlist;

    public double getAverageDeltaRMS() {
        return averageDeltaRMS;
    }

    public void setAverageDeltaRMS(double averageDeltaRMS) {
        this.averageDeltaRMS = averageDeltaRMS;
    }

    public double getMaxDeltaRMS() {
        return maxDeltaRMS;
    }

    public double getRMS() {
        return RMS;
    }

    public double[] getRMSdinamics() {
        return RMSdinamics;
    }

    public double[] getRMSlist() {
        return RMSlist;
    }

    public void setMaxDeltaRMS(double maxDeltaRMS) {
        this.maxDeltaRMS = maxDeltaRMS;
    }

    public void setRMS(double RMS) {
        this.RMS = RMS;
    }

    public void setRMSdinamics(double[] RMSdinamics) {
        this.RMSdinamics = RMSdinamics;
    }

    public void setRMSlist(double[] RMSlist) {
        this.RMSlist = RMSlist;
    }

    @Override
    public String toString(){
        String result = new String();
        result = result.concat("RMS: "+getRMS()+"\n");
        result = result.concat("Max dRMS: "+getMaxDeltaRMS()+"\n");
        return result;
    }
}
