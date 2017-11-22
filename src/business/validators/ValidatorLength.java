package business.validators;

import java.util.ArrayList;

import business.domainModel.Component;
import business.domainModel.Wall;

/**
 * Validator für die Länge von Wänden.
 * Überprüft, ob eine Wand mindestens 10 cm lang ist.
 * 
 * @author rhaba001
 *
 */
public class ValidatorLength implements Validator {

	private ArrayList<Responsibility> responsibility;
	private static final int SHORTEST_WALLLENGTH = 40;
	
	public ValidatorLength() {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();
	
		responsibility.add(Responsibility.createWall);
	}

	public boolean validate(Component obj,
			ArrayList<Component> environment) {
		Wall wll = (Wall) obj;
		
		// Länge beider Seiten der Wand berechnen und ggf. positivieren
		int dis1 = (wll.getEndX() - wll.getStartX());
		int dis2 = (wll.getEndY() - wll.getStartY());
		dis1 = dis1 < 0? -dis1 : dis1;
		dis2 = dis2 < 0? -dis2 : dis2;
		
		if(dis2 >= SHORTEST_WALLLENGTH && dis1 == 0 || dis1 >= SHORTEST_WALLLENGTH && dis2 == 0)
			return true;
		return false;
	}
	
	public boolean isResponsible(Responsibility r){
		for(Responsibility res: responsibility){
			if(res == r){
				return true;
			}
		}
		return false;
	}	
}