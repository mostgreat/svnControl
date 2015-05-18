package gz.test.mostgreat2.common.login.service;

import gz.test.mostgreat2.common.login.dao.LoginDao;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginService {
	
	@Resource(name="loginDao")
	private LoginDao loginDao;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	/**
	 * Login
	 * @param userId
	 * @param passWd
	 * @throws Exception
	 */
	public Map readMember(String userId, String passWd) throws Exception{
		
		return loginDao.readMember(userId, passWd);
		
	}
	
}
