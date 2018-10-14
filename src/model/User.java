package model;

public class User {
	private int UserID;
	private String Password, Email;
	private Position position; 
	private boolean ArchiveFlag, validUser; 
	private boolean loggedin;
	private String AdminFlag, Firstname, Lastname; 
	private String sessionid;
	/// Constructor(s)
	public User() {
		
	}
	
	public User(int UserID, String Password, String Email, Position position) {
		this.UserID = UserID;
		this.Password = Password;
		this.Email = Email;
		this.position = position;
		loggedin = false;
	}
	
	/// Getters/Setters
	
	public void setUserID(int UserID) {
		this.UserID = UserID;	
	}
	
	public int getUserID() {
		return this.UserID;	
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

	public String isAdminFlag() {
		return AdminFlag;
	}

	public void setAdminFlag(String adminFlag) {
		AdminFlag = adminFlag;
	}

	public boolean isValidUser() {
		return validUser;
	}

	public void setValidUser(boolean validUser) {
		this.validUser = validUser;
	}

	public boolean getLoginStatus() {
		return loggedin;
	}

	public void setLoginStatus(boolean l) {
		loggedin = l;
	}

	public String getFirstname() {
		return Firstname;
	}

	public void setFirstname(String firstname) {
		Firstname = firstname;
	}

	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		Lastname = lastname;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

}
