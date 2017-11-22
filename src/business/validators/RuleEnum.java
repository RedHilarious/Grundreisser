package business.validators;

public enum RuleEnum {
	
	BeneathAndAbove(true,true),
	Beneath(true, false),
	Above(false, true),
	NeitherBeneathNorAbove(false,false);
	
	public boolean beneath;
	public boolean above;
	
	private RuleEnum(boolean beneath, boolean above){
		this.beneath = beneath;
		this.above = above;
	}
}
