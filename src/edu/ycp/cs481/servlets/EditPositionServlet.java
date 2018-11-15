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
public class EditPositionServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {
			req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
		}
		
		//do a permissions check here and if they are a user return them to the home page 
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		boolean editError = false;
		PositionController pc = new PositionController(); 
		String newPositionTitle = req.getParameter("newTitle");
		String newPositionTitleConfirm = req.getParameter("newTitleConfirm");
		
		if((newPositionTitle == null ||  newPositionTitle.equalsIgnoreCase("")) || (newPositionTitleConfirm == null || newPositionTitleConfirm.equalsIgnoreCase(""))) {
			//do not do the edit 
		}
		else if(!newPositionTitle.equalsIgnoreCase(newPositionTitleConfirm)) {
			req.setAttribute("changeTitleError", "Titles do not match!");
			editError = true; 
		} else {
			
		}
		
		
		
		req.getRequestDispatcher("/edit_position.jsp").forward(req, resp);
	}
}
