package streamMachine;

public interface SensorDataSet {
    /**
     *
     * @return time of value creation
     */
    long getTime();

    /**
     *
     * @return actual values
     */
    float[] getValues();
}
