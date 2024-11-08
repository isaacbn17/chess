package ui;

import server.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    -register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    -login <USERNAME> <PASSWORD> - to play chess
                    -quit - playing chess
                    -help - with possible commands
                     """;
        }
        else {
            return """
                    -create <NAME> - create a game
                    -list - list games
                    -join <ID> [WHITE|BLACK] - join a game
                    -observe <ID> - observe a game
                    -logout - when you are done
                    -quit - stop playing chess
                    -help - with possible commands
                    """;
        }
    }
}
