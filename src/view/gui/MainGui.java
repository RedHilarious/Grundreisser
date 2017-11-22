package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Exceptions.SaveException;
import business.Store;
import business.commands.CommandManager;
import business.domainModel.Plan;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
/**
 * Diese Klasse erstellt alle 
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 * @param <E> String oder File
 */
public class MainGui<E> extends JFrame {
	
	private static final long serialVersionUID = 6589278018973017466L;
	private JPanel pane;
	private DrawArea dA;
	private InfoPanel iP;
	private MenuPanel mP;
	private ToolPanel tP;
	private Plan plan;
	private CommandManager cdm;
	
	public MainGui(E mode){
		
		pane = new JPanel();
		plan = new Plan();
		cdm = CommandManager.getInstance();
		
		NimRODTheme nt = new NimRODTheme();
		nt.setPrimary1( new Color(31, 46, 58));
		//selectedColor
		nt.setPrimary2( new Color(200,200,200));
		nt.setPrimary3( new Color(51, 66, 78));
		nt.setSecondary1(new Color(59, 59, 59));
		nt.setSecondary2(new Color(69, 69, 69));
		nt.setSecondary3(new Color(79, 79, 79));
		nt.setWhite(new Color(113, 113, 113));
		nt.setBlack(new Color(255, 255, 255));
		nt.setMenuOpacity(0);
		nt.setFrameOpacity(0);
		
		NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
		NimRODLookAndFeel.setCurrentTheme( nt);
		
		try {
			UIManager.setLookAndFeel( NimRODLF);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		iP = new InfoPanel(cdm);
		dA = new DrawArea(plan,cdm, iP);
		mP = new MenuPanel(cdm, iP, dA);
		tP = new ToolPanel(dA, iP);
		
		iP.setDrawArea(dA);
		
		initStartMode(mode);
	}
	
	/**
	 * initialisiere Komponenten
	 */
	public void initComponents() {
		Toolkit tk = Toolkit.getDefaultToolkit();	
		setSize(tk.getScreenSize());
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.setPreferredSize(new Dimension(280, 1000)); // Höhe wird von Layout-Manager ignoriert
		pane.add(tP);
		pane.add(iP);
		
	    pane.setVisible(true);
	    dA.setVisible(true);
	    iP.setVisible(true);
	    mP.setVisible(true);
	    tP.setVisible(true);
	    
	    add(mP, BorderLayout.NORTH);
	    add(pane,BorderLayout.WEST);
	    add(dA,BorderLayout.CENTER);
	    
	    pack();
	    setVisible(true);
	}
	
	/**
	 * 
	 * @param mode
	 */
	public<E> void initStartMode(E mode){
		
		if(mode instanceof File){
			
			Plan loadedPlan = null;
			try {
				loadedPlan = Store.getInstance().loadAll(dA.getPlan(),((File)mode).getAbsolutePath());
				dA.loadNewPlan(loadedPlan);
				loadedPlan.informObserversLoad();
			} catch (SaveException e) {
				dA.getActFloor().displayErrorMessage(new ErrorMessage("Plan konnte nicht geladen werden."));
			}
			
		} else if (mode instanceof String){
			String cmd = (String)mode;
			
			// newProjekt --> nichts tun; einfach starten
			
			// NewGroundPlot --> Standartgrundriss laden
			if(cmd.equals("NewGroundPlotL")){
				dA.loadStandard("l");				
			} else if (cmd.equals("NewGroundPlotR")){
				dA.loadStandard("r");
			}
		}
	}
}