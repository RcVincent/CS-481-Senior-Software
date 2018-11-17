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
public class VerifyEmailServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Verify Email Servlet: doGet");
		req.getRequestDispatcher("/verify_email.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Verify Email Servlet: doPost");
		
		String errorMessage = null;
		String email = null;
		String pin = null;
		int userID;
		
		UserController uc = new UserController();
		
		email = req.getParameter("email");
		pin = req.getParameter("pin");
		
		if(email == null || pin == null || email.equals("") || pin.equals("")) {
			errorMessage = "Please specify both email and pin"; 
		}else{
			userID = uc.verifyUser(email,pin);
		}
		if(errorMessage != null){
			req.setAttribute("errorMessage", errorMessage);
			req.getRequestDispatcher("/verify_email.jsp").forward(req, resp);
		}else{
			HttpSession session = req.getSession();
			resp.sendRedirect(req.getContextPath() + "/login");
		}	
	}
}
