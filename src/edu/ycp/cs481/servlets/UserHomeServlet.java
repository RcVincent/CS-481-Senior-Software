package edu.ycp.cs481.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;

@SuppressWarnings("serial")
public class UserHomeServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("User Home Servlet: doGet");
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			// TODO: Check for admin/privileges check
			// Plan is to put attribute in so that user_home.jsp can check that for what to display and not display
			if(session.getAttribute("error") != null){
				req.setAttribute("error", session.getAttribute("error"));
				session.setAttribute("error", null);
			}
			if(session.getAttribute("success") != null){
				req.setAttribute("success", session.getAttribute("success"));
				session.setAttribute("success", null);
			}
			req.getRequestDispatcher("/user_home.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("User Home Servlet: doPost");
		
		if(req.getParameter("logout").equalsIgnoreCase("logout")){
			UserController.logout(req);
			resp.sendRedirect(req.getContextPath() + "/login");
		}
	}
}
