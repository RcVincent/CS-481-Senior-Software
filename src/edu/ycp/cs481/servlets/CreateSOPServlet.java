package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.model.User;

@SuppressWarnings("serial")
public class CreateSOPServlet extends HttpServlet{
		
	private SOPController sc = null; 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Create SOP Servlet: doget");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			//user isnt logged in or the session expired 
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {
			//TODO: Check for admin/privileges check
			req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
		}
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		//String errorMessage = null; 
		//String successMessage = null; 
		
		boolean goodSOP = true;
		
		SOPController sc = new SOPController(); 
		
		String sopTitle = null; 
		String sopDescription = null;  
		int version = 0; 
		int priority = 0; 
		int authorID = 0; 
		
		 
		
		sopTitle = req.getParameter("title");
		if(sopTitle == null || sopTitle.equalsIgnoreCase("")) {
			goodSOP = false; 
			req.setAttribute("titleError", "Please enter a valid title!");
		}
		
		sopDescription = req.getParameter("description");
		if(sopDescription == null || sopDescription.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("descriptionError", "Please enter a valid description");
		}
		String Priority = req.getParameter("priority");
		if(Priority == null || Priority.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("priorityError", "Please enter a valid priority");
		} else {
			priority = Integer.parseInt(Priority);
			if(priority <= 0 || priority > 10) {
				goodSOP = false;
				req.setAttribute("conversionErr", "There was an error parsing from string to integer");
			}
		}
		String Version = req.getParameter("revision");
		if(Version == null || Version.equalsIgnoreCase("")) {
			goodSOP = false;
			req.setAttribute("versionError", "Please enter a valid version number");
		} else {
			version = Integer.parseInt(Version);
			if(version <= 0) {
				goodSOP = false; 
				req.setAttribute("conversionErr", "There was an error parsing from string to integer");
			}
		}
		
		authorID = (int) session.getAttribute("user_id"); 
		if(authorID <= 0) {
			goodSOP = false; 
			req.setAttribute("authorIDError", "There was an error getting the authorID from the author and sessoin data.");
		}
		
		if(goodSOP) {
			//insert sop 
			int sopID = sc.insertSOP(sopTitle, sopDescription, priority, version, authorID, false);
			if(sopID <= 0) {
				System.out.println("There was an error inserting the sop into the DB");
			}
			else {
				System.out.println("SOP inserted into the DB with ID " + sopID);
				resp.sendRedirect(req.getContextPath() + "/user_home");
			}
		} else {
			req.setAttribute("title", sopTitle);
			req.setAttribute("description", sopDescription);
			req.setAttribute("priority", Priority);
			req.setAttribute("revision", Version);
			
			req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
			
		}
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
	}
	
}
