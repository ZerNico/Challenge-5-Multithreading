package streamMachine;

import sensorData.SensorDataReceiver;
import sensorData.SensorDataSender;
import transmission.DataConnection;
import transmission.DataConnector;

import java.io.*;

public class WriteAndReadDataSet {
    public static void main(String[] args) throws IOException {
        // three example data sets
        String sensorName = "MyGoodOldSensor"; // does not change

        long timeStamps[] = new long[3];
        timeStamps[0] = System.currentTimeMillis();
        timeStamps[1] = timeStamps[0] + 1; // milli sec later
        timeStamps[2] = timeStamps[1] + 1000; // second later

        float[][] values = new float[3][];
        // 1st measure .. just one value
        float[] valueSet = new float[1];
        values[0] = valueSet;
        valueSet[0] = (float) 1.5; // example value 1.5 degrees

        // 2nd measure .. just three values
        valueSet = new float[3];
        values[1] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        // 3rd measure .. two values
        valueSet = new float[2];
        values[2] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;

        // send data over TCP and store it in file
        StreamMachine dataStorage = new StreamMachineFS(sensorName);
        DataConnection receiverConnection = new DataConnector(9876);
        SensorDataReceiver sensorDataReceiver = new SensorDataReceiver(receiverConnection, dataStorage);
        DataConnection senderConnection = new DataConnector("localhost", 9876);
        SensorDataSender sensorDataSender = new SensorDataSender(senderConnection);

        for (int i = 0; i < timeStamps.length; i++) {
            sensorDataSender.sendData(sensorName, timeStamps[i], values[i]);
            sensorDataReceiver.storeData();
        }

        // display data in console
        for (int i = 0; i < dataStorage.size(); i++) {
            try {
                SensorDataSet recievedData = dataStorage.getDataSet(i);
                System.out.println(recievedData.getTime());
                float[] recievedValues = recievedData.getValues();
                for (int j = 0; j < recievedValues.length; j++) {
                    System.out.println(recievedValues[j]);
                }
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
    }
}
