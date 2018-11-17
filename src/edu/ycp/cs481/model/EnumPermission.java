package edu.ycp.cs481.model;

public enum EnumPermission{
	ALL(1, "all"),
	CREATE_POSITION(2, "createPosition"), CREATE_SOP(3, "createSOP"), CREATE_USER(4, "createUser"),
	HAVE_SUBORDINATES(5, "haveSubordinates"), SEARCH_USERS(6, "searchUsers"),
	EDIT_USERS(7, "editUsers"), EDIT_POSITIONS(8, "editPositions"), EDIT_SOPS(9, "editSOPs");
	
	private final int id;
	private final String perm;
	
	private EnumPermission(int id, String perm){
		this.id = id;
		this.perm = perm;
	}
	
	public int getID(){
		return id;
	}
	
	public String getPerm(){
		return perm;
	}
}
