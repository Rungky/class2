package sec03.brd01;

import java.util.ArrayList;
import java.util.List;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	List recursive(int pId, List list) {
		List resultList = new ArrayList();
		
		for(int i=0; i<list.size(); i++) {
			
			ArticleVO vo = (ArticleVO)list.get(i);
			if(vo.getParentNO() == pId) {
				resultList.add(vo);

				List tempList = recursive(vo.getArticleNO(), list);
				resultList.add( tempList );
				
			}
		}
		
		return resultList;
	}
	
	public int getTotal() {
		return boardDAO.selectTotalArticles();
	}
	public List<ArticleVO> listArticles(int pageNum, int countPerPage){
		List<ArticleVO> articlesList = boardDAO.selectAllArticles(pageNum, countPerPage);
		// list는 그냥 모든 row 즉, select * from t_board order by articleno
//		articlesList = recursive(0, articlesList);
		
		return articlesList;
	}
	public List<ArticleVO> listSearchArticles(String keyword){
		List<ArticleVO> articlesList = boardDAO.selectSearchArticles(keyword);
		return articlesList;
	}
	public void addArticle(ArticleVO articleVO) {
		boardDAO.insertNewArticle(articleVO);
	}
	
	public ArticleVO viewArticle(int articleNo) {
		boardDAO.plusViewCount(articleNo);
		ArticleVO vo = boardDAO.selectArticle(articleNo);
		return vo;
	}
	
	public int plusLikeCount(int articleNo) {
		boardDAO.plusLikeCount(articleNo);
		
		ArticleVO vo = boardDAO.selectArticle(articleNo);
		return vo.getLike_count();
	}
	
	public void modArticle(ArticleVO articleVO) {
		boardDAO.updateArticle(articleVO);
	}
	
	public void removeArticle(int articleNO) {
		boardDAO.deleteArticle(articleNO);
	}

	public void addReply(ArticleVO articleVO) {
		boardDAO.insertNewArticle(articleVO);
	}

}
