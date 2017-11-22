package view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.states.ArrangeMode;
import view.states.BuildMode;
import view.states.StateMode;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FloorException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.basicFunctions.CopyAndPaste;
import business.commands.building.CreateWall;
import business.commands.building.InsertFloor;
import business.domainModel.Floor;
import business.domainModel.Plan;
import business.domainModel.Wall;
/**
 * Klasse realisiert die "Zeichenfläche" der Anwendung und ist somit Kernklasse.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class DrawArea extends JTabbedPane implements Observer{
	
	private static final long serialVersionUID = -5330622465393023858L;
	
	// verschiedene States der drawArea
	private StateMode active, build, furnish;

	private CommandManager cdm;
	
	// domainModels
	private Floor actFloor;
	private Plan plan;
	
	// andere bekannte Panels
	private InfoPanel iP;
	
	private String roomComponent = "";
	
	private boolean delete = false;
	
	private int furnishId = -1;
	
	// aktive/angeklickte Panel
	private JPanel actPanel = null;
	
	public DrawArea(Plan plan, final CommandManager cdm, InfoPanel iP){
		this.cdm = cdm;
		this.plan = plan;
		this.iP = iP;
			
		//states initialisieren und untereinander bekannt machen
		build = new BuildMode(this, cdm, iP);
		furnish = new ArrangeMode(this, cdm, iP);
		build.setRemoveListener(furnish.getMyListener());
		furnish.setRemoveListener(build.getMyListener());
		active = build;
		
		// mich beim Plan als Beobachter anmelden
		plan.addObserver(this);
		
		// TabbedPane Konfigurationen
		setTabPlacement(BOTTOM);
		setFocusable(true);
		configureKeyListener();
		
		//erste Etage einfügen
		addFloor();
		
		addChangeListener(new ChangeListener() {
	    	// angestoßen, wenn Baumodus-Tab oder Einrichtungsmodus-Tab geklickt
			public void stateChanged(ChangeEvent arg0) {
				changeFloor();
			}
		});
	}
	
	/**
	 * Wechselt Floor und setzt aktuellen Floor in anderen Panels.
	 */
	private void changeFloor(){
		if (getTabCount() != 0){
			actFloor = getActFloor().getFloor();
			furnish.setFloor(getActFloor());
			build.setFloor(getActFloor());
			System.out.println("Im ArrangeMode:" + furnish.getFloor().getFloor().getNr());
			System.out.println("Im BuildMode:" + build.getFloor().getFloor().getNr());
			System.out.println("Auf Etage Nr " + actFloor.getNr() + " gewechselt.");
		}
	}
	
	/**
	 * Fügt den KeyListener hinzu. 
	 * Der reagiert auf die Entf-Taste und löscht das Model des gerade aktiven Panels.
	 */
	private void configureKeyListener(){
		this.addKeyListener(new KeyAdapter() {
			
			Map<Integer, Boolean> pressedKeys = new HashMap<Integer, Boolean>();
			
			public void keyPressed(KeyEvent arg0) {				
				int code = arg0.getKeyCode();
					
				if(code == KeyEvent.VK_DELETE){
						active.del(actPanel);						
				}
				
				if(code == KeyEvent.VK_CONTROL){
					pressedKeys.put(KeyEvent.VK_CONTROL, true);
				}	
				
				if(code == KeyEvent.VK_C){
					pressedKeys.put(KeyEvent.VK_C, true);
				}	
				
				if(code == KeyEvent.VK_V){
					pressedKeys.put(KeyEvent.VK_V, true);
				}
				
				if(pressedKeys.containsKey(KeyEvent.VK_CONTROL) && pressedKeys.containsKey(KeyEvent.VK_C)){
					if(pressedKeys.get(KeyEvent.VK_CONTROL) && pressedKeys.get(KeyEvent.VK_C)){
						// ctrl and c pressed --> copy
						if(actPanel instanceof FurniturePanel){
							FurniturePanel fP = (FurniturePanel)actPanel;
							try {
								cdm.execPush(new CopyAndPaste(fP.getFurniture(), actFloor));	
							} catch (ControlException | BuildException
									| FurnishException | SaveException e) {
								getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
								System.out.println(e.getMessage());			
							}
						}
						pressedKeys.clear();
					}
				}
				
				if(code == KeyEvent.VK_ENTER){
					actPanel= null;
				}
			}
		});
	}
	
	/**
	 * Methode stößte Methode im Plan an, um neuen Floor einzufügen
	 */
	public void addFloor(){
		try {
			InsertFloor iF = new InsertFloor(plan);
			cdm.execPush(iF);	
		} catch (BuildException | ControlException | FurnishException | SaveException e) {
			getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Methode, die den Etagenwechsel vornimmt. Es wird überprüft, ob alle Wände geschlossen sind.
	 * 
	 * @return True für Wechsel erfolgreich
	 */
	public boolean changeState() {
		boolean changeOk = false;
		
		if(active == build){
			try {	
				// Bei Wechsel auf Einrichtung erst überprüfen, ob Wechsel ok (Wände geschlossen in aktueller Etage)
				plan.validateModeChange(actFloor.getNorthernWall(), actFloor.getWalls());
				changeOk = true;
			} catch (FloorException | ControlException e) {
				getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
				System.out.println(e.getMessage());
				changeOk = false;
			}
			active = furnish;
		} else {
			active = build;
		}
		
		//jedem Floor aktueller State mitteilen
		for(Component cp : getComponents()){
			FloorPanel floor = (FloorPanel)cp;
			floor.setState(active);
		}	
		
		return changeOk;
	}
	
	/**
	 * Methode läd einen Standardgrundriss ins Programm
	 * dabei wird der Plan erst resettet
	 * @param grundriss Form des Grundrisses
	 */
	public void loadStandard(String grundriss){
		
		plan.reset();
		
		//entsprechend des Enums Wände einzeichnen
		int corners[][];
		if(grundriss.equals("l")){
			corners= StandardGroundEnum.L.corners;
		} else{
			corners= StandardGroundEnum.R.corners;
		}
		
		for(int i = 0; i < corners.length; i++){
			try {
				cdm.execPush(new CreateWall(actFloor, corners[i][0], corners[i][1], corners[i][2], corners[i][3]));
			} catch (ControlException | BuildException | FurnishException | SaveException e) {
				getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
				System.out.println(e.getMessage());
			}
		}
		repaint();
		
		
	}
	/**
	 * Mehtode setze das übergebene Panel als "Angeklicktes" aktives Panel.
	 * Dieses Panel kann dann speziell bearbeitet werden und wird vom InfoPanel beobachtet
	 * @param aP Aktives Panel
	 */
	public void setActPanel(JPanel aP){
		if (actPanel != null){
			//bisheriges Panel wieder zurück setzten
			if (actPanel instanceof FurniturePanel) {
				FurniturePanel actFurniturePanel = (FurniturePanel) actPanel;
				actPanel.setBackground(actFurniturePanel.getColor());
			} else if (actPanel instanceof WindowPanel) {
				WindowPanel actWindowPanel = (WindowPanel) actPanel;
				actPanel.setBackground(actWindowPanel.getUnmarkedColor());
			} else if (actPanel instanceof DoorPanel) {
				DoorPanel actDoorPanel = (DoorPanel) actPanel;
				actPanel.setBackground(actDoorPanel.getUnmarkedColor());
			} else {
				actPanel.setBackground(Color.gray);
			}
		}
		actPanel = aP;
		if (actPanel != null){
			actPanel.setBackground(Color.green.darker());
		}
		iP.editContent(aP);
		repaint();
	}
	
	/**
	 * alle Tabs der Drawarea werden gelöscht
	 */
	private void reset(){
	// entferne alle Tabs + aktuelle Anzahl wissen -> besser als removeAll();
		while (this.getTabCount() > 0) {
			this.remove(0);
		}
	}
	
	/**
	 * new eingelesener Plan wird an die DrawArea gebunden.
	 * @param plan Plan, der Observed wird
	 */
	public void loadNewPlan(Plan plan) {
		//plan.reset();
		reset();
		this.plan = plan;
		// mich beim Plan als Beobachter anmelden
		plan.addObserver(this);
	}
	
	public int getFurnishId(){
		return furnishId;
	}
	
	public void setFurnishId(int id){
		furnishId = id;
	}
	
	public void setDelete(boolean delete){
		this.delete = delete;
	}
	
	public boolean getDelete(){
		return delete;
	}

	public void setRoomComponent(String rC){
		roomComponent = rC;
	}
	public String getRoomComponent(){
		return roomComponent;
	}
	
	public FloorPanel getActFloor(){
		// es ist sicher, dass es immer FloorPanel ist
		return (FloorPanel)getSelectedComponent();
	}
	
	/**
	 * Methode regelt das Anhängen der entsprechenden Listener, je nach Bedarf
	 */
	public void changeBuildListener(){
		FloorPanel fP = (FloorPanel)getSelectedComponent();
		fP.getState().changeListener();
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	
	/**
	 * Methode registiert die Änderungen der Domainklasse Plan
	 * Mögliche Änderungen sind Etage eingefügt, Etage gelöscht und Plan resettet
	 */
	public void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		Map<String,Floor> changes = (Map<String,Floor>) arg;
		
		Floor cp = changes.get("create");
		if(cp instanceof Floor) {
			actFloor = cp;
			//erzeuge gui-Element
			FloorPanel floor = new FloorPanel(actFloor, this, active, cdm);
			
			//Model heiratet View
			actFloor.addObserver(floor);
			
			
			if(actFloor.hasFloorBeneath()) {
				for(business.domainModel.Component wll: actFloor.getFlBeneath().getWalls()){
					Wall wl = (Wall)wll;
					try {
						CreateWall cw = new CreateWall((Floor)cp, wl.getStartX(), wl.getStartY(), wl.getEndX() , wl.getEndY());
						cdm.execPush(cw);
						
					} catch (ControlException | BuildException
							| FurnishException | SaveException e) {
						getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
						System.out.println(e.getMessage());
					}
				}
			}
			//zeige View an
			addTab("Etage " + actFloor.getNr(), floor);
			// Auswahl des Tabs springt auf neuen Floor
			setSelectedComponent(floor);		
			repaint();
		}
		
		cp = changes.get("delete");
		if(cp instanceof Floor) {
			cdm.toString();
			if(cp.hasFloorBeneath()){
				// floor beneath is new actFloor
				actFloor = cp.getFlBeneath();
				removeTabAt(actFloor.getNr());
				actFloor = null;
			} else {
				try {
					throw new BuildException("Erdgeschoss kann nicht gelöscht werden.");
				} catch (BuildException e) {
					getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
					System.out.println(e.getMessage());
				}
			}
		}
		
		cp = changes.get("reset");
		if(cp instanceof Floor) {
			reset();
			// ersten Floor wieder einfügen
			addFloor();
		}
	}
	
}