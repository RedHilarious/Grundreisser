package business.validators;

import java.util.ArrayList;

import Exceptions.ControlException;
import Exceptions.WrongTypeForValidationException;
import business.domainModel.Component;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;

public class ValidatorPointInFloor implements Validator {

	private ArrayList<Responsibility> responsibility;
	private int lowestX = Integer.MAX_VALUE, highestX = -1, lowestY = Integer.MAX_VALUE, highestY = -1;
	
	
	public ValidatorPointInFloor() {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();
		
		// entsprechende Zugehörigkeiten
		responsibility.add(Responsibility.createBuildElement);
		responsibility.add(Responsibility.moveFurniture);
	}

	public boolean validate(Component obj,
			ArrayList<Component> environment) throws ControlException {
		
		// äußerste Begrenzungen der Etage herausfinden
		findOuterFloorBorder(environment);		
		
		if(!(obj instanceof RoomComponent)){
			throw new WrongTypeForValidationException("Type RoomComponent required for Validation.");
		}
		
		RoomComponent rc = (RoomComponent) obj;
		int width = rc.getXLength();
		int height = rc.getYLength();
		if(rc.getXPos() > lowestX && rc.getXPos() + width/2 < highestX && rc.getYPos() > lowestY && rc.getYPos()+height < highestY)
			return true;
		return false;
	}
	
	private void findOuterFloorBorder(ArrayList<Component> environment){
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