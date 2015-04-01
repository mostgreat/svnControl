package gz.test.mostgreat2.common.login.dao;

import gz.test.mostgreat2.common.commonDao.commonDao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginDao")
public class LoginDao extends commonDao{
	
	@Autowired
	private SqlSession sqlQuery;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginDao.class);
		
	public Map readMember(String userId, String passWord) throws Exception{
		
		addItem("userId", userId);		
		addItem("passWord", passWord);
		
		Map result = (Map)sqlQuery.selectOne("readMember", getInputMap());
		logger.debug("====Query Result====:" + result);
		if(result != null){
			return result; 
		}
		else{
			return null;
		}
	}
	
	
}
