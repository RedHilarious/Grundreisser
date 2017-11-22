package view.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.Store;
import business.commands.CommandManager;
import business.domainModel.Component;
import business.domainModel.Plan;
import business.domainModel.Wall;

public class MenuPanel extends JMenuBar {

	private static final long serialVersionUID = 7052391223622438207L;
	private final DrawArea dA;

	private JFileChooser chooser;

	public MenuPanel(final CommandManager cdm, final InfoPanel ip, final DrawArea dA){
		this.dA = dA;
		
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Grundreisser-Dateien", "gr"));
		
		setPreferredSize(new Dimension(250,30));
		
		//Erstellen einer Menüleiste
		//JMenuBar menuBar = new JMenuBar();
		
		//Hinzufügen von Menüs
		JMenu menuFile = 
			new JMenu("Datei");
		JMenu menuEdit = 
			new JMenu("Bearbeiten");
		JMenu menuHelp = 
			new JMenu("Hilfe");
		
		add(menuFile);
		add(menuEdit);
		add(menuHelp);
		
		final String funktionen = "Standardgrundriss laden:\n‚Bearbeiten’ -> 'Rechteck Grundriss' oder 'L Grundriss' \n\n" + 
				"Speichern:\nMenuleiste über ‚Datei’ -> ‚Speichern als’ \n\n" +
				"Laden:\nMenuleiste über ‚Datei’ -> ‚Öffnen’\n" +
				"Etagenwechsel:\nKlick auf Tabs mit Namen der Etage am unteren Rand\n\n"+
				"Undo/Redo:\nButtons ‚Erneut’ und ‚Rückgängig’ in der Menuleiste \n\n"+
				"Copy/Paste:\nMöbelstücke anklicken, dann ‚Strg + C’\n\n"+
				"Wandfolge einzeichnen:\nButton ‚Wand einzeichnen’; Klick auf Zeichenfläche markiert Anfangspunkt, Doppelklick beendet die Wandfolge\n\n"+
				"Wand löschen:\nKlick auf eine Wand; Taste ‚entf’\n\n"+
				"Etage einfügen / löschen:\nZwei Buttons im ToolPanel im Baumodus\n\n"+
				"Bauelement platzieren:\nButtons für Elemente im ToolPanel im Baumodus\n\n"+
				"Möbelstück platzieren:\nAuswahl eines Möbelstücks mit Klick auf entsprechenden Button; Klick in die Zeichenfläche platziert es\n\n"+
				"Eigenes Möbelstück erstellen:\nButton im ToolPanel im Einrichtungsmodus; Eigenschaften im InfoPanel darunter eintragen und speichern\n\n"+
				"Möbelstück ändern:\nMöbelstück anklicken und Objekteigenschaften im InfoPanel ändern; mit ‚Enter’ bestätigen\n\n"+
				"Möbelstück löschen:\nMöbelstück anklicken und ‚entf’ drücken\n\n"+
				"Möbelstück verschieben:\nMöbelstück anklicken und ziehen (Drag&Drop)\n\n"+
				"Möbelstück sperren:\nPassiert automatisch beim Wechsel zum Baumodus (ToolPanel)\n\n";
		
		JMenuItem helpSite = new JMenuItem("Funktionen");
		menuHelp.add(helpSite);
		helpSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, funktionen);
			}
		});
		
		//Hinzufügen von Untermenüs von Menü
		JMenuItem menuFileNew = 
			new JMenuItem("Neu");
		
		menuFile.add(menuFileNew);
		menuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					dA.getPlan().reset();
					System.out.println("Resettet");					
				
			}
		});
		
		//Hinzufügen von Menüeinträgen in das Dateimenü
		JMenuItem menuItemFileOpen = 
			new JMenuItem("Öffnen");
		JMenuItem menuItemFileSave = 
			new JMenuItem("Speichern als");
		
		JMenuItem menuItemFileExit = 
			new JMenuItem("Beenden");
		

		menuFile.add(menuItemFileOpen);
		menuFile.add(menuItemFileSave);
		menuFile.addSeparator();
		menuFile.add(menuItemFileExit);
		
		// Speichern
		menuItemFileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// zeige FileChooser öffnen Dialog
				int rueck = chooser.showSaveDialog(chooser.getParent());
				
				// nur wenn gültige Datei ausgewählt
				if (rueck == JFileChooser.APPROVE_OPTION) {
					try {
						Store.getInstance().saveAll(dA.getPlan(), chooser.getSelectedFile().getAbsolutePath());
					} catch (SaveException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Hoppla!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		// Laden
		menuItemFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// zeige FileChooser öffnen Dialog
				int rueck = chooser.showOpenDialog(chooser.getParent());
				
				// nur wenn gültige Datei ausgewählt
				if (rueck == JFileChooser.APPROVE_OPTION) {
					Plan loadedPlan;
					try {
						loadedPlan = Store.getInstance().loadAll(dA.getPlan(), chooser.getSelectedFile().getAbsolutePath());
						dA.loadNewPlan(loadedPlan);
						loadedPlan.informObserversLoad();
						for(Component wl: loadedPlan.getFloor(0).getWalls()){
							Wall wll = (Wall)wl;
							System.out.println("Start " + wll.getStartX() + " " + wll.getStartY() + " End " + wll.getEndX() + " " + wll.getEndY());
						}
					} catch (SaveException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Hoppla!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		
		// Beenden
		menuItemFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		
		
		
		//Hinzufügen von Untermenüs bei Bearbeiten
		JMenuItem menuLoadStandardR = 
				new JMenuItem("Rechteck Grundriss");
		JMenuItem menuLoadStandardL = 
				new JMenuItem("L- Grundriss");
		
		menuLoadStandardR.addActionListener(loadStandard("r"));
		menuLoadStandardL.addActionListener(loadStandard("l"));
		menuEdit.add(menuLoadStandardR);	
		menuEdit.add(menuLoadStandardL);
		
		
		// Hinzufuegen von Redo/Undo Buttons
		JButton redo = new JButton("Erneut");
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					cdm.redo();
					
				} catch (ControlException | BuildException | FurnishException | SaveException e1) {
					dA.getActFloor().displayErrorMessage(new ErrorMessage(e1.getMessage()));
					System.out.println(e1.getMessage());
				}
			}
		});
		
		JButton undo = new JButton("Rückgängig");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try {
				
					cdm.undo();
					
				} catch (ControlException | BuildException | FurnishException e1) {
					dA.getActFloor().displayErrorMessage(new ErrorMessage(e1.getMessage()));
					System.out.println(e1.getMessage());
				}
			}
		});
		
		add(redo);
		add(undo);
		
		//add(menuBar, BorderLayout.WEST);
	}

	public ActionListener loadStandard(final String grundriss) {
		ActionListener aL = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dA.loadStandard(grundriss);

			}
		};
		return aL;
	}
}