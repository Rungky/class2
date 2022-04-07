package sec01.ex01;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	MemberDAO memberDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	memberDAO = new MemberDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

//		String uri = request.getRequestURI();
//		String info = request.getPathInfo();
//		String query = request.getQueryString();
//		String param = request.getParameter("name");
//		
//		System.out.println("uri: "+ uri);
//		System.out.println("info: "+ info);
//		System.out.println("query: "+ query);
//		System.out.println("param(name): "+ param);
		
		String nextPage = "/test01/listMembers.jsp";
		String action = request.getPathInfo();
		if(action.equals("/listMembers.do")) {
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			
			nextPage = "/test01/listMembers.jsp";
		} else if (action.equals("/memberForm.do")){
			
			nextPage = "/test01/memberForm.jsp";
			
		} else if (action.equals("/addMember.do")){

			String empno = request.getParameter("empno");
			String ename = request.getParameter("ename");
			String sal = request.getParameter("sal");
			
			MemberVO memberVo = new MemberVO();
			try {
				memberVo.setEmpno(Integer.parseInt(empno));
				memberVo.setEname(ename);
				memberVo.setSal(Integer.parseInt(sal));
				
				memberDAO.addMember(memberVo);
				
				request.setAttribute("msg", ename+"님 가입완료");
				nextPage = "/member/listMembers.do";
				
			} catch(Exception e) {
				
				nextPage = "/test01/memberForm.jsp";
				
			}
		} else if (action.equals("/modMemberForm.do")){
			
			// empno를 이용해서 회원 정보를 조회
			String empno = request.getParameter("empno");
			
			MemberVO memberVo = new MemberVO();
			try {
				memberVo.setEmpno(Integer.parseInt(empno));
				
				List<MemberVO> list = memberDAO.listMembers(memberVo);
				System.out.println(list.size());
				if(list != null && list.size() > 0) {
					MemberVO vo = list.get(0);
					request.setAttribute("memInfo", vo);

					// 조회한 내용을 view로 전달
					nextPage = "/test01/modMemberForm.jsp";
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				nextPage = "/test01/memberForm.jsp";
			}
			
		} else if (action.equals("/modMember.do")){
			String empno = request.getParameter("empno");
			String ename = request.getParameter("ename");
			String sal = request.getParameter("sal");
			
			MemberVO memberVo = new MemberVO();
			try {
				memberVo.setEmpno(Integer.parseInt(empno));
				memberVo.setEname(ename);
				memberVo.setSal(Integer.parseInt(sal));
				
				memberDAO.modMember(memberVo);
				
				nextPage = "/member/listMembers.do";
				
			} catch(Exception e) {
				
				nextPage = "/member/modMemberForm.do?empno="+empno;
				
			}
		} else if (action.equals("/delMember.do")){
			String empno = request.getParameter("empno");
			MemberVO memberVo = new MemberVO();
			try {
				memberVo.setEmpno(Integer.parseInt(empno));
				
				memberDAO.delMember(memberVo);
				
				nextPage = "/member/listMembers.do";
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		
	}
}
