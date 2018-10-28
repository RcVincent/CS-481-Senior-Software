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


public class RetryChangeEmailServlet extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		/*HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}*/
		
		req.getRequestDispatcher("/retrychangeemail.jsp").forward(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		HttpSession session = req.getSession();
		
		String email = (String) session.getAttribute("email");
		UserController uc = new UserController(); 
		ArrayList<User> result = uc.searchForUsers(0, email, "", "", 0); 
		User user = result.get(0);
		
		String oldEmail = (String) req.getParameter("oldEmail"); 
		String newEmail = (String) req.getParameter("newEmail");
		String newEmail2 = (String) req.getParameter("newEmail2");
		
		if(newEmail.equals(newEmail2)) {
			uc.changeUserPassword(oldEmail, newEmail, user.getPassword());
			System.out.println(oldEmail); 
			System.out.println(newEmail); 
			System.out.println(user.getPassword()); 
		}
		
		else {
			System.out.println("The passwords donot match please try again");
			resp.sendRedirect(req.getContextPath() + "/retrychangeemail");
		}
		
		req.getRequestDispatcher("/retrychangeemail.jsp").forward(req, resp);
	}

}
