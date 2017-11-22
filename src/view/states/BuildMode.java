package view.states;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import view.functions.DimensionCalculator;
import view.gui.DoorPanel;
import view.gui.DrawArea;
import view.gui.ErrorMessage;
import view.gui.FloorPanel;
import view.gui.FurniturePanel;
import view.gui.InfoPanel;
import view.gui.WallPanel;
import view.gui.WindowPanel;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.building.CreateWall;
import business.commands.building.DeleteBuildComponent;
import business.commands.building.DeleteWall;
import business.domainModel.Door;
import business.domainModel.Floor;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;
import business.domainModel.Window;
/**
 * Klasse realisiert den Baumodus 
 * Wenn der Floor in diesem Modus können Wände gezeichnet werden
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class BuildMode implements StateMode {

	private MouseListener drawListener;
	private MouseListener selectionListener;
	private DrawArea dA;
	private CommandManager cdm;
	
	private int firstX = -1;
	private int firstY = -1;
	
	private Floor floorModel;
	private FloorPanel floorPane;
	
	public BuildMode(DrawArea dA, CommandManager cdm, InfoPanel iP){
		this.dA = dA;
		this.cdm = cdm;

		drawListener = Action();
		selectionListener = markAction();
	}
	/**
	 * Methode wird aufgerufen, wenn dieser Modus aktiv wird
	 */
	public void handle(FloorPanel f) {
		f.addMouseListener(selectionListener);
		firstX = -1;
		firstY = -1;
		floorPane = f;
		floorModel = f.getFloor();
		//alle Möbel sperren
		for(FurniturePanel fP : floorPane.getFurnitures()){
			fP.removeListener();
		}
		for(WallPanel wP : floorPane.getWalls()){
			wP.addListener();
		}
		
	}

	/**
	 * Methode initialisiert den MouseListener zum Zeichnen von Wänden auf dem aktuellen Floor
	 * @return MouseListener 
	 */
	
	private MouseAdapter Action() {
		
		final DimensionCalculator calculator = DimensionCalculator.getInstance();
		
		MouseAdapter Ml = new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				
				
				if (!dA.getDelete()) {
					// Doppelklick tauschte die MouseListener von "ZeichenWerkzeug" zu "Auswahlwerkzeug"
					if (arg0.getClickCount() == 2) {
						floorPane.removeMouseListener(this);
						floorPane.addMouseListener(selectionListener);
						firstX = -1;
						firstY = -1;
					}
					
					if (arg0.getClickCount() == 1) {
						
						// Erster Klick registriert Startkoordianten der neuen Wand
						if (firstX == -1) {
							firstX = arg0.getX();
							firstY = arg0.getY();
						} else {
							int xPos = arg0.getX();
							int yPos = arg0.getY();
							
							try {
								
								cdm.execPush(new CreateWall(floorModel, calculator.pixelToCentimeters(firstX),
										calculator.pixelToCentimeters(firstY), 
										calculator.pixelToCentimeters(xPos), 
										calculator.pixelToCentimeters(yPos)));
							} catch (BuildException | ControlException | FurnishException | SaveException e) {
								// neuen Startpunkt setzen möglich machen
								firstX = -1;
								firstY = -1;
								dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
								System.out.println("FadeoutErrorMessage");
							}		
						}
					}		
				}
			}
		};
		return Ml;
	}
	/**
	 * Methode initialiert MouseAdapter um Wände auszuwählen
	 * @return
	 */
	private MouseAdapter markAction(){
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				floorPane.getDrawArea().setActPanel(null);
			}
		};
	}
	public void del(JPanel activePanel) {
		
		if(activePanel instanceof WallPanel){
			WallPanel cp = (WallPanel)activePanel;
			Wall wall = cp.getWall();
			
			try {	
				cdm.execPush(new DeleteWall(floorModel, wall));
			} catch (BuildException | ControlException | FurnishException | SaveException e) {
				dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
				System.out.println("FadeoutErrorMessage");
			}	
		} else { 
			try{
				if(activePanel instanceof DoorPanel){
					DoorPanel dp = (DoorPanel)activePanel;
					Door d = dp.getDoor();
					cdm.execPush(new DeleteBuildComponent(d, dp.getMyWall()));
				} else if(activePanel instanceof WindowPanel){
					WindowPanel wp = (WindowPanel)activePanel;
					Window w = wp.getWindow();
					cdm.execPush(new DeleteBuildComponent(w, wp.getMyWall()));
				}
			} catch (BuildException | ControlException | FurnishException | SaveException e) {
				dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
				System.out.println("FadeoutErrorMessage");
			}
		}
	}
	
	
/**	
 * Methode tauscht die MouseListener von "Auswahl-Werkzeug" auf "Zeichenwerkzeug"
 */
	public void changeListener(){
		floorPane.removeMouseListener(selectionListener);
		floorPane.addMouseListener(drawListener);
		firstX = -1;
		firstY = -1;
	}
	public void setFloor(FloorPanel floor){
		this.floorPane = floor;
		this.floorModel = floor.getFloor();
	}
	public FloorPanel getFloor(){
		return floorPane;
	}
	public void setRemoveListener(MouseListener rm) {
		//Nothing to do	
	}
	public MouseListener getMyListener() {
		return drawListener;
	}

	
	public void setFirstX(int x){
		firstX = x;
	}
	
	public void setFirstY(int y){
		firstY = y;
	}
}