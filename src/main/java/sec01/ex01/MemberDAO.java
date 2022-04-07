package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "scott";
	private String pw = "tiger";
	
	private Statement stmt;
	private PreparedStatement pstmt;
	private Connection con;
	private DataSource dataFactory;
	
	public MemberDAO() {
		try {

			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void connDB() {
		try {
			
			Class.forName(driver);
			System.out.println("Oracle 드라이버 로딩 성공");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(url, id, pw);
			System.out.println("Connection 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			stmt = con.createStatement();
			System.out.println("Statement 생성 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isExsited(MemberVO memberVO) {
		boolean result = false;
		
		int empno = memberVO.getEmpno();
		String ename = memberVO.getEname();
		
		try {
			con = dataFactory.getConnection();
			
			String query = 	" SELECT ";
			query += " count(*) as count, ";
			query += " DECODE( count(*), 1, 'true', 'false' ) as result";
			query += " FROM emp";
			query += " WHERE empno = ? AND ename = ?";
			
			// SQL 준비
//			pstmt = con.prepareStatement(query);
			pstmt = new LoggableStatement(con, query);
			pstmt.setInt(1, empno);
			pstmt.setString(2, ename);
			
			System.out.println( ((LoggableStatement)pstmt).getQueryString() );
			
			// 실행
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = Boolean.parseBoolean(rs.getString("result"));
				System.out.println("count : "+rs.getString("count"));
				System.out.println("result : "+ result);
				
//				if("true".equals(rs.getString("result"))) {
//					result = true;
//				} else {
//					result = false;
//				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<MemberVO> listMembers(MemberVO memberVO){
		List<MemberVO> list = new ArrayList<MemberVO>();

		try {
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");
			
			String query = "";
			query += " SELECT * ";
			query += " FROM emp2";
			query += " WHERE empno = ?";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, memberVO.getEmpno());
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				int mgr = rs.getInt("empno");
				Date hiredate = rs.getDate("hiredate");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				int deptno = rs.getInt("deptno");
				
				MemberVO vo = new MemberVO();
				vo.setEmpno(empno);
				vo.setEname(ename);
				vo.setJob(job);
				vo.setMgr(mgr);
				vo.setHiredate(hiredate);
				vo.setSal(sal);
				vo.setComm(comm);
				vo.setDeptno(deptno);
				
				list.add(vo);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<MemberVO> listMembers(){
		List<MemberVO> list = new ArrayList<MemberVO>();

		try {
//			connDB();
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");
			
			int salLimit = 1000;
//			String query = "select * from emp where sal >= ? and sal < ?";
			String query = "";
			query += " SELECT * ";
			query += " FROM emp2";
			System.out.println(query);
			
//			ResultSet rs = stmt.executeQuery(query);
			pstmt = con.prepareStatement(query);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				int mgr = rs.getInt("empno");
				Date hiredate = rs.getDate("hiredate");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				int deptno = rs.getInt("deptno");
				
				MemberVO vo = new MemberVO();
				vo.setEmpno(empno);
				vo.setEname(ename);
				vo.setJob(job);
				vo.setMgr(mgr);
				vo.setHiredate(hiredate);
				vo.setSal(sal);
				vo.setComm(comm);
				vo.setDeptno(deptno);
				
				list.add(vo);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	// 추가; insert
	public void addMember(MemberVO vo) {
		try {
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");

			String query = " INSERT INTO emp2 (empno, ename, sal)";
			query +=       " VALUES (?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, vo.getEmpno());
			pstmt.setString(2, vo.getEname());
			pstmt.setInt(3, vo.getSal());
			
//			pstmt.executeQuery();
			int result = pstmt.executeUpdate();
			System.out.println("insert 성공: "+ result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (con != null) con.close(); 
				if (pstmt != null) pstmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void delMember(MemberVO vo) {
		try {
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");

			String query = " DELETE FROM emp2 ";
			query +=       " WHERE empno = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, vo.getEmpno());
			pstmt.executeQuery();

			System.out.println("delete 성공 : " + vo.getEmpno());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (con != null) con.close(); 
				if (pstmt != null) pstmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public List<MemberVO> searchMembers(MemberVO memberVO){
		List<MemberVO> list = new ArrayList<MemberVO>();

		try {
//			connDB();
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");
			
			String query = "";
			query += " SELECT * ";
			query += " FROM emp2";
			query += " WHERE 1 = 1";
			
			String param_ename = memberVO.getEname();
			if(param_ename != null) {
//				query += " AND UPPER(ename) LIKE '%"+ param_ename.toUpperCase() +"%'";
				query += " AND UPPER(ename) LIKE '%'|| ? ||'%'";

				pstmt = new LoggableStatement(con, query);
//				pstmt = con.prepareStatement(query);
				pstmt.setString(1, param_ename.toUpperCase());
				System.out.println( ((LoggableStatement)pstmt).getQueryString() );
			} else {
				pstmt = con.prepareStatement(query);
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				int mgr = rs.getInt("empno");
				Date hiredate = rs.getDate("hiredate");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				int deptno = rs.getInt("deptno");
				
				MemberVO vo = new MemberVO();
				vo.setEmpno(empno);
				vo.setEname(ename);
				vo.setJob(job);
				vo.setMgr(mgr);
				vo.setHiredate(hiredate);
				vo.setSal(sal);
				vo.setComm(comm);
				vo.setDeptno(deptno);
				
				list.add(vo);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(con != null) {
				con.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void modMember(MemberVO vo) {
		try {
			con = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");

			String query = " update emp2 ";
			query +=       " set";
			query +=       " ename = ?";
			query +=       " ,sal = ?";
			query +=       " where empno = ?";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, vo.getEname());
			pstmt.setInt(2, vo.getSal());
			pstmt.setInt(3, vo.getEmpno());
			
//			pstmt.executeQuery();
			int result = pstmt.executeUpdate();
			System.out.println("update 성공: "+ result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (con != null) con.close(); 
				if (pstmt != null) pstmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}
