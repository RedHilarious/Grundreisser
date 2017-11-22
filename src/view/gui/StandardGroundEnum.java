package view.gui;

public enum StandardGroundEnum {
	
	L (new int[][]{{122, 128, 1198, 128},
		{1198, 128, 1198, 1084},
		{1198, 1084, 846, 1084},
		{846, 1084, 846, 724},
		{846, 724, 124, 724},
		{124, 724, 122, 128}}),
	
	R (new int[][] {{100 ,100, 1100, 100},
					{1100, 100, 1100, 1100},
					{1100, 1100, 100, 1100},
					{100, 1100, 100, 100}});
	
	public int [][] corners;
	
	
	StandardGroundEnum(int [][] corners) {
	        this.corners = corners;
	        
	     		
	}
}
