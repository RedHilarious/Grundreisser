package business.validators;

import java.util.ArrayList;

import business.domainModel.Component;
import business.domainModel.Furniture;

/**
 * Validator, der überprüft, ob beim Einfügen und/oder Verschieben eines
 * Möbelstücks die Regelen des Übereinander- bzw Untereinander-platzierens
 * eingehalten werden.
 * 
 * @author rhaba001
 * 
 */
public class ValidatorRuleKeeper implements Validator {

	private ArrayList<Responsibility> responsibility;
	private ValidatorComponentCollides collideValider;

	public ValidatorRuleKeeper(ValidatorComponentCollides collideValider) {
		// Liste mit Zuständigkeiten (für Funktionen)
		responsibility = new ArrayList<Responsibility>();

		// Collide Validator um Möbelstücke in meinem Bereich zu finden
		this.collideValider = collideValider;

		responsibility.add(Responsibility.moveFurniture);
		responsibility.add(Responsibility.createFurniture);
	}

	public boolean validate(Component obj, ArrayList<Component> environment) {

		Furniture furnit = (Furniture) obj;
		Furniture neighboor;

		// alle Komponenten der Umgebung durchlaufen
		for (Component rc : environment) {
			if (rc instanceof Furniture) {
				neighboor = (Furniture) rc;
				if (neighboor != furnit) {
					if (collideValider.checkOneComponent(neighboor, furnit)) {
						// das Möbelstück liegt in meinem Bereich; neu
						// einzufügendes Möbelstück liegt immer ganz oben

						// beim Nachbarmöbelstück Regelkonformheit überprüfen
						if (neighboor.getRuleEnum().above != true) {
							// throw new
							// RuleViolationException("Auf dem darunter gelegenen Möbelstück darf kein weiteres Möbelstück liegen.");
							return false;
						}

						// beim zu testenden Möbelstück Regelkonformheit
						// überprüfen
						if (furnit.getRuleEnum().beneath != true) {
							return false;
						}

						// im neuen Möbelstück den gefundenen Nachbarn als
						// unteres Möbel setzen
						furnit.setBeneath(neighboor);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Überprüft, ob dieser verantwortlich ist.
	 * 
	 * @param r
	 *            - aktuelle Zuständigkeit
	 */
	public boolean isResponsible(Responsibility r) {
		for (Responsibility res : responsibility) {
			if (res == r) {
				return true;
			}
		}
		return false;
	}
}