package streamMachine;

public class SensorDataSetImp implements SensorDataSet {
    private final long timeStamp;
    private final float[] values;

    public SensorDataSetImp(long timeStamp, float[] values) {
        this.timeStamp = timeStamp;
        this.values = values;
    }

    @Override
    public long getTime() {
        return this.timeStamp;
    }

    @Override
    public float[] getValues() {
        return this.values;
    }
}
