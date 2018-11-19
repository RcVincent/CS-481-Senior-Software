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
public class SearchSOPsServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		if(session.getAttribute("user_id") == null){
			resp.sendRedirect(req.getContextPath() + "/login");
		}else{
			// TODO: Use posID if it's sent in via url
			SOPController sc = new SOPController();
			ArrayList<SOP> sops = sc.searchForSOPs(-1, false, null, false, null, -1, -1, -1);
			req.setAttribute("sops", sops);
			// Set default page and display size
			req.setAttribute("page", 0);
			req.setAttribute("displaySize", 10);
			req.getRequestDispatcher("/search_sops.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String changePage = req.getParameter("changePage");
		String changeDisplaySize = req.getParameter("changeDisplaySize");
		int currentDisplaySize = Integer.parseInt(req.getParameter("displaySize"));
		
		if(changePage != null && !changePage.equalsIgnoreCase("")){
			int currentPage = Integer.parseInt(req.getParameter("page"));
			if(changePage.equalsIgnoreCase("prev")){
				req.setAttribute("page", currentPage - 1);
			}else if(changePage.equalsIgnoreCase("next")){
				req.setAttribute("page", currentPage + 1);
			}
		}else{
			req.setAttribute("page", 0);
		}
		
		if(changeDisplaySize != null && !changeDisplaySize.equalsIgnoreCase("")){
			req.setAttribute("displaySize", Integer.parseInt(changeDisplaySize));
		}else{
			req.setAttribute("displaySize", currentDisplaySize);
		}
		
		String idStr = req.getParameter("sopID");
		String priorityStr = req.getParameter("priority");
		String versionStr = req.getParameter("version");
		String authorIDStr = req.getParameter("authorID");
		
		int id = idStr.equalsIgnoreCase("")?-1:Integer.parseInt(idStr);
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		int priority = priorityStr.equalsIgnoreCase("")?-1:Integer.parseInt(priorityStr);
		int version = versionStr.equalsIgnoreCase("")?-1:Integer.parseInt(versionStr);
		int authorID = authorIDStr.equalsIgnoreCase("")?-1:Integer.parseInt(authorIDStr);
		
		SOPController sc = new SOPController();
		ArrayList<SOP> sops = sc.searchForSOPs(id, true, title, true, description, priority, version, authorID);
		
		req.setAttribute("sops", sops);
		
		// Push parameters back so User doesn't have to retype them
		req.setAttribute("sopID", idStr);
		req.setAttribute("title", title);
		req.setAttribute("description", description);
		req.setAttribute("priority", priorityStr);
		req.setAttribute("version", versionStr);
		req.setAttribute("authorID", authorIDStr);
		
		req.getRequestDispatcher("/search_sops.jsp").forward(req, resp);
		
	}
}
