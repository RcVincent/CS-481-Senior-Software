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
public class SearchPositionServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		
		
		req.getRequestDispatcher("/searchPositions.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		PositionController pc = new PositionController();
		
		String id = req.getParameter("positionID"); 
		String title = req.getParameter("title");
		String desc = req.getParameter("description"); 
		String priority = req.getParameter("priority"); 
		
		int searchID, prio;
		if(id == null || id == "" || id == " ") {
			searchID = 2; 
		} else {
			searchID = Integer.parseInt(id); 
		}
		
		if(priority == null || priority == "" || priority == " ") {
			prio = 0;
		} else {
			prio = Integer.parseInt(priority); 
		}
		
		ArrayList<Position> result = pc.searchForPositions(searchID, title, desc, prio);
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/searchPositions.jsp").forward(req, resp);
	}
}
