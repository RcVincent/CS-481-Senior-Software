package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.UserController;

@SuppressWarnings("serial")
public class EditPositionServlet extends HttpServlet{
	
	public void loadPosition(HttpServletRequest req) {
		int positionID = Integer.parseInt(req.getParameter("posID"));
		PositionController pc = new PositionController(); 
		Position p = pc.searchForPositions(positionID, false, null, false, null, -1).get(0);
		req.setAttribute("positionID", p.getID());
		req.setAttribute("title", p.getTitle());
		req.setAttribute("description", p.getDescription());
		req.setAttribute("priority", p.getPriority());
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.EDIT_POSITIONS)){
				loadPosition(req); 
				req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to Edit Positions!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int id = Integer.parseInt(req.getParameter("posID"));
		PositionController pc = new PositionController();
		String action = req.getParameter("doStuff");
		
		Position pos = pc.searchForPositions(id, false, "", false, "", -1).get(0);
		
		
		if(action.equalsIgnoreCase("changeTitle")) {
			String newPositionTitle = req.getParameter("newTitle");
			String newPositionTitleConfirm = req.getParameter("newTitleConfirm");
			
			//test change title
			if((newPositionTitle == null ||  newPositionTitle.equalsIgnoreCase("")) && (newPositionTitleConfirm == null || newPositionTitleConfirm.equalsIgnoreCase(""))) {
				//do not do the edit 
				System.out.println("Position title not changed");
			}
			//make sure the two fields are identical
			else if(!newPositionTitle.equalsIgnoreCase(newPositionTitleConfirm)) {
				req.setAttribute("changeTitleError", "Titles do not match!");
			} 
			else {
				//change the title field
				pc.changePositionTitle(id, newPositionTitle);
				System.out.println("Position title successfully changed");
				req.setAttribute("successMessage", "Title successfully Changed!");
			}
		}
		//change the description
		else if(action.equalsIgnoreCase("changeDescription")) {
			String newPositionDescription = req.getParameter("newDescription");
			
			//check if both fields are empty and if they are do not edit
			if(newPositionDescription == null || newPositionDescription.equalsIgnoreCase("")) {
				//do not do the edit
				System.out.println("Description field not changed.");

			} 
			else {
				//change the description field
				pc.changePositionDescription(id, newPositionDescription);
				System.out.println("Position description successfully changed");
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
		} 
		
		//change the priority
		else if(action.equalsIgnoreCase("changePriority")) {
			String newP = req.getParameter("newPriority");
			String newPConfirm = req.getParameter("newPriorityConfirmation");
			
			if((newP == null || newP.equalsIgnoreCase("")) && (newPConfirm == null || newPConfirm.equalsIgnoreCase("")) ) {
				//do not do the edit if both fields are empty
				System.out.println("Priority not changed.");
			}
			//make sure the two fields are identical
			else if(!newP.equalsIgnoreCase(newPConfirm)) {
				req.setAttribute("priorityError", "Priorities do not match!");
			} else {
				//parse the int
				int newPriority = Integer.parseInt(newP);
				//make sure the value is valid
				if(newPriority <=0 || newPriority > 10) {
					req.setAttribute("priorityValueError", "Invalid priority value!");
				}
				else {
					//change the position priority 
					pc.changePriority(id, newPriority);
					System.out.println("Position priority successfully changed");
					req.setAttribute("successMessage", "Position Priority Updated!");
				}
			}
	
		}
		
		else if(action.equalsIgnoreCase("assignSOP")) {
			String sop_id = req.getParameter("sop_id");
			
			if(sop_id.equalsIgnoreCase("") || sop_id == null) {
				System.out.println("Invalid ID, do not add the SOP to this position");
			} 
			else {
				int sopID = Integer.parseInt(sop_id);
				if(sopID <= 0) {
					System.out.println("Invalid SOP id, must be larger than 0");
					req.setAttribute("sopIDError", "Invalid SOP id, please enter a valid ID.");
				}
				else {
					pc.insertPositionSOP(id, sopID);
					req.setAttribute("successMessage", "SOP Assigned!");
					System.out.println("SOP with id " + sopID + "assigned to the position with position id:" + id);
				}
			}
		}
		
		/*else if(action.equalsIgnoreCase("changePermissions")) {
			String reqID = req.getParameter("permissionID"); 
			if(reqID.equalsIgnoreCase("") || reqID == null) {
				System.out.println("No permission id specified, do not add one");
			}
			else { 
				int permID =Integer.parseInt(reqID);
				if(permID <= 0) {
					System.out.println("Invalid permission ID");
					req.setAttribute("permIDError",	"Please specify a valid permissions ID!");
				} else {
					EnumPermission perm;
					perm.getID();
					pc.addPositionPermission(pos, perm);
					req.setAttribute("successMessage", "Permission added to position!");
				}
			}
		}*/
		
		
		else if(action.equalsIgnoreCase("deletePosition")) {
			pc.removePosition(id);
			//get session data to return home
			HttpSession session = req.getSession();
			session.setAttribute("success", "Deleted Position with id " + id);
			//return after deleting the position
			resp.sendRedirect(req.getContextPath() + "/user_home");
			return;
		}

		loadPosition(req);
		req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		
	}
}