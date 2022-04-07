package login.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import sec01.ex01.MemberVO;

public class LoginDAO {
	private PreparedStatement pstmt;
	private Connection conn;
	private DataSource dataFactory;
	
	public LoginDAO() {
		try {

			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MemberVO selectUserInfo(MemberVO vo) {
		try {
//			connDB();
			conn = dataFactory.getConnection();
			System.out.println("커넥션풀 성공");
			
			String query = "";
			query += " SELECT * ";
			query += " FROM emp2";
			query += " WHERE empno = ?";
			System.out.println(query);
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, vo.getEmpno());
			
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
				
				vo = new MemberVO();
				vo.setEmpno(empno);
				vo.setEname(ename);
				vo.setJob(job);
				vo.setMgr(mgr);
				vo.setHiredate(hiredate);
				vo.setSal(sal);
				vo.setComm(comm);
				vo.setDeptno(deptno);
			}
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return vo;
		
	}
	
	
}
