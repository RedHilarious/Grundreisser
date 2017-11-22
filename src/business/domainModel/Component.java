package business.domainModel;

import java.io.Serializable;
import java.util.Observable;
/**
 * Abstrakte Klasse repräsentiert alle Komponenten die in einer Etage platziert werden können.
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public abstract class Component extends Observable implements Serializable {
	private static final long serialVersionUID = 6081629009340201317L;
}
