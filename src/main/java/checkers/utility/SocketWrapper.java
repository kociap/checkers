package checkers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWrapper {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketWrapper(Socket socket) throws Exception {
        this.socket = socket;
        reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}