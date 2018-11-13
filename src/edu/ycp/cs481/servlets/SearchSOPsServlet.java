package edu.ycp.cs481.servlets;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.control.SOPController;
import edu.ycp.cs481.model.SOP;

@SuppressWarnings("serial")
public class SearchSOPsServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			req.getRequestDispatcher("/search_sops.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		SOPController sc = new SOPController(); 
		
		String idStr = req.getParameter("sopID");
		int id = idStr.equalsIgnoreCase("")?-1:Integer.parseInt(idStr);
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String priorityStr = req.getParameter("priority");
		int priority = priorityStr.equalsIgnoreCase("")?-1:Integer.parseInt(priorityStr);
		String versionStr = req.getParameter("version");
		int version = versionStr.equalsIgnoreCase("")?-1:Integer.parseInt(versionStr);
		String authorIDStr = req.getParameter("authorID");
		int authorID = authorIDStr.equalsIgnoreCase("")?-1:Integer.parseInt(authorIDStr);
		
		ArrayList<SOP> sops = sc.searchForSOPs(id, true, title, true, description, priority, version, authorID);
		for(int i = 0; i < sops.size(); i++){
			req.setAttribute("sopID" + (i+1), sops.get(i).getID());
			req.setAttribute("title" + (i+1), sops.get(i).getTitle());
			req.setAttribute("description" + (i+1), sops.get(i).getDescription());
			req.setAttribute("priority" + (i+1), sops.get(i).getPriority());
			req.setAttribute("version" + (i+1), sops.get(i).getVersion());
			req.setAttribute("authorID" + (i+1), sops.get(i).getAuthorID());
		}
		
		req.getRequestDispatcher("/search_sops.jsp").forward(req, resp);
		
	}
}
