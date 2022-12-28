package checkers.server;

import checkers.CommandBuilder;
import checkers.Piece;
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

    private ClientThread threadClientRed;
    private ClientThread threadClientWhite;

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
        try {
            // TODO: Move to thread;
            SocketWrapper clientRed = new SocketWrapper(socket.accept());
            sendHello(clientRed, Piece.Color.red);
            threadClientRed = new ClientThread(this, clientRed);
            threadClientRed.start();
            coordinator.notifyClientConnected();
            // clientWhite = new SocketWrapper(socket.accept());
            // sendHello(clientWhite, Piece.Color.white);
            // coordinator.notifyClientConnected();
        } catch(Exception e) {}
    }

    private void sendHello(SocketWrapper client, Piece.Color color) {
        final PrintWriter writer = client.getWriter();
        final CommandBuilder builder = new CommandBuilder();
        builder.command("hello").parameter(color);
        writer.format(builder.finalise());
    }
}
