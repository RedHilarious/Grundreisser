package business.commands.setup;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.FurnitureException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Furniture;

public class PutNewFurniture implements ICommand {
	private int furnitureId;
	private int xPos;
	private int yPos;
	private Floor myFloor;
	private Furniture furnish;
	
	public PutNewFurniture (int furnitureId,int xPos, int yPos, Floor myFloor){
		this.furnitureId = furnitureId;
		this.xPos = xPos;
		this.yPos = yPos;
		this.myFloor = myFloor;
	}
	
	public void execute() throws ControlException, FurnishException {
		furnish = myFloor.putNewFurniture(furnitureId, xPos, yPos);
	}

	public void undo() throws FurnitureException {
		myFloor.removeFurniture(furnish);
	}

	public void describe() {
		throw new UnsupportedOperationException();
	}
}