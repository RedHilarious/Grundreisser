package view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import view.functions.DimensionCalculator;
import view.states.BuildMode;
import view.states.StateMode;
import business.commands.CommandManager;
import business.domainModel.Component;
import business.domainModel.Floor;
import business.domainModel.Furniture;
import business.domainModel.Wall;
/**
 * Diese Klasse stellt eine Ebene aus dem Domainmodel dar und observed diese.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class FloorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 231475470104756737L;
	private DrawArea dA;
	private Floor floor;
	private CommandManager cdm;
	private StateMode state;
	private ArrayList<WallPanel> walls;
	private ArrayList<FurniturePanel> furnitures;
	
	private static final Color RASTERCOLOR = new Color(200, 200, 200);
	private static final int RASTERGAB = 60;

	public FloorPanel(final Floor floor, DrawArea dA,
			StateMode state, CommandManager cdm) {
		this.dA = dA;
		this.floor = floor;
		this.state = state;
		this.cdm = cdm;
		
		// several Panel-Configuration
		setPreferredSize(new Dimension(200, 160));
		setBackground(Color.WHITE);

		// setze Null-Layout damit Elemente absolut positioniert werden
		setLayout(null);
		setVisible(true);

		walls = new ArrayList<WallPanel>();
		furnitures = new ArrayList<FurniturePanel>();
		state.handle(this);
	}

	public void del(JPanel pane) {
		state.del(pane);
	}

	public void mark(JPanel cp) {
		dA.setActPanel(cp);
	}

	/**
	 * Update-Methode für geänderte Eigenschaften des beobachteten Models.
	 */
	public void update(Observable o, Object arg) {
		// Änderungsmap (geänderte Komponente auf Änderung als String)
		@SuppressWarnings("unchecked")
		Map<String,Component> changes = (Map<String,Component>) arg;
		
		DimensionCalculator calculator = DimensionCalculator.getInstance();
		Component cp = changes.get("create");
		if(cp instanceof Wall){
			
			// neues WandPanel erstellen; Gui-Element erzeugen
			Wall w = (Wall) cp;
			System.out.println("#### Start " + w.getStartX() + " " + w.getStartY() + " End " + w.getEndX() + " " + w.getEndY());
			WallPanel wall = new WallPanel(this, w,dA, cdm);

			// View heiratet Model
			w.addObserver(wall);

			// gui-Anzeigen
			add(wall);
			walls.add(wall);
			wall.setVisible(true);

			// Anfangskoordinaten für die nächste Wand setzen
			if (state instanceof BuildMode) {
				BuildMode bM = (BuildMode) state;
				bM.setFirstX(calculator.centimetersToPixel(w.getEndX()));
				bM.setFirstY(calculator.centimetersToPixel(w.getEndY()));
			}
		} else if (cp instanceof Furniture){
			
			// neues FurniturePanel erzeugen
			Furniture f = (Furniture) cp;
			FurniturePanel fP = new FurniturePanel(this, f, cdm, dA);

			// verheiraten
			f.addObserver(fP);

			// in Furnitureliste packen
			furnitures.add(fP);
			
			// gui-Anzeigen
			add(fP);
			fP.setVisible(true);
		}
		
		
		cp = changes.get("delete");
		if(cp instanceof Wall){
			
			// vorhandene WandPanel löschen
			for (WallPanel w : walls) {
				if (w.getWall() == (Wall) cp) {
					remove(w);
					walls.remove(w);
					w = null;
					break;
				}
			}
			BuildMode bM = (BuildMode) state;
			if(walls.size() < 2){
				bM.setFirstX(-1);
				bM.setFirstY(-1);
			} else {
				Wall wl = walls.get(walls.size()-1).getWall();
				bM.setFirstX(calculator.centimetersToPixel(wl.getEndX()));
				bM.setFirstY(calculator.centimetersToPixel(wl.getEndY()));
			}
			
		} else if(cp instanceof Furniture){
			
			// vorhandenes FurniturePanel löschen
			for(FurniturePanel fp: furnitures){
				if (fp.getFurniture() == (Furniture) cp) {
					remove(fp);
					furnitures.remove(fp);
					fp = null;
					break;
				}
			}
		}
	
		repaint();
	}

	public StateMode getState(){
		return state;
	}
	
	public void setState(StateMode active) {
		state = active;
		for(MouseListener mL : getMouseListeners()){
			removeMouseListener(mL);
		}
		
		active.handle(this);
	}
	
	public ArrayList<FurniturePanel> getFurnitures(){
		return furnitures;
	}
	public ArrayList<WallPanel> getWalls(){
		return walls;
	}
	public DrawArea getDrawArea() {
		return dA;
	}

	public Floor getFloor() {
		return floor;
	}

	/**
	 * Überschriebene Paint-Methode, um Linien anzuzeigen.
	 */
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(RASTERCOLOR);

		int countMeters = 0;
		
		for (int i = 0; i < (int) getSize().getWidth(); i += RASTERGAB) {
			if(i % 4 == 0){

				String text = countMeters + " m";
				countMeters++;
				g2.setFont(new Font("Arial", Font.PLAIN, 15));
				g2.drawString(text, i+3, 20);
			}
			g2.drawLine(i, 0, i, (int) getSize().getHeight());
		}

		countMeters = 0;
		
		for (int i = 0; i < (int) getSize().getHeight(); i += RASTERGAB) {
			if(i % 4 == 0){
				String text = countMeters + " m";
				countMeters++;

				g2.setFont(new Font("Arial", Font.PLAIN, 15));
				g2.drawString(text, 5, i-2);
			}
			g2.drawLine(0, i, (int) getSize().getWidth(), i);
		}
	}
	
	/**
	 * Zeigt error-Message an und startet den Timer.
	 * 
	 * @param errorMessage
	 */
	public void displayErrorMessage(ErrorMessage errorMessage) {
		errorMessage.setBound(getWidth()-1000, 50, 1000, 70);
		errorMessage.setOpaque(false);
		errorMessage.setVisible(true);
		errorMessage.startTimer();
		add(errorMessage);
		validate();
		repaint();
	}
}