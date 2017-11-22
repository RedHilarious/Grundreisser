package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnitureException;
import business.commands.ICommand;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;

/**
 * Funktionsklasse zum Löschen von Bauelementen.
 * @author rhaba001
 *
 */
public class DeleteBuildComponent implements ICommand {

	RoomComponent copyRp;
	RoomComponent rp;
	Wall myWall;
	
	public DeleteBuildComponent(RoomComponent rp, Wall myWall) {
		this.rp = rp;
		this.myWall = myWall;
		copyRp = rp.clone();
	}
	
	public void execute() throws FurnitureException{
		myWall.removeBuildComponent(rp);
	}

	public void undo() throws BuildException, ControlException {
		rp = myWall.addBuildComponent(rp.getName(), rp.getXPos(), rp.getYPos());
	}

	public void describe() {
		System.out.println("Lösche Raumkomponente " + rp.getName() + " von Position x " + rp.getXPos() + " und  y: " + rp.getYPos() + " mit der Länge " + rp.getXLength());
	}
}