package business.validators;

import java.awt.Rectangle;
import java.util.ArrayList;

import business.domainModel.Component;
import business.domainModel.RoomComponent;

/**
 * Testet, ob das übergebene Bauelement auf keinem anderen Bauelement platziert ist.
 * 
 * @author rhaba001
 *
 */
public class ValidatorComponentCollides implements Validator {

	private ArrayList<Responsibility> responsibility;
	
	public ValidatorComponentCollides() {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();
		
		// entsprechende Zugehörigkeiten
		responsibility.add(Responsibility.createBuildElement);
	}
	
	
	public boolean validate(Component component, ArrayList<Component> environment){
		RoomComponent neighboor;
		for(Component c: environment){
			neighboor = (RoomComponent) c;
			
			if(!checkOneComponent(neighboor, (RoomComponent)component)){
				return false;
			}
		}
		return true;
	}
	
	public boolean checkOneComponent(RoomComponent neighboor, RoomComponent newComponent){
		
		Rectangle n = new Rectangle(neighboor.getXPos(), neighboor.getYPos(), neighboor.getXLength(), neighboor.getYLength());
		Rectangle t = new Rectangle(newComponent.getXPos(), newComponent.getYPos(), newComponent.getXLength(), newComponent.getYLength());
		
		return n.intersects(t);
	}
	
	
	public boolean isResponsible(Responsibility r){
		for(Responsibility res: responsibility){
			if(res == r)
				return true;
		}
		return false;
	}
}