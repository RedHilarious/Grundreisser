package view.gui;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ToolPanel extends JTabbedPane {
	
	private static final long serialVersionUID = -4271698582977847588L;
	private BuiltTools built;
	private FurnishTools furnish;
	private DrawArea dA;
	
	public ToolPanel(final DrawArea dA, InfoPanel iP){
		setBackground(Color.DARK_GRAY);
		
		built = new BuiltTools(dA);
		furnish = new FurnishTools(dA,iP);
		built.setBackground(new Color(24,116,205));
		furnish.setBackground(new Color(34,139,34));
		
		// Hier werden die JPanels als Registerkarten hinzugefügt
	    addTab("Bauen", built);
	    addTab("Einrichten", furnish);
	    
	    addChangeListener(new ChangeListener() {
	    	// angestoßen, wenn Baumodus-Tab oder Einrichtungsmodus-Tab geklickt
			public void stateChanged(ChangeEvent arg0) {
				furnish.unlock(dA.changeState());
			}
		});
	}
}