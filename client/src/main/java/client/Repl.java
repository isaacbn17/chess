package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static client.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    public Repl(String serverURL) {
        client = new ChessClient(serverURL, this);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (! "quit".equals(result)) {
            System.out.print(SET_TEXT_COLOR_WHITE + "\n" +  ">>> ");
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable ex) {
                String message = ex.toString();
                System.out.print(message);
            }
        }
        System.out.println();
    }

    public void notify(String message) {
        ServerMessage.ServerMessageType type = new Gson().fromJson(message, ServerMessage.class).getServerMessageType();
        switch (type) {
            case LOAD_GAME -> {
                LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                ChessGame game = gameMessage.getGame();
                ChessGame.TeamColor color = gameMessage.getColor();
                if (color == ChessGame.TeamColor.BLACK) {
                    System.out.println("\n");
                    PrintBoard.drawBlackPerspective(game, null);
                }
                else {
                    System.out.println("\n");
                    PrintBoard.drawWhitePerspective(game, null);
                }
            }
            case ERROR -> {
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                System.out.printf("\nError: %s", errorMessage.getMessage());
            }
        }
//        System.out.print(SET_TEXT_COLOR_YELLOW + message.toString());
        System.out.print(SET_TEXT_COLOR_WHITE + "\n" +  ">>> ");
    }

}
