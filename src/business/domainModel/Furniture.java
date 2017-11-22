package business.domainModel;

import java.awt.Color;

import business.validators.RuleEnum;

import com.google.gson.annotations.Expose;

/**
 * Domainmodel eines Möbelstücks.
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 *
 */
public class Furniture extends RoomComponent {
	private static final long serialVersionUID = 307233224973971606L;
	@Expose
	private char form;
	private Furniture beneath;
	@Expose
	private RuleEnum placeRule;
	@Expose
	private String texturePath;
	@Expose
	private Color color;
	
	public Furniture(int id, char form, String name, int xPos, int yPos,
			int xLength, int yLength, double angle, Floor myFloor, RuleEnum placeRule, Furniture beneath, String texturePath, Color color) {
		super(name, xPos, yPos, xLength, yLength, angle, myFloor, id);
		this.form = form;
		this.placeRule = placeRule;
		this.beneath = beneath;
		this.texturePath = texturePath;
		this.color = color;
	}

	public char getForm() {
		return form;
	}

	public void setForm(char newForm) {
		form = newForm;
	}
	
	public Floor getMyFloor(){
		return myFloor;
	}
	
	public RuleEnum getRuleEnum(){
		return placeRule;
	}
	
	public void setBeneath(Furniture beneath){
		this.beneath = beneath;
	}
	public void setId(int id){
		this.id = id;
	}
	/**
	 * Methode zum Duplizieren des Möbelstücks
	 */
	public Furniture clone() {
		Furniture newF = new Furniture(id, form, name, xPos, yPos, xLength, yLength, angle, myFloor, placeRule, beneath, texturePath, color);
		return newF;
	}
	
	public String getTexturePath(){
		return texturePath;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}