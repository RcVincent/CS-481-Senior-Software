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
			ArrayList<User> users = null; 
			//match password
			
			if(users != null && users.size() > 0) {
				User u = users.get(0);
				System.out.println(u.getEmail());
				//Time to authenticate
				if(uc.authenticate(loginUser, password) == true) {
					HttpSession session = req.getSession(); 
					session.setAttribute("email", loginUser.getEmail());
					session.setAttribute("user_id", loginUser.getUserID());
					session.setAttribute("admin_flag", loginUser.isAdminFlag());
					session.setAttribute("first_name", loginUser.getFirstname());
					session.setAttribute("last_name", loginUser.getLastname());
					System.out.println("Session info");
					System.out.println(req.getSession().getAttribute("email"));
					System.out.println(req.getSession().getAttribute("user_id"));
					System.out.println(req.getSession().getAttribute("first_name"));
					System.out.println(req.getSession().getAttribute("last_name"));
					System.out.println(req.getSession().getAttribute("admin_flag"));
					
					loginUser.setSessionid(req.getSession().getId());
					
					if(u.isAdminFlag().equals("Admin") || u.isAdminFlag().equals("Admin")) {
						resp.sendRedirect(req.getContextPath() + "/index");
					} else {
						resp.sendRedirect(req.getContextPath() + "/MainPage");
					}
					
				} else {
					errorMessage = "Incorrect email or Password";
					req.setAttribute("errorMessage", errorMessage);
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
				}
				
			}
			else {
				errorMessage = "Incorrect email or password";
				req.setAttribute("errorMessage", errorMessage);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				System.out.println("  Invalid Login -- returning to /Login");
				
			}
			
			req.setAttribute("sessionid", loginUser.getSessionid());
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}		
	}
}
