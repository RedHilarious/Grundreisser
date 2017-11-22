package view.states;

import java.awt.Color;

import javax.swing.JPanel;

import view.gui.FurniturePanel;

/**
 * 
 * State bei Gesperrenten FurnishPanels, 
 * Realisierung nicht implementiert
 * * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 * 
 */
public class LockedMode implements LockMode {

	private FurniturePanel furniture;
	
	public LockedMode(FurniturePanel furniture){
		this.furniture = furniture;
	}
	
	
	public void display(JPanel furnish) {
		
		
		// Farbe ausgrauen / halb transparente graue Schicht darüber
		furnish.setBackground(Color.GRAY);
		
		// MouseListener abhängen / deaktivieren
		
	}
}