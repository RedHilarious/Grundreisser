package business.domainModel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FloorException;
import business.validators.Responsibility;
import business.validators.Validator;
import business.validators.ValidatorComponentCollides;
import business.validators.ValidatorIsClosed;
import business.validators.ValidatorLength;
import business.validators.ValidatorNotInAir;
import business.validators.ValidatorPointInFloor;
import business.validators.ValidatorRuleKeeper;
import business.validators.ValiderEnum;
/**
 * Klasse ist des Model eines Bauplans, er enthält alle Etagen
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Plan extends Observable implements Serializable {

	private static final long serialVersionUID = 28620284204329072L;
	private String name;
	private ArrayList<Floor> floors;
	private transient ArrayList<Validator> tempValidator;
	private transient Map<ValiderEnum, ArrayList<Validator>> validators;

	public Plan() {
		floors = new ArrayList<Floor>();
		
		createValidators();
	}
	/**
	 * Methoder initialisiert alle im Plan verwendeten Validatioren und ordnet sie den entsprechenden Enums zu
	 */
	private void createValidators(){
		validators = new HashMap<ValiderEnum, ArrayList<Validator>>();

		// Komponenten - Validatoren
		tempValidator = new ArrayList<Validator>();
		tempValidator.add(new ValidatorLength());
		tempValidator.add(new ValidatorNotInAir());
		validators.put(ValiderEnum.wall, tempValidator);
		
		// Komponenten - Validatoren
		tempValidator = new ArrayList<Validator>();
		tempValidator.add(new ValidatorComponentCollides());
		tempValidator.add(new ValidatorPointInFloor());
		validators.put(ValiderEnum.components, tempValidator);

		// Etagen - Validatoren
		tempValidator = new ArrayList<Validator>();
		tempValidator.add(new ValidatorIsClosed());
		validators.put(ValiderEnum.floor, tempValidator);
		
		// Furniture - Validatoren
		tempValidator = new ArrayList<Validator>();
		tempValidator.add(new ValidatorPointInFloor());
		tempValidator.add(new ValidatorRuleKeeper(new ValidatorComponentCollides()));
		validators.put(ValiderEnum.furniture, tempValidator);
		
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Floor getFloor(int number) {
		return floors.get(number);
	}
	
	public ArrayList<Validator> getValidators(ValiderEnum e){
		return validators.get(e);
	}
	/**
	 * Methode fügt neue Etage ein
	 * @return neue Etage
	 * @throws BuildException Fehler beim erstellen der neuen Etage
	 * @throws ControlException Fehler bei der Validierung
	 */
	public Floor createFloor() throws BuildException, ControlException {
		Floor flBeneath =  floors.size() == 0 ? null : (floors.get(floors.size()-1));
		
		// alle Validatoren in der Liste überprüfen
		ArrayList<Validator> bla = getValidators(ValiderEnum.floor);
		
		Iterator<Validator> it = bla.iterator();
		Validator v = null;
		while(it.hasNext()){
			v = it.next();
			// wenn der validator für diese Operation zuständig
			if(v.isResponsible(Responsibility.createFloor)){
				if(flBeneath != null){	// wenn nicht die unterste Etage
					if (!v.validate(flBeneath.getNorthernWall(), flBeneath.getWalls())) {
						throw new BuildException("Etage nicht geschlossen");
					}
				}
			}
		}
		
		//neuer Floor erstellen
		Floor floor =  new Floor(floors.size()+1, this, flBeneath, null);
		
		floors.add(floor);
		
		Map<String, Floor> changes = new HashMap<String, Floor>();
		changes.put("create", floor);
		
		// Observer informieren
		setChanged();
        notifyObservers(changes);
		
		return floor;
	}
	/**
	 * Methode löscht alle Etagen aus dem Plan
	 */
	public void reset(){
		
		Floor delFloor = null;
		for(int i = 0; i < floors.size(); i++){
			delFloor = floors.remove(i);
		}
		
		Map<String, Floor> changes = new HashMap<String, Floor>();
		changes.put("reset", delFloor);
		
		// Observer informieren
		setChanged();
        notifyObservers(changes);
	}
	/**
	 * Methode löscht eine Etage aus dem Plan
	 * @param delFloor Etage, die gelöscht werden soll
	 * @throws FloorException zu löschende Etage ist nicht vorhanden
	 */
	public void deleteFloor(Floor delFloor) throws FloorException {
		boolean removes = floors.remove(delFloor);
		if (!removes) {
			throw new FloorException("Objekt nicht gefunden");
		}
		Map<String, Floor> changes = new HashMap<String, Floor>();
		changes.put("delete", delFloor);

		
		// Observer informieren
		setChanged();
        notifyObservers(changes);
        
        delFloor = null;
    }
	/**
	 * Methode tauscht die aktuellen Validatoren aus, je nach Mode
	 * @param cp zu testende Komponente 
	 * @param environment Umgebung der Komponenten
	 * @return true, wenn wechseln des Modus erlaubet, false, wenn nicht
	 * @throws ControlException 
	 * @throws FloorException
	 */
	public boolean validateModeChange(Component cp, ArrayList<Component> environment) throws ControlException, FloorException{
		Validator vic = validators.get(ValiderEnum.floor).get(0);
		if(!vic.validate(cp, environment)){
			throw new FloorException("Raum nicht geschlossen");
		} 
		return true;
	}
	
	public int getLevels(){
		return floors.size();
	}
	
	/**
	 * Erstelle Validators und benachrichtige für jeden Floor
	 * die Observer, sodass für jeden Floor ein View erzeugt wird.
	 * Für jeden Floor wird die informObserversLoad aufgerufen.
	 */
	public void informObserversLoad() {
		// erstelle Validators
		createValidators();
		
		for (Floor floor : floors) {
			Map<String, Floor> changes = new HashMap<String, Floor>();
			changes.put("create", floor);
			
			//Observer über Änderung informieren
			setChanged();
	        notifyObservers(changes);
	        floor.informObserversLoad();
		}
	}
}