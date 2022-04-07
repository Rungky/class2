package sec03.brd01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	BoardService boardService;
	ArticleVO articleVO;
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
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
		String nextPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo();
		
		HttpSession session = request.getSession();
		
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
			
//			int idx = action.indexOf("listArticles.do");
//			if(idx != -1)
//			if(action.indexOf("listArticles.do") != -1)
			if (action.equals("/listArticles.do")) {
				
				int pageNum = 1;
				int countPerPage = 5;

				String strPageNum = request.getParameter("pageNum");
				String strCountPerPage = request.getParameter("countPerPage");
				if(strPageNum != null) {
					pageNum = Integer.parseInt(strPageNum);
				}
				if(strCountPerPage != null) {
					countPerPage = Integer.parseInt(strCountPerPage);
				}
				
				articlesList = boardService.listArticles(pageNum, countPerPage);
				request.setAttribute("articlesList", articlesList);

				int total = boardService.getTotal();
				request.setAttribute("total", total);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("countPerPage", countPerPage);
				
				nextPage = "/board01/listArticles.jsp";
				
			} else if (action.equals("/search.do")) {
				
				String keyword = request.getParameter("keyword");
				
				articlesList = boardService.listSearchArticles(keyword);
				request.setAttribute("articlesList", articlesList);
				
				nextPage = "/board01/listArticles.jsp";
				
				
			} else if (action.equals("/articleForm.do")) {
				
				nextPage = "/board01/articleForm.jsp";

			} else if (action.equals("/addArticle.do")) {
				
				String title = request.getParameter("title");
				String content = request.getParameter("content");
//				System.out.println("content : "+ content);
//				
//				byte[] c = content.getBytes();
//				for(int i=0; i<c.length; i++) {
//					System.out.println(c[i]);
//				}
				
				Integer id = (Integer) session.getAttribute("empno");
				if(id == null) id = 7839;
				
				articleVO = new ArticleVO();
				articleVO.setParentNO(0);
				articleVO.setId(id);
				articleVO.setTitle(title);
				articleVO.setContent(content);
				
				boardService.addArticle(articleVO);
				
				nextPage = "/board/listArticles.do";
			} else if (action.equals("/viewArticle.do")) {
				
				String aNo = request.getParameter("articleNO");
				String plusLike = request.getParameter("plusLike");
				if(plusLike != null && "true".equals(plusLike)) {
					boardService.plusLikeCount(Integer.parseInt(aNo));
				}

				articleVO = boardService.viewArticle(Integer.parseInt(aNo));
				
				// 줄넘김을 <br>로 교체
				String content = articleVO.getContent();
				if(content != null && content.length() > 0) {
					content = content.replaceAll("\n", "<br>");
					articleVO.setContent(content);
				}
				
				request.setAttribute("article", articleVO);
				
				nextPage = "/board01/viewArticle.jsp";
				
			} else if (action.equals("/plusLikeCount.do")) {
				String aNo = request.getParameter("articleNO");
				int newLikeCount = boardService.plusLikeCount(Integer.parseInt(aNo));
				
				response.getWriter().print("{\"like_count\":\""+ newLikeCount +"\"}");
				return;
				
				
			} else if (action.equals("/modifyArticle.do")) {
				
				String aNo = request.getParameter("articleNO");
				articleVO = boardService.viewArticle(Integer.parseInt(aNo));
				request.setAttribute("article", articleVO);
				nextPage = "/board01/modifyArticle.jsp";
				
			} else if (action.equals("/modArticle.do")) {
				
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				String articleNO = request.getParameter("articleNO");
				
				articleVO = new ArticleVO();
				articleVO.setArticleNO( Integer.parseInt(articleNO) );
				articleVO.setTitle(title);
				articleVO.setContent(content);
				
				boardService.modArticle(articleVO);
				
				nextPage = "/board/viewArticle.do?articleNO="+articleNO;
				
			} else if (action.equals("/removeArticle.do")) {
				
				String articleNO = request.getParameter("articleNO");
				boardService.removeArticle(Integer.parseInt(articleNO));
				
				nextPage = "/board/listArticles.do";
				
			} else if (action.equals("/replyForm.do")) {
			
				String parentNO = request.getParameter("parentNO");
				request.setAttribute("parentNO", parentNO);
				nextPage = "/board01/replyForm.jsp";
			
			} else if (action.equals("/addReply.do")) {
				
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				String parentNO = request.getParameter("parentNO");
				
				articleVO = new ArticleVO();
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setParentNO( Integer.parseInt(parentNO) );
				articleVO.setId(7902);

				boardService.addReply(articleVO);

				nextPage = "/board/listArticles.do";
			} else {
				nextPage = "/board01/deny.jsp";
			}
			
			System.out.println("nextPage: "+ nextPage);
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
