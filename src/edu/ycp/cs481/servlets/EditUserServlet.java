package edu.ycp.cs481.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.model.Position;
import edu.ycp.cs481.control.PositionController;

@SuppressWarnings("serial")
public class EditUserServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		req.getRequestDispatcher("/change_positionPriority.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		PositionController pc = new PositionController(); 
		
		String positionID = req.getParameter("PositionID");
		int id = Integer.parseInt(positionID);
		
		ArrayList<Position> result = pc.searchForPositions(id, "", "", 0);
		Position p = result.get(0);
	
		String NewPriority = req.getParameter("newPriority");
		int newp = Integer.parseInt(NewPriority);
		
		if(p.getPriority() == newp || newp <= 0 || newp > 10) {
			System.out.println("Please enter a different priority, the one you chose is invalid");
			resp.sendRedirect(req.getContextPath() + "/changepositionpriority");
		}
		else {
			pc.changePositionPriority(p, newp);
			System.out.println("Position priority updated");
		}
		
		req.getRequestDispatcher("/change_positionPriority.jsp").forward(req, resp);
	}
}
