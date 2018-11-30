package edu.ycp.cs481.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.UserController;
import edu.ycp.cs481.control.SOPController;

@SuppressWarnings("serial")
public class CompleteSOP extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			//
			req.getRequestDispatcher("/complete_sop.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int sopID =  Integer.parseInt(req.getParameter("sopID"));
		UserController uc = new UserController(); 
		SOPController sc = new SOPController(); 
		HttpSession session = req.getSession();
		int userID = (int) session.getAttribute("user_id");
		
		//User you = uc.searchForUsers(userID, -1, false, "", false, "", false, "", -1, -1).get(0);
		String action = req.getParameter("sopAction");
		
		if(action.equalsIgnoreCase("completeSOP")) {
			if(uc.SOPisCompleted(userID, sopID)) {
				System.out.println("This sop is already complete, you cannot do it again");
				req.setAttribute("sopComplete", "That SOP is already complete");
				resp.sendRedirect(req.getContextPath() + "/");
			}
			else {
				sc.insertCompletedSOP(userID, sopID);
				if(!uc.SOPisCompleted(userID, sopID)) {
					System.out.println("There was a problem adding that SOP to the completed List");
					resp.sendRedirect(req.getContextPath() + "/");
					session.setAttribute("Error", "There was an issue sending the Completed SOP to the appropriate table.");
				}
				else {
					System.out.println("The sop was completed. Good for you.");
					session.setAttribute("Success", "The SOP was completed.");
				}
			}
		}
		
		else if(action.equalsIgnoreCase("return")) {
			resp.sendRedirect(req.getContextPath() + "/");
		}
		
		req.getRequestDispatcher("/complete_sop.jsp").forward(req, resp);
	}
}
