package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Wall;

public class DeleteWall implements ICommand {

	Floor fl;
	Wall wl;
	int x1Pos,y1Pos,x2Pos, y2Pos;
	
	public DeleteWall(Floor fl, Wall wl){
		this.fl = fl;
		this.wl = wl;
		
		x1Pos = wl.getEndX();
		x2Pos = wl.getStartX();
		y1Pos = wl.getEndY();
		y2Pos = wl.getStartY();
	}
	
	public void execute() throws BuildException{
		fl.deleteWall(wl);
	}

	public void undo() throws BuildException, ControlException {
		fl.createWall(x1Pos, y1Pos, x2Pos, y2Pos);
	}

	public void describe() {
		System.out.println("Lösche Wand mit den Koordinaten x1: " + x1Pos + " y1: " + y1Pos + " und x2: " + x2Pos + " y2: " + y2Pos + ".");
	}
}