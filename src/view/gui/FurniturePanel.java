package view.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import view.functions.DimensionCalculator;
import view.states.LockMode;
import view.states.LockedMode;
import view.states.UnlockedMode;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.setup.ChangeFurnitureProperties;
import business.domainModel.Furniture;
/**
 * Diese Klasse stellt ein Möbelstück aus dem Domainmodel dar und observed es.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class FurniturePanel extends JPanel implements Observer {

	private static final long serialVersionUID = 2256887642778804422L;
	// Modusverwaltung des Möbelstücks
	private LockMode active, lock, unlock;
	private boolean locked;
	private FloorPanel floor;
	private Furniture furniture;
	private CommandManager cdm;
	private DrawArea dA;

	private int xPos;
	private int yPos;
	private int xLen;
	private int yLen;
	private String name;
	private char form;
	private double angle;
	private Color color;
	private int startX;
	private int startY;
	private int absStartX;
	private int absStartY;
	private boolean dragged = false;
	private MouseMotionAdapter myMouseMotionListener;
	private MouseAdapter myMouseListener;
	
	private DimensionCalculator calculator;

	private BufferedImage texture;

	public FurniturePanel(final FloorPanel floor, final Furniture furniture,
			final CommandManager cdm, DrawArea dA) {
		calculator = DimensionCalculator.getInstance();

		this.floor = floor;
		this.furniture = furniture;
		this.cdm = cdm;
		this.dA = dA;
		this.xPos = calculator.centimetersToPixel(furniture.getXPos());
		this.yPos = calculator.centimetersToPixel(furniture.getYPos());
		this.xLen = calculator.centimetersToPixel(furniture.getXLength());
		this.yLen = calculator.centimetersToPixel(furniture.getYLength());
		this.name = furniture.getName();
		this.form = furniture.getForm();
		this.angle = furniture.getAngle();
		this.color = furniture.getColor();
		if (this.color == null) {
			this.color = Color.gray;
		}
		
		// Modi initialisieren
		lock = new LockedMode(this);
		unlock = new UnlockedMode(this);
		// Default Modus (unlocked) setzen
		setMode(unlock);
		
		String texturePath = furniture.getTexturePath();
		
		if (texturePath != null && texturePath != "") {
			try {
				texture = ImageIO.read(new File(furniture.getTexturePath()));
			} catch (IOException e1) {
				// Bild konnte nicht geladen werden -> nehme Farbe
			}
		} else {
			setBackground(color);
		}
			
		
		setBounds(xPos, yPos, yLen, xLen);
		setVisible(true);
		
		//initialisieren der MouseListener
		myMouseListener = FurnitureAction(this, calculator);		
		myMouseMotionListener = new MouseMotionAdapter() {
			// Maus mit gedrückter Taste gezogen
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				// relative Verschiebung zu Drag-Startposition ermitteln
				int verschiebungX = e.getXOnScreen() - absStartX;
				int verschiebungY = e.getYOnScreen() - absStartY;

				// Zur Visualisierung der Drag-Operation verschieben,
				// Model wird erst nach Loslassen (mouseRelease) aktualisiert,
				// das Model muss ja nicht jede Zwischenposition mitbekommen.
				setLocation(startX + verschiebungX, startY + verschiebungY);
				dragged = true;
			}
		};
		
		//mouseListener anhängen
		addMouseMotionListener(myMouseMotionListener);
		addMouseListener(myMouseListener);

		display();
	}

	/**
	 * Methode zum Anzeigen des Möbelstücks. Leitet Anzeige weiter an
	 * Zustandsobjekt.
	 */
	public void display() {
		active.display(this);
	}
	/**
	 * entfernt Listener
	 */
	public void removeListener(){
		removeMouseListener(myMouseListener);
		removeMouseMotionListener(myMouseMotionListener);
	}
	/**
	 * fügt Listener hinzu
	 */
	public void addListener(){
		addMouseListener(myMouseListener);
		addMouseMotionListener(myMouseMotionListener);
	}
	
	public void setMode(LockMode active) {
		this.active = active;
	}

	public LockMode getMode() {
		return active;
	}

	public void switchMode() {
		active = locked ? unlock : lock;
	}
	
	/**
	 * Update-Methode für geänderte Eigenschaften des beobachteten Models.
	 */
	public void update(Observable o, Object arg) {
		Furniture furn = (Furniture) o;

		if ("xPos".equals(arg)){
			xPos = calculator.centimetersToPixel(furn.getXPos());
		}
		if ("yPos".equals(arg)) {
			yPos = calculator.centimetersToPixel(furn.getYPos());
		}
		if ("xLenght".equals(arg)) {
			xLen = calculator.centimetersToPixel(furn.getXLength());
		}
		if ("yLenght".equals(arg)) {
			yLen = calculator.centimetersToPixel(furn.getYLength());
		}
		if ("angle".equals(arg)) {
			angle = furn.getAngle();
		}
		if ("moved".equals(arg)) {
			xPos = calculator.centimetersToPixel(furn.getXPos());
			yPos = calculator.centimetersToPixel(furn.getYPos());
		}
		
		setBounds(xPos, yPos, yLen, xLen);
	}
	
	/**
	 * Mausadapter für Drag'n'Drop der Möbelstücke
	 */
	private MouseAdapter FurnitureAction(final FurniturePanel fP,
			final DimensionCalculator calculator) {

		MouseInputAdapter mA = new MouseInputAdapter() {
			// Maus losgelassen: Model anpassen
			public void mouseReleased(final MouseEvent e) {
				super.mouseReleased(e);
				if (dragged) {

					try {
						final int moveX = calculator.pixelToCentimeters(e
								.getXOnScreen() - absStartX);
						final int moveY = calculator.pixelToCentimeters(e
								.getYOnScreen() - absStartY);
						
						// hier vllt eher MoveFurniture?
						cdm.execPush(new ChangeFurnitureProperties(furniture,
								new HashMap<String, Object>() {
									private static final long serialVersionUID = 2305766499451769063L;
									{
										put("xPos", moveX);
										put("yPos", moveY);
									}
								}));
					} catch (ControlException | BuildException
							| FurnishException | SaveException e1) {
						dA.getActFloor().displayErrorMessage(new ErrorMessage(e1.getMessage()));
						setLocation(startX, startY);
					}
				}
				dragged = false;
			}

			// Maus gedrückt: Merken, wo eventuelles Draggen losgeht
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				// relative Position innerhalb des Canvas merken
				startX = getX();
				startY = getY();
				// absolute Position auf dem Bildschirm (für
				// Verschiebungsverfolgung)
				absStartX = e.getXOnScreen();
				absStartY = e.getYOnScreen();
				
				fP.setForeground(new Color(0, 0, 0, 1.0f));
				fP.validate();
				fP.repaint();
				dA.requestFocusInWindow();
				floor.mark(fP);
			}

			public void mouseClicked(MouseEvent arg0) {
				fP.setForeground(new Color(0, 0, 0, 1.0f));
				fP.validate();
				fP.repaint();
				dA.requestFocusInWindow();
				floor.mark(fP);
			}
		};
		return mA;
	}

	public Furniture getFurniture() {
		return furniture;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(texture != null)
			g.drawImage(texture, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	public Color getColor() {
		return color;
	}
}