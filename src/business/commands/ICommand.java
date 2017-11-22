package business.commands;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;

public interface ICommand {

	public void execute() throws BuildException, FurnishException, ControlException, SaveException;

	public void undo() throws BuildException, FurnishException, ControlException;

	public void describe();
}