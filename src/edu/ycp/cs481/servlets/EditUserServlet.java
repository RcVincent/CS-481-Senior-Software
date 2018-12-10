package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class EditUserServlet extends HttpServlet {
	private void loadUser(HttpServletRequest req) {
		int user_id = Integer.parseInt(req.getParameter("userID"));
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
	
	//comment 
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.EDIT_USERS)){
				loadUser(req);
				req.getRequestDispatcher("/edit_user.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to edit users!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int userID = Integer.parseInt(req.getParameter("userID"));
		
		UserController uc = new UserController();
		PositionController pc = new PositionController(); 
		String action = req.getParameter("editType");
		User you = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
		Position p = you.getPosition();
		//boolean goodEdits = true;
		
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
				uc.changePosition(you, positionID);
				req.setAttribute("successMessage", "User Position Changed!");
				
			}
		}
		
		else if(action.equalsIgnoreCase("assignSOP")) {
			String sopid = req.getParameter("sop_ID");
			if(sopid == null || sopid.equalsIgnoreCase("")) {
				System.out.println("SOP not being assigned, the sop id was empty");
			}
			else {
				int sopID = Integer.parseInt(sopid);
				if(sopID <= 0) {
					req.setAttribute("sopIDError", "Invalid SOP ID. Please try again.");
				}
				else {
					uc.assignSOP(userID, sopID);
					pc.insertPositionSOP(p.getID(), sopID);
					
					System.out.println("Added sop with ID "+sopID+"assigned to user with ID " + userID);
					req.setAttribute("successMessage", "SOP Assigned to this user!");
				}
			}
		}
			
		
		loadUser(req);
		req.getRequestDispatcher("/edit_user.jsp").forward(req, resp);
	}
}
