package huydat.io;

public interface ISessionHandler {
    void processMessage(Session var1, Message var2);

    void onConnectionFail(Session var1);

    void onDisconnected(Session var1);

    void onConnectOK(Session var1);
}
