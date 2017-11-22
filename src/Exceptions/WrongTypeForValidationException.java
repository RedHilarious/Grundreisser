package Exceptions;

public class WrongTypeForValidationException extends ControlException{

	private static final long serialVersionUID = 8905436691075535416L;

	public WrongTypeForValidationException(String msg) {
		super(msg);
	}
}
