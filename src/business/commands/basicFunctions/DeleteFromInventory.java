package business.commands.basicFunctions;

import Exceptions.FurnitureException;
import business.commands.ICommand;
import business.domainModel.Furniture;
import business.domainModel.Inventory;

/**
 * --- Diese Funktion wird leider in dem GUI nicht angezeigt. ----
 * 
 * Aus dem Inventar löschen - Funktion
 * 
 * Die Funktion löscht ein Möbelstück aus dem eigenen Inventar.
 * Bei Undo wird das Möbelstück dem Inventar wieder hinzugefuegt.
 * 
 * @author Rahel Habacker
 *
 */
public class DeleteFromInventory implements ICommand {

	private Inventory invent;
	private Furniture furnit;
	
	public DeleteFromInventory(Inventory invent, Furniture furnit){
		this.invent = invent;
		this.furnit = furnit;
	}
	
	public void execute() throws FurnitureException {
		invent.removeFurniture(furnit);
	}

	public void undo() {
		invent.addFurniture(furnit);
	}

	public void describe() {
		System.out.println("Löscht das Möbelstück " + furnit.getName() + " aus dem Inventar.");
	}
}