package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Plan;

public class DeleteFloor implements ICommand {

	private Plan plan;
	private Floor fl;
	
	public DeleteFloor(Plan plan, Floor fl){
		this.plan = plan;
		this.fl = fl;
	}
	
	public void execute() throws ControlException, BuildException {
		plan.deleteFloor(fl);
	}

	public void undo() throws BuildException, FurnishException, ControlException{
		fl = plan.createFloor();
	}
	
	public void describe() {
		System.out.println("Erzeuge Etage "+fl.getNr()+" (" + fl.getName() + ")");
	}
	
	public Floor getFloor(){
		return fl;
	}
}