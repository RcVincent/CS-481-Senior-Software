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

public class SearchUsersServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		
		req.getRequestDispatcher("/searchUsers.jsp").forward(req, resp);
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		UserController uc = new UserController(); 
		
		String email = (String) req.getParameter("email");
		String fname = (String) req.getParameter("firstname"); 
		String lname = (String) req.getParameter("lastname");
		String id = (String) req.getParameter("userID");
		String pID = (String) req.getParameter("positionID");
		int searchID;
		int posSearchID; 
		
		if(id == null || id == "" || id == " ") {
			searchID = -1; 
		}
		
		else {
			searchID = Integer.parseInt(id);
		}
		
		if(pID == null || pID == "" || pID == " ") {
			posSearchID = 2;
		}
		else {
			posSearchID = Integer.parseInt(pID);
		}
		
		ArrayList<User> result = uc.searchForUsers(searchID, email, fname, lname, posSearchID);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/searchUsers.jsp").forward(req, resp);
	}
}
