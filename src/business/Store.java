package business;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import persistence.DeSerializer;
import persistence.JSONConverter;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.domainModel.Component;
import business.domainModel.Furniture;
import business.domainModel.Plan;
import business.domainModel.Wall;

/**
 * Diese Klasse wird als Singleton Objekt genutzt und hält die eingelesenen Möbelstücke
 * und verwaltet Speicher- und Ladeanfragen und reicht diese weiter an die Persistence-Schicht.+
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class Store extends Observable{
	private static Store theStore;
	private String furnitureBaseFolder;
	private JSONConverter converter;
	private DeSerializer deSerializer;
	private Map<Integer, Furniture> availFurniture;
	
	/**
	 * Hole Instanz von Singleton Store. Falls noch keine Instanz vorhanden, wird eine erzeugt.
	 * 
	 * @return Instanz von Store
	 */
	public static Store getInstance() {
		if (theStore == null) {
			theStore = new Store(new File("").getAbsolutePath() + "/defaultFurniture/");
		}
		return theStore;
	}
	
	public Store(String furnitureBaseFolder) {
		this.furnitureBaseFolder = furnitureBaseFolder;
		converter = new JSONConverter();
		availFurniture = converter.convertToFurniture(this.furnitureBaseFolder);
		deSerializer = new DeSerializer();
	}
	
	/**
	 * Gibt Kopie des Möbelstücks der übergebenen ID zurück. Falls nicht vorhanden,
	 * wird eine Exception geworfen.
	 * 
	 * @param furnitureId Id des Möbelstücks, für das eine Instanz erzeugt werden soll
	 * @return neues (geclontes) Möbelstück
	 * @throws FurnishException Wenn das Möbelstück (ID) nicht vorhanden ist
	 */
	public Furniture getNewFurnitureById(int furnitureId) throws FurnishException {
		Furniture f = availFurniture.get(furnitureId);
		
		if (f == null) {
			throw new FurnishException("Furniture wasn't found!");
		}
		
		return f.clone();	
	}
	
	/**
	 * Gibt Map von ID -> Möbelstück von allen verfügbaren Möbelstücken zurück
	 * 
	 * @return ID -> Name des Möbelstücks
	 */
	public Map<Integer, Furniture> getAvailableFurnitureList() {	
		return availFurniture;
	}
	
	/**
	 * Fügt neues Möbelstück hinzu und gibt Observern bescheid.
	 * Zusätzlich wird das Speichern des Möbelstück aus der persistence-Schicht angestoßen.
	 * 
	 * @param newFurniture Furniture-Objekt, dass gespeichert werden soll
	 * @throws SaveException Falls beim Speichern oder der Konvertierung etwas schief geht
	 */

	public void addNewFurniture(Furniture newFurniture) throws SaveException{
		
		int id = availFurniture.size() + 1;
		newFurniture.setId(id);
		availFurniture.put(id, newFurniture);
		
		Map<String, Integer> hM = new HashMap<String, Integer>();
		hM.put(newFurniture.getName(), id);
		
		setChanged();
		notifyObservers(hM);
		
		// schreibe JSON-Datei, damit Möbelstück beim nächsten Start verfügbar
		converter.createJSONForFurniture(newFurniture, furnitureBaseFolder);
	}
	
	/**
	 * Nimmt Plan und Pfad zur gespeicherten Datei entgegen.
	 * Stößt die Deserialisierung der persistence-Schicht an und gibt den geladenen Plan zurück
	 * 
	 * @param plan aktueller Plan
	 * @param path Pfad zur gespeicherten Datei
	 * @return geladener Plan
	 * @throws SaveException Falls beim Laden oder der Konvertierung etwas schief geht
	 */
	public Plan loadAll(Plan plan, String path) throws SaveException {
		Plan newPlan = deSerializer.loadAll(path);
		plan = newPlan;
		return plan;
	}
	
	/**
	 * Nimmt Plan und Pfad, in dem die Datei gespeichert werden soll, entgegen.
	 * Stößt das Speichern der persistence-Schicht an.
	 * 
	 * @param plan aktueller Plan
	 * @param path Pfad, in dem die Datei gespeichert werden soll
	 * @throws SaveException Falls beim Speichern oder der Konvertierung etwas schief geht
	 */
	public void saveAll(Plan plan, String path) throws SaveException {
		for(Component wl: plan.getFloor(0).getWalls()){
			Wall wll = (Wall)wl;
			System.out.println("Start " + wll.getStartX() + " " + wll.getStartY() + " End " + wll.getEndX() + " " + wll.getEndY());
		}
		deSerializer.saveAll(plan, path);
	}
}