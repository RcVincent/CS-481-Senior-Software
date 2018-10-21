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

public class SearchSOPServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
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
		
		String id = req.getParameter(); 
		String title = req.getParameter();
		String desc = req.getParameter(); 
		String prio = req.getParameter();
		String version = req.getParameter();
		String authorid = req.getParameter(); 
		
		int sopID, priority, revision, authorID; 
		
		ArrayList<SOP> result = sc.searchForSOP(sopID, title, desc, priority, revision, authorID);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/searchSOPs.jsp").forward(req, resp);
		
	}
}
