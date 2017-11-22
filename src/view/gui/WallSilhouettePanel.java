package view.gui;

import java.awt.Color;

import javax.swing.JPanel;

import view.functions.DimensionCalculator;
import business.domainModel.Wall;

public class WallSilhouettePanel extends JPanel {

	private static final long serialVersionUID = -3265208316016452294L;
	final int WALLTHICK = 24;
	private int firstX;
	private int firstY;
	private int xPos;
	private int yPos;
	
	private DimensionCalculator calculator;

	public WallSilhouettePanel(Wall wall) {
		calculator = DimensionCalculator.getInstance();
		this.firstX = calculator.centimetersToPixel(wall.getStartX());
		this.firstY = calculator.centimetersToPixel(wall.getStartY());
		this.xPos = calculator.centimetersToPixel(wall.getEndX());
		this.yPos = calculator.centimetersToPixel(wall.getEndY());
		
		int disX = calculator.centimetersToPixel(wall.getDisX());
		int disY = calculator.centimetersToPixel(wall.getDisY());
				
		if(disX > 0 && disY > 0){
			setBounds(firstX, firstY, Math.abs(disX),Math.abs(disY));
		} else {
			setBounds(xPos, yPos , Math.abs(disX),Math.abs(disY));
		}

		setBackground(Color.gray.brighter());
		setVisible(true);
	}
}
