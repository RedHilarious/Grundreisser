package business.domainModel;

import static java.lang.Math.min;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.FurnitureException;
import Exceptions.SaveException;
import Exceptions.WallException;
import business.Store;
import business.validators.Responsibility;
import business.validators.RuleEnum;
import business.validators.Validator;
import business.validators.ValidatorLength;
import business.validators.ValidatorRuleKeeper;
import business.validators.ValiderEnum;
/**
 * Klasse ist ein Model einer Etage
 *  @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Floor extends Observable implements Serializable {

	private static final long serialVersionUID = -4212961923646418113L;
	
	private String name;
	private int number;
	private Wall northernWall = null;
	
	private ArrayList<Component> walls;
	private ArrayList<Component> furniture;
	
	private Plan plan;
	private Floor flBeneath;
	
	private transient Store store;
	
	public Floor(int number, Plan plan, Floor flBeneath, Floor flAbove) {
		this.number = number;
		this.walls = new ArrayList<Component>();
		this.furniture = new ArrayList<Component>();
		this.plan = plan;
		this.flBeneath = flBeneath;
		
		// Hole Instanz von Store, um Furniture Objekte zu platzieren
		store = Store.getInstance();
	}
	/**
	 * Methode platziert Wände auf der Etage
	 * @param x1Pos Anfangsposition x
	 * @param y1Pos Anfangsposition y
	 * @param x2Pos Endposition x
	 * @param y2Pos Endposition y
	 * @return die erstellet Wall
	 * @throws BuildException bei Problemen beim erstellen
	 * @throws ControlException bei Validator Problemem
	 */
	public Wall createWall(int x1Pos, int y1Pos, int x2Pos, int y2Pos) throws BuildException, ControlException{
		Wall newWall = new Wall(walls.size(), x1Pos, y1Pos, x2Pos, y2Pos, plan);
		
		searchWallLinking(newWall);
		
		// alle Validatoren in der Liste überprüfen
		ArrayList<Validator> bla = plan.getValidators(ValiderEnum.wall);
		
		Iterator<Validator> it = bla.iterator();
		Validator v;
		while(it.hasNext()){
			v = it.next();
			// wenn der validator für diese Operation zuständig
			if(v.isResponsible(Responsibility.createWall)){
				
				if(flBeneath != null || v instanceof ValidatorLength) { // wenn überhaupt eine Etage darunter ist
					if (!v.validate(newWall, flBeneath == null? null : flBeneath.walls)) {
						throw new BuildException("Wand entweder außerhalb der Wände er unteren Etage, oder zu kurz.");
					}
				}
			}
		}
		
		// wenn alles geklappt hat --> hinzufuegen
		walls.add(newWall);
		
		//prüfen ob die neue Wand nördlicher als die anderen sind
		if (northernWall == null) {
			northernWall = newWall;
		} else if (min(y1Pos, y2Pos) < min(northernWall.getStartY(),
				northernWall.getStartY())) {
			northernWall = newWall;
		}
		
		Map<String, Wall> changes = new HashMap<String, Wall>();
		changes.put("create", newWall);
		
		//Observer informieren
		setChanged();
        notifyObservers(changes);
        
		return newWall;
	}
	/**
	 * Methode "verlinkt" die Wände miteinanger, als aufeinangerfolgende, werden erkannt und miteinander verbunden.
	 * @param newWall neu eingefügte Wand, die auf nachbar Wände getestet wird.
	 */
	private void searchWallLinking(Wall newWall){
		// Finden der vorherigen Wand --> neue Wand ist Nachfolger der vorherigen Wand
		Wall compareWall;
		for(Component cp: walls){
			compareWall = (Wall) cp;
			
			int cpWllX = compareWall.getEndX();
			int cpWllY = compareWall.getEndY();
			int margin = 30;
			
			boolean matchingX = (cpWllX + margin >= newWall.getStartX() && cpWllX - margin <= newWall.getStartX());
			boolean matchingY = (cpWllY + margin >= newWall.getStartY() && cpWllY - margin <= newWall.getStartY());
			
			if(matchingX && matchingY){
				
				// verlinken der Waende
				compareWall.setWallAfter(newWall);
				
				
				// Ende der Vergleichswand ist == Start der neu erstellten Wand
				// setze Vergleichswand als vorherige Wand der neuen Wand
				
				// Start x der neuen Wall auf den Wert der compWall setzen
				compareWall.setStartX(newWall.getEndX());
				// Start y der neuen Wall auf den Wert der compWall setzen
				compareWall.setStartY(newWall.getEndY());
				
				if(compareWall.getWallAfter() != null){
					compareWall.getWallAfter().setStartY(compareWall.getEndY());
				}
				newWall.setWallAfter(compareWall);
			}
		}
	}
	
	/**
	 * Methode löscht Wand von der Etage.
	 * @param delWall Wand die gelöscht werden soll
	 * @throws WallException Fehler beim löschen einer Wand
	 */
	public void deleteWall(Wall delWall) throws WallException {
		boolean deleted = walls.remove(delWall);
		
		if (!deleted) {
			throw new WallException("Objekt nicht gefunden");
		}
		
		Map<String, Wall> changes = new HashMap<String, Wall>();
		changes.put("delete",delWall);
		
		//Observer informieren
	    setChanged();
	    notifyObservers(changes);
	    
        delWall = null;
	}
	/**
	 * Methoder ermöglicht es ein eigenes Möbelstüch zu generieren mit folgenden Attributen
	 * @param form des neuen Möbelstücks
	 * @param name des neuen Möbelstücks
	 * @param xPos des neuen Möbelstücks
	 * @param yPos des neuen Möbelstücks
	 * @param xLength des neuen Möbelstücks
	 * @param yLength des neuen Möbelstücks
	 * @param angle des neuen Möbelstücks
	 * @param placeRule des neuen Möbelstücks
	 * @param texturePath des neuen Möbelstücks
	 * @param color des neuen Möbelstücks
	 * @return neues Möbelstück
	 * @throws SaveException, wenn beim Speichern des neuen Möbelstücks als JSON ein Fehler auftritt.
	 */
	public Furniture createNewFurniture(char form, String name, int xPos, int yPos,
			int xLength, int yLength, double angle, RuleEnum placeRule, String texturePath, Color color) throws SaveException {
		
		Furniture newFurniture = new Furniture(furniture.size(), form, name, xPos, yPos,
				xLength, yLength, angle, this, placeRule, null, texturePath, color);
		furniture.add(newFurniture);
		store.addNewFurniture(newFurniture);
		
		Map<String, Furniture> changes = new HashMap<String, Furniture>();
		changes.put("create", newFurniture);
		
		// Observer informieren
		setChanged();
        notifyObservers(changes);
		
        return newFurniture;
	}

	/**
	 * Methode platziert Möbelstück auf der Etage
	 * @param furnitureId Id des zu platzierenden Möbelstücks
	 * @param xPos Position X auf der Etage
	 * @param yPos Position Y auf der Etage
	 * @return platziertes Möbelstück
	 * @throws ControlException Validatorfehler
	 * @throws FurnishException Fehler beim Erstellen des Möbelsstücks
	 */
	
	public Furniture putNewFurniture(int furnitureId, int xPos, int yPos) throws ControlException, FurnishException {
		// entsprechendes Furniture aus dem Store holen und Position setzen
		Furniture newFurniture = store.getNewFurnitureById(furnitureId);
		newFurniture.setXPos(xPos);
		newFurniture.setYPos(yPos);	
		newFurniture.setMyFloor(this);

		// Position validieren
		ArrayList<Validator> validators = plan.getValidators(ValiderEnum.furniture);
		Iterator<Validator> it = validators.iterator();
		Validator v;
		while(it.hasNext()){
			v = it.next();
			// wenn der validator für diese Operation zuständig
			if(v.isResponsible(Responsibility.moveFurniture)){
				ArrayList<Component>	input;
				input = v instanceof ValidatorRuleKeeper? furniture : walls;
				if (!v.validate(newFurniture, input)) {
					throw new FurnitureException("Möbel kann hier nicht platziert werden.");
				}
			}
		}
		
		furniture.add(newFurniture);
		
		Map<String, Furniture> changes = new HashMap<String, Furniture>();
		changes.put("create", newFurniture);
		
		setChanged();
        notifyObservers(changes);
		
		return newFurniture;
	}
	/**
	 * Methode löscht ein Möbelstück von der Etage.
	 * @param rmFurniture Das zu löschende Möbelstück.
	 * @throws FurnitureException zu Löschendes Möbelstück existiert nicht
	 */
	public void removeFurniture(Furniture rmFurniture)
			throws FurnitureException {
		if (!furniture.remove(rmFurniture)) {
			throw new FurnitureException("Objekt nicht gefunden");
		}
		
		Map<String, Furniture> changes = new HashMap<String, Furniture>();
		changes.put("delete", rmFurniture);
		
		
		setChanged();
        notifyObservers(changes);
		
		rmFurniture = null;
	}
	/**
	 * Methode zum Platzieren einer Treppe auf der Etage, nicht benutzt
	 * @param stairs Treppe, die platziert werden soll
	 * @throws BuildException Fehler beim Erstellen der Treppe
	 * @throws ControlException Fehler bei der Validierung
	 */
	public void addStairs(RoomComponent stairs) throws BuildException, ControlException {
		// alle Validatoren in der Liste überprüfen
		for (Validator v : plan.getValidators(ValiderEnum.components)) {
			// wenn der validator für diese Operation zuständig
			if(v.isResponsible(Responsibility.createBuildElement)){
				if (!v.validate(stairs, furniture)) {
					throw new BuildException("Unpossible Place for stairs.");
				}
			}
		}
		// wenn alles valid ist --> hinzufuegen
		furniture.add(stairs);
	}
	
	
	public Plan getPlan(){
		return plan;
	}
	
	public Floor getFlBeneath(){
		return flBeneath;
	}
	
	public void setFlBeneath(Floor flBeneath){
		this.flBeneath = flBeneath;
	}
	
	public Floor getFlAbove(){
		return flBeneath;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNr() {
		return this.number;
	}

	public void setNr(int number) {
		this.number = number;
	}

	public Wall getWall(int number) {
		return (Wall)walls.get(number);
	}

	public ArrayList<Component> getFurniture() {
		return furniture;
	}
	
	public ArrayList<Component> getWalls(){
		return walls;
	}
	
	public Wall getNorthernWall(){
		return northernWall;
	}
	
	public boolean hasFloorBeneath(){
		return flBeneath != null;
	}
	/**
	 * Methode zu Anzeigen eines geladenenen Plans, für alle Möbel und Walls wird ein Panel erzeugt
	 */
	public void informObserversLoad() {
		
		for (Component cp : walls) {
			
			Map<String, Component> changes = new HashMap<String, Component>();
			changes.put("create", cp);
			
			setChanged();
	        notifyObservers(changes);
		}
		
		for (Component cp : furniture) {
			
			Map<String, Component> changes = new HashMap<String, Component>();
			changes.put("create", cp);
			
			setChanged();
	        notifyObservers(changes);
		}

	}
}