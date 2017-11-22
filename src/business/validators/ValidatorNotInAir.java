package business.validators;

import java.util.ArrayList;

import business.domainModel.Component;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;

/**
 * Validator für die Platzierung von Wänden.
 * Verhindert, dass Wände außerhalb der Grenzen des unteren Stockwerks liegen.
 * @author rhaba001
 *
 */
public class ValidatorNotInAir implements Validator {

ArrayList<Responsibility> responsibility;
	
	public ValidatorNotInAir() {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();
	
		responsibility.add(Responsibility.createWall);
		responsibility.add(Responsibility.moveFurniture);
	}

	public boolean validate(Component obj,
			ArrayList<Component> environment) {
		
		if(environment == null) {
			return true;
		}
		
		int lowestX = Integer.MAX_VALUE, highestX = -1, lowestY = Integer.MAX_VALUE, highestY = -1;
		
		Wall wll;
		for(Component rc: environment){
			if(rc instanceof Wall){
				wll = (Wall)rc;
				
				int x1 = wll.getEndX();
				int x2 = wll.getStartX();
				int y1 = wll.getEndY();
				int y2 = wll.getStartY();
				
				int lowX = (x1 < x2)? x1 : x2;
				lowestX = (lowX < lowestX)? lowX : lowestX;
				
				int highX = (x1 < x2)? x2 : x1;
				highestX = (highX > highestX)? highX : highestX;
				
				int lowY = (y1 < y2)? y1 : y2;
				lowestY = (lowY < lowestY)? lowY : lowestY;
				
				int highY = (y1 < y2)? y2 : y1;
				highestY = (highY > highestY)? highY : highestY;
			}
		}
		
		if(obj instanceof Wall){
			Wall actWall = (Wall)obj;	
			if(actWall.getEndX() < lowestX || actWall.getEndX() > highestX || actWall.getEndY() < lowestY || actWall.getEndY() > highestY){
				return false;
			}
			return true;
		} else if(obj instanceof RoomComponent){
			RoomComponent rc = (RoomComponent) obj;
			if(rc.getXPos() < lowestX || rc.getXPos() > highestX || rc.getYPos() < lowestY || rc.getYPos() > highestY){
				return false;
			}
			return true;
		} else {
			return false;
		}
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
