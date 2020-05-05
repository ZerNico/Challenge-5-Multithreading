package streamMachine;

import org.junit.Assert;
import org.junit.Test;

public class StreamMachineTest {
    @Test
    public void gutTest1() throws PersistenceException {
        String sensorName1 = "sensor1";
        StreamMachine machine1 = new StreamMachineFS(sensorName1);
        machine1.clean();

        String sensorName2 = "sensor2";
        StreamMachine machine2 = new StreamMachineFS(sensorName2);
        machine2.clean();

        long time1 = System.currentTimeMillis();
        float[] valueSet = new float[3];
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        machine1.saveData(time1, valueSet);

        long time2_0 = System.currentTimeMillis();
        float[] valueSet2_0 = new float[2];
        valueSet2_0[0] = (float) 2.3;
        valueSet2_0[1] = (float) 4.2;

        // write 1st data set
        machine2.saveData(time2_0, valueSet2_0);

        // create second set
        long time2_1 = time2_0 + 1; // one milli second later
        float[] valueSet2_1 = new float[3];
        valueSet2_1[0] = (float) 0.2;
        valueSet2_1[1] = (float) 8.2;
        valueSet2_1[2] = (float) 10.4;

        // write 2nd data set
        machine2.saveData(time2_1, valueSet2_1);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                                   test                                                  //
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // restore values machine 1
        int size = machine1.size();
        Assert.assertEquals(1, size);

        SensorDataSet dataSet = machine1.getDataSet(0);
        Assert.assertEquals(time1, dataSet.getTime());
        Assert.assertArrayEquals(valueSet, dataSet.getValues(), 0);

        // restore values machine 2
        size = machine2.size();
        Assert.assertEquals(2, size);

        // get dataset @ index 0
        dataSet = machine2.getDataSet(0);
        Assert.assertEquals(time2_0, dataSet.getTime());
        Assert.assertArrayEquals(valueSet2_0, dataSet.getValues(), 0);

        // get dataset @ index 1
        dataSet = machine2.getDataSet(1);
        Assert.assertEquals(time2_1, dataSet.getTime());
        Assert.assertArrayEquals(valueSet2_1, dataSet.getValues(), 0);
    }

    /**
     * expect an exception: null values must be rejected.
     * @throws PersistenceException
     */
    @Test(expected = PersistenceException.class)
    public void schlechtTestNullValue() throws PersistenceException {
        StreamMachine machine1 = new StreamMachineFS("X");
        machine1.clean();

        machine1.saveData(System.currentTimeMillis(), null);
        // we must not reach that line
    }

    /**
     * expect an exception: time must be a positive long value.
     * @throws PersistenceException
     */
    @Test(expected = PersistenceException.class)
    public void schlechtTestMinusTime() throws PersistenceException {
        StreamMachine machine1 = new StreamMachineFS("X");
        machine1.clean();
        float[] values = new float[1];
        values[0] = (float) 4.2;

        machine1.saveData(-1, values);
        // we must not reach that line
    }

    /**
     * expect an exception: try to reach beyond last index
     * @throws PersistenceException
     */
    @Test(expected = PersistenceException.class)
    public void schlechtTestBeyondIndex() throws PersistenceException {
        StreamMachine machine1 = new StreamMachineFS("X");
        machine1.clean();
        float[] values = new float[1];
        values[0] = (float) 4.2;

        machine1.saveData(System.currentTimeMillis(), values);

        // try to get get data set @ 1 - which does not exist
        machine1.getDataSet(1);
        // we must not reach that line
    }
}