package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import controller.UserController;

public class ChangePasswordServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		
		HttpSession session = req.getSession();
		if(session.getAttribute("email") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		req.getRequestDispatcher("/change_password.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		if(req.getParameter("index") != null) {
			System.out.println("returning to index");
			resp.sendRedirect(req.getContextPath() + "/index");
		}
		
		HttpSession session = req.getSession();
		
		String email = (String) session.getAttribute("email");
		UserController uc = new UserController(); 
		
		User user = uc.findUserByEmail(email);
		
		String oldPassword = (String) req.getParameter("oldPass"); 
		String newPassword = (String) req.getParameter("newPass");
		String newPassword2 = (String) req.getParameter("newPass2");
		
		if(newPassword.equals(newPassword2)) {
			uc.changeUserPassword(user.getEmail(), oldPassword, newPassword);
			System.out.println(user.getEmail()); 
			System.out.println(oldPassword); 
			System.out.println(newPassword); 
		}
		
		else {
			System.out.println("The passwords donot match please try again");
			resp.sendRedirect(req.getContextPath() + "/retrychangepassword");
		}
		
		req.getRequestDispatcher("/change_password.jsp").forward(req, resp);
		
	}
}
