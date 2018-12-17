package edu.ycp.cs481.servlets;
import java.io.IOException;
import java.util.ArrayList;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.User;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.Position;


@SuppressWarnings("serial")
public class EmployeeProfileServlet extends HttpServlet{
	
	private void loadUser(HttpServletRequest req) {
		HttpSession session = req.getSession();
		int user_id = (int) session.getAttribute("user_id");
		UserController uc = new UserController(); 
		User u = uc.searchForUsers(user_id, -1, false, "", false, "", false, "", -1, -1).get(0);
		req.setAttribute("user_id", u.getID());
		req.setAttribute("email", u.getEmail());
		req.setAttribute("firstname", u.getFirstName());
		req.setAttribute("lastname", u.getLastName());
		req.setAttribute("archived", u.isArchived());
		req.setAttribute("locked_out", u.isLockedOut());
		req.setAttribute("position_title", u.getPosition().getTitle());
		
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			SOPController sc = new SOPController();
			loadUser(req);
			int id = (int) session.getAttribute("user_id");
			//here it is. My maltese falcon. My Sistine chapel" - Justin Hammer "Iron Man 2"
			ArrayList<SOP> sops = sc.getSOPs(-1, false, null, false, null, -1, -1, -1, id);
			req.setAttribute("sops", sops); 
			req.getRequestDispatcher("/employee_profile.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession(); 
		UserController uc = new UserController(); 
		PositionController pc = new PositionController(); 
		SOPController sc = new SOPController();
		int userID = (int) session.getAttribute("user_id");
		
		User you = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
		Position P = pc.getPositionByUser(userID);
		
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
		//req.setAttribute("firstName", you.getFirstName());
		//req.setAttribute("lastName", you.getLastName());
		//req.setAttribute("PositionTitle", P.getTitle());
		
		//set manager info to be displayed 
		req.setAttribute("ManagerFirstName", manager.getFirstName());
		req.setAttribute("ManagerLastName", manager.getLastName());
		req.setAttribute("ManagerPositionTitle", managerP.getTitle());
		
		
		loadUser(req);
		req.getRequestDispatcher("/employee_profile.jsp").forward(req, resp);
	}

}
