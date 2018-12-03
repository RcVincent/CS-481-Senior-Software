package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class ManagerZoneServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.EDIT_USERS) || 
					uc.userHasPermission(userID, EnumPermission.HAVE_SUBORDINATES)){
				//loadUser(req);
				req.getRequestDispatcher("/manager_zone.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to edit subordinates!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int userID = Integer.parseInt(req.getParameter("user_ID"));
		UserController uc = new UserController(); 
		SOPController sc = new SOPController(); 
		PositionController pc = new PositionController(); 
		HttpSession session = req.getSession();
		int managerID = (int) session.getAttribute("user_id");
		
		String action = req.getParameter("managerEdit");
		
		if(action.equalsIgnoreCase("overTurnLockout")) {
			System.out.println("Overturning lockout user with ID" + userID);
			if(uc.isLockedOut(userID)) {
				uc.overturnLockout(userID);
				req.setAttribute("successMessage", "User lockout overturned");
			}
		}
		
		else if(action.equalsIgnoreCase("AssignSOP")) {
			if(!uc.managerHasSubordinate(managerID, userID)) {
				System.out.println("This manager cannot assign this user an SOP.");
			} else {	
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
						User u = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
						Position p = u.getPosition();
						pc.insertPositionSOP(p.getID(), sopID);
						System.out.println("Added sop with ID "+ sopID +" assigned to user with ID " + userID);
					}
				}
			}
		}
		
		else if(action.equalsIgnoreCase("addSubordinate")) {
			if(uc.managerHasSubordinate(managerID, userID)) {
				System.out.println("This manager already has this user as a subordinate");
				req.setAttribute("addSubordinateError", "User is already reporting to you.");
			} else {
				uc.addSubordinate(managerID, userID);
				System.out.println("Subordinate added to manager");
			}
		}
		
		else if(action.equalsIgnoreCase("removeSubordinate")) {
			if(!uc.managerHasSubordinate(managerID, userID)) {
				System.out.println("This manager does not have that subordinate that can be removed");
				req.setAttribute("removeSubordinateError", "User is already not reporting to you.");
			} else {
				uc.removeSubordinate(managerID, userID);
				System.out.println("Subordinate removed from manager");
			}
		}
		
		req.getRequestDispatcher("/manager_zone.jsp").forward(req, resp);
	}
}
