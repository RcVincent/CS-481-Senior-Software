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
public class SearchUsersServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			req.getRequestDispatcher("/search_users.jsp").forward(req, resp);
		}
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String idStr = req.getParameter("userID");
		String eIDStr = req.getParameter("employeeID");
		String posIDStr = req.getParameter("positionID");
		
		int userID = idStr.equalsIgnoreCase("")?-1:Integer.parseInt(idStr);
		int employeeID = eIDStr.equalsIgnoreCase("")?-1:Integer.parseInt(eIDStr);
		String email = req.getParameter("email");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		int positionID = posIDStr.equalsIgnoreCase("")?-1:Integer.parseInt(posIDStr);
		
		UserController uc = new UserController();
		ArrayList<User> users = uc.searchForUsers(userID, employeeID, true, email, true, firstName, true, lastName, positionID, -1);
		
		for(int i = 0; i < users.size(); i++){
			req.setAttribute("userID" + (i+1), users.get(i).getUserID());
			req.setAttribute("employeeID" + (i+1), users.get(i).getEmployeeID());
			req.setAttribute("email" + (i+1), users.get(i).getEmail());
			req.setAttribute("firstName" + (i+1), users.get(i).getFirstName());
			req.setAttribute("lastName" + (i+1), users.get(i).getLastName());
			req.setAttribute("posTitle" + (i+1), users.get(i).getPosition().getTitle());
		}
		
		req.getRequestDispatcher("/search_users.jsp").forward(req, resp);
	}
}
