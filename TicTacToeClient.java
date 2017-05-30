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
import java.util.List;

// Luis Cortes
// CS 380
// Project 6

public class TicTacToeClient extends JFrame {
	private JPanel panel;
	private ImageButton[][] imageButtons; // GUI Board
	private ImageIcon letterX;
	private ImageIcon letterO;
	private ImageIcon questionMark;

	private final int WINDOW_WIDTH = 600;
	private final int WINDOW_HEIGHT = 600;
	private final int PORT = 38006;
	private byte[][] gameBoard;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	
	protected ListenForMessage listener;

	public TicTacToeClient() {

		System.out.println("Establishing connection");

		// Connect to server
		try (Socket socket = new Socket("codebank.xyz", PORT)) {
			System.out.println("Connected");

			// Setup streams for serializtions
			outStream = new ObjectOutputStream(
				new PrintStream(socket.getOutputStream(), true));
			inStream = new ObjectInputStream(socket.getInputStream());


			initComp();

			// // Identify self to server
			// Message connectMsg = new ConnectMessage("Luis Cortes");
			
			// outStream.writeObject(connectMsg);
			
			listener = new ListenForMessage(imageButtons, inStream, outStream, socket);
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					imageButtons[i][j].setListen(listener);
				}
			}

			listener.execute();

			// // Command message. Begin a new game
			// Message commandMsg = new CommandMessage(CommandMessage.Command.NEW_GAME);
			// outStream.writeObject(commandMsg);

			// Read in from server
			// Message readMessage = (Message) inStream.readObject();
			// System.out.println(readMessage.getType().toString());

			// gameBoard = ((BoardMessage)readMessage).getBoard();
			// updateGUI();


 		} catch (Exception e) {
 			System.out.println("ERROR --> TicTacToeClient()");
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

		System.out.println("Initializing components");

		// Panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(3,3));

		// Create a 3 x 3 array to hold buttons
		imageButtons = new ImageButton[3][3];

		// Assign a button to box in grid
		int num = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Create ImageButton for every box in grid
				imageButtons[i][j] = new ImageButton(i, j, outStream);

				// panel.add(buttons[i][j]);
				panel.add(imageButtons[i][j]);
			}
		}
		this.add(panel);

		letterX = new ImageIcon("x_image.png");
		letterO = new ImageIcon("o_image.png");
		questionMark = new ImageIcon("question_icon.png");
		// buttons[0][0].setIcon(letterX);
		// buttons[0][0].setDisabledIcon(letterX);

	}

	// private void updateGUI() {
	// 	for (int i = 0; i < gameBoard[0].length; i++) {
	// 			for (int j = 0; j < gameBoard[1].length; j++) {
	// 				byte box = gameBoard[i][j];
	// 				ImageButton imageButton = imageButtons[i][j];

	// 				if (box == 0) { // Empty 
	// 					imageButton.setEnabled(true);
	// 				}
	// 				else if (box == 1) { // player 1: X
	// 					imageButton.setEnabled(false);
	// 					imageButton.setDisabledIcon(letterX);
	// 				} else { // player 2: O
	// 					imageButton.setEnabled(false);
	// 					imageButton.setDisabledIcon(letterO);
	// 				}
	// 			}
	// 		}
	// }

	/** 
	 *	Listens to messages sent by server.
	 */
	public class ListenForMessage extends SwingWorker<Void, BoardMessage> {
		private ImageButton[][] imageButtons;
		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		private byte[][] gameBoard = null;
		private BoardMessage boardMsg = null;
		private BoardMessage.Status status = null;
		private boolean waitForValue = true;
		private Socket socket;

		public ListenForMessage(ImageButton[][] imageButtons, 
			ObjectInputStream inStream, ObjectOutputStream outStream, Socket socket) throws IOException, ClassNotFoundException {

				// this.stream = new ObjectInputStream(socket.getInputStream());
				this.imageButtons = imageButtons;
				this.inStream = inStream;
				this.outStream = outStream;
				this.socket = socket;

			// Identify self to server
			Message connectMsg = new ConnectMessage("Luis Cortes");
			outStream.writeObject(connectMsg);

			// Command message. Begin a new game
			Message commandMsg = new CommandMessage(CommandMessage.Command.NEW_GAME);
			outStream.writeObject(commandMsg);
			System.out.println("Wating for message ... ");
				

			Message readMessage = (Message)inStream.readObject();
			System.out.println(readMessage.getType().toString());

			gameBoard = ((BoardMessage)readMessage).getBoard();
			boardMsg = (BoardMessage) readMessage;
			status = ((BoardMessage)readMessage).getStatus();

		}

		@Override
		protected Void doInBackground() throws IOException, 
			ClassNotFoundException, InterruptedException {
			// read in message
			// Message readMessage = (Message)inStream.readObject();
			// System.out.println(readMessage.getType().toString());
			// // Identify self to server
			// Message connectMsg = new ConnectMessage("Luis Cortes");
			// outStream.writeObject(connectMsg);

			// // Command message. Begin a new game
			// Message commandMsg = new CommandMessage(CommandMessage.Command.NEW_GAME);
			// outStream.writeObject(commandMsg);
			// System.out.println("Wating for message ... ");
			// // read in message
			// Message readMessage = (Message)inStream.readObject();
			// System.out.println(readMessage.getType().toString());
			
			// Get board for first time
			// gameBoard = ((BoardMessage)readMessage).getBoard();
			// System.out.println(readMessage.getBoard().toString());
			
			// While game is in progress
			// while(true) {			
				

			// 	// Get status of game
			// 	BoardMessage.Status status = ((BoardMessage)readMessage).getStatus();
				
			// 	if (status == BoardMessage.Status.IN_PROGRESS) {			
			// 		publish((BoardMessage)readMessage);
			// 		System.out.println("Game is IN_PROGRESS ");
			// 		// readMessage = (BoardMessage) inStream.readObject();
			// 		// publish(readMessage);
			// 	} else {
			// 		return null;
			// 	}
					 
			// 	// } catch (Exception e) {
			// 	// 	System.out.println("ERROR --> doInBackground()");
			// 	// 	e.printStackTrace();
			// 	// 	return null;
			// 	// } 
			// }
			synchronized(this) {
			publish(boardMsg);

			// MoveMessage moveMsg = new MoveMessage((byte)0,(byte)0);
			// outStream.writeObject(moveMsg);

			// BoardMessage msg = (BoardMessage)inStream.readObject();
			// System.out.println("Turn: "+msg.getTurn());
			// gameBoard = msg.getBoard();
			// status = msg.getStatus();
			// publish(msg);

			System.out.println("Game Starting");
			while (true) {
					wait();
					System.out.println("NO LONGER WAITING");
				// if (inStream.available() > 0) {
					ObjectInputStream readStream = new ObjectInputStream(socket.getInputStream());
					BoardMessage msg = (BoardMessage)readStream.readObject();
					System.out.println("Turn: "+msg.getTurn());
					gameBoard = msg.getBoard();
					status = msg.getStatus();
					publish(msg);
				// } 
				if (status != BoardMessage.Status.IN_PROGRESS)
					return null;
				System.out.println("Waiting");
			}
			// return null;
			}
		}

		@Override 
		protected void process(List<BoardMessage> chunks) {
			try {
				// BoardMessage message = chunks.get(chunks.size() -1);
				for (final BoardMessage message : chunks) {
					gameBoard = message.getBoard();
					updateGUI();
				}
				
			} catch(Exception e) {
				System.out.println("ERROR -> process()");
				e.printStackTrace();
			}

		}

		public synchronized void wakeMe() {
			notify();
			this.waitForValue = false;
		}

		private void updateGUI() {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					byte box = gameBoard[i][j];
					ImageButton imageButton = imageButtons[i][j];

					if (box == 0) { // Empty 
						imageButton.setEnabled(true);
						imageButton.setIcon(questionMark);
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
	}


// 	import javax.swing.*;
// import java.awt.event.*;
// import java.io.ObjectOutputStream;
// import java.io.IOException;
// import java.util.EventObject;

public class ImageButton extends JButton implements ActionListener {	
	private ObjectOutputStream outStream;
	private ListenForMessage listener; 

	private int row_index; // Row number on grid
	private int col_index; // Column number for grid  

	/**
	 *	Construct with the conrresponding index and disable all buttons
	 */
	public ImageButton(int row, int col, ObjectOutputStream stream) {
		row_index = row;
		col_index = col;
		outStream = stream;
		setEnabled(false);
		this.addActionListener(this);
	}

	public int getRow() {return this.row_index;}

	public int getCol() {return this.col_index;}
	
	public void setListen(ListenForMessage listener) {
		this.listener = listener;
	}

	/**
	 *	Send MoveMessage to server
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("Pressed: "+row_index+" "+col_index);

		MoveMessage moveMsg = new MoveMessage(
			(byte)row_index, (byte)col_index);
		
		try {
			outStream.writeObject(moveMsg);
			listener.wakeMe();
		} catch(IOException e) {e.printStackTrace();}

	}
}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() { 
				new TicTacToeClient().setVisible(true);
			}
		});
	}
}