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
		
		String token = req.getParameter("token");
		boolean verify = false;
		UserController uc = new UserController();
		
		verify = uc.verifyUser(token);
		
		if(!verify) {
			req.setAttribute("errorMessage", "Email verification failed");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		} else {
			req.setAttribute("errorMessage", "Email successfully verified");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
		
		req.getRequestDispatcher("/verify_email.jsp").forward(req, resp);
	}
}
