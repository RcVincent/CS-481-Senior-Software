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

@SuppressWarnings("serial")
public class CreatePositionServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		System.out.println("Create Position: doget");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			UserController uc = new UserController();
			int userID = (int) session.getAttribute("user_id");
			if(uc.userHasPermission(userID, EnumPermission.ALL) || uc.userHasPermission(userID, EnumPermission.CREATE_POSITION)){
				req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
			}else{
				session.setAttribute("error", "You don't have permission to create a position!");
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		}	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		System.out.println("Getting Position info from the webform");
		
		boolean goodPosition = true;
		int priority = 0;
		PositionController pc = new PositionController();
		
		String title = req.getParameter("title");
		if(title == null || title.equalsIgnoreCase("")){
			goodPosition = false;
			req.setAttribute("titleError", "Please enter a valid position title.");
		}
		
		String description = req.getParameter("description");
		if(description == null || description.equalsIgnoreCase("")) {
			goodPosition = false;
			req.setAttribute("descError", "Please enter a valid position description.");
		}
		
		String priorityStr = req.getParameter("priority");
		if(priorityStr == null || priorityStr.equalsIgnoreCase("")) {
			goodPosition = false; 
			req.setAttribute("priorityError", "Please enter a valid priority.");
		} else {
			priority = Integer.parseInt(priorityStr);
			if(priority <= 0 || priority > 10) {
				goodPosition = false; 
				req.setAttribute("priorityError", "Priority must be between 1 and 10!");
			}
		}
		
		if(goodPosition){
			pc.insertPosition(title, description, priority);
			HttpSession session = req.getSession();
			session.setAttribute("success", "Created Position " + title + " with priority " + priority);
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}else{
			req.setAttribute("title", title);
			req.setAttribute("description", description);
			req.setAttribute("priority", priority);
			req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
		}
	}
}
