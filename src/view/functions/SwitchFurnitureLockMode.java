package view.functions;

import view.gui.FurniturePanel;

/**
 *
 *	Klasse implementiert das Wechseln zwischen LockedMode und UnlockesMode
 *	Klasse wird noch nicht verwendet 
 *  @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class SwitchFurnitureLockMode {

	private SwitchFurnitureLockMode lF;
	
	/**
	 * Ensures existence on one single Instance.
	 * @return
	 */
	public SwitchFurnitureLockMode getInstance(){
		if(lF == null){
			lF = new SwitchFurnitureLockMode();
		}
		return lF;
	}
	
	
	public void switchMode(FurniturePanel furniture){
		furniture.switchMode();
	}
}