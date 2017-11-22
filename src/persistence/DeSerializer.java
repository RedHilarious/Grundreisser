package persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Exceptions.SaveException;
import business.domainModel.Plan;
/**
 * Diese Klasse ist für das Speichern und Laden mit Hilfe von Serialisierung zuständig.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class DeSerializer {
	/**
	 * Methode, die eine serialisiertes Plan-Objekt einliest und als Plan-Objekt zurückgibt.
	 * 
	 * @param path Pfad zur Datei
	 * @return Plan-Objekt
	 * @throws SaveException Falls beim Einlesen oder der Konvertierung etwas schief geht
	 */
	public Plan loadAll(String path) throws SaveException {
		Plan plan = null;
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream( path );
			ObjectInputStream o = new ObjectInputStream( fis );
			plan = (Plan) o.readObject();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			throw new SaveException("Datei konnte nicht geladen werden!");
		} 
		return plan;
	}
	
	/**
	 * Methode, die ein übergebenes Plan-Objekt serialisiert und in eine Datei schreibt.
	 * 
	 * @param plan Plan, der serialisiert werden soll
	 * @param path Pfad, in welchem die Datei gespeichert werden soll
	 * @throws SaveException Falls beim Speichern oder der Konvertierung etwas schief geht
	 */
	public void saveAll(Plan plan, String path) throws SaveException {
		if (!path.endsWith(".gr")) {
			path = path + ".gr";
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream( path );
			ObjectOutputStream o = new ObjectOutputStream( fos );
			o.writeObject(plan);
			fos.close();
		} catch (IOException e) {
			throw new SaveException("Datei konnte nicht gespeichert werden!");
		}	
	}
}
