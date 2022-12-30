package checkers.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SocketWrapper {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Lock readerLock = new ReentrantLock();
    private Lock writerLock = new ReentrantLock();

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
        } catch(Exception e) {
            return;
        } finally {
            writerLock.unlock();
        }
    }
}
