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
public class SearchSOPServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		
		req.getRequestDispatcher("/searchSOPs.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		SOPController sc = new SOPController(); 
		
		String id = req.getParameter("sopID"); 
		String title = req.getParameter("title");
		String desc = req.getParameter("description"); 
		String prio = req.getParameter("priority");
		String version = req.getParameter("version");
		String authorid = req.getParameter("authorID"); 
		
		int sopID, priority, revision, authorID; 
		
		if(id == null || id == "" || id == " ") {
			sopID = -1; 
		}else {
			sopID = Integer.parseInt(id);
		}
		
		if(prio == null || prio == "" || prio == " ") {
			priority = 0; 
		} else {
			priority = Integer.parseInt(prio);
		}
		
		if(version == null || version == "" || version == " ") {
			revision = 0; 
		} else {
			revision = Integer.parseInt(version);
		}
		
		if(authorid == null || authorid == "" || authorid == " ") {
			authorID = 0;
		} else {
			authorID = Integer.parseInt(authorid);
		}
		ArrayList<SOP> result = sc.searchForSOPs(sopID, title, desc, priority, revision, authorID);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/searchSOPs.jsp").forward(req, resp);
		
	}
}
