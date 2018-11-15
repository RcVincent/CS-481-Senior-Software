package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.control.PositionController;

@SuppressWarnings("serial")
public class EditPositionServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			
		} else { 
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
			}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		boolean editError = false;
		PositionController pc = new PositionController(); 
		
		int searchID = Integer.parseInt(req.getParameter("position_id"));
		
		Position p = pc.searchForPositions(searchID, false, "", false, "", -1).get(0);
		
		String newPositionTitle = req.getParameter("newTitle");
		String newPositionTitleConfirm = req.getParameter("newTitleConfirm");
		
		//test change title
		if((newPositionTitle == null ||  newPositionTitle.equalsIgnoreCase("")) && (newPositionTitleConfirm == null || newPositionTitleConfirm.equalsIgnoreCase(""))) {
			//do not do the edit 
			System.out.println("Position title not changed");
		}
		else if(!newPositionTitle.equalsIgnoreCase(newPositionTitleConfirm)) {
			req.setAttribute("changeTitleError", "Titles do not match!");
			editError = true; 
		} else {
			pc.changePositionTitle(p, newPositionTitle);
			System.out.println("Position title successfully changed");
		}
		
		String newPositionDescription = req.getParameter("newDescription");
		String newPositionDescriptionConfirm = req.getParameter("newDescriptionConfirmation");
		if((newPositionDescription == null || newPositionDescription.equalsIgnoreCase("")) && newPositionDescriptionConfirm == null || newPositionDescriptionConfirm.equalsIgnoreCase("")) {
			//do not do the edit
			System.out.println("Description field not changed.");
			
		} 
		else if(!newPositionDescription.equalsIgnoreCase(newPositionDescriptionConfirm)) {
			req.setAttribute("changeDescriptionError", "Descriptions do not match!");
			editError = true; 
		}
		else {
			pc.changePositionDescription(p, newPositionDescription);
			System.out.println("Position description successfully changed");
		}
		
		String newP = req.getParameter("newPriority");
		String newPConfirm = req.getParameter("newPriorityConfirmation");
		if((newP == null || newP.equalsIgnoreCase("")) && (newPConfirm == null || newPConfirm.equalsIgnoreCase("")) ) {
			System.out.println("Priority not changed.");
		}
		else if(!newP.equalsIgnoreCase(newPConfirm)) {
			req.setAttribute("priorityError", "Priorities do not match!");
			editError = true;
		} else {
			int newPriority = Integer.parseInt(newP);
			
			if(newPriority <=0 || newPriority > 10) {
				req.setAttribute("priorityValueError", "Invalid priority value!");
				editError = true;
			}
			else {
				pc.changePositionPriority(p, newPriority);
				System.out.println("Position priority successfully changed");
			}
		}
		
		if(!editError) {
			req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
		}
		
		req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
	}
}
