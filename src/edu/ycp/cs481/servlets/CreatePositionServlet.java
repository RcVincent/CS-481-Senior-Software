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
public class CreatePositionServlet extends HttpServlet {
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Create Position: doget");
		
		HttpSession session = req.getSession();
		if (session.getAttribute("user_id") == null) {
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {
			req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
		}	
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Getting Position info from the webform");
		
		int priority = 0;
		String positionTitle = req.getParameter("title");
		String positionDescription = req.getParameter("description");
		String Priority = req.getParameter("priority");
		priority = Integer.parseInt(Priority);
		
		
		
		
		PositionController pc = new PositionController(); 
		
		
		//get the created position and error check it. 
		
		
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
	}
	
}
