package edu.ycp.cs481.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Index Servlet: doGet");
		
		/*
		HttpSession session = req.getSession();
		/*
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}
		else{
			System.out.println("User_ID: " + session.getAttribute("user_id"));
			resp.sendRedirect(req.getContextPath() + "/user_home");
		}
		*/

		resp.sendRedirect(req.getContextPath() + "/user_home");

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("Index Servlet: doPost");
	}
}
