package business.commands.setup;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Furniture;
import business.domainModel.RoomComponent;

/**
 * Funktionsklasse um Möbelstück aus dem Plan zu löschen.
 * @author rhaba001
 *
 */
public class DeleteFurniture implements ICommand {

	RoomComponent furniture,copyFurniture;
	Floor fl;
	
	public DeleteFurniture(RoomComponent furniture, Floor fl){
		this.furniture = furniture;
		this.fl = fl;
	}
	
	public void execute() throws FurnishException{
		copyFurniture = furniture.clone();
		fl.removeFurniture((Furniture)furniture);
	}

	public void undo() throws ControlException, FurnishException {
		fl.putNewFurniture(((Furniture)copyFurniture).getId(), copyFurniture.getXPos(),copyFurniture.getYPos());
		furniture = copyFurniture;
	}

	public void describe() {
		System.out.println("Erstelle neues Möbelstück '" + furniture.getName()+ "'.");
	}
}