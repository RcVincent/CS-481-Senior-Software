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
		ArrayList<User> user = null;
		
		UserController uc = new UserController();
		
		email = req.getParameter("email");
		password = req.getParameter("password");
		
		if(email == null || password == null || email.equals("") || password.equals("")) {
			errorMessage = "Please specify both email and password"; 
		}else{
			user = uc.searchForUsers(-1, email, null, null, -1);
			if(user == null || user.size() == 0 || !uc.authenticate(user.get(0), password)){
				errorMessage = "Incorrect email or password";
			}
		}
		if(errorMessage != null){
			System.out.println("LoginServlet doPost ERROR:"+errorMessage);
			req.setAttribute("errorMessage", errorMessage);
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
			
		}else{
			User u = user.get(0);
			HttpSession session = req.getSession();
			session.setAttribute("user_id", u.getUserID());
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
