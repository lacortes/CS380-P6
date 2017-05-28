import javax.swing.*;
import java.awt.event.*;
import java.util.EventObject;

public class ImageButton extends JButton implements ActionListener {
	int row_index; // Row number on grid
	int col_index; // Column number for grid 

	/**
	 *	Construct with 
	 */
	public ImageButton(int row, int col) {
		row_index = row;
		col_index = col;
		setEnabled(false);
		this.addActionListener(this);
	}

	public int getRow() {return this.row_index;}

	public int getCol() {return this.col_index;}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("Pressed: "+row_index+" "+col_index);
	}
}