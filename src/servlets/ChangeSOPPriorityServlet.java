package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.SOP;
import controller.SOPController;

public class ChangeSOPPriorityServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		req.getRequestDispatcher("/change_sopPriority.jsp").forward(req, resp);
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
		
		SOP s = sc.findSOPbyID(sid);
		
		String newPriority = req.getParameter("newPriority");
		int np = Integer.parseInt(newPriority); 
		
		if(s.getPriority() == np || np <= 0 || np > 10) {
			System.out.println("Please enter a different priority, the one you chose is invalid");
			resp.sendRedirect(req.getContextPath() + "/changesoppriority");
		} 
		else {
			sc.changeSOPPirority(sid, np);
			System.out.println("SOP priority sucessfully changed");
		}
		
		req.getRequestDispatcher("/change_sopPriority.jsp").forward(req, resp);
	}
}