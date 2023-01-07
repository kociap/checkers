package checkers;

public interface CommandReceiver {
    void receiveCommand(int socketID, String command);
}
