package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.model.SOP;

@SuppressWarnings("serial")
public class EditSOPServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {
			req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
		}
		
		//do a permissions check here and if they are a user return them to the home page
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
		SOPController sc = new SOPController(); 
		SOP s = null;
		boolean editError = false; 
		String action = req.getParameter("doStuff");
		int searchID = Integer.parseInt(req.getParameter("sop_id"));

		//make sure the search value is valid even before we execute the search
		if(searchID <=0 ) {
			System.out.println("Invalid search id");
			req.setAttribute("searchError", "Invalid search ID!");
		} else {
			//do the search
			s = sc.searchForSOPs(searchID, false, "", false, "", -1, -1, -1).get(0);
		}

		//do a null change to make sure that we pulled the object we need to edit
		if(s == null) {
			System.out.println("There was a search error, returning to the homepage");
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}
		
		//time to get cracking 
		
		if(action.equalsIgnoreCase("changeTitle")) {
			//get the new title parameters 
			String newSOPTitle = req.getParameter("newTitle");
			String newSOPTitleConfirm = req.getParameter("newTitleConfirmation");
			
			//if both fields are empty do not do the edit
			if((newSOPTitle == null || newSOPTitle.equalsIgnoreCase("")) && (newSOPTitleConfirm == null || newSOPTitleConfirm.equalsIgnoreCase(""))) {
				//do not do the edit
				System.out.println("SOP title not edited");
				
				//make sure the two parameters are identical 
			} else if(!newSOPTitle.equalsIgnoreCase(newSOPTitleConfirm)) {
				req.setAttribute("titleError", "The titles must match!");
				editError = true; 
				
			} else {
				//update the SOP title
				sc.changeSOPTitle(s, newSOPTitle);
			}
			
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
		}

		else if(action.equalsIgnoreCase("changeDescription")) {
			//get description parameters
			String newSOPDesc = req.getParameter("newDescription");
			String newSOPDescConfirm = req.getParameter("newDescriptionConfirmation");
			//if both fields are empty do not make the change 
			if((newSOPDesc == null || newSOPDesc.equalsIgnoreCase("")) && (newSOPDescConfirm == null || newSOPDescConfirm.equalsIgnoreCase(""))) {
				System.out.println("SOP description field not changed"); 
			}
			else if(!newSOPDesc.equalsIgnoreCase(newSOPDescConfirm)) {
				req.setAttribute("descriptionError", "The description fields must match!");
				editError = true; 
			}
			else {
				sc.changeSOPDescription(s, newSOPDesc);
				System.out.println("SOP description Successfully changed!");
			}
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}

		else if(action.equalsIgnoreCase("changePriority")) {
			//get the parameters
			String newP = req.getParameter("newPriority");
			String newPC = req.getParameter("newPriorityConfirmation");
			//if both fields are empty do not change the priority
			if((newP == null || newP.equalsIgnoreCase("")) && (newPC == null || newPC.equalsIgnoreCase(""))) {
			}
			//make sure the parameters match
			else if(!newP.equalsIgnoreCase(newPC)) {
				req.setAttribute("priorityError", "The priority fields must match!");
				editError = true;
			}
			else {
				//parse the value and check its validity 
				int newPriority = Integer.parseInt(newP);
				if(newPriority <= 0 || newPriority > 10 || newPriority == s.getPriority()) {
					req.setAttribute("priorityValueError", "Invalid priority value!");
					editError = true;
				}
				else {
					//change the priority
					sc.changeSOPPriority(s, newPriority);
					System.out.println("SOP priority successfully changed!");
				}
			}
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}

		else if(action.equalsIgnoreCase("changeVersion")) {
			String newV = req.getParameter("newVersion");
			//CHARLIE IN THE TREES
			String newVC = req.getParameter("newVersionConfirmation");
			//if both parameters are blank, do not change the SOP version
			if((newV == null || newV.equalsIgnoreCase("")) && (newVC == null || newVC.equalsIgnoreCase(""))) {
				System.out.println("SOP version not updated.");
			}
			//ensure the two parameters are identical
			else if(!newV.equalsIgnoreCase(newVC)) {
				req.setAttribute("versionError", "The version values must match!");
				editError = true;
			}
			else {
				//parse the integer value 
				int newVersion = Integer.parseInt(newV);
				//see if the version has an invalid value or is still the same value
				if(newVersion <= 0 || newVersion == s.getVersion()) {
					req.setAttribute("versionValueError", "The version has an invalid value!");
					editError = true;
				}
				else {
					//change the value 
					sc.changeSOPVersion(s, newVersion);
					System.out.println("SOP version successfully updated!");
				}
			}
			if(!editError) {
				req.setAttribute("SuccessMessage", "Fields Successfully Changed!");
			}
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}
		
		//add an archive option
		else if(action.equalsIgnoreCase("archiveSOP")) {
			sc.archiveSOP(s.getID());
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
			
		}
	}
}
