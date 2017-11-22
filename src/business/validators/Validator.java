package business.validators;

import java.util.ArrayList;

import Exceptions.ControlException;
import business.domainModel.Component;

public interface Validator {

	public boolean validate(Component obj, ArrayList<Component> environment) throws ControlException;
	public boolean isResponsible(Responsibility res);
}