package business.domainModel;
/**
 * Klasse ist ein Model einer Treppe
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß2
 *
 */
public class Stairs extends RoomComponent {
	private static final long serialVersionUID = 7762244277797124537L;

	public Stairs(int xPos, int yPos, int xLength, int yLength, double angle, Floor myFloor){
		super(xPos, yPos, xLength, yLength, angle, myFloor);
	}
}