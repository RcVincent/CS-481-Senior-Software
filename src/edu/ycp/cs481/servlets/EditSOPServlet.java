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
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		SOPController sc = new SOPController(); 
		SOP s = null;
		int searchID = Integer.parseInt(req.getParameter("sop_id"));
		
		if(searchID <=0 ) {
			System.out.println("Invalid search id");
			req.setAttribute("searchError", "Invalid search ID!");
		} else {
			s = sc.searchForSOPs(searchID, false, "", false, "", -1, -1, -1).get(0);
		}
		
		if(s == null) {
			System.out.println("There was a search error, returning to the homepage");
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}
		req.getRequestDispatcher("/edit_sop.jsp").forward(req, resp);
	}
}
