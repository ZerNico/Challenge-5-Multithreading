package sensorData;

import streamMachine.PersistenceException;
import streamMachine.StreamMachine;
import transmission.DataConnection;

import java.io.DataInputStream;
import java.io.IOException;

public class SensorDataReceiver {
    private final DataConnection connection;
    private final StreamMachine storage;

    public SensorDataReceiver(DataConnection connection, StreamMachine storage) {
        this.connection = connection;
        this.storage = storage;
    }

    public void storeData() throws IOException {
        DataInputStream dataInputStream = connection.getDataInputStream();
        dataInputStream.readUTF();
        long time = dataInputStream.readLong();
        int numberReceived = dataInputStream.readInt();
        float[] values = new float[numberReceived];
        for (int j = 0; j < numberReceived; j++) {
            values[j] = dataInputStream.readFloat();
        }
        try {
            storage.saveData(time, values);
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    DataInputStream getInputStream() throws IOException {
        return this.connection.getDataInputStream();
    }

    StreamMachine getStorage() {
        return storage;
    }
}
