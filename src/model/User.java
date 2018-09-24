package model;

public class User {
	private int UserID;
	private String Username, Password, Email;
	private Position position; 
	private boolean AdminFlag, ArchiveFlag, validUser; 
	/// Constructor(s)
	
	
	public User() {
		
	}
	
	/// Getters/Setters
	
	public void setUserID(int UserID) {
		this.UserID = UserID;	
	}
	
	public int getUserID() {
		return this.UserID;	
	}
	
	public void setUsername(String Username) {
		this.Username = Username;	
	}
	
	public String getUsername() {
		return this.Username;	
	}
	
	public void setPassword(String Password) {
		this.Password = Password;	
	}
	
	public String getPassword() {
		return this.Password;	
	}
	
	public void setEmail(String Email) {
		this.Email = Email;	
	}
	
	public String getEmail() {
		return this.Email;	
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position p) {
		this.position = p;
	}

	public boolean isArchiveFlag() {
		return ArchiveFlag;
	}

	public void setArchiveFlag(boolean archiveFlag) {
		ArchiveFlag = archiveFlag;
	}

	public boolean isAdminFlag() {
		return AdminFlag;
	}

	public void setAdminFlag(boolean adminFlag) {
		AdminFlag = adminFlag;
	}

	public boolean isValidUser() {
		return validUser;
	}

	public void setValidUser(boolean validUser) {
		this.validUser = validUser;
	}

}
