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

@SuppressWarnings("serial")
public class CreateSOPServlet extends HttpServlet{
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		System.out.println("Create SOP Servlet: doget");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.CREATE_SOP)){
				req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to create an SOP!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		boolean goodSOP = true;
		
		SOPController sc = new SOPController(); 
		
		String title = null; 
		String description = null;  
		int version = 0; 
		int priority = 0; 
		int authorID = 0; 
		
		title = req.getParameter("title");
		if(title == null || title.equalsIgnoreCase("")) {
			goodSOP = false; 
			req.setAttribute("titleError", "Please enter a valid title!");
		}
		
		description = req.getParameter("description");
		if(description == null || description.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("descriptionError", "Please enter a valid description");
		}
		String priorityStr = req.getParameter("priority");
		if(priorityStr == null || priorityStr.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("priorityError", "Please enter a valid priority");
		} else {
			priority = Integer.parseInt(priorityStr);
			if(priority <= 0 || priority > 10) {
				goodSOP = false;
				req.setAttribute("priorityError", "Priority must be 1-10!");
			}
		}
		String versionStr = req.getParameter("revision");
		if(versionStr == null || versionStr.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("versionError", "Please enter a valid version number");
		} else {
			version = Integer.parseInt(versionStr);
			if(version <= 0) {
				goodSOP = false; 
				req.setAttribute("versionError", "Version must be greater than 0!");
			}
		}
		
		HttpSession session = req.getSession();
		authorID = (int) session.getAttribute("user_id"); 
		if(authorID <= 0) {
			goodSOP = false; 
			req.setAttribute("authorIDError", "There was an error getting the authorID from the author and sessoin data.");
		}
		
		if(goodSOP){
			sc.insertSOP(title, description, priority, version, authorID, false);
			session.setAttribute("success", "Created SOP " + title + " v." + version + " with priority " + priority);
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}else{
			req.setAttribute("title", title);
			req.setAttribute("description", description);
			req.setAttribute("priority", priority);
			req.setAttribute("revision", version);
			req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
		}
	}
}
