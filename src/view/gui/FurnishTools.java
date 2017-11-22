package view.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import business.Store;
import business.domainModel.Furniture;
/**
 * Diese Klasse erweitert ein JPanel und stellt die verfügbaren Möbelstücke dar.
 * Sie bietet die Möglichkeit ein Möbelstück auszuwählen, welches dann
 * in der Zeichenfläche platziert werden kann.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class FurnishTools extends JPanel implements Observer {
	
	private static final long serialVersionUID = 4042055984892772903L;
	
	private DrawArea dA;
	private List<JButton> buttons;
	private JLabel notReleased;
	private Store store;
	private InfoPanel iP;
	private boolean released;
	private GridBagConstraints c;
	private int cellX ;
	private int cellY;
	private JButton ownFurniture;
	
	
	public FurnishTools(final DrawArea dA, final InfoPanel iP) {
		this.dA = dA;
		this.iP = iP;

		this.released = false;
		
		
		store = Store.getInstance();
		store.addObserver(this);
		buttons = new LinkedList<JButton>();
		
		setBackground(Color.gray);
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		cellX = 0;
		cellY = 0;
		
		Set<Entry<Integer, Furniture>> available = store.getAvailableFurnitureList().entrySet();
		if(available.size() == 0) {
			JOptionPane.showMessageDialog(null, "Möbel konnten nicht eingelesen werden. Ggfs. den Pfad überprüfen.");
		} else {
			// gehe alle Möbelstücke durch und füge entsprechend Buttons ein
			for (Entry<Integer, Furniture> entry : available) {
			
				// Button erzeugen
				JButton aktButton = new ToolsButton(entry.getValue().getName(), entry.getValue().getTexturePath(), 20, 30);
			
				// Liste hinzufügen
				buttons.add(aktButton);
			
				// Button ActionListener anhängen
				aktButton.addMouseListener(Action(entry.getKey()));
			
				c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 0.5;
				// zu Panel hinzufügen
				if (cellX % 2 == 0) { // linke Seite
					c.insets = new Insets(15,15,0,15); // padding
					c.gridx = cellX % 2;
					c.gridy = cellY;
				} else {
					// rechte Seite
					c.insets = new Insets(15,0,0,15); // padding
					c.gridx = cellX % 2;
					c.gridy = cellY;
					cellY++;
				}
				cellX++;
				add(aktButton, c);
			}
		}
		
		// Eigenes Möbelstück erstellen
		ownFurniture = new ToolsButton("eigenes Möbelstück erstellen");
		ownFurniture.setVisible(true);
		ownFurniture.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				iP.createOwnFurniture();
				
			}
		});
		buttons.add(ownFurniture);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(15,15,15,15);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = cellY+1;       
		add(ownFurniture, c);
		
		
		// Dieser Modus ist noch nicht freigeschaltet
		notReleased = new JLabel("Dieser Modus ist noch nicht frei geschaltet.");
		add(notReleased);
		
		switchDisplay();
	}
	
	private void switchDisplay(){
		notReleased.setVisible(!released);
		
		for(JButton jb : buttons){
			jb.setVisible(released);
		}
	}
	
	private MouseAdapter Action(final int furnitureId){
		MouseAdapter act = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dA.setFurnishId(furnitureId);
			}
		};
		return act;
	}
	
	public void unlock(boolean lock){
		this.released = lock;
		switchDisplay();
	}
	
	/**
	 * Update-Methode für geänderte Eigenschaften des beobachteten Models.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		HashMap<String, Integer> hM = (HashMap)arg1;
		String name=(String)hM.keySet().toArray()[0];
		
		JButton aktButton = new ToolsButton(name);
		// Liste hinzufügen
		buttons.add(aktButton);
		// Button ActionListener anhängen
		aktButton.addMouseListener(Action(hM.get(name)));
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		// zu Panel hinzufügen
		if (cellX % 2 == 0) { // linke Seite
			c.insets = new Insets(15,15,0,15); // padding
			c.gridx = cellX % 2;
			c.gridy = cellY;
		} else {
			// rechte Seite
			c.insets = new Insets(15,0,0,15); // padding
			c.gridx = cellX % 2;
			c.gridy = cellY;
			cellY++;
		}
		cellX++;
		
		// zu Panel hinzufügen
		add(aktButton, c);
		
		// Füge ownFurniture-Button in neue Zeile ein
		remove(ownFurniture);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(15,15,15,15);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = cellY+1;  
		add(ownFurniture, c);
		
		repaint();
	}

}
