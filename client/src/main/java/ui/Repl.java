package ui;

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

}
