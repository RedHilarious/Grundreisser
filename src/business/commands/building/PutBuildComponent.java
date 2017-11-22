package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.FurnitureException;
import business.commands.ICommand;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;

public class PutBuildComponent implements ICommand {
	private String name;
	private int xPos;
	private int yPos;
	private Wall myWall;
	private RoomComponent rC;

	
	public PutBuildComponent (String name, int xPos, int yPos, Wall myWall){
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.myWall = myWall;
	}

	public void execute() throws ControlException, FurnishException, BuildException {
		rC = myWall.addBuildComponent(name, xPos, yPos);

	}

	public void undo() throws FurnitureException {
		myWall.removeBuildComponent(rC);
	}

	public void describe() {
		throw new UnsupportedOperationException();
	}
}