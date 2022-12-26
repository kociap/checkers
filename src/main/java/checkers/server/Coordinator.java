package checkers.server;

public class Coordinator {
    private Server server = new Server(this);
    private UI ui = new UI(this);

    public void run() {
        ui.show();
        if(!server.run()) {
            return;
        }
    }

    public void notifyEngineSelected(Engine engine) {
        server.notifyEngineSelected(engine);
    }

    public void notifyClientConnected() {
        ui.notifyClientConnected();
    }
}
