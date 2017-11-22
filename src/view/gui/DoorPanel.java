package view.gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import business.domainModel.Door;
import business.domainModel.Wall;
/**
 * Klasse erbt von JPanel und realisiert die View einer Tür
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class DoorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 231475470104756737L;
	private Door door;
	private Color markedColor;
	private Color unmarkedColor;
	private DrawArea dA;
	private Wall myWall;
	
	public DoorPanel(Door door, final DrawArea dA, Wall myWall) {
		this.dA = dA;
		this.door = door;
		this.myWall = myWall;
			
		unmarkedColor = new Color(139,69,19);
		markedColor = unmarkedColor.brighter();
		
		setBackground(unmarkedColor);
		
		addMouseListener(myMouseAdapter(this));
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
				
			}
		});

		
		setVisible(true);
	}
	/**
	 * Methode initialisiert einen MouseAdapter, der beim Klicken auf das Panel es als aktuell Markiertes setzt	
	 * @param me JPanel, das sich bei anklicken ändern soll
	 * @return MouseAdapter, der an das Panel gehängt wird
	 */
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
	/**
	 * Bei Erweiterung der Anwendung, könnten hier Änderungen des Domainmodels auch visuallisiert werden,
	 * die Möglichkeit etwas am Model zu ändern, existiert jedoch noch nicht 
	 */
	public void update(Observable o, Object arg) {
		// Änderungsmap (geänderte Komponente auf Änderung als String)
	}
	
	public Door getDoor(){
		return door;
	}

	public Wall getMyWall(){
		return myWall;
	}
	
	public Color getUnmarkedColor() {
		return unmarkedColor;
	}
}