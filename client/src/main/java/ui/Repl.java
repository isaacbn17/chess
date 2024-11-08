package ui;

import ui.ChessClient;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;
    public Repl(String serverURL) {
        client = new ChessClient(serverURL);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" +  ">>>" + SET_TEXT_COLOR_BLUE);
    }
}
