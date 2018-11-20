package edu.ycp.cs481.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.EnumPermission;
import edu.ycp.cs481.model.SOP;

@SuppressWarnings("serial")
public class EditSOPServlet extends HttpServlet{
	
	private void loadSOP(HttpServletRequest req){
		// TODO: Error checking
		int sopID = Integer.parseInt(req.getParameter("sopID"));
		SOPController sc = new SOPController();
		SOP sop = sc.searchForSOPs(sopID, false, null, false, null, -1, -1, -1).get(0);
		req.setAttribute("sopID", sop.getID());
		req.setAttribute("title", sop.getTitle());
		req.setAttribute("priority", sop.getPriority());
		req.setAttribute("version", sop.getVersion());
		req.setAttribute("authorID", sop.getAuthorID());
		req.setAttribute("description", sop.getDescription());
		req.setAttribute("archived", sop.isArchived());
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
				loadSOP(req);
				req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to edit SOPs!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int id = Integer.parseInt(req.getParameter("sopID"));
		
		SOPController sc = new SOPController();
		String action = req.getParameter("doStuff");
		boolean goodUpdate = true;
		
		if(action.equalsIgnoreCase("archiveSOP")){
			sc.archiveSOP(id);
			req.setAttribute("successMessage", "Archived SOP!");
		}else if(action.equalsIgnoreCase("unarchiveSOP")){
			sc.unarchiveSOP(id);
			req.setAttribute("successMessage", "Unarchived SOP!");
		}else if(action.equalsIgnoreCase("changeTitle")){
			String newTitle = req.getParameter("newTitle");
			String newTitleConfirm = req.getParameter("newTitleConfirm");
			
			if(newTitle == null || newTitle.equalsIgnoreCase("")){
				req.setAttribute("titleError", "Title can't be null!");
				goodUpdate = false;
			}
			if(!newTitle.equalsIgnoreCase(newTitleConfirm)){
				req.setAttribute("titleConfirmError", "The titles don't match!");
				goodUpdate = false;
			}
			if(goodUpdate){
				sc.changeTitle(id, newTitle);
				req.setAttribute("successMessage", "Updated Title to " + newTitle + "!");
			}
		}else if(action.equalsIgnoreCase("changePriority")){
			String newPriorityStr = req.getParameter("newPriority");
			String newPriorityConfirmStr = req.getParameter("newPriorityConfirm");
			
			if(newPriorityStr == null || newPriorityStr.equalsIgnoreCase("")){
				req.setAttribute("priorityError", "Please enter a number!");
				goodUpdate = false;
			}
			if(!newPriorityStr.equalsIgnoreCase(newPriorityConfirmStr)){
				req.setAttribute("priorityConfirmError", "The priorities don't match!");
				goodUpdate = false;
			}
			if(goodUpdate){
				int newPriority = Integer.parseInt(newPriorityStr);
				if(newPriority <= 0 || newPriority > 10){
					req.setAttribute("priorityError", "Priority must be between 1 and 10!");
				}else{
					sc.changePriority(id, newPriority);
					req.setAttribute("successMessage", "Updated priority to " + newPriority + "!");
				}
			}
		}else if(action.equalsIgnoreCase("changeVersion")){
			String newVersionStr = req.getParameter("newVersion");
			String newVersionConfirmStr = req.getParameter("newVersionConfirm");
			
			if(newVersionStr == null || newVersionStr.equalsIgnoreCase("")){
				req.setAttribute("versionError", "Please enter a number!");
				goodUpdate = false;
			}
			if(!newVersionStr.equalsIgnoreCase(newVersionConfirmStr)){
				req.setAttribute("versionConfirmError", "The versions don't match!");
				goodUpdate = false;
			}
			if(goodUpdate){
				int newVersion = Integer.parseInt(req.getParameter("newVersion"));
				if(newVersion <= 0){
					req.setAttribute("versionError", "Version must be greater than 0!");
				}else{
					sc.changeVersion(id, newVersion);
					req.setAttribute("successMessage", "Updated version to " + newVersion + "!");
				}
			}
		}else if(action.equalsIgnoreCase("changeDescription")){
			String newDescription = req.getParameter("newDescription");
			sc.changeDescription(id, newDescription);
			req.setAttribute("successMessage", "Updated description!");
		}
		loadSOP(req);
		req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
	}
}
