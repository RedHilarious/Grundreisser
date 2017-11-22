package unittest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import business.domainModel.Component;
import business.domainModel.Plan;
import business.domainModel.Wall;
import business.validators.ValidatorIsClosed;

public class TestIsClosed {
	ValidatorIsClosed val = new ValidatorIsClosed();
	ArrayList<Component>environment = new ArrayList<Component>();
	
	Wall wall1 = new Wall(1,100 ,100, 1100, 100, null);
	Wall wall2 = new Wall(2,1100, 100, 1100, 1100,null);
	Wall wall3 = new Wall(3,1100, 1100, 100, 1100, null);
	Wall wall4 = new Wall(4,100, 1100, 100, 100, null);
	Wall wall5 = new Wall(5, 100, 100, 1200, 100, null);
	
	@Test
	public void testisClosed() {
		init();
		wall1.setWallAfter(wall2);
		wall2.setWallAfter(wall3);
		wall3.setWallAfter(wall4);
		wall4.setWallAfter(wall1);
		
		assertTrue(val.validate(wall1, environment));
		
	}
	@Test
	public void testisNotClosed() {
		init();
		wall1.setWallAfter(wall2);
		wall2.setWallAfter(wall3);
		wall3.setWallAfter(wall5);
		wall4.setWallAfter(wall1);
		
		assertFalse((val.validate(wall1, environment)));
		
	}
	private void init(){
		environment.add(wall1);
		environment.add(wall2);
		environment.add(wall3);
		environment.add(wall4);
		environment.add(wall5);
	}

}
