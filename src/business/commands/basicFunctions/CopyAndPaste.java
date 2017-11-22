package business.commands.basicFunctions;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.FurnitureException;
import Exceptions.WallException;
import business.commands.ICommand;
import business.domainModel.Component;
import business.domainModel.Floor;
import business.domainModel.Furniture;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;

/**
 * CopyAndPaste-Funktionsklasse
 * 
 * Realisiert die Möglichkeit eine Componente des Grundreissers zu klonen und
 * den Klon ebenfalls der Zeichenfläche hinzuzufügen.
 * Diese Funktion ist redoable/undoable und implementiert daher das Interface ICommand.
 * 
 * @author rhaba001
 *
 */
public class CopyAndPaste implements ICommand {

	private Component cp;
	private Floor fl;
	private Wall newWll;
	private RoomComponent newCp;
	
	/**
	 * Konstruktor nimmt die zu klonende Komponente und die entsprechende Etage entgegen.
	 * @param cp - zu klonende Komponente
	 * @param fl - enthaltender Floor
	 */
	public CopyAndPaste(Component cp, Floor fl){
		this.cp = cp;		
		this.fl = fl;
		this.newWll = null;
		this.newCp = null;
	}
	
	/**
	 * Ausführende Methode der Funktion. 
	 * Erstellt eine neue Wand oder Raumkomponentente und fügt sie der Etage hinzu.
	 * @throws FurnishException 
	 */
	public void execute() throws BuildException, ControlException, FurnishException {
		if(cp instanceof Wall){
			Wall wll = ((Wall)cp);
			this.newWll = fl.createWall(wll.getStartX(), wll.getStartY(), wll.getEndX(), wll.getEndY());
		} else {
			this.newCp = ((RoomComponent)cp).clone();
			System.out.println("im Command copy");
			fl.putNewFurniture(newCp.getId(), newCp.getXPos()-30, newCp.getYPos()-30);
		}
	}

	/**
	 * Rückgängig machen der execute()-Methode.
	 * Nimmt die Änderungen der execute()-Methode zurück, indem die Wand/Raumkomponente wieder aus
	 * der Etage gelöscht wird.
	 */
	public void undo() throws WallException, FurnitureException {
		if(newWll != null && newCp == null){
			fl.deleteWall(newWll);
		} else if(newWll == null && newCp != null){
			fl.removeFurniture((Furniture)newCp);
		}
	}

	/**
	 * Beschreibung der Funktion.
	 */
	public void describe() {
		if(newWll == null && newCp != null){
			System.out.println("Kopiert die Raumkomponente " + newCp.getName() + " und fügt das Dublikat in die Möbelliste der Etage " + fl.getNr() + " ein.");
		} else if (newWll != null && newCp == null){
			System.out.println("Kopiert die Wand " + newWll.getId() + " und fügt das Dublikat in die Wandliste der Etage " + fl.getNr() + " ein.");
		}
	}
}