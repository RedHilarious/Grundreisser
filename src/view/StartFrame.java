package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

/**
 * StartFrame wird bei Start des Programmes erzeugt und bietet die Auswahl mit
 * welchem Modus das Programm gestartet werden soll (vorhandener Plan laden,
 * neuen Plan erstellen oder Standardgrundriss laden).
 * 
 * Nach Auswahl einer Option wird der eigentliche Frame des Programms geladen.
 * 
 * @author rhaba001
 * 
 */
public class StartFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 568341768850997666L;
	private JButton load, newPlot, newGroundPlot_L, newGroundPlot_R;
	private JPanel container, image, signature;
	private JFileChooser fc;

	public StartFrame() {

		// konfiguriere das Theme des Look and Feels
		NimRODTheme nt = new NimRODTheme();
		nt.setPrimary1(new Color(31, 46, 58));
		nt.setPrimary2(new Color(200, 200, 200));
		nt.setPrimary3(new Color(51, 66, 78));
		nt.setSecondary1(new Color(59, 59, 59));
		nt.setSecondary2(new Color(69, 69, 69));
		nt.setSecondary3(new Color(79, 79, 79));
		nt.setWhite(new Color(113, 113, 113));
		nt.setBlack(new Color(255, 255, 255));
		nt.setMenuOpacity(0);
		nt.setFrameOpacity(0);

		// setze das Look and Feel
		NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
		NimRODLookAndFeel.setCurrentTheme(nt);

		try {
			UIManager.setLookAndFeel(NimRODLF);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Panels initialiseren
		initComponents();
	}

	public void initComponents() {
		Toolkit tk = Toolkit.getDefaultToolkit();

		setSize(tk.getScreenSize());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// StartFenster in die Mitte setzen
		Dimension dim = tk.getScreenSize();
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		setLocation(x, y);

		image = new ImagePanel("config/grundriss.png");
		image.setPreferredSize(new Dimension(700, 600));

		signature = new SignaturePanel();
		signature.setPreferredSize(new Dimension(700, 100));

		container = new JPanel();

		load = new JButton("Lade vorhandenes Projekt");
		load.addActionListener(FileAction(this));
		load.setActionCommand("LoadProjekt");

		newPlot = new JButton("Neues Projekt erstellen");
		newPlot.addActionListener(this);
		newPlot.setActionCommand("NewProjekt");

		newGroundPlot_L = new JButton("Grundriss L");
		newGroundPlot_L.setActionCommand("NewGroundPlotL");
		newGroundPlot_L.addActionListener(this);
		
		newGroundPlot_R = new JButton("Grundriss V-Eck");
		newGroundPlot_R.setActionCommand("NewGroundPlotR");
		newGroundPlot_R.addActionListener(this);

		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("Grundreisser-Dateien",
				"gr"));

		container.add(load);
		container.add(newPlot);
		container.add(newGroundPlot_L);
		container.add(newGroundPlot_R);

		add(container, BorderLayout.CENTER);
		add(image, BorderLayout.NORTH);
		add(signature, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	private ActionListener FileAction(final JFrame me) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				int returnVal = -1;

				if (cmd.equals("LoadProjekt")) {
					returnVal = fc.showOpenDialog(me);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// start Program
						me.setVisible(false);
						me.dispose();
						App.startGrundreisser(fc.getSelectedFile());
					}
				}
			}
		};
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("NewProjekt") || cmd.equals("NewGroundPlotL") || cmd.equals("NewGroundPlotR")) {
			// start Program
			this.dispose();
			App.startGrundreisser(cmd);
		}
	}

	public void exitProgram() {
		System.exit(0);
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = -1777303358380950337L;
		private BufferedImage image;

		public ImagePanel(String path) {
			try {
				image = ImageIO.read(new File(path));
			} catch (IOException e) {
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setColor(Color.white);

			g2.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 50));
			String header = "Grundreisser 1.0";
			// Startposition aus Breite des Feldes und Stringlänge berechnen
			int stringLen = (int) g2.getFontMetrics()
					.getStringBounds(header, g2).getWidth();
			g2.drawString(header, (getWidth() / 2 - stringLen / 2), 100);

			g2.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 15));
			String explanation = "Bitte zum Starten des Einrichtungsplaners auswählen:";
			stringLen = (int) g2.getFontMetrics()
					.getStringBounds(explanation, g2).getWidth();
			g2.drawString(explanation, (getWidth() / 2 - stringLen / 2), 580);

			g2.drawImage(image, 130, 160, image.getWidth(), image.getHeight(),
					null);
		}

	}

	private class SignaturePanel extends JPanel {
		private static final long serialVersionUID = -1777303358380950337L;
		private BufferedImage image;

		public SignaturePanel() {
			try {
				image = ImageIO.read(new File("config/copyright.png"));
			} catch (IOException e) {
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setColor(Color.white);

			// Konfiguration der Signatur
			g2.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 10));
			String signature = " by Rahel Habacker, Elizabeth Schneiss, Jonas Theis";
			int stringLen = (int) g2.getFontMetrics()
					.getStringBounds(signature, g2).getWidth();
			g2.drawString(signature, (getWidth() / 2 - stringLen / 2), 80);

			g2.drawImage(image, 200, 67, 20, 20, null);
		}

	}
}
