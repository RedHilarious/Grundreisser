package view.states;

import javax.swing.JPanel;

import view.gui.FurniturePanel;

/**
 * 
 * State bei Entsperrenten FurnishPanels, 
 * Realisierung nicht implementiert
 * * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 * 
 */
public class UnlockedMode implements LockMode {

	private FurniturePanel furniture;

	public UnlockedMode(FurniturePanel furniture) {
		this.furniture = furniture;
	}

	/**
	 * Display Furniture dependent on this state.
	 */
	

	@Override
	public void display(JPanel furnish) {
		
		// TODO Auto-generated method stub
		
	}
}