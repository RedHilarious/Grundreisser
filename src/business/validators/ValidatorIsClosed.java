package business.validators;

import java.util.ArrayList;

import business.domainModel.Component;
import business.domainModel.Wall;

/**
 * Überprüft die Geschlossenheit des unteren Floors beim Einfügen eines neuen.
 * @author rhaba001
 *
 */
public class ValidatorIsClosed implements Validator {

	ArrayList<Responsibility> responsibility;
	
	public ValidatorIsClosed() {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();
		
		// entsprechende Zugehörigkeiten
		responsibility.add(Responsibility.createFloor);
	}

	public boolean validate(Component obj,
			ArrayList<Component> environment) {
		
			// Northern Wall wird übergeben
			Wall start = (Wall) obj;
			Wall forward = start;
			int nrOfWalls = environment.size();
			
			// Erste Wand danach schon mal setzen
			if(start == null)
				return false;
			
			forward = start.getWallAfter();
			int countWalls = 1;
			
			// solange weiterlaufen, bis Northern Wall (Start-Wand) wieder erreicht
			while(countWalls < nrOfWalls){
				if (forward == start){
					return true;
				}
				
				try {
					forward = forward.getWallAfter();
					countWalls++;
				} catch(NullPointerException e) {
					// eine Wand hat keine nächste Wand
					return false;
				}
			}
			
			if (forward == start){
				return true;
			}
			
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