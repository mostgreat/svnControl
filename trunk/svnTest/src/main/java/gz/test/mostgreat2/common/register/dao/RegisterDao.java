package gz.test.mostgreat2.common.register.dao;

import gz.test.mostgreat2.common.commonDao.commonDao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("registerDao")
public class RegisterDao extends commonDao{
	
	@Autowired
	private SqlSession sqlQuery;
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterDao.class);
	
	public int registerMember(String userId, String userName, String passWord, int age, String gender, String addr) throws Exception{
		
		addItem("userId", userId);		
		addItem("userName", userName);
		addItem("passWord", passWord);
		addItem("age", age);
		addItem("gender", gender);
		addItem("addr", addr);
		
		
		int result = sqlQuery.insert("registerMember", getInputMap());
		logger.debug("====Query Result====:" + result);
		
		return result;
	}
	
	
}
