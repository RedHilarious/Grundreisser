package business.commands;

import java.util.ArrayList;
import java.util.List;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import Exceptions.UndoRedoException;

public class CommandManager {
	
	private static CommandManager theCommandManager = null;
	private List<ICommand> commands = new ArrayList<ICommand>();
	private int index;

	public static CommandManager getInstance() {
		if (theCommandManager == null) {
			theCommandManager = new CommandManager();
		}
		return theCommandManager;
	}
	
	public void pushCommand(ICommand cmd)  throws UndoRedoException {
		if (index < commands.size()) {
			// für eventuelle 'redo' behaltene, zuvor 'undo'te Kommandos verwerfen,
			// sobald etwas Neues hinzukommt
			commands = commands.subList(0, index);
		}
		commands.add(cmd);
		index++;
	}

	public void undo()  throws ControlException, FurnishException, BuildException {
		if (index > 0) {
			index--;
			ICommand cmd = commands.get(index);
			cmd.undo();
		} else {
			throw new UndoRedoException("Nothing to undo");
		}
	}

	public void redo() throws ControlException, BuildException, FurnishException, SaveException {
		if (index < commands.size()){
			// wenn 'undo'te alte Kommandos da sind,
			// naechstes erneut 'execute'n und index vorruecken
			ICommand cmd = commands.get(index);
			cmd.execute();
			index++;
		} else {
			throw new UndoRedoException("Nothing to redo");
		}
	
	}

	public void execPush(ICommand cmd) throws ControlException, BuildException, FurnishException, SaveException {
		cmd.execute();
		pushCommand(cmd);
	}

	public String toString(){
		String res = "CommandStack(\n";
		
		for (int i=0; i < commands.size(); i++) {
			if (i == index) {
				res += "-------------------------\n";
			}
			res += "  " + commands.get(i) + ".: " + "\n";
		}
		res += ")";
		return res;
	}
}