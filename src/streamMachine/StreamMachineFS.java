package streamMachine;

import java.io.*;

public class StreamMachineFS implements StreamMachine {
    private final String sensorName;
    private final String filename;
    private int numberDataSets = 0;

    public StreamMachineFS(String sensorName) {
        this.sensorName = sensorName;
        this.filename = this.getFileName();

        try {
            this.readMetaData();
        } catch (IOException e) {
            // ignore
        }
    }

    private String getFileName() {
        return this.sensorName + ".txt";
    }

    private String getMetaFileName() {
        return this.sensorName + "_meta.txt";
    }

    private void saveMetaData() throws IOException {
        String metaDataFileName = this.getMetaFileName();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(metaDataFileName));

        dos.writeUTF(this.sensorName);
        dos.writeInt(this.numberDataSets);
    }

    private void readMetaData() throws IOException {
        String metaDataFileName = this.getMetaFileName();
        DataInputStream dos = new DataInputStream(new FileInputStream(metaDataFileName));

        dos.readUTF();
        this.numberDataSets = dos.readInt();
    }

    @Override
    public void saveData(long time, float[] values) throws PersistenceException {
        // test of invalid parameters
        if(time < 0 || values == null || values.length < 1) {
            throw new PersistenceException("parameters are valid, values null or negative time");
        }

        OutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(this.filename, true);
        } catch (FileNotFoundException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }

        DataOutputStream dos = new DataOutputStream(fileOutputStream);

        try {
            // timestamp
            dos.writeLong(time);

            // values
            dos.writeInt(values.length);
            for (int i = 0; i < values.length; i++) {
                dos.writeFloat(values[i]);
            }
            dos.close();
            this.numberDataSets++;
            this.saveMetaData();
        } catch (IOException e) {
            throw new PersistenceException(e.getLocalizedMessage());
        }
    }

    @Override
    public int size() {
        return this.numberDataSets;
    }

    private SensorDataSet[] sensorData = null;

    private void readFromFile() throws IOException {
        this.sensorData = new SensorDataSet[this.numberDataSets];

        // open file
        InputStream is = new FileInputStream(this.filename);
        DataInputStream dis = new DataInputStream(is);

        for(int i = 0; i < this.numberDataSets; i++) {
            // read data set
            // timestamp
            long timeStamp = dis.readLong();

            // values
            int numberReceived = dis.readInt();
            float[] values = new float[numberReceived];
            for (int j = 0; j < numberReceived; j++) {
                values[j] = dis.readFloat();
            }

            // keep it in memory
            this.sensorData[i] = new SensorDataSetImp(timeStamp, values);
        }
    }

    @Override
    public SensorDataSet getDataSet(int index) throws PersistenceException {
        if(index < 0 || index >= this.numberDataSets) {
            throw new PersistenceException("index negative or to big");
        }

        if(this.sensorData == null) {
            try {
                this.readFromFile();
            } catch (IOException e) {
                throw new PersistenceException(e.getLocalizedMessage());
            }
        }

        return this.sensorData[index];
    }

    @Override
    public void clean() {
        File file = new File(this.getMetaFileName());
        file.delete();

        file = new File(this.getFileName());
        file.delete();

        this.numberDataSets = 0;
        this.sensorData = null;
    }
}
