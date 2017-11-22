package business.commands.basicFunctions;

import java.util.ArrayList;

import Exceptions.FurnitureException;
import business.commands.ICommand;
import business.domainModel.Component;
import business.domainModel.Floor;
import business.domainModel.Furniture;
import business.domainModel.Inventory;

/**--- Diese Funktion wird leider in dem GUI nicht angezeigt. ----
* 
* Dem Inventar hinzufuegen - Funktion
* 
* Die Funktion fügt dem eigenen Inventar ein Möbelstück hinzu.
* Bei Undo wird das Möbelstück wieder aus dem Inventar gelöscht.
* 
* @author Rahel Habacker
* */
public class PutToInventory implements ICommand {

	private Inventory invent;
	private Furniture furnit;
	private Floor fl;
	
	public PutToInventory(Inventory invent, Furniture furnit, Floor fl){
		this.invent = invent;
		this.furnit = furnit;
		this.fl = fl;
	}
	
	public void execute() {
		ArrayList<Component> components = fl.getFurniture();
		int index = components.indexOf(furnit);
		invent.addFurniture((Furniture)components.remove(index));
	}

	public void undo() throws FurnitureException {
		ArrayList<Component> components = fl.getFurniture();
		components.add(furnit);
		invent.removeFurniture(furnit);
	}

	public void describe() {
		System.out.println("Entfernt das Möbelstück " + furnit.getName() + " von der Zeichenfläche und schiebt es ins Inventar.");
	}
}