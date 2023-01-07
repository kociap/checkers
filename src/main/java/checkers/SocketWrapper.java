package checkers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SocketWrapper {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Lock readerLock = new ReentrantLock();
    private final Lock writerLock = new ReentrantLock();

    public SocketWrapper(Socket socket) throws Exception {
        this.socket = socket;
        reader =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream()));
    }

    public String read() {
        readerLock.lock();
        try {
            return reader.readLine();
        } catch(Exception e) {
            return null;
        } finally {
            readerLock.unlock();
        }
    }

    public void write(String string) {
        writerLock.lock();
        try {
            writer.write(string);
            writer.flush();
        } finally {
            writerLock.unlock();
            // Suppress exceptions.
            return;
        }
    }
}
