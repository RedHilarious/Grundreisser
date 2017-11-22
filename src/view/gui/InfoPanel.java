package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.functions.DimensionCalculator;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.setup.ChangeFurnitureProperties;
import business.commands.setup.CreateOwnFurniture;
import business.domainModel.Component;
import business.domainModel.Furniture;
import business.domainModel.Wall;
import business.validators.RuleEnum;
/**
 * Diese Klasse stellt das Infopanel dar. Es werden immer
 * die Daten des aktuell in der Zeichenfläche selektierten 
 * Objekts dargestellt. Wenn ein neues Möbelstück erstellt werden soll,
 * wird die Anzeige des Panels etwas angepasst.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class InfoPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -7605521472090771799L;
	
	final private CommandManager cdm;
	private JLabel objectProperties;
	// Name
	private JLabel labelName;
	private JTextField tfName;
	
	// XPos
	private JLabel labelxPos;
	private JTextField tfxPos;
	
	// YPos
	private JLabel labelyPos;
	private JTextField tfyPos;
	
	// Lenght
	private JLabel labelLength;
	private JTextField tfLength;
	
	// width
	private JLabel labelwidth;
	private JTextField tfwidth;

	// Buttons
	private JButton chooseColor;
	private JButton okay;
	
	private Color choosedColor;

	private Component actObservable;
	private DrawArea dA;
	
	private GridBagConstraints c;
	private JPanel gridbagLayout;
	
	public InfoPanel(final CommandManager cdm) {
		this.cdm = cdm;
		choosedColor = null;
		setBackground(new Color(79, 79, 79));
		
		// gridbagLayout wird in North eingefügt -> alles oben am Rand
		setLayout(new BorderLayout());
		
		gridbagLayout = new JPanel(new GridBagLayout());

		initComponents();
		initListeners();
	}
	
	/**
	 * initialisiere Komponenten
	 */
	private void initComponents() {
		// Objekteigenschaften
		objectProperties = new JLabel("Objekteigenschaften");
		objectProperties.setFont(new Font("Helvetica", Font.PLAIN, 20));
		c = new GridBagConstraints();
		c.insets = new Insets(30,15,0,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		gridbagLayout.add(objectProperties, c);
		
		// Name
		labelName = new JLabel("Name:");
		c = new GridBagConstraints();
		c.insets = new Insets(15,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 1;
		gridbagLayout.add(labelName, c);
		
		tfName = new JTextField(50);
		c = new GridBagConstraints();
		c.insets = new Insets(15,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.7;
		c.gridx = 1;
		c.gridy = 1;
		gridbagLayout.add(tfName, c);
		

		// XPos
		labelxPos = new JLabel("X-Position (m):");
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		gridbagLayout.add(labelxPos, c);
		
		tfxPos = new JTextField(20);
		tfxPos.addFocusListener(Action("xPos", tfxPos));
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.7;
		c.gridx = 1;
		c.gridy = 2;
		gridbagLayout.add(tfxPos, c);


		// YPos
		labelyPos = new JLabel("Y-Position (m):");
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 3;
		gridbagLayout.add(labelyPos, c);
		
		tfyPos = new JTextField(20);
		tfyPos.addFocusListener(Action("yPos", tfyPos));
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.7;
		c.gridx = 1;
		c.gridy = 3;
		gridbagLayout.add(tfyPos, c);
		

		// Length
		labelLength = new JLabel("Länge (m):");
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 4;
		gridbagLayout.add(labelLength, c);
		
		tfLength = new JTextField(20);
		tfLength.addFocusListener(Action("xLength", tfLength));
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.7;
		c.gridx = 1;
		c.gridy = 4;
		gridbagLayout.add(tfLength, c);

		
		// width
		labelwidth = new JLabel("Breite (m):");
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		c.gridx = 0;
		c.gridy = 5;
		gridbagLayout.add(labelwidth, c);
		
		tfwidth = new JTextField(20);
		tfwidth.addFocusListener(Action("yLength", tfwidth));
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.7;
		c.gridx = 1;
		c.gridy = 5;
		gridbagLayout.add(tfwidth, c);
		
		
		// Color
		chooseColor = new JButton("Farbe auswählen");
		chooseColor.setVisible(false);
		c = new GridBagConstraints();
		c.insets = new Insets(0,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.7;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 6;
		gridbagLayout.add(chooseColor, c);
		
		okay = new JButton("Möbelstück speichern");
		okay.setVisible(false);
		c = new GridBagConstraints();
		c.insets = new Insets(15,15,15,15); // padding
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 7;
		gridbagLayout.add(okay, c);
		
		
		
		gridbagLayout.setVisible(false);
		add(gridbagLayout, BorderLayout.NORTH);
	}
	
	/**
	 * initialisiere Listener für Komponenten
	 */
	private void initListeners() {
		// Color Chooser
		chooseColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				choosedColor = JColorChooser.showDialog(chooseColor.getParent(), "Farbe auswählen", null);
			}
		});
		
		// Eigenes Möbelstück speichern
		okay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (choosedColor == null) {
						choosedColor = Color.gray;
					}
					cdm.execPush(new CreateOwnFurniture('r', tfName.getText(), 
							(int)(Double.parseDouble(tfxPos.getText())*DimensionCalculator.CONVERT_METERS), (int)(Double.parseDouble(tfyPos.getText())*DimensionCalculator.CONVERT_METERS),
							(int)(Double.parseDouble(tfLength.getText())*DimensionCalculator.CONVERT_METERS), (int)(Double.parseDouble(tfwidth.getText())*DimensionCalculator.CONVERT_METERS), 
							0.0, dA.getActFloor().getFloor(), RuleEnum.Above, null, choosedColor));
				} catch (NumberFormatException e2){
					dA.getActFloor().displayErrorMessage(new ErrorMessage("Bitte eine ganzzahlige Position eingeben."));
				} catch( ControlException | BuildException | FurnishException | SaveException e1) {
					dA.getActFloor().displayErrorMessage(new ErrorMessage(e1.getMessage()));
				}
				
				// Panel wieder zurücksetzen, dass Objekteigenschaften angezeigt werden
				objectProperties.setText("Objekteigenschaften");
				gridbagLayout.setVisible(false);
				okay.setVisible(false);
				chooseColor.setVisible(false);
			}
		});
	}
	
	/**
	 * Editiert den Inhalt der einzelnen Zellen.
	 * Wird aufgerufen, wenn ein neues/anderes 
	 * Element selektiert wird.
	 * @param actPanel aktuell selektiertes Panel
	 */
	public void editContent(JPanel actPanel) {
		objectProperties.setText("Objekteigenschaften");
		chooseColor.setVisible(false);
		okay.setVisible(false);
		
		if (actObservable != null) {
			actObservable.deleteObserver(this);
		}
		
		if (actPanel == null) {
			gridbagLayout.setVisible(false);
		}
		
		if (actPanel instanceof WallPanel) {
			WallPanel actWallPanel = (WallPanel) actPanel;
			Wall actWall = actWallPanel.getWall();
			
			// adde Observer
			actWall.addObserver(this);
			actObservable = actWall;
			
			// Deaktiviere Textfelder, da keine Änderungen vorgenommen werden dürfen
			tfName.setEditable(false);
			tfxPos.setEditable(false);
			tfyPos.setEditable(false);
			tfLength.setEditable(false);
			tfwidth.setEditable(false);
			
			tfName.setText("Wall");
			tfxPos.setText(actWall.getStartX() / DimensionCalculator.CONVERT_METERS + "");
			tfyPos.setText(actWall.getStartY() / DimensionCalculator.CONVERT_METERS + "");
			tfLength.setText(actWall.getDisX() / DimensionCalculator.CONVERT_METERS + "");
			tfwidth.setText(actWall.getDisY() / DimensionCalculator.CONVERT_METERS + "");
			
			gridbagLayout.setVisible(true);
			repaint();
		}
		
		if (actPanel instanceof FurniturePanel) {
			FurniturePanel actFurniturePanel = (FurniturePanel) actPanel;
			Furniture actFurniture = actFurniturePanel.getFurniture();
			
			// adde Observer
			actFurniture.addObserver(this);
			actObservable = actFurniture;
			
			tfName.setEditable(true);
			tfxPos.setEditable(true);
			tfyPos.setEditable(true);
			tfLength.setEditable(true);
			tfwidth.setEditable(true);
			
			tfName.setText(actFurniture.getName());
			tfxPos.setText(actFurniture.getXPos() / DimensionCalculator.CONVERT_METERS + "");
			tfyPos.setText(actFurniture.getYPos() / DimensionCalculator.CONVERT_METERS + "");
			tfLength.setText(actFurniture.getXLength() / DimensionCalculator.CONVERT_METERS + "");
			tfwidth.setText(actFurniture.getYLength() / DimensionCalculator.CONVERT_METERS + "");
			
			gridbagLayout.setVisible(true);
			repaint();
		}
	}
	
	/**
	 * Update-Methode für geänderte Eigenschaften des beobachteten Models.
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Furniture) {
			Furniture actFurniture = (Furniture) arg0;

			tfName.setEditable(true);
			tfxPos.setEditable(true);
			tfyPos.setEditable(true);
			tfLength.setEditable(true);
			tfwidth.setEditable(true);
			
			tfName.setText(actFurniture.getName());
			tfxPos.setText(actFurniture.getXPos() / DimensionCalculator.CONVERT_METERS + "");
			tfyPos.setText(actFurniture.getYPos() / DimensionCalculator.CONVERT_METERS + "");
			tfLength.setText(actFurniture.getXLength() / DimensionCalculator.CONVERT_METERS + "");
			tfwidth.setText(actFurniture.getYLength() / DimensionCalculator.CONVERT_METERS + "");

			repaint();
		}
	}
	
	/**
	 * Adapter, der triggert, wenn ein Input-Feld den Focus verliert
	 * 
	 * @param key Identifikation
	 * @param tf Textfeld
	 * @return Focusadapter
	 */
	public FocusAdapter Action(final String key, final JTextField tf) {
		return new FocusAdapter() {

			public void focusLost(FocusEvent arg0) {
				final int newValue = (int)(Double.parseDouble(tf.getText())*DimensionCalculator.CONVERT_METERS);
				
				if (actObservable instanceof Furniture) {
					Furniture actFurniture = (Furniture) actObservable;

					try {
						cdm.execPush(new ChangeFurnitureProperties(actFurniture, new HashMap<String, Object>() {
							private static final long serialVersionUID = -8220804237378048184L; 
							{
							put(key, newValue);
							}
						}));
					} catch (ControlException | BuildException | FurnishException | SaveException e) {
						dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
					}
				}
			}
		};
	}

	public void setDrawArea(DrawArea dA) {
		this.dA = dA;
	}
	
	/**
	 * Setzt alle Felder auf leer und fügt einen Button zum 
	 * Speichern des Möbelstücks und eine Farbauswahl hinzu.
	 */
	public void createOwnFurniture() {
		objectProperties.setText("Eigenes Möbelstück");
		
		// Setze alle Felder auf leer
		tfName.setText("");
		tfLength.setText("");
		tfwidth.setText("");
		tfxPos.setText("");
		tfyPos.setText("");
		
		if (actObservable != null) {
			actObservable.deleteObserver(this);
		}
		actObservable = null;
		
		// Zeige Buttons
		gridbagLayout.setVisible(true);
		chooseColor.setVisible(true);
		okay.setVisible(true);
	};
}