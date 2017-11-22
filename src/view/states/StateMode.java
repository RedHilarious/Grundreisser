package view.states;

import java.awt.event.MouseListener;

import javax.swing.JPanel;

import view.gui.FloorPanel;
/**
 * Interface beschreibt die Methoden die die Klassen der verschiedenen States implementieren müssen.
 * States sind Bau und Einrichtungsmodus
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */

public interface StateMode {
	
	
	public void handle(FloorPanel f);
	public MouseListener getMyListener();
	public void setRemoveListener(MouseListener rm);
	public void del(JPanel activePanel);
	public void setFloor(FloorPanel floor);
	public FloorPanel getFloor();
	public void changeListener();
	
}