package business.commands.basicFunctions;

import java.util.ArrayList;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.Component;
import business.domainModel.Furniture;
import business.domainModel.Inventory;
import business.domainModel.Plan;

/**
 * --- Diese Funktion wird leider in dem GUI nicht angezeigt. ----
 * 
 * Alles in das Inventar schieben.
 * 
 * Diese Funktion nimmt alle Möbelstücke aus der Zeichenfläche heraus und legt sie im Inventar ab.
 * 
 * @author Rahel Habacker
 *
 */
public class PutAllToInventory implements ICommand {

	private Plan plan;
	private Inventory invent;
	
	public PutAllToInventory(Plan plan, Inventory invent){
		this.plan = plan;
		this.invent = invent;
	}
	
	public void execute() {
		for(int i = 0; i < plan.getLevels(); i++){
			ArrayList<Component> comps = plan.getFloor(i).getFurniture();
			for(int j = 0; j < comps.size(); j++){
				if(comps.get(j) instanceof Furniture)
					invent.addFurniture((Furniture)comps.remove(j));
			}
		}
		System.out.println("Alle Möbel ins Inventar geschoben.");
	}

	public void undo() throws ControlException, FurnishException {
		ArrayList<Furniture> allFurniture = invent.getFurniture(); 
		Furniture actFurnit;
		for(int i = 0; i < allFurniture.size(); i++){
			actFurnit = invent.getFurnit(i);
			
			actFurnit.getMyFloor().putNewFurniture(actFurnit.getId(), actFurnit.getXPos(), actFurnit.getYPos());
			//actFurnit.getMyFloor().addRoomComponent(actFurnit);
			invent.removeFurniture(actFurnit);
		}
		System.out.println("Alle Möbel vom Inventar wieder in ihre Etage");
	}

	public void describe() {
		System.out.println("Entfernt alle Möbel von der Zeichenfläche und schiebt sie ins Inventar.");
	}
}