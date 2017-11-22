package view.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FloorException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.building.DeleteFloor;
import business.domainModel.Floor;
/**
 * Diese Klasse ist das Panel, in dem alle Werkzeuge zum Bauen untergebracht sind
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class BuiltTools extends JPanel {
	
	private static final long serialVersionUID = -1344856685433806161L;
	private DrawArea dA;
	
	private JButton newFloor;
	private JButton stairs;
	private JButton window;
	private JButton door;
	private JButton walls;
	private JButton removeFloor;
	
	private GridBagConstraints c;
	
	public BuiltTools(DrawArea dA){
		this.dA = dA;
		setBackground(Color.gray);
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		initButtons();
	}
	/**
	 * Methode initialisiert die Buttons: Wand einzeichnen, Tuer, Fenster, Treppe (nicht angezeigt, da nicht fertig), 
	 * Etage einfügen und Etage löschen. Jedem Button werden die entsprechenden ActionListener angehängt.
	 */
	private void initButtons(){	
		// Oben
		// Wand Button
		walls = new ToolsButton("Wand einzeichnen");
		walls.setVisible(true);
		walls.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				dA.changeBuildListener();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START; //top of space
		c.insets = new Insets(15,15,15,15);  // padding
		c.gridx = 0;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = 0;       //third row
		add(walls, c);

		// Linke Seite
		// Tür Button
		door = new ToolsButton("Tür", "textures/buttons/tuer.jpg", 20, 30);
		door.setVisible(true);
		door.addMouseListener(Action("Door", door));
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		add(door, c);
		
		
		// Rechte Seite
		// Fenster
		window = new ToolsButton("Fenster", "textures/buttons/fenster.jpg", 25, 30);
		window.setVisible(true);
		window.addMouseListener(Action("Window", window));
		c = new GridBagConstraints();
		c.insets = new Insets(0,0,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 1;
		add(window, c);
		
		
		// Rechte Seite
		// Treppe Button
		stairs = new ToolsButton("Treppe", "textures/buttons/treppe.jpg", 30, 25);
		stairs.setVisible(true);
		stairs.addMouseListener(Action("Treppe", stairs));
		c = new GridBagConstraints();
		c.insets = new Insets(0,0,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 1;
		//add(stairs, c);
		
		
		// unten
		//new Floor Button
		newFloor = new ToolsButton("Neue Etage einfügen");
		newFloor.setVisible(true);
		newFloor.addMouseListener(newFloorAction(newFloor));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(15,15,15,15);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = 3;       //third row
		add(newFloor, c);
		
		// unten
		//new Floor Button
		removeFloor = new ToolsButton("Aktuelle Etage löschen");
		removeFloor.setVisible(true);
		removeFloor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Floor actFloor = dA.getActFloor().getFloor();
					if(actFloor.hasFloorBeneath()){
						CommandManager.getInstance().execPush(new DeleteFloor(dA.getPlan(), actFloor));						
					} else {
						throw new FloorException("Erdgeschoss kann nicht gelöscht werden.");
					}
				} catch (ControlException | BuildException | FurnishException | SaveException e) {
					dA.getActFloor().displayErrorMessage(new ErrorMessage("Erdgeschoss kann nicht gelöscht werden!"));
				}
			}
		});;
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(0,15,15,15);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = 4;       //third row
		add(removeFloor, c);
		
	}
	
	/**
	 * Methode initialisiert einen ActionListener, der beim Klicken, die übergebene Message der DrawArea gibt.
	 * @param msg Message für die DrawArea
	 * @param me Button von dem der Klick kommt
	 * @return MouseAdapter der an den Button gehängt werden kann
	 */
	private MouseAdapter Action(final String msg, final JButton me){
		MouseAdapter mA = new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				dA.setRoomComponent(msg);
			}
		};
		return mA;
	}
	/**
	 * Methode initialisiert den MouseListener zum einfügen einer Etage
	 * @param me Button, der reagieren soll
	 * @return MouseAdapter der an dem Button gehängt werden kann
	 */
	private MouseAdapter newFloorAction(final JButton me){
		MouseAdapter newFloor = new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				dA.addFloor();	
			}
		};
		return newFloor;	
	}
}