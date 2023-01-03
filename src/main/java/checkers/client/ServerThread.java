package checkers.client;

import checkers.utility.SocketWrapper;

public class ServerThread extends Thread {
    private SocketWrapper socket;
    private CommandQueue queue;

    public ServerThread(SocketWrapper socket, CommandQueue queue) {
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
