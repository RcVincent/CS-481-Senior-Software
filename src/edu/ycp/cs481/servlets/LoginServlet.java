package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Login Servlet: doGet");
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Login Servlet: doPost");
		
		String errorMessage = null;
		String email = null; 
		String password = null; 
		
		UserController uc = new UserController(); 
		User loginUser = new User(); 
		
		email = req.getParameter("email");
		password = req.getParameter("password");
		
		System.out.println("    Name: <"+email+"> PW: <" +password+">"); 
		
		if(email == null || password == null || email.equals("") || password.equals("")) {
			errorMessage = "Please specify both email and password"; 
			
		} else {
			ArrayList<User> users =  uc.searchForUsers(-1, email, null, null, -1);
			if(users == null || !uc.authenticate(users.get(0), password)){
				errorMessage = "Incorrect email or Password";
				req.setAttribute("email", null);
				req.setAttribute("password", null);
				req.setAttribute("errorMessage", errorMessage);
				req.getRequestDispatcher("/login").forward(req, resp);
			}else{
				System.out.println("Successful Login");
				User u = users.get(0);
				HttpSession session = req.getSession();
				session.setAttribute("user_id", u.getUserID());
				// Only did user_id because other data would be grabbed from the User object as needed?
				// Not sure 100% on that, but wouldn't we just need a verified login and the rest we can grab as needed?
				/*session.setAttribute("email", loginUser.getEmail());
				session.setAttribute("user_id", loginUser.getUserID());
				session.setAttribute("admin_flag", loginUser.isAdminFlag());
				session.setAttribute("first_name", loginUser.getFirstname());
				session.setAttribute("last_name", loginUser.getLastname());*/
				u.setSessionid(session.getId());
				
				// TODO: In the future, all users would go to a "user home" and in it, we would only give the options
				// that they can actually do
				if(u.isAdminFlag()){
					resp.sendRedirect(req.getContextPath() + "/index");
				}else{
					resp.sendRedirect(req.getContextPath() + "/MainPage");
				}
			}
		}	
	}
}
