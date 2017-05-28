import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

// Luis Cortes
// CS 380
// Project 6

public class TicTacToeClient extends JFrame {
	private JPanel panel;
	private ImageButton[][] imageButtons; // GUI Board
	private ImageIcon letterX;
	private ImageIcon letterO;

	private final int WINDOW_WIDTH = 600;
	private final int WINDOW_HEIGHT = 600;
	private final int PORT = 38006;
	private byte[][] gameBoard; 

	public TicTacToeClient() {
		initComp();

		System.out.println("Establishing connection");

		// Connect to server
		try (Socket socket = new Socket("codebank.xyz", PORT)) {
			System.out.println("Connected");

			// Setup streams for serializtions
			ObjectOutputStream outStream = new ObjectOutputStream(
				new PrintStream(socket.getOutputStream(), true));
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

			// Identify self to server
			Message connectMsg = new ConnectMessage("Luis Cortes");
			
			outStream.writeObject(connectMsg);

			// Command message. Begin a new game
			Message commandMsg = new CommandMessage(CommandMessage.Command.NEW_GAME);
			outStream.writeObject(commandMsg);

			// Read in from server
			Message readMessage = (Message) inStream.readObject();
			System.out.println(readMessage.getType().toString());

			gameBoard = ((BoardMessage)readMessage).getBoard();

			updateGUI();

			// Move Message


 		} catch (UnknownHostException e) {
			System.out.println("Socket error");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 *	All swing components configured
	 */
	private void initComp() {
		// Set up Frame
		this.setTitle("Tic Tac Toe Client");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setResizable(false);

		// Panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(3,3));

		// Create a 3 x 3 array to hold buttons
		imageButtons = new ImageButton[3][3];

		// Assign a button to box in grid
		int num = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// buttons[i][j] = new JButton();
				// buttons[i][j].setEnabled(true);
				
				// buttons[i][j].addActionListener(new ActionListener() {
				// 	public void actionPerformed(ActionEvent event) {
				// 		ImageButtonActionPerformed(event);
				// 	}
				// });

				// Create ImageButton for every box in grid
				imageButtons[i][j] = new ImageButton(i, j);

				// panel.add(buttons[i][j]);
				panel.add(imageButtons[i][j]);
			}
		}
		this.add(panel);

		letterX = new ImageIcon("x_image.png");
		letterO = new ImageIcon("o_image.png");
		// buttons[0][0].setIcon(letterX);
		// buttons[0][0].setDisabledIcon(letterX);

	}

	private void updateGUI() {
		for (int i = 0; i < gameBoard[0].length; i++) {
				for (int j = 0; j < gameBoard[1].length; j++) {
					byte box = gameBoard[i][j];
					ImageButton imageButton = imageButtons[i][j];

					if (box == 0) { // Empty 
						imageButton.setEnabled(true);
					}
					else if (box == 1) { // player 1: X
						imageButton.setEnabled(false);
						imageButton.setDisabledIcon(letterX);
					} else { // player 2: O
						imageButton.setEnabled(false);
						imageButton.setDisabledIcon(letterO);
					}
				}
			}
	}

	private class ListenForMessage extends SwingWorker<Void,Void> {
		final private ImageButton imageButtons;

		public ListenForMessage(final ImageButton imageButtons ) {
			this.imageButtons = imageButtons;
		}

		@Override
		protected Void doInBackground() {
			return null;
		}

		// @Override 
		// protected void process(List<Void> chunks) {

		// }
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TicTacToeClient().setVisible(true);
			}
		});
	}
}