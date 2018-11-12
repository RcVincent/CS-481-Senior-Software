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
			req.getRequestDispatcher("/change_sopPriority.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		SOPController sc = new SOPController(); 
		String sopID = req.getParameter("sopID");
		int sid = Integer.parseInt(sopID);
		
		ArrayList<SOP> result = sc.searchForSOPs(sid, false, "", false, "", 0, 0, 0);
		SOP s = result.get(0);
		
		String newPriority = req.getParameter("newPriority");
		int np = Integer.parseInt(newPriority); 
		
		if(s.getPriority() == np || np <= 0 || np > 10) {
			System.out.println("Please enter a different priority, the one you chose is invalid");
			resp.sendRedirect(req.getContextPath() + "/changesoppriority");
		} 
		else {
			sc.changeSOPPriority(s, np);
			System.out.println("SOP priority sucessfully changed");
		}
		
		req.getRequestDispatcher("/change_sopPriority.jsp").forward(req, resp);
	}
}
