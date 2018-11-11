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
public class SearchPositionsServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			req.getRequestDispatcher("/search_positions.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String idStr = req.getParameter("positionID");
		int id = idStr.equalsIgnoreCase("")?-1:Integer.parseInt(idStr);
		String title = req.getParameter("title");
		String desc = req.getParameter("description"); 
		String priorityStr = req.getParameter("priority");
		int priority = priorityStr.equalsIgnoreCase("")?-1:Integer.parseInt(priorityStr);
		
		PositionController pc = new PositionController();
		ArrayList<Position> positions = pc.searchForPositions(id, title, desc, priority);
		
		for(int i = 0; i < positions.size(); i++){
			req.setAttribute("positionID" + (i+1), positions.get(i).getID());
			req.setAttribute("title" + (i+1), positions.get(i).getTitle());
			req.setAttribute("description" + (i+1), positions.get(i).getDescription());
			req.setAttribute("priority" + (i+1), positions.get(i).getPriority());
		}
		
		req.getRequestDispatcher("/search_positions.jsp").forward(req, resp);
	}
}
