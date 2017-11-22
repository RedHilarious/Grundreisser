package business.domainModel;

/**
 * Klasse ist ein Model einer Tür mit der festen Standardlänge 1,20 m (Quelle www.baumarkt.de),
 * die von RoomComponent erbt.
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Door extends RoomComponent {

	private static final long serialVersionUID = 225068350677950142L;
	final private int LENGTH = 120;
	private char direction;
	
	public Door(char direction, int xPos, int yPos, int wallthick){
		super(xPos, yPos, 0, 0, 0.0, null);
		this.direction = direction;
		
		if (direction == 'h'){
			xLength = LENGTH;
			yLength = wallthick;
		} else {
			xLength = wallthick;
			yLength = LENGTH;
		}
	}
	
	public char getDirection(){
		return direction;
	}
}