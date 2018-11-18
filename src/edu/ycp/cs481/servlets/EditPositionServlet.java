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
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.EDIT_POSITIONS)){
				// TODO: Error checking
				int posID = Integer.parseInt(req.getParameter("posID"));
				PositionController pc = new PositionController();
				Position pos = pc.searchForPositions(posID, false, null, false, null, -1).get(0);
				req.setAttribute("ID", pos.getID());
				req.setAttribute("oldTitle", pos.getTitle());
				req.setAttribute("oldPriority", pos.getPriority());
				req.setAttribute("oldDescription", pos.getDescription());
				// TODO: Current Requirements
				req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to Edit Positions!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		boolean editError = false;
		PositionController pc = new PositionController();

		Position p = null;

		String action = req.getParameter("doStuff");
		

		//do a null change to make sure that we pulled the object we need to edit
		if(p == null) {
			System.out.println("There was a search error, returning to the homepage");
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}
		
		if(action.equalsIgnoreCase("changeTitle")) {
			String newPositionTitle = req.getParameter("newTitle");
			String newPositionTitleConfirm = req.getParameter("newTitleConfirm");
			String title = p.getTitle();
			//test change title
			if((newPositionTitle == null ||  newPositionTitle.equalsIgnoreCase("")) && (newPositionTitleConfirm == null || newPositionTitleConfirm.equalsIgnoreCase(""))) {
				//do not do the edit 
				System.out.println("Position title not changed");
			}
			//make sure the two fields are identical
			else if(!newPositionTitle.equalsIgnoreCase(newPositionTitleConfirm) || newPositionTitle.equalsIgnoreCase(title)) {
				req.setAttribute("changeTitleError", "Titles do not match!");
				editError = true; 
			} else {
				//change the title field
				pc.changePositionTitle(p, newPositionTitle);
				System.out.println("Position title successfully changed");
			}
			
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}
		//change the description
		else if(action.equalsIgnoreCase("changeDescription")) {
			String newPositionDescription = req.getParameter("newDescription");
			String newPositionDescriptionConfirm = req.getParameter("newDescriptionConfirmation");
			String description = p.getDescription();
			
			//check if both fields are empty and if they are do not edit
			if((newPositionDescription == null || newPositionDescription.equalsIgnoreCase("")) && newPositionDescriptionConfirm == null || newPositionDescriptionConfirm.equalsIgnoreCase("")) {
				//do not do the edit
				System.out.println("Description field not changed.");

			} 
			//make sure the two fields are identical
			else if(!newPositionDescription.equalsIgnoreCase(newPositionDescriptionConfirm) || newPositionDescription.equalsIgnoreCase(description)) {
				req.setAttribute("changeDescriptionError", "Descriptions do not match!");
				editError = true; 
			}
			else {
				//change the description field
				pc.changePositionDescription(p, newPositionDescription);
				System.out.println("Position description successfully changed");
			}
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		} 
		
		//change the priority
		else if(action.equalsIgnoreCase("changePriority")) {
			String newP = req.getParameter("newPriority");
			String newPConfirm = req.getParameter("newPriorityConfirmation");
			int priority = p.getPriority();
			
			if((newP == null || newP.equalsIgnoreCase("")) && (newPConfirm == null || newPConfirm.equalsIgnoreCase("")) ) {
				//do not do the edit if both fields are empty
				System.out.println("Priority not changed.");
			}
			//make sure the two fields are identical
			else if(!newP.equalsIgnoreCase(newPConfirm)) {
				req.setAttribute("priorityError", "Priorities do not match!");
				editError = true;
			} else {
				//parse the int
				int newPriority = Integer.parseInt(newP);
				//make sure the value is valid
				if(newPriority <=0 || newPriority > 10 || newPriority == priority) {
					req.setAttribute("priorityValueError", "Invalid priority value!");
					editError = true;
				}
				else {
					//change the position priority 
					pc.changePositionPriority(p, newPriority);
					System.out.println("Position priority successfully changed");
				}
			}
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}
		
		/*
		else if(action.equalsIgnoreCase("deletePosition")) {
			pc.removePosition(p.getID());
			req.setAttribute("RemoveMessage", "Position removed from the DB.");
			System.out.println("Position deleted.");
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}*/


	}
}

