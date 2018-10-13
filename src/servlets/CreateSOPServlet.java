package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.SOP;
import controller.SOPController;
import model.User;

public class CreateSOPServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private SOPController sc = null; 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Create SOP Servlet: doget");
		
		HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
			//user isnt logged in or the session expired 
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		if(session.getAttribute("admin_flag").equals("User") || session.getAttribute("admin_flag").equals("User")) {
			resp.sendRedirect(req.getContextPath() + "/Mainpage");
		}
		
		
		
		req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		String errorMessage = null; 
		String successMessage = null; 
		
		String sopName = null; 
		String sopDescription = null; 
		int sopid = 0; 
		int version = 0; 
		int priority = 0; 
		int authorID = 0; 
		
		 
		
		sopName = req.getParameter("title");
		sopDescription = req.getParameter("description");
		
		String Priority = req.getParameter("priority");
		priority = Integer.parseInt(Priority);
		
		String Version = req.getParameter("revision");
		version = Integer.parseInt(Version);

		authorID = (int) session.getAttribute("user_id"); 
		
		sc = new SOPController(); 
		
		//add the sop to the db
		
		req.setAttribute("title", sopName);
		req.setAttribute("description", sopDescription);
		req.setAttribute("priority", priority);
		req.setAttribute("version", version);
		req.setAttribute("author_id", authorID);
		//set the version, id, author id, 
		
		//test the sop we just made
		List<SOP> test = sc.findSOPbyName(sopName); 
		
		if(test.isEmpty()) {
			errorMessage = "There was no SOP with name" + sopName + "added to the database";
			return; 
		}else {
			SOP testSOP = test.get(0); 
			if(!sc.validSOP(testSOP)) {
				errorMessage = "There is an invalid field in the sop, please try again"; 
			}
			else {
				successMessage = "Succcessfully created SOP!";
			}
		}
		
		req.setAttribute("errorMessage", errorMessage);
		
		
		req.setAttribute("successMessage" , successMessage);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/create_sop.jsp").forward(req, resp);
	}
	
}
