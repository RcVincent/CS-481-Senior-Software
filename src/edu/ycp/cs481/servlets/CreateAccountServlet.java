package edu.ycp.cs481.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class CreateAccountServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doGet");
		
		// TODO: Check for manager/admin status
		
		req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doPost");
		
		User userProfile = new User();
		UserController uc = new UserController();
		
		boolean goodUser = true;
		
		// Get the information
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String emailConfirm = req.getParameter("emailConfirm");
		String password = req.getParameter("password");
		String passwordConfirm = req.getParameter("passwordConfirm");
		System.out.println("First Name: " + firstName);
		
		if(firstName == null || firstName.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("firstNameError", "Please enter first name!");
		}
		
		if(lastName == null || lastName.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("lastNameError", "Please enter last name!");
		}
		
		if(email == null || email.equalsIgnoreCase("")){
			// TODO: Check that it's a valid email
			goodUser = false;
			req.setAttribute("emailError", "Please enter a valid email!");
		}
		
		if(!email.equals(emailConfirm)){
			goodUser = false;
			req.setAttribute("emailConfirmError", "Emails don't match!");
		}
		
		if(password == null || password.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("passwordError", "Password can't be blank!");
		}
		
		if(!password.equals(passwordConfirm)){
			goodUser = false;
			req.setAttribute("passwordConfirmError", "Passwords don't match!");
		}
		
		if(goodUser){
			// TODO: Handling stuff for when Manager/Admin creates Account
			
			//add to the DB
			int id = uc.insertUserAndGetID(userProfile);
			
			System.out.println("User inserted with id" + id);
			req.setAttribute("sessionid", userProfile);
			
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("email", email);
			req.setAttribute("emailConfirm", emailConfirm);
			req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
		}
		
		
	}
}