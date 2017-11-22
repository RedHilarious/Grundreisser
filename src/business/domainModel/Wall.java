package business.domainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Exceptions.BuildException;
import Exceptions.ControlException;

import com.google.gson.annotations.Expose;
/**
 * Klasse ist ein Model einer Wand
 *@author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Wall extends Component{
	
	private static final long serialVersionUID = 8780539755163093193L;

	//Standardwanddicke nach baumarkt.de
	final public static int WALLTHICK = 26;
	private char direction;
	@Expose
	private int id;
	@Expose
	private int startX;
	@Expose
	private int startY;
	@Expose
	private int endX;
	@Expose
	private int endY;
	@Expose
	private int disX;
	@Expose
	private int disY;
	
	private Wall before;
	private Wall after;
	
	private Plan plan;
	@Expose
	private ArrayList<Component> buildComponents;
	
	public Wall(int id, int firstX, int firstY, int xPos, int yPos, Plan plan){
		this.startX = firstX;
		this.startY = firstY;
		this.endX = xPos;
		this.endY = yPos;
		this.id = id;
		this.plan = plan;
		
		calculateCoordinates();
		// Liste von Baukomponenten einer Wand
		
		buildComponents = new ArrayList<Component>();
	}
	/**
	 * Methode errechnet Koordinaten, da nur horizontale und vertikale Wände erlaubt sind
	 */
	private void calculateCoordinates(){
		//Koordinaten verändern um nur horizontale und vertikale Wände zuzulassen
		disX = endX - startX;
		disY = endY - startY;

		if (Math.abs(disX) == Math.max(Math.abs(disX),Math.abs(disY))) {
			endY = startY;
			disY = WALLTHICK;
			direction = 'h';
		} else {
			endX = startX;
			disX = WALLTHICK;
			direction = 'v';
		}

	}
	
	public int getId(){
		return id;
	}
	
	public int getStartX() {
		return this.startX;
	}

	public void setStartX(int xPos) {
		this.startX = xPos;
	}

	public int getStartY() {
		return this.startY;
	}

	public void setStartY(int yPos) {
		this.startY = yPos;
	}
	public int getEndX() {
		return this.endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return this.endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}
	
	public int getDisX() {
		return disX;
	}
	
	public int getDisY() {
		return disY;
	}
	
	public Wall getWallBefore(){
		return before;
	}
	
	public Wall getWallAfter(){
		return after;
	}
	
	public void setWallBefore(Wall newBefore){
		before = newBefore;
	}
	
	public void setWallAfter(Wall newAfter){
		after = newAfter;
	}
	
	public ArrayList<Component> getBuildComponents(){
		return buildComponents;
	}
	/**
	 * Methode fügt er Wand Bauelemente, wie Fenster, und Türen hinzu
	 * @param name Name des Bauelements
	 * @param x Position X
	 * @param y Posotion Y
	 * @return platziertes Möbelstück
	 * @throws BuildException
	 * @throws ControlException
	 */
	public RoomComponent addBuildComponent(String name, int x, int y) throws BuildException, ControlException {
		RoomComponent rc= null;

		if(name.equals("Door")){
			rc = new Door(direction, x, y, WALLTHICK);
		}
		if(name.equals("Window")){
			rc = new Window(direction, x, y, WALLTHICK);
		}
		

		buildComponents.add(rc);
		
		Map<String, RoomComponent> changes = new HashMap<String, RoomComponent>();
		changes.put("create", rc);
		
		//Observer informieren
		setChanged();
        notifyObservers(changes);
        
		return rc;
	}
	/**
	 * Methode löscht Bauelement aus einer Wand
	 * @param rC
	 */
	public void removeBuildComponent(RoomComponent rC){
		buildComponents.remove(rC);
		
		Map<String, RoomComponent> changes = new HashMap<String, RoomComponent>();
		changes.put("delete", rC);
		
		//Observer informieren
		setChanged();
        notifyObservers(changes);
	}
	
	public Wall clone(){
		return new Wall(id, startX,startY, endX, endY, plan);
	}
}