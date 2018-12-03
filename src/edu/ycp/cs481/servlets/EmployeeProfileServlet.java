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
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.Position;


@SuppressWarnings("serial")
public class EmployeeProfileServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			req.getRequestDispatcher("/employee_profile.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession(); 
		UserController uc = new UserController(); 
		int userID = (int) session.getAttribute("user_id");
		
		User you = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
		Position P = you.getPosition();
		
		//change the display size of required SOPs
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
		
		User manager = uc.getManagersOfUser(userID).get(0);
		Position managerP = manager.getPosition();
		
		//set information for display in the jsp
		req.setAttribute("firstName", you.getFirstName());
		req.setAttribute("lastName", you.getLastName());
		req.setAttribute("PositionTitle", P.getTitle());
		
		//set manager info to be displayed 
		req.setAttribute("ManagerFirstName", manager.getFirstName());
		req.setAttribute("ManagerLastName", manager.getLastName());
		req.setAttribute("ManagerPositionTitle", managerP.getTitle());
		
		ArrayList<SOP> sops = P.getIncompleteSOPs(P);
		req.setAttribute("sops", sops); 
		
		req.getRequestDispatcher("/employee_profile.jsp").forward(req, resp);
	}

}
