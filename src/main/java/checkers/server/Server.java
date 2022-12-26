package checkers.server;

import checkers.SocketWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Server
// Listens on localhost port 8080.
//
public class Server {
    private Engine engine;
    private ServerSocket socket;
    private final Coordinator coordinator;

    private SocketWrapper client1;
    private SocketWrapper client2;

    public Server(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public boolean run() {
        try {
            // TODO: Hardcoded port.
            socket = new ServerSocket(8080);
        } catch(Exception e) { return false; }

        return true;
    }

    public void notifyEngineSelected(Engine engine) {
        this.engine = engine;

        // TODO: Move to thread;
        client1 = new SocketWrapper(socket.accept());
        coordinator.notifyClientConnected();
        client2 = new SocketWrapper(socket.accept());
        coordinator.notifyClientConnected();
        } catch(Exception e) { return false; }
    }
}
