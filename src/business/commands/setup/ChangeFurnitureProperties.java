package business.commands.setup;

import java.util.HashMap;
import java.util.Map;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.RoomComponent;

/**
 * Funktionsklasse zum Ändern der Eigenschaften von Möbeln.
 * 
 * @author rhaba001
 *
 */
public class ChangeFurnitureProperties implements ICommand {

	RoomComponent rp;
	Map<String, Object> newProperties, oldProperties;
	
	/**
	 * Map<String, Object> gibt das property, was geändert werden soll an (String) und den jeweiligen Wert.
	 * @param rp - zu ändernde Komponente
	 * @param newProperties - Map mit neuen Eigenschaften
	 */
	public ChangeFurnitureProperties(RoomComponent rp, Map<String,Object> newProperties){
		this.rp = rp;
		this.newProperties = newProperties;
		oldProperties = new HashMap<String, Object>();
	}
	
	public void execute() throws ControlException, FurnishException{	
		// alle zu ändernden Werte durchlaufen
		for(String prop: newProperties.keySet()){
			
			// Abfrage, welches Property es ist + jeweilige Set-Methode aufrufen
			if(prop.equalsIgnoreCase("name")){
				oldProperties.put("name", rp.getName());
				rp.setName((String)newProperties.get(prop));
			} else if (prop.equalsIgnoreCase("xPos")){
				oldProperties.put("xPos", rp.getXPos());
				rp.move((int)newProperties.get(prop), 0);
			} else if (prop.equalsIgnoreCase("yPos")){
				oldProperties.put("yPos", rp.getYPos());
				rp.move(0,(int)newProperties.get(prop));
			} else if (prop.equalsIgnoreCase("xLength")){
				oldProperties.put("xLength", rp.getXLength());
				rp.setXLength((int)newProperties.get(prop));
			} else if (prop.equalsIgnoreCase("yLength")){
				oldProperties.put("yLength", rp.getYLength());
				rp.setYLength((int)newProperties.get(prop));
			} else if (prop.equalsIgnoreCase("angle")){
				oldProperties.put("angle", rp.getAngle());
				rp.setAngle((double)newProperties.get(prop));
			}
		}
	}

	public void undo() throws ControlException, FurnishException {
		// alte Werte durchlaufen
		for(String oldProp: oldProperties.keySet()){
			
			// Abfrage, welches Property auf den alten Wert zurück gesetzt werden soll
			if(oldProp.equalsIgnoreCase("name")){
				rp.setName((String)oldProperties.get(oldProp));
			} else if (oldProp.equalsIgnoreCase("xPos")){
				rp.setXPos((int)oldProperties.get(oldProp));
			} else if (oldProp.equalsIgnoreCase("yPos")){
				rp.setYPos((int)oldProperties.get(oldProp));
			} else if (oldProp.equalsIgnoreCase("xLength")){
				rp.setXLength((int)oldProperties.get(oldProp));
			} else if (oldProp.equalsIgnoreCase("yLength")){
				rp.setYLength((int)oldProperties.get(oldProp));
			} else if (oldProp.equalsIgnoreCase("angle")){
				rp.setAngle((double)oldProperties.get(oldProp));
			}	
		}
	}

	public void describe() {
		for(String prop: newProperties.keySet()){
			System.out.println("Verändere Eigenschaft '" + prop + "'. Alter Wert: " + oldProperties.get(prop) + " Neuer Wert: " + newProperties.get(prop));
		}
	}
}