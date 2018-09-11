package model;

public class User {
	private int UserID;
	private String Username, Password, Email;
	
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
}
