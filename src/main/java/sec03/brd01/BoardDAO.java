package sec03.brd01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import sec01.ex01.LoggableStatement;

public class BoardDAO {
	private PreparedStatement pstmt;
	private Connection conn;
	private DataSource dataFactory;
	
	public BoardDAO() {
		try {

			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int selectTotalArticles() {
		int total = 0;
		
		try {
			conn = dataFactory.getConnection();
			
			String query = "select count(*) as total from t_board";
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				total = rs.getInt("total");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return total;
	}
	
	public List<ArticleVO> selectAllArticles(int pageNum, int countPerPage){
		List<ArticleVO> articlesList = new ArrayList();
		
		try {
			conn = dataFactory.getConnection();

			String query = "";
			query += " select tmp.* from ("; 
			query += "     select"; 
			query += "         rownum as rnum,"; 
			query += "         level,"; 
			query += "         articleno,"; 
			query += "         parentno, ";
			query += "         title,";
			query += "         content,";
			query += "         id,";
			query += "         writedate,";
			query += "         e.ename,";
			query += "         view_count";
			query += "     from t_board t, emp2 e";
			query += "     where t.id = e.empno";
			query += "     start with parentno = 0";
			query += "     connect by prior articleno = parentno";
			query += "     order siblings by articleno desc";
			query += " ) tmp";
			query += " where rnum > ? and rnum <= ?";
			
			//공식
			int offset = (pageNum - 1) * countPerPage;
			int to = offset + countPerPage;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, to);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleVO article = new ArticleVO();
				article.setLevel(rs.getInt("level"));
				article.setArticleNO(rs.getInt("articleNO"));
				article.setParentNO(rs.getInt("parentNO"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				article.setId(rs.getInt("id"));
				article.setWriteDate(rs.getDate("writeDate"));
				article.setEname(rs.getString("ename"));
				article.setView_count(rs.getInt("view_count"));
				
				articlesList.add(article);
			}

			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return articlesList;
	}
	
	public List<ArticleVO> selectSearchArticles(String keyword){
		List<ArticleVO> articlesList = new ArrayList();
		
		try {
			conn = dataFactory.getConnection();

			String query = "";
			query += " select"; 
			query += "     level,"; 
			query += "     articleno,"; 
			query += "     parentno, ";
			query += "     title,";
			query += "     content,";
			query += "     id,";
			query += "     writedate,";
			query += "     e.ename,";
			query += "     view_count";
			query += " from t_board t, emp2 e";
			query += " where t.id = e.empno";
			query += " and title like '%'|| ? ||'%'";
			query += " start with parentno = 0";
			query += " connect by prior articleno = parentno";
			query += " order siblings by articleno desc";
			
			
			pstmt = new LoggableStatement(conn, query);
			pstmt.setString(1, keyword);
			System.out.println( ((LoggableStatement)pstmt).getQueryString() );
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleVO article = new ArticleVO();
				article.setLevel(rs.getInt("level"));
				article.setArticleNO(rs.getInt("articleNO"));
				article.setParentNO(rs.getInt("parentNO"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				article.setId(rs.getInt("id"));
				article.setWriteDate(rs.getDate("writeDate"));
				article.setEname(rs.getString("ename"));
				article.setView_count(rs.getInt("view_count"));
				
				articlesList.add(article);
			}

			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return articlesList;
	}
	
	public void insertNewArticle(ArticleVO article) {
		try {
			conn = dataFactory.getConnection();
			String query = "";
			query += " INSERT INTO t_board (";
			query += " 		articleno, parentno, title, content, ";
			query += " 		imageFileName, id )";
			query += " values (";
			query += " 		t_board_seq.nextval, ?, ?, ?, ";
			query += " 		?, ? ";
			query += " )";
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, article.getParentNO());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContent());
			pstmt.setString(4, article.getImageFileName());
			pstmt.setInt(5, article.getId());
			
			int result = pstmt.executeUpdate();
			System.out.println("새글등록 : result : "+ result);
			
//			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArticleVO selectArticle(int articleNo) {
		ArticleVO article = new ArticleVO();
		
		try {
			
			conn = dataFactory.getConnection();
			String query = "";
			query += " select t.*, e.ename ";
			query += " from t_board t, emp2 e ";
			query += " where t.articleno = ? ";
			query += " and t.id = e.empno";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			article.setArticleNO(rs.getInt("articleNO"));
			article.setParentNO(rs.getInt("parentNO"));
			article.setTitle(rs.getString("title"));
			article.setContent(rs.getString("content"));
			article.setId(rs.getInt("id"));
			article.setWriteDate(rs.getDate("writeDate"));
			article.setEname(rs.getString("ename"));
			article.setView_count(rs.getInt("view_count"));
			article.setLike_count(rs.getInt("like_count"));
			
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return article;
	}
	
	public void plusViewCount(int articleNo) {
		try {
			
			conn = dataFactory.getConnection();
			String query = "";
			query += " update t_board";
			query += " set view_count = view_count + 1";
			query += " where articleno = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			pstmt.executeUpdate();
			
//			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void plusLikeCount(int articleNo) {
		try {
			
			conn = dataFactory.getConnection();
			
			String query = "";
			query += " update t_board";
			query += " set like_count = like_count + 1";
			query += " where articleno = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			pstmt.executeUpdate();
			
//			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateArticle(ArticleVO article) {
		try {
			
			conn = dataFactory.getConnection();
			
			String query = "";
			query += " update t_board";
			query += " set";
			query += "  title = ?";
			query += "  ,content = ?";
			query += " where articleno = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			pstmt.setInt(3, article.getArticleNO());
			pstmt.executeUpdate();
			
//			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteArticle(int articleNO) {
		try {
			
			conn = dataFactory.getConnection();
			
			String query = "";
			query += " delete from t_board";
			query += " where articleno in ("; 
			query += " 	    select articleno from t_board";
			query += " 	    start with articleno = ?";
			query += " 	    connect by prior articleno = parentno";
			query += " 	)";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			
//			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
