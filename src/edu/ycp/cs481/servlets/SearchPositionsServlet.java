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
			// Set default search with no parameters
			PositionController pc = new PositionController();
			ArrayList<Position> positions = pc.searchForPositions(-1, false, null, false, null, -1);
			req.setAttribute("positions", positions);
			// Set default page and display size
			req.setAttribute("page", 0);
			req.setAttribute("displaySize", 10);
			req.getRequestDispatcher("/search_positions.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String changePage = req.getParameter("changePage");
		String changeDisplaySize = req.getParameter("changeDisplaySize");
		int currentDisplaySize = Integer.parseInt(req.getParameter("displaySize"));
		
		if(changePage != null && !changePage.equalsIgnoreCase("")){
			int currentPage = Integer.parseInt(req.getParameter("page"));
			if(changePage.equalsIgnoreCase("prev")){
				req.setAttribute("page", currentPage - 1);
			}else if(changePage.equalsIgnoreCase("next")){
				req.setAttribute("page", currentPage + 1);
			}
		}else{
			req.setAttribute("page", 0);
		}
		
		if(changeDisplaySize != null && !changeDisplaySize.equalsIgnoreCase("")){
			req.setAttribute("displaySize", Integer.parseInt(changeDisplaySize));
		}else{
			req.setAttribute("displaySize", currentDisplaySize);
		}
		
		String idStr = req.getParameter("positionID");
		int id = idStr.equalsIgnoreCase("")?-1:Integer.parseInt(idStr);
		String title = req.getParameter("title");
		String desc = req.getParameter("description"); 
		String priorityStr = req.getParameter("priority");
		int priority = priorityStr.equalsIgnoreCase("")?-1:Integer.parseInt(priorityStr);
		
		PositionController pc = new PositionController();
		ArrayList<Position> positions = pc.searchForPositions(id, true, title, true, desc, priority);
		
		req.setAttribute("positions", positions);
		
		// Push parameters back so user doesn't have to type them every time
		req.setAttribute("positionID", idStr);
		req.setAttribute("title", title);
		req.setAttribute("description", desc);
		req.setAttribute("priority", priorityStr);
		
		req.getRequestDispatcher("/search_positions.jsp").forward(req, resp);
	}
}
