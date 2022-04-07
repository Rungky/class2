package login.ex01;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sec01.ex01.MemberVO;

@WebServlet("/login/*")
public class loginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
       
    public loginController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String nextPage = "";
		
		if(pathInfo.equals("/login.do")) {
			nextPage = "/login01/login.jsp";
		} else if(pathInfo.equals("/loginCheck.do")) {
			String empno = request.getParameter("empno");
			
			MemberVO vo = new MemberVO();
			
			try {
				vo.setEmpno(Integer.parseInt(empno));
				
				LoginService loginService = new LoginService();
				vo = loginService.loginCheck(vo);
				if(vo.getEname() != null) {
					request.getSession().setAttribute("empno", vo.getEmpno());
				}
				nextPage = "/board/listArticles.do";
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else if(pathInfo.equals("/exam1.do")) {
			char[] origin = {'a', 'b', 'c', '?', 'e', 'f', '?', 'x','y','z'};
			char from = '?';
			char to = '*';
			for(int i = 0; i< origin.length; i++) {
				if(origin[i] == from ) {
					origin[i] = to;
				}
			}
			System.out.println(origin);
			nextPage = "/board/listArticles.do";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
		
	}

}
