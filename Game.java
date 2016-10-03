/**
 * Game.java - lab2
 * Handles the game logic.
 */

import lib.Board;
import lib.TCPStream;
import lib.UDPStream;
import lib.NetworkStream;
import lib.DataListener;

import java.util.Scanner;

class Game {
	private static Board board = new Board();
	private static Scanner stdin = new Scanner(System.in);
	private static NetworkStream stream;
	private static boolean master = false;
	private static String myChar;

	// randomly generate number to decide who goes first
	private final static double A = Math.random();

    public static void main(String[] args) {
		String rawinput, host;
		String[] input;
		int port;

		do {
			System.out.print("Enter the connection string or a port number to listen as TCP server [protocol://host:port/]: ");
			rawinput = stdin.nextLine().trim();
		} while (!rawinput.matches("[0-9]+") && !rawinput.matches("[a-zA-Z]+\\://[a-zA-Z0-9.]+\\:[0-9]+(/?)"));
		input = rawinput.split(":");

		if (rawinput.matches("[0-9]+")) {
			stream = new TCPStream(Integer.parseInt(rawinput, 10));
			System.out.format("\nAwaiting connection @:%s ...\n", rawinput);
		} else {
			host = input[1].replaceAll("[^a-zA-Z0-9.]+", "");
			port = Integer.parseInt(input[2].replaceAll("[^0-9]+", ""), 10);

			if (input[0].equals("tcp")) stream = new TCPStream(host, port);
			else {
				do {
					System.out.print("Enter a port to listen on: ");
					rawinput = stdin.nextLine();
				} while (!rawinput.matches("[0-9]+"));

				stream = new UDPStream(host, port, Integer.parseInt(rawinput, 10));
			}

			System.out.format("\nConnecting to %s:%s ...\n", host, port);
		}

		// add all appropriate event handlers
		final NetworkStream me = stream;
		stream.onConnect(new Runnable() {
			public void run() {
				me.send("M:" + A);
			}
		});

		stream.onDisconnect(new Runnable() {
			public void run() {
				System.exit(0);
			}
		});

		stream.onError(new DataListener() {
			public void eventHandler(String data) {
				System.out.format("\nError: %s\n", data);
				System.exit(-1);
			}
		});

		stream.receive(new DataListener() {
			public void eventHandler(String data) {
				String[] input = data.split(":");

				switch (input[0]) {
					case "M":
					double B = Double.parseDouble(input[1]),
						   C = A + B; // 0 <= C <= 2
 
					master = A > B;
					board.setTurn(C >= 1 ? "X" : "O");
					myChar = board.getTurn();
					if (!master) myChar = myChar.equals("X") ? "O" : "X";
					break;

					case "P":
					input = input[1].split(",");
					board.play(
						Integer.parseInt(input[0], 10),
						Integer.parseInt(input[1], 10)
					);
					break;

					default:
					break;
				}

				nextTurn();
			}
		});
    }

	private static void nextTurn() {
		int victor;
		if ((victor = board.checkWinStatus()) != 0) {
			System.out.format("\n%s won!\n", !board.getTurn().equals(myChar) ? "You" : "The opponent");
			stream.close();
			return;
		} else if (victor == -1) {
			System.out.println("\nNobody won... try again later.");
			stream.close();
			return;
		}

		System.out.format(
			"\n%s\nPlayer turn: %s\n%s",
			board, board.getTurn(),
			board.getTurn().equals(myChar) ? "Enter your place [x,y]: " : "Awaiting opponent's play ...\n"
		);

		if (!board.getTurn().equals(myChar)) return;

		String[] input = stdin.nextLine().split(",");
		int x = Integer.parseInt(input[0], 10),
			y = Integer.parseInt(input[1], 10);

		board.play(x, y);
		stream.send("P:" + x + "," + y);

		if ((victor = board.checkWinStatus()) == 0) nextTurn();
		else {
			System.out.format("\n%s won!\n", !board.getTurn().equals(myChar) ? "You" : "The opponent");
			stream.close();
		}
	}
}
