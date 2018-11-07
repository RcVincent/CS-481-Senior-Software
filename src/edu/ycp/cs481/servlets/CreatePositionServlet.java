package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.model.Position;

@SuppressWarnings("serial")
public class CreatePositionServlet extends HttpServlet {
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Create Position: doget");
		
		HttpSession session = req.getSession();
		if (session.getAttribute("user_id") == null) {
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {
			req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
		}	

		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Getting Position info from the webform");
		
		boolean goodPosition = true; 
		int priority = 0;
		PositionController pc = new PositionController(); 
		
		String positionTitle = req.getParameter("title");
		if(positionTitle == null || positionTitle.equalsIgnoreCase("")) {
			goodPosition = false; 
			req.setAttribute("titleError", "Please enter a valid position title.");
		}
		String positionDescription = req.getParameter("description");
		if(positionDescription == null || positionDescription.equalsIgnoreCase("")) {
			goodPosition = false;
			req.setAttribute("descError", "Please enter a valid position description.");
		}
		
		String Priority = req.getParameter("priority");
		if(Priority == null || Priority.equalsIgnoreCase("")) {
			goodPosition = false; 
			req.setAttribute("priError", "Please enter a valid priority.");
		} else {
			priority = Integer.parseInt(Priority);
			if(priority <= 0 || priority > 10) {
				goodPosition = false; 
				req.setAttribute("prioError", "There was either a conversion error or an invalid value.");
			}
		}
		
		
		//get the created position and error check it. 
		if(goodPosition) {
			int posID = pc.insertPosition(positionTitle, positionDescription, priority);
			
			if(posID <= 0) {
				System.out.println("There was an error inserting the position into the DB");
				req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
			} else {
				System.out.println("Position successfully inserted into db with ID " + posID);
				
			}
		} else {
			req.setAttribute("title", positionTitle);
			req.setAttribute("description", positionDescription);
			req.setAttribute("priority", Priority);
			req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
		}
		
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		
	}
	
}
