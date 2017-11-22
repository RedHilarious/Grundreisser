package view.gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import business.domainModel.Wall;
import business.domainModel.Window;

public class WindowPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 231475470104756737L;
	private Window win;
	private final Color unmarkedColor;
	

	private final Color markedColor;
	private final DrawArea dA;
	private Wall myWall;
	
	public WindowPanel(Window win, final DrawArea dA, Wall myWall) {
		this.win = win;
		this.dA = dA;
		this.myWall = myWall;
		
		unmarkedColor = new Color(100,149,237);
		markedColor = unmarkedColor.brighter();
		setBackground(unmarkedColor);
		
		addMouseListener(myMouseAdapter(this));

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
			}
		});
		setVisible(true);
	}

	private MouseAdapter myMouseAdapter(final JPanel me){
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (getBackground() == markedColor){
					setBackground(unmarkedColor);
				} else {
					setBackground(markedColor);
					dA.requestFocusInWindow();
					dA.setActPanel(me);
				}
			}
		};
	}
	
	public void update(Observable o, Object arg) {
		if(arg instanceof Window){
			
		}
	}
	
	public Window getWindow(){
		return win;
	}
	
	public Wall getMyWall(){
		return myWall;
	}
	
	public Color getUnmarkedColor() {
		return unmarkedColor;
	}
}