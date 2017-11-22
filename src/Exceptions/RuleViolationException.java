package Exceptions;

public class RuleViolationException extends ControlException {

	private static final long serialVersionUID = 6651131144528827031L;

	public RuleViolationException(String msg) {
		super(msg);
	}	
}
