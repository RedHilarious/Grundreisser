package view.states;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import view.functions.DimensionCalculator;
import view.gui.ErrorMessage;
import view.gui.FloorPanel;
import view.gui.FurniturePanel;
import view.gui.InfoPanel;
import view.gui.DrawArea;
import view.gui.WallPanel;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.setup.DeleteFurniture;
import business.commands.setup.PutNewFurniture;
import business.domainModel.Floor;
import business.domainModel.RoomComponent;
/**
 * Klasse realisiert den Einrichtungsmodus 
 * Wenn der Floor in diesem Modus ist lassen sich Möbel platzieren, löschen bearbeiten und erstellen
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class ArrangeMode implements StateMode {

	private MouseListener myMouseListener;
	private MouseListener removeListerner;
	private DrawArea dA;
	private CommandManager cdm;
	
	private Floor floorModel;
	private FloorPanel floorPane;
	
	public ArrangeMode(DrawArea dA, CommandManager cdm, InfoPanel iP){
		this.dA = dA;
		this.cdm = cdm;
		
		myMouseListener = Action();
	}
	/**
	 * Methode wird aufgerufen, wenn dieser Modus aktiv wird
	 */
	public void handle(FloorPanel f) {
		f.addMouseListener(myMouseListener);
		floorPane = f;
		floorModel = f.getFloor();
		
		for(FurniturePanel fP : floorPane.getFurnitures()){
			fP.addListener();
		}
		for(WallPanel wP : floorPane.getWalls()){
			wP.removeListener();
		}
	}

	public MouseListener getMyListener() {
		return myMouseListener;
	}

	public void setRemoveListener(MouseListener rm) {
		removeListerner = rm;
	}
	/**
	 * Methode initialisiert einen MouseAdapter zum Platzieren vom Möbeln auf dem Floor 
	 * @return MouseAdapter
	 */
	private MouseAdapter Action(){
		MouseAdapter ml = new MouseAdapter() {
			DimensionCalculator calculator = DimensionCalculator.getInstance();
			public void mouseClicked(MouseEvent arg0){
				if(dA.getFurnishId()!=-1){
				try {
					cdm.execPush(new PutNewFurniture(dA.getFurnishId(), calculator.pixelToCentimeters(arg0.getX()),calculator.pixelToCentimeters(arg0.getY()), dA.getActFloor().getFloor()));
					dA.setFurnishId(-1);
				} catch (BuildException | ControlException | FurnishException | SaveException e) {
					dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
					System.out.println("FadeoutErrorMessage");
				}
			}else {
				dA.setActPanel(null);
			}
			}
		};
		return ml;
	}
	/**
	 * Methode löscht ein markiertes Möbelstück vom Floor 
	 */
	public void del(JPanel activePanel) {
		FurniturePanel fp = (FurniturePanel) activePanel;
		RoomComponent rC = (RoomComponent)fp.getFurniture();
		
		try {			
			cdm.execPush(new DeleteFurniture(rC, floorPane.getFloor()));
		} catch (BuildException | ControlException | FurnishException | SaveException e) {
			dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
			System.out.println("FadeoutErrorMessage");
		}
	}
	
	public void setFloor(FloorPanel floor){
		this.floorPane = floor;
	}
	
	public FloorPanel getFloor(){
		return floorPane;
	}
	public void changeListener(){
		//Nothing to do
	}
}