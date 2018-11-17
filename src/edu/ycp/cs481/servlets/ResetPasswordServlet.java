package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;

@SuppressWarnings("serial")
public class ResetPasswordServlet extends HttpServlet{
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
		
		UserController uc = new UserController();
		
		email = req.getParameter("email");
		
		if(email == null || email.equals("")) {
			errorMessage = "Please specify an email"; 
		}else{
			uc.resetPassword(email);
		}
		if(errorMessage != null){
			req.setAttribute("errorMessage", errorMessage);
			req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
		}else{
			HttpSession session = req.getSession();
			resp.sendRedirect(req.getContextPath() + "/login");
		}	
	}
}