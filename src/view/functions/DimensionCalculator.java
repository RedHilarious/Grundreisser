package view.functions;


/**
 * Singleton DimensionCalculater.
 * 
 * Rechnet die Pixelwerte auf der Zeichenflaeche in Meter zur Anzeige um
 * und anders herum.
 * 
 *  @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class DimensionCalculator {

	private static DimensionCalculator dimCalc;
	public static final double CONVERT_METERS = 100.0;
	private static final int CONVERTFACTOR = 2;
	
	public static DimensionCalculator getInstance(){
		if(dimCalc == null){
			dimCalc = new DimensionCalculator();
		}
		return dimCalc;
	}
	
	public int pixelToCentimeters(int pix) {
		return pix * CONVERTFACTOR;
	}
	
	public int centimetersToPixel(int cm) {
		return Math.round(cm / CONVERTFACTOR);
	}
	
	public int pixelToMeters(int pix){
		return pixelToCentimeters(pix)/100;
	}
	
}