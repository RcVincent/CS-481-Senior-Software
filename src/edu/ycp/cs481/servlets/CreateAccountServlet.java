package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class CreateAccountServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doGet");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.CREATE_USER)){
				req.setAttribute("managerCreate", "true");
				req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to Create a User!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Create Account Servlet: doPost");
		
		UserController userControl = new UserController();
		
		boolean goodUser = true;
		
		// Get the information
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String emailConfirm = req.getParameter("emailConfirm");
		String password = req.getParameter("password");
		String passwordConfirm = req.getParameter("passwordConfirm");
		
		if(firstName == null || firstName.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("firstNameError", "Please enter first name!");
		}
		
		if(lastName == null || lastName.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("lastNameError", "Please enter last name!");
		}
		
		if(email == null || email.equalsIgnoreCase("")){
			goodUser = false;
			req.setAttribute("emailError", "Please enter a valid email!");
		}else{
			ArrayList<User> users = userControl.searchForUsers(-1, -1, false, email, false, null, false, null, -1, -1);
			boolean quarantineUserFound = userControl.findQuarantineUser(email);
			if(users != null && users.size() > 0 || quarantineUserFound){
				goodUser = false;
				req.setAttribute("emailError", "Email is already taken");
			}
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
			// Insert into the database
			userControl.insertQuarantineUser(email, password, firstName, lastName);
			
			System.out.println("User " + firstName + " " + lastName +" inserted with email " + email);
			
			String managerCreate = (String) req.getParameter("managerCreate");
			System.out.println("MANCREATE: " + managerCreate);
			if(managerCreate != null && managerCreate.equalsIgnoreCase("true")){
				// TODO: Send different email with info?
				req.getSession().setAttribute("success", "Created Account with email " + email + " for " + firstName + " " +
						lastName);
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}else{
				resp.sendRedirect(req.getContextPath() + "/login");
			}
		}else{
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("email", email);
			req.setAttribute("emailConfirm", emailConfirm);
			req.getRequestDispatcher("/create_account.jsp").forward(req, resp);
		}
	}
}