package unittest;

import static org.junit.Assert.*;


import java.util.ArrayList;

import org.junit.Test;
import business.domainModel.Component;
import business.domainModel.Furniture;
import business.validators.RuleEnum;
import business.validators.ValidatorComponentCollides;
import business.validators.ValidatorRuleKeeper;

public class testIntersectFurniture {
	ValidatorComponentCollides cc = new ValidatorComponentCollides();
	ValidatorRuleKeeper val = new ValidatorRuleKeeper(cc);
	
	
	@Test
	public void testCollides() {

		Furniture n = new Furniture(1, 'r', "", 100, 100, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		Furniture o = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		assertTrue(cc.checkOneComponent(n,o));
		
	}
	@Test
	public void testNotCollidesShort() {

		Furniture n = new Furniture(1, 'r', "", 701, 500, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		Furniture o = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		assertFalse((cc.checkOneComponent(n,o)));
	}
	@Test
	public void testRuleAbove() {

		Furniture n = new Furniture(1, 'r', "", 100, 500, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		Furniture o1 = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		Furniture o2 = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Above, null, "", null);
		ArrayList<Component> environment = new ArrayList<Component>();
		environment.add(o1);
		environment.add(o2);
		
		assertFalse((val.validate(n, environment)));
		
	}
	@Test
	public void testRuleBeneath() {

		Furniture n = new Furniture(1, 'r', "", 100, 500, 500, 500, 0.0, null, RuleEnum.Beneath, null, "", null);
		Furniture o1 = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Beneath, null, "", null);
		Furniture o2 = new Furniture(2, 'r', "", 200, 100, 500, 500, 0.0, null, RuleEnum.Beneath, null, "", null);
		ArrayList<Component> environment = new ArrayList<Component>();
		environment.add(o1);
		environment.add(o2);
		
		assertFalse((val.validate(n, environment)));
		
	}
	
	

}
