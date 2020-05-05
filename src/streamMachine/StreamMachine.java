package streamMachine;

/**
 * We assume: Each sensor gets its own storage engine. There wont be a parameter
 * sensor name.
 */
public interface StreamMachine {
    /**
     * This method can be called by a sensor to save a data set.
     * @param time UNIX time when measurement took place
     * @param values sensor data
     * @throws PersistenceException if something unexpected happened. Insufficient right, medium broken, offline..
     */
    void saveData(long time, float[] values) throws PersistenceException;

    /**
     * @return number of data sets
     */
    int size();

    /**
     *
     * @param index position of data set
     */
    SensorDataSet getDataSet(int index) throws PersistenceException;

    /**
     * remove any data
     */
    void clean();
}
