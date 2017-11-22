package view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ToolsButton extends JButton {
	private static final long serialVersionUID = -9165371916506347698L;
	private String text;
	private int height;
	
	private BufferedImage texture;
	private int imageHeight;
	private int imageWidth;
	
	public ToolsButton(String text) {
		this(text, null, 0, 0);
	}
	
	public ToolsButton(String text, String texturePath, int imageWidth, int imageHeight) {
		this.text = text;
		this.height = 50;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		setPreferredSize(new Dimension(100, 50));
		
		// Lese Bild ein
		if (texturePath != null) {
			try {
				texture = ImageIO.read(new File(texturePath));
			} catch (IOException e1) {
				texture = null;
			}
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.white);
		
		g2.setFont(new Font("Helvetica", Font.PLAIN, 13));
		
		// Startposition aus Breite des Feldes und Stringlänge berechnen
		int stringLen = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		
		// Bild zeichnen
		if (texture != null) {
			g2.drawImage(texture, 12, (height-imageHeight) / 2, imageWidth, imageHeight, null);
			// zeichne String etwas nach rechts versetzt
			g2.drawString(text, (getWidth() / 2 - stringLen / 2 + (imageWidth+4) / 2), (height / 2 + 5));
			
		} else {
			// Zeichne String zentriert
			g2.drawString(text, (getWidth() / 2 - stringLen / 2), (height / 2 + 5));
		}
		
	}
	
}
