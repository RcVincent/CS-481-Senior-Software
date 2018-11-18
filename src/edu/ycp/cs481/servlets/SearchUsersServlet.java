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
public class SearchUsersServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.SEARCH_USERS)){
				ArrayList<User> users = uc.searchForUsers(-1, -1, false, null, false, null, false, null, -1, -1);
				req.setAttribute("users", users);
				// Set default page and display size
				req.setAttribute("page", 0);
				req.setAttribute("displaySize", 10);
				req.getRequestDispatcher("/search_users.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to search Users!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String changePage = req.getParameter("changePage");
		String changeDisplaySize = req.getParameter("changeDisplaySize");
		int currentDisplaySize = Integer.parseInt(req.getParameter("displaySize"));
		
		if(changePage != null && !changePage.equalsIgnoreCase("")){
			int currentPage = Integer.parseInt(req.getParameter("page"));
			if(changePage.equalsIgnoreCase("prev")){
				req.setAttribute("page", currentPage - 1);
			}else if(changePage.equalsIgnoreCase("next")){
				req.setAttribute("page", currentPage + 1);
			}
		}else{
			req.setAttribute("page", 0);
		}
		
		if(changeDisplaySize != null && !changeDisplaySize.equalsIgnoreCase("")){
			req.setAttribute("displaySize", Integer.parseInt(changeDisplaySize));
		}else{
			req.setAttribute("displaySize", currentDisplaySize);
		}
		
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
		
		req.setAttribute("users", users);
		
		// Push parameters back so User doesn't have to retype them every time
		req.setAttribute("userID", idStr);
		req.setAttribute("employeeID", eIDStr);
		req.setAttribute("email", email);
		req.setAttribute("firstName", firstName);
		req.setAttribute("lastName", lastName);
		req.setAttribute("positionID", posIDStr);
		
		req.getRequestDispatcher("/search_users.jsp").forward(req, resp);
	}
}
