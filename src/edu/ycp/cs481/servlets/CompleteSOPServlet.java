package edu.ycp.cs481.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.model.SOP;
import edu.ycp.cs481.control.PositionController;
import edu.ycp.cs481.control.SOPController;


@SuppressWarnings("serial")
public class CompleteSOPServlet extends HttpServlet {
	
	private void loadSOP(HttpServletRequest req){
		// TODO: Error checking
		int sopID = Integer.parseInt(req.getParameter("sopID"));
		SOPController sc = new SOPController();
		SOP sop = sc.searchForSOPs(sopID, false, null, false, null, -1, -1, -1).get(0);
		req.setAttribute("sopID", sop.getID());
		req.setAttribute("title", sop.getTitle());
		req.setAttribute("priority", sop.getPriority());
		req.setAttribute("version", sop.getVersion());
		req.setAttribute("authorID", sop.getAuthorID());
		req.setAttribute("description", sop.getDescription());
		req.setAttribute("archived", sop.isArchived());
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			//
			loadSOP(req);
			req.getRequestDispatcher("/complete_sop.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int sopID =  Integer.parseInt(req.getParameter("sopID"));
		UserController uc = new UserController(); 
		SOPController sc = new SOPController(); 
		PositionController pc = new PositionController(); 
		HttpSession session = req.getSession();
		int userID = (int) session.getAttribute("user_id");
		
		//User you = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
		String action = req.getParameter("sopAction");
		
		if(action.equalsIgnoreCase("completeSOP")) {
			if(uc.SOPisCompleted(userID, sopID)) {
				System.out.println("This sop is already complete, you cannot do it again");
				req.setAttribute("sopComplete", "That SOP is already complete");
				resp.sendRedirect(req.getContextPath() + "/employeeProfile");
			}
			else {
				sc.insertCompletedSOP(userID, sopID);
				if(!uc.SOPisCompleted(userID, sopID)) {
					System.out.println("There was a problem adding that SOP to the completed List");
					session.setAttribute("Error", "There was an issue sending the Completed SOP to the appropriate table.");
					resp.sendRedirect(req.getContextPath() + "/employeeProfile");
				}
				else {
					System.out.println("The sop was completed. Good for you.");
					session.setAttribute("Success", "The SOP was completed.");
				}
			}
		}
		
		else if(action.equalsIgnoreCase("return")) {
			resp.sendRedirect(req.getContextPath() + "/employeeProfile");
		}
		
		loadSOP(req);
		req.getRequestDispatcher("/complete_sop.jsp").forward(req, resp);
	}
}
