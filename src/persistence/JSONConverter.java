package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Exceptions.SaveException;
import business.domainModel.Furniture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Diese Klasse ist für das Speichern und Lesen von JSON-Dateien (Möbel) zuständig.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class JSONConverter {
	private Gson gson;

	public JSONConverter() {
		this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	}
	
	/**
	 * Gibt eine HashMap mit Furniture-Objekten zurück. Liest alle Dateien im
	 * übergebenen Ordner ein
	 * 
	 * @param path
	 *            Ordner, in dem alle Dateien eingelesen werden sollen
	 * @return Map (Name, Furniture Objekt) aller Dateien im übergebenen Ordner
	 */
	public Map<Integer, Furniture> convertToFurniture(String path) {
		Map<Integer, Furniture> furnitureMap = new HashMap<Integer, Furniture>();

		try {
			String[] filesInFolder = getFilesInFolder(path);

			for (int i = 0; i < filesInFolder.length; i++) {

				BufferedReader br = new BufferedReader(new FileReader(path + filesInFolder[i]));

				// konvertiere zu Objekt
				
				Furniture obj = gson.fromJson(br, Furniture.class);
				// füge zur HashMap hinzu
				furnitureMap.put(obj.getId(), obj);
			}
		} catch (IOException e) {
			// Wird ignoriert, da bei fehlerhaften Dateien nur weniger Möbel angeboten werden.
		}

		return furnitureMap;
	}

	/**
	 * Gibt Liste von Dateinamen, die mit .grjson enden, in übergebenem Ordner zurück
	 * 
	 * @param path
	 *            Pfad zum Ordner, dessen Inhalt aufgelistet werden soll
	 * @return Array von Dateinamen in Ordner
	 */
	private String[] getFilesInFolder(String path) {
		return new File(path).list(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".grjson");
			    }
		});
	}
	
	/**
	 * Nimmt furniture-Objekt entgegen und erstellt im angegebenen Pfad eine .grjson-Datei,
	 * damit das Möbelstück beim nächsten Start wieder geladen werden kann.
	 * Der Dateiname wird aus dem Namen des Möbelstücks - evtl. Sonderzeichen gebildet.
	 * 
	 * @param newFurniture Furniture-Objekt, das als JSON gespeichert werden soll
	 * @param path Pfad, in dem die Datei gespeichert werden soll
	 * @throws SaveException Falls beim Speichern oder der Konvertierung etwas schief geht
	 */
	public void createJSONForFurniture(Furniture newFurniture, String path) throws SaveException {
		// Entferne Sonderzeichen o.ä. aus dem Namen, damit daraus der Dateiname erzeugt werden kann
		String fileName = newFurniture.getName().replaceAll("[^a-zA-Z0-9]+","");
		String json = gson.toJson(newFurniture);
		FileWriter writer;
		try {
			writer = new FileWriter(path + fileName + ".grjson");
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			throw new SaveException("Möbelstück konnte nicht abgespeichert werden.");
		}
	}
}