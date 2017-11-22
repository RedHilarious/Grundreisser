package Exceptions;

/**
 * Runtime-Exception oder nicht?
 * 
 * @author rhaba001
 *
 */
public class UndoRedoException extends ControlException {
	
	private static final long serialVersionUID = 1023601706673436996L;

	public UndoRedoException(String msg) {
		super(msg);
	}
	
	public String toString() {
		return super.toString();
	}
}
