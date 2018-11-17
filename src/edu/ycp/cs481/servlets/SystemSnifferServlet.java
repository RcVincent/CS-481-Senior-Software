package edu.ycp.cs481.servlets;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.SystemSnifferController;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Messenger;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class SystemSnifferServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL)){
				// Only admins with full permissions can go here ^
				req.getRequestDispatcher("/search_system.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to Swiffer the Seinor!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		SystemSnifferController sfc = new SystemSnifferController(); 
		UserController uc = new UserController(); 
		String action = req.getParameter("WhatToSniff");
		
		if(action.equalsIgnoreCase("searchSpecific")) {
		String searchID = req.getParameter("user_id");
		
		if(searchID.equalsIgnoreCase("") || searchID == null) {
			System.out.println("There is no ID to search by don't do the search.");
		}
		else {
			int search_id = Integer.parseInt(searchID);
			User u = uc.searchForUsers(search_id, 0, false, "", false, "", false, "", 0, -1).get(0);
			Position p = u.getPosition();
			
			if(!sfc.checkIfToDoIsEmpty(p)) {
				req.setAttribute("historyNotEmpty", "There are SOPs you must finish");
				Messenger.main(new String[] {u.getEmail(), "Incomplete Training", u.getFirstName() + ", you have incomplete SOPs in your training"
						+ "	history, please complete these as soon as possible. A message will also be sent to your manager. Have a great day."});
			} else {
				System.out.println("There were no gaps in this users history");
				req.setAttribute("SuccessMessage", "There were no gaps in this users training history.");
				
			}
			req.getRequestDispatcher("/search_system.jsp").forward(req, resp);
			
			
		}
		
		}
		else if(action.equalsIgnoreCase("searchAll")) {
			sfc.SniffDeeply();
			req.getRequestDispatcher("/search_system.jsp").forward(req, resp);
		}
	}
}
