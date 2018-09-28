package controller;
import model.User;
public class UserController {
    
    U = this.User();

    public void login(){
        U.setLoginStatus(true);
    }
    
    public void logout(){
    U.setLoginStatus(false);
    }
    
    public boolean Authenticate(){
        return false;
    }
    
    public void addPosition(Position p){
        
    }

    public void ChangeEmail(){

    }
    
    public void Archive(){
        U.setArchiveFlag(true);
    } 
}
