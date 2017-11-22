package business.domainModel;
/**
 * Klasse ist ein Model eines Fensters
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Window extends RoomComponent {
	
	private static final long serialVersionUID = 225068350677950142L;
	final private int LENGTH = 100;
	private char direction;
	
	public Window(char direction, int xPos, int yPos, int wallthick){
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