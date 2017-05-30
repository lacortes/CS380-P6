import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.EventObject;

public class ImageButton extends JButton implements ActionListener {	
	private ObjectOutputStream outStream;
	private ListenForMessage listener; 

	private int row_index; // Row number on grid
	private int col_index; // Column number for grid  

	/**
	 *	Construct with the conrresponding index and disable all buttons
	 */
	public ImageButton(int row, int col, ObjectOutputStream stream, ListenForMessage listener) {
		row_index = row;
		col_index = col;
		outStream = stream;
		setEnabled(false);
		this.addActionListener(this);
		this.listener = listener;
	}

	public int getRow() {return this.row_index;}

	public int getCol() {return this.col_index;}
	

	/**
	 *	Send MoveMessage to server
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("Pressed: "+row_index+" "+col_index);

		synchronized(listener) {
		MoveMessage moveMsg = new MoveMessage(
			(byte)row_index, (byte)col_index);
		
		try {
			outStream.writeObject(moveMsg);
			listener.wakeMe();
		} catch(IOException e) {e.printStackTrace();}
		}

	}
}