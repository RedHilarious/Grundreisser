package business.commands.building;

import Exceptions.BuildException;
import Exceptions.ControlException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Wall;

/**
 * Erstelle Wand - Funktion
 * 
 * Klasse für Objekte, die die Funktion eine Wand zu erzeugen realisiert.
 * Sie ist rückgängig machbar und wiederholbar.
 * 
 * @author Rahel Habacker
 */
public class CreateWall implements ICommand {
	
	Floor fl;
	Wall wll;
	int x1Pos,y1Pos,x2Pos, y2Pos;
	
	/**
	 * Speichert alle relevanten Daten für eine Wand.
	 * @param fl - aktuelle Etage
	 * @param x1Pos - start-X-Position
	 * @param y1Pos - start-Y-Position
	 * @param x2Pos - end-X-Position
	 * @param y2Pos - end-Y-Position
	 */
	public CreateWall(Floor fl, int x1Pos, int y1Pos, int x2Pos, int y2Pos){
		this.fl = fl;
		this.x1Pos = x1Pos;
		this.y1Pos = y1Pos;
		this.x2Pos = x2Pos;
		this.y2Pos = y2Pos;
		}
		
	/**
	 * Stoeßt Methode zum Erstellen einer Wand im aktuellen Floor an.
	 */
	public void execute() throws BuildException, ControlException{
		wll = fl.createWall(x1Pos, y1Pos, x2Pos, y2Pos);
	}

	/**
	 * Stoeßt Methode zum Loeschen einer Wand im aktuellen Floor an.
	 */
	public void undo() throws BuildException {
		fl.deleteWall(wll);
	}

	public void describe() {
		System.out.println("Erzeuge Wand mit den Koordinaten x1: " + x1Pos + " y1: " + y1Pos + " und x2: " + x2Pos + " y2: " + y2Pos + ".");
	}
}