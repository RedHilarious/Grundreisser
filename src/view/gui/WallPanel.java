package view.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import view.functions.DimensionCalculator;
import Exceptions.BuildException;
import Exceptions.ControlException;
import Exceptions.FurnishException;
import Exceptions.SaveException;
import business.commands.CommandManager;
import business.commands.building.PutBuildComponent;
import business.domainModel.Door;
import business.domainModel.RoomComponent;
import business.domainModel.Wall;
import business.domainModel.Window;

public class WallPanel extends JPanel implements Observer{
	
	private static final long serialVersionUID = 4181285457260215875L;
	
	//Standard Wanddicke nach Baumarkt.de
	private int firstX;
	private int firstY;
	private int xPos;
	private int yPos;
	private FloorPanel myFloor;
	private Wall wall;
	private DrawArea dA;
	private CommandManager cdm;
	private DimensionCalculator calculator;
	private ArrayList<DoorPanel> doors;
	private ArrayList<WindowPanel> windows;
	private MouseAdapter myMouseListener;
	
	public WallPanel(FloorPanel myFloor, Wall wall, DrawArea dA, CommandManager cdm){
		
		calculator = DimensionCalculator.getInstance();
		
		this.myFloor = myFloor;
		this.wall = wall;
		this.dA = dA;
		this.firstX = calculator.centimetersToPixel(wall.getStartX());
		this.firstY = calculator.centimetersToPixel(wall.getStartY());
		this.xPos = calculator.centimetersToPixel(wall.getEndX());
		this.yPos = calculator.centimetersToPixel(wall.getEndY());
		this.cdm = cdm;
		int disX = calculator.centimetersToPixel(wall.getDisX());
		int disY = calculator.centimetersToPixel(wall.getDisY());
		
		doors = new ArrayList<DoorPanel>();
		windows = new ArrayList<WindowPanel>();
		
		if(disX > 0 && disY > 0){
			setBounds(firstX, firstY, Math.abs(disX),Math.abs(disY));
		} else {
			
			if(wall.getDisX() < 0) {
				setBounds(xPos, yPos - calculator.centimetersToPixel(Wall.WALLTHICK) , Math.abs(disX),Math.abs(disY));
				} 
			else if(wall.getDisY() < 0){
				setBounds(xPos  - calculator.centimetersToPixel(Wall.WALLTHICK), yPos, Math.abs(disX),Math.abs(disY));
			} else {
				setBounds(xPos, yPos, Math.abs(disX),Math.abs(disY));
			}	
			
		}
		System.out.println("Hallo"+ firstX + " "+firstY);
		
		//Damit Objekte absolut platziert werden können
		setLayout(null);
		setBackground(Color.gray);
		setVisible(true);
		myMouseListener = WallAction(this); 
		addMouseListener(myMouseListener);	
	}

	public void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		Map<String,RoomComponent> changes = (Map<String,RoomComponent>) arg;
		
		DimensionCalculator calculator = DimensionCalculator.getInstance();
		RoomComponent cp = changes.get("create");
		if(cp instanceof Door){
			
			Door door = (Door)cp;
			DoorPanel doorP = new DoorPanel(door,dA, wall);
			int x = calculator.centimetersToPixel(cp.getXPos());
			int y = calculator.centimetersToPixel(cp.getYPos());
			int lenX = calculator.centimetersToPixel(cp.getXLength());
			int lenY = calculator.centimetersToPixel(cp.getYLength());
			if(door.getDirection()=='h'){
				y = 0;
			} else{
				x = 0;
			}
			doorP.setBounds(x, y, lenX, lenY);
			doors.add(doorP);
			add(doorP);
		}
		if(cp instanceof Window){
			
			Window window = (Window)cp;
			WindowPanel windowP = new WindowPanel(window, dA, wall);
			int x = calculator.centimetersToPixel(cp.getXPos());
			int y = calculator.centimetersToPixel(cp.getYPos());
			int lenX = calculator.centimetersToPixel(cp.getXLength());
			int lenY = calculator.centimetersToPixel(cp.getYLength());
			if(window.getDirection()=='h'){
				y = 0;
			} else{
				x = 0;
			}
			windowP.setBounds(x, y, lenX, lenY);
			windows.add(windowP);
			add(windowP);
		}
		cp = changes.get("delete");
		
		if(cp instanceof Door){
			for(DoorPanel dP : doors){
				if(dP.getDoor()== (Door)cp){
					remove(dP);
					doors.remove(dP);
					break;
				}
			}
			
		}
		if(cp instanceof Window){
			for(WindowPanel wP : windows){
				if(wP.getWindow() == (Window)cp){
					remove(wP);
					windows.remove(wP);
					break;
				}
			}
			
		}
		repaint();
	}
	public void removeListener(){
		removeMouseListener(myMouseListener);
		
	}
	public void addListener(){
		addMouseListener(myMouseListener);
		
	}
	
	private MouseAdapter WallAction(final WallPanel wP){
		MouseAdapter mA = new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(!dA.getRoomComponent().equals("")){
					try {
						cdm.execPush(new PutBuildComponent(dA.getRoomComponent(),calculator.pixelToCentimeters(arg0.getX()), calculator.pixelToCentimeters(arg0.getY()), wall));
					} catch (ControlException | BuildException
							| FurnishException | SaveException e) {
						dA.getActFloor().displayErrorMessage(new ErrorMessage(e.getMessage()));
						
					}
					//wieder leeren
					dA.setRoomComponent("");
				}else if(myFloor.getDrawArea().getDelete()){
					myFloor.del(wP);
				} else{
					myFloor.mark(wP);
					dA.requestFocusInWindow();
				}
			}
		};
		
		
		return mA;
	}
	
	public Wall getWall(){
		return wall;
	}
}
