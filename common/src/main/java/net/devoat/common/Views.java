package net.devoat.common;

public class Views {

	public static interface Add {
		
	}
	public static interface Update {
		
	}
	public static interface List {
		
	}
	public static interface View extends List{
		
	}	
	public static interface Allways extends Add,Update,List,View{
		
	}
	
}
