package view;

import java.io.File;

import javax.swing.SwingUtilities;

import view.gui.MainGui;
/**
 * Klasse startet das Programm, zuerst das Startframe, dann die eigentliche Gui der Anwendung
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 * 
 */
public class App {

	
	static MainGui grundreisser;
	
	public static void main(String[] args) {
		// Wenn Ordnerstruktur nicht vorhanden -> gebe Fehlermeldung aus, beende Programm
		File config = new File("config/");
		File defaultFurniture = new File("defaultFurniture/");
		File textures = new File("textures/");
		if (!config.isDirectory() || !defaultFurniture.isDirectory() || !textures.isDirectory()) {
			System.out.println("Bitte darauf achten, dass folgende Ordner an der richtigen Stelle sind: config, defaultFurniture, textures");
			System.exit(-1);
		}
		
		
		final StartFrame start = new StartFrame();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				start.initComponents();
	
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread() { 
			public void run() { 
			
			} 
		}); 
	}
	
	
	public static <E> void startGrundreisser(E mode){
		
		grundreisser = new MainGui(mode);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				grundreisser.initComponents();
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread() { 
			public void run() { 
				
			} 
		}); 
	}
}
