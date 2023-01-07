package checkers;

public class SocketThread extends Thread {
    private SocketWrapper socket;
    private CommandQueue queue;

    public SocketThread(SocketWrapper socket, CommandQueue queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true) {
            final String line = socket.read();
            if(line == null) {
                break;
            }

            queue.push(line);
            queue.signal();
        }
    }

    public void sendCommand(String command) {
        socket.write(command);
    }
}
