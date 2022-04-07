package login.ex01;

import sec01.ex01.MemberVO;

public class LoginService {
	
	LoginDAO loginDAO;
	
	LoginService(){
		loginDAO = new LoginDAO();
	}
	
	public MemberVO loginCheck(MemberVO vo) {
		return loginDAO.selectUserInfo(vo);
	}
}
