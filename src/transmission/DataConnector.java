package transmission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataConnector extends Thread implements DataConnection {
    private Socket socket;
    private final ServerSocket serverSocket;

    /**
     * Create client side - open connection to address / port
     * @param address
     */
    public DataConnector(String address, int port) throws IOException {
        this.socket = new Socket(address, port);

        System.out.println("connected to " + address + ":" + port);
        serverSocket = null;
    }

    /**
     * Create server side - open port on this port and wait for one client
     * @param port
     */
    public DataConnector(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.start();
    }

    public void run() {
        try {
            this.socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("connection establised");
    }

    @Override
    public DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(this.socket.getInputStream());
    }

    @Override
    public DataOutputStream getDataOutputStream() throws IOException {
        return new DataOutputStream(this.socket.getOutputStream());
    }
}
