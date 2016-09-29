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

    public static void main(String[] args) {
		String[] input;
		int choice;

		System.out.println("\nChoose your role:");
		System.out.println(" 1. Host");
		System.out.println(" 2. Client");
		System.out.println();
		do {
			System.out.print("Enter: ");
			choice = Integer.parseInt(stdin.nextLine(), 10);
		} while (choice != 1 && choice != 2);

		if (choice == 1) {
			// select a connection-type
			System.out.println("Choose your game:");
			System.out.println(" 1. TCP");
			System.out.println(" 2. UDP");
			System.out.println();
			do {
				System.out.print("Enter: ");
				choice = Integer.parseInt(stdin.nextLine(), 10);
			} while (choice != 1 && choice != 2);

			// we are the masters
			master = true;
			myChar = board.getTurn();

			if (choice == 1) {
				stream = new TCPStream(8080);
				System.out.println("\nAwaiting connection @:8080 ...");
			} else {
				System.out.print("Enter the connection string [protocol://host:port/]: ");
				input = stdin.nextLine().split(":");

				int port = Integer.parseInt(input[2].replaceAll("[^0-9]+", ""));
				stream = new UDPStream(input[1].replaceAll("[^0-9]+", ""), port, port + 1);
				System.out.format("\nAwaiting connection @:%d ...\n", port + 1);
			}
		} else {
			System.out.print("Enter the connection string [protocol://host:port/]: ");
			input = stdin.nextLine().split(":");

			if (input[0].equalsIgnoreCase("tcp")) {
				stream = new TCPStream(
					input[1].replaceAll("[^0-9]+", ""),
					Integer.parseInt(input[2].replaceAll("[^0-9]+", ""), 10)
				);
			} else {
				int port = Integer.parseInt(input[2].replaceAll("[^0-9]+", ""));
				stream = new UDPStream(input[1].replaceAll("[^0-9]+", ""), port - 1, port);
				System.out.format("\nAwaiting connection @:%d ...\n", port + 1);
			}
		}

		// add all appropriate event handlers
		final NetworkStream me = stream;
		stream.onConnect(new Runnable() {
			public void run() {
				if (master) {
					me.send("T:" + board.getTurn());
					nextTurn();
				}
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
					case "T":
					board.setTurn(input[1]);
					myChar = input[1].equals("X") ? "O" : "X";
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
			System.out.format("\n%s won!\n", board.getTurn().equals(myChar) ? "You" : "The opponent");
			stream.close();
			return;
		}

		System.out.format(
			"\n%s\nPlayer turn: %s\n%s",
			board, board.getTurn(),
			board.getTurn().equals(myChar) ? "Awaiting opponent's play ...\n" : "Enter your place [x,y]: "
		);

		if (board.getTurn().equals(myChar)) return;

		String[] input = stdin.nextLine().split(",");
		int x = Integer.parseInt(input[0], 10),
			y = Integer.parseInt(input[1], 10);

		board.play(x, y);
		stream.send("P:" + x + "," + y);

		if ((victor = board.checkWinStatus()) == 0) nextTurn();
		else {
			System.out.format("\n%s won!\n", board.getTurn().equals(myChar) ? "You" : "The opponent");
			stream.close();
		}
	}
}