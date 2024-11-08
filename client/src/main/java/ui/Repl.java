package ui;

import ui.ChessClient;

public class Repl {
    private final ChessClient client;
    public Repl(String serverURL) {
        client = new ChessClient(serverURL);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        System.out.print(client.help());
    }
}
