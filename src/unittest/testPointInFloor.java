package unittest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Exceptions.ControlException;
import Exceptions.WrongTypeForValidationException;
import business.domainModel.Component;
import business.domainModel.Plan;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;
import business.validators.ValidatorPointInFloor;

public class testPointInFloor {
	
	ValidatorPointInFloor val = new ValidatorPointInFloor();
	ArrayList<Component>environment = new ArrayList<Component>();

	
	@Test public void testinFloor(){
		init();
		Component obj = new RoomComponent(500, 500, 100, 100, 0.0, null);
		try {
			assertTrue(val.validate(obj, environment));
		} catch (ControlException e) {
			
		}
	}
	@Test public void testinFloorShort(){
		init();
		Component obj = new RoomComponent(101, 101, 100, 100, 0.0, null);
		try {
			assertTrue(val.validate(obj, environment));
		} catch (ControlException e) {
			
		}
	}
	@Test public void testoutOFFloor(){
		init();
		Component obj = new RoomComponent(0, 500, 100, 100, 0.0, null);
		try {
			assertFalse((val.validate(obj, environment)));
		} catch (ControlException e) {
			
		}
	}
	@Test public void testoutOFFloorShort(){
		init();
		Component obj = new RoomComponent(1101, 1101, 100, 100, 0.0, null);
		try {
			assertFalse((val.validate(obj, environment)));
		} catch (ControlException e) {
			
		}
	}
	@Test (expected=ControlException.class )
	public void testinFloorException() throws ControlException{
		init();
		Component obj = new Wall(5, 200, 500, 100, 100, null);
		val.validate(obj, environment);
		
	}
	

	private void init(){
		Plan plan = new Plan();
		environment.add(new Wall(1,100 ,100, 1100, 100, plan));
		environment.add(new Wall(2,1100, 100, 1100, 1100, plan));
		environment.add(new Wall(3,1100, 1100, 100, 1100, plan));
		environment.add(new Wall(4,100, 1100, 100, 100, plan));
	}
	
	

}
