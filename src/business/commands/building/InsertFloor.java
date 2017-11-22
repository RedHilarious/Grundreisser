package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Plan;

public class InsertFloor implements ICommand {

	private Plan plan;
	private Floor fl;
	
	public InsertFloor(Plan plan){
		this.plan = plan;
	}
	
	public void execute() throws ControlException, BuildException {
		fl = plan.createFloor();
	}

	public void undo() throws BuildException, FurnishException, ControlException{
		
		plan.deleteFloor(fl);
	}
	
	public void describe() {
		System.out.println("Erzeuge Etage "+fl.getNr()+" (" + fl.getName() + ")");
	}
	
	public Floor getFloor(){
		return fl;
	}
}