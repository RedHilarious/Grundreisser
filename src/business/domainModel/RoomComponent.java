package business.domainModel;

import java.util.ArrayList;
import java.util.Iterator;

import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.FurnitureException;
import business.validators.Responsibility;
import business.validators.Validator;
import business.validators.ValidatorRuleKeeper;
import business.validators.ValiderEnum;

import com.google.gson.annotations.Expose;
/**
 * Methode ist Model aller Raumkomponenten, wie Möbeln, Treppen, Fenstern,..
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class RoomComponent extends Component{
	
	private static final long serialVersionUID = 2924612534948402055L;
	@Expose
	protected int id;
	@Expose
	protected String name;
	@Expose
	protected int xPos;
	@Expose
	protected int yPos;
	@Expose
	protected int xLength;
	@Expose
	protected int yLength;
	@Expose
	protected double angle;
	@Expose
	protected Floor myFloor;
	
	public RoomComponent(int xPos, int yPos, int xLength, int yLength, double angle, Floor myFloor){
		this.xPos = xPos;
		this.yPos = yPos;
		this.xLength = xLength;
		this.yLength = yLength;
		this.angle = angle;
		this.myFloor = myFloor;
	}
	public RoomComponent(String name,int xPos, int yPos, int xLength, int yLength, double angle, Floor myFloor, int id){
		this(xPos, yPos, xLength, yLength, angle, myFloor);
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		// Validator?
		this.name = name;
	}
	
	public void setMyFloor(Floor myFloor){
		this.myFloor = myFloor;
	}
	
	public int getId(){
		return id;
	}

	public int getXPos() {
		return this.xPos;
	}

	public void setXPos(int xPos) throws ControlException, FurnishException {
		this.xPos = xPos;
		//Observer informieren
		setChanged();
		notifyObservers("xPos");
	}

	public int getYPos() {
		return this.yPos;
	}

	public void setYPos(int yPos) throws FurnishException, ControlException{
		this.yPos = yPos;
		setChanged();
		notifyObservers("yPos");
	}

	public int getXLength() {
		return this.xLength;
	}

	public void setXLength(int xLength) throws FurnishException, ControlException{
		this.xLength = xLength;
		setChanged();
		notifyObservers("xLenght");
	}

	public int getYLength() {
		return this.yLength;
	}

	public void setYLength(int yLength) throws FurnishException, ControlException{
		this.yLength = yLength;
		setChanged();
		notifyObservers("yLenght");
	}

	public double getAngle() {
		return this.angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;

		setChanged();
		notifyObservers("angle");
	}
	public void move(int dx, int dy) throws FurnitureException, ControlException {
		xPos += dx;
		yPos += dy;
		
		// Position validieren
		ArrayList<Validator> validators = myFloor.getPlan().getValidators(ValiderEnum.furniture);
		Iterator<Validator> it = validators.iterator();
		Validator v;
		while(it.hasNext()){
			v = it.next();
			// wenn der validator für diese Operation zuständig
			if(v.isResponsible(Responsibility.moveFurniture)){
				ArrayList<Component>	input;
				input = v instanceof ValidatorRuleKeeper? myFloor.getFurniture() : myFloor.getWalls();
				if (!v.validate(this, input)) {
					xPos -= dx;
					yPos -= dy;
					setChanged();
					notifyObservers("moved");
					throw new FurnitureException("Ungültige Position");
				}
			}
		}
		
		setChanged();
		notifyObservers("moved");
	}
	
	public RoomComponent clone(){
		return new RoomComponent(xPos,yPos, xLength, yLength, angle, myFloor);
	}
}