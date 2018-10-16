package edu.ycp.cs481.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.User;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class CreateAccountServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doGet");
		
		HttpSession session = req.getSession(); 
		System.out.println(session.getAttribute("email")); 
		if(session.getAttribute("email") == null) {
			resp.sendRedirect(req.getContextPath() + "/Login");
			return;
		}
		
		//do the request 
		req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doPost");
		
		User userProfile = new User();
		UserController uc = new UserController(); 
		
		//set the information
		String Email = (String) req.getParameter("email");
		userProfile.setEmail(Email);
		
		String Password = (String) req.getParameter("password");
		userProfile.setPassword(Password);
		
		String Firstname = (String) req.getParameter("first_name");
		userProfile.setFirstname(Firstname);
		
		String Lastname = (String) req.getParameter("last_name");
		userProfile.setLastname(Lastname);
		
		String isAdmin = (String) req.getParameter("admin_flag");
		userProfile.setAdminFlag(Boolean.parseBoolean(isAdmin));
		
		//add to the DB
		//uc
		req.setAttribute("sessionid", userProfile);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/Index");
		}
		
		req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
		
	}
	
	
}
