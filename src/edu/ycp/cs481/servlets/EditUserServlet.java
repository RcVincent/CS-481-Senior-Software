package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class EditUserServlet extends HttpServlet {
	private void loadUser(HttpServletRequest req) {
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		UserController uc = new UserController(); 
		User u = uc.searchForUsers(user_id, -1, false, "", false, "", false, "", 0, -1).get(0);
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
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.EDIT_SOPS)){
				loadUser(req);
				req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to edit SOPs!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int userID = Integer.parseInt(req.getParameter("user_id"));
		
		UserController uc = new UserController();
		String action = req.getParameter("editType");
		boolean goodEdits = true;
		
		if(action.equalsIgnoreCase("archiveUser")) {
			System.out.println("Archiving user with ID" + userID);
			uc.archiveUser(userID);
			req.setAttribute("successMessage", "User successfully archived!");
		} 
		else if(action.equalsIgnoreCase("unarchiveUser")) {
			System.out.println("Unarchiving user with ID" + userID);
			uc.unarchiveUser(userID);
			req.setAttribute("successMessage", "User successfully unarchived");
		}
		
		else if(action.equalsIgnoreCase("lockOutUser")) {
			System.out.println("Locking out user with ID" + userID);
			uc.Lockout(userID);
			req.setAttribute("successMessage", "User locked out");
		}
		else if(action.equalsIgnoreCase("overTurnLockout")) {
			System.out.println("Overturning lockout user with ID" + userID);
			if(uc.isLockedOut(userID)) {
				uc.overturnLockout(userID);
				req.setAttribute("successMessage", "User lockout overturned");
			}
		}
		else if(action.equalsIgnoreCase("changePosition")) {
			int positionID = Integer.parseInt(req.getParameter("newPositionID"));
			if(positionID <= 0) {
				req.setAttribute("positionIDError", "Invalid position ID, please try again.");
			} else {
				//uc.changePosition(user, positionID);
			}
		}
		
		else if(action.equalsIgnoreCase("AssignSOP")) {
			int sopID = Integer.parseInt(req.getParameter("sop_id"));
			if(sopID <= 0) {
				req.setAttribute("sopIDError", "Invalid SOP ID. Please try again.");
			}
			else {
				uc.assignSOP(userID, sopID);
				System.out.println("Added sop with ID "+sopID+"assigned to user with ID " + userID);
			}
		}
		
		loadUser(req);
		req.getRequestDispatcher("/edit_user.jsp").forward(req, resp);
	}
}
