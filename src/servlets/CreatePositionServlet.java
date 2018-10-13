package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Position;
import controller.PositionController;

public class CreatePositionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PositionController pc = null; 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Create Position: doget");
		HttpSession session = req.getSession();
		
		//get session info
		System.out.println(session.getAttribute("email"));
		
		if (session.getAttribute("email") == null) {
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/Login");
			return;
		}
		
		if(session.getAttribute("isAdmin").equals("User") || 
			session.getAttribute("isAdmin").equals("user")){
			resp.sendRedirect(req.getContextPath() + "/MainPage");

		}
		
		req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Getting Position info from the webform");
		
		String positionTitle = req.getParameter("title");
		String positionDescription = req.getParameter("description");
		
		pc = new PositionController(); 
		
		
		if(req.getParameter("index") != null) {
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		req.getRequestDispatcher("/create_position.jsp").forward(req, resp);
	}
	
}
