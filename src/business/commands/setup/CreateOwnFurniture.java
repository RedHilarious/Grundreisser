package business.commands.setup;

import java.awt.Color;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.ICommand;
import business.domainModel.Floor;
import business.domainModel.Furniture;
import business.validators.RuleEnum;

/**
 * Funktionsklasse um ein eigenes Möbelstück zu erstellen.
 * 
 * 
 * @author rhaba001
 *
 */
public class CreateOwnFurniture implements ICommand {
		
	private char form;
	private String name;
	private int xPos;
	private int yPos;
	private int xLength;
	private int yLength;
	private double angle;
	private Floor myFloor;
	private RuleEnum placeRule;
	private String texturePath;
	private Color color;
	private Furniture furnish;
	
	public CreateOwnFurniture (char form, String name,int xPos, int yPos, int xLength, int yLength, 
			double angle, Floor myFloor, RuleEnum placeRule, String texture, Color color){
		
		this.form = form;
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xLength = xLength;
		this.yLength = yLength;
		this.angle = angle;
		this.myFloor = myFloor;
		this.placeRule = placeRule;
		this.texturePath = texture;
		this.color = color;
		}

	public void execute() throws BuildException, FurnishException,
			ControlException, SaveException {
			furnish = myFloor.createNewFurniture(form, name, xPos, yPos, xLength, yLength, angle,placeRule, texturePath, color);
	}

	public void undo() throws BuildException, FurnishException,
			ControlException {	
		// myFloor.removeNewFurniture
	}

	public void describe() {
		System.out.println("Erstelle neues Möbelstück " + name);
	}
}