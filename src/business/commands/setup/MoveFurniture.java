package business.commands.setup;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.RoomComponent;

/**
 * Functionclass to move a furniture from its origin place to another.
 * 
 * @author rhaba001
 *
 */
public class MoveFurniture implements ICommand {

	private RoomComponent furniture;
	int posX, posY;
	
	public MoveFurniture(RoomComponent furniture, int posX, int posY) {
		this.furniture = furniture;
		this.posX = posX;
		this.posY = posY;
	}
	
	public void execute() throws FurnishException, ControlException {
		furniture.setXPos(posX);
		furniture.setYPos(posY);
	}

	public void undo() throws FurnishException, ControlException {
		furniture.setXPos(-posX);
		furniture.setXPos(-posY);
	}

	public void describe() {
		System.out.println("Bewege " + furniture.getName() + " Möbelstück von " + furniture.getXPos() + "/"+furniture.getYPos()+" nach "+posX+"/"+posY);
	}
}