package view.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Klasse, die eine Fehlernachricht ausgibt und diese langsam wieder ausfadet.
 * 
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class ErrorMessage extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4281998925600839788L;

	Timer timer;
	private float alpha = 1f;
	private String msg;

	private long start;
	private static long STAYTIME = 1000;
	private int delay;

	private int height;
	private Color background;

	public ErrorMessage(String msg) {
		this.height = 40;
		
		delay = 30;
		background = new Color(200, 9, 9);
		this.msg = msg;
		
		timer = new Timer(delay, this);
	}
	
	/**
	 * Setzt die Position der Fehlernachricht
	 * 
	 * @param xPos X-Position in Pixeln
	 * @param yPos Y-Position in Pixeln
	 * @param width Breite in Pixeln
	 * @param heigth Länge in Pixeln
	 */
	public void setBound(int xPos, int yPos, int width, int heigth){
		this.height = heigth;
		setBounds(xPos, yPos, width, heigth);
	}
	
	/**
	 * Startet den Timer
	 */
	public void startTimer(){
		start = System.currentTimeMillis();
		timer.start();
	}
	
	/**
	 * Drawed die Nachricht.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g2d.setColor(background);
		
		g2d.setPaint(background);
	    g2d.setFont(new Font("SansSerif", Font.PLAIN, 40));
		int x = 20;
		int y =  height- height/5;
		g2d.drawString(msg, x, y);
		g2d.dispose();
	}
	
	/**
	 * Faded langsam aus bis Zeit abgelaufen ist und Timer gestoppt wird.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getWhen() > (start + STAYTIME)) {
			alpha += -0.01f;
			if (alpha <= 0) {
				alpha = 0;
				timer.stop();
			}
			repaint();
		}
	}
}