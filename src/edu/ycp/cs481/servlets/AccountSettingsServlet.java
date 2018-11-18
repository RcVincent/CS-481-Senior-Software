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
public class AccountSettingsServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Account Settings Servlet: doGet");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			ArrayList<User> user = uc.searchForUsers((int) session.getAttribute("user_id"), -1, false, null, false, null, 
					false, null, -1, -1);
			session.setAttribute("email", user.get(0).getEmail());
			req.getRequestDispatcher("/account_settings.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Account Settings Servlet: doPost");
		
		HttpSession session = req.getSession();
		String action = req.getParameter("doStuff");
		
		UserController uc = new UserController();
		ArrayList<User> users = uc.searchForUsers((int) session.getAttribute("user_id"), -1, false, null, false, null, 
				false, null, -1, -1);
		User user = users.get(0);
		
		if(action.equalsIgnoreCase("changeEmail")){
			String email = user.getEmail();
			String newEmail = req.getParameter("newEmail");
			String newEmailConfirm = req.getParameter("newEmailConfirm");
			String password = req.getParameter("emailPass");
			
			boolean error = false;
			
			if(!newEmail.equals(newEmailConfirm)){
				req.setAttribute("changeEmailError", "Emails don't match!");
				error = true;
			}else if(!uc.authenticate(user, password)){
				req.setAttribute("changeEmailError", "Incorrect password!");
				error = true;
			}else{
				uc.changeUserEmail(user.getID(), email, newEmail);
				req.setAttribute("email", newEmail);
				req.setAttribute("changeEmailSuccess", "Changed email to " + newEmail);
			}
			if(error){
				req.setAttribute("email", email);
				req.setAttribute("newEmail", newEmail);
				req.setAttribute("newEmailConfirm", newEmailConfirm);
			}
			req.getRequestDispatcher("/account_settings.jsp").forward(req, resp);
		}else if(action.equalsIgnoreCase("changePassword")){
			String newPassword = req.getParameter("newPassword");
			String newPasswordConfirm = req.getParameter("newPasswordConfirm");
			String password = req.getParameter("passPass");
			
			if(!newPassword.equals(newPasswordConfirm)){
				req.setAttribute("changePasswordError", "Passwords don't match!");
			}else if(!uc.authenticate(user, password)){
				req.setAttribute("changePasswordError", "Incorrect password!");
			}else{
				uc.changeUserPassword(user.getID(), newPassword);
				req.setAttribute("changePasswordSuccess", "Changed password");
			}
			req.getRequestDispatcher("/account_settings.jsp").forward(req, resp);
		}
	}
}
