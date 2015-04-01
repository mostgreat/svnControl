package gz.test.mostgreat2.common.util.dao;

import gz.test.mostgreat2.common.commonDao.commonDao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commonCodeDao")
public class CommonCodeDao extends commonDao{
	
	@Autowired
	private SqlSession sqlQuery;
	
	private static final Logger logger = LoggerFactory.getLogger(CommonCodeDao.class);
	
	public List selectCommonCode(String codeId){
		
		addItem("id", codeId);
		
		List result = sqlQuery.selectList("readCommonCode", getInputMap());
		logger.debug("====Query Result====:" + result);
		if(result != null){
			return result; 
		}
		else{
			return null;
		}
		
	}
	
	public List selectCommonCodeDetail(String codeId){
		
		addItem("id", codeId);		
		
		List result = sqlQuery.selectList("readCommonCode_detail", getInputMap());
		logger.debug("====Query Result====:" + result);
		if(result != null){
			return result; 
		}
		else{
			return null;
		}
		
	}
	
	
}
