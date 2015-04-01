package gz.test.mostgreat2.common.commonDao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class commonDao {
	
	@Autowired
	private SqlSession sqlQuery;
	
	private HashMap inputMap = null;
	
	private static final Logger logger = LoggerFactory.getLogger(commonDao.class);
	
	public void newInputMap(){
		inputMap = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getInputMap(){
		return inputMap;
	}
	
	public void addItem(String key, String value){
		if(inputMap == null)
			newInputMap();
		inputMap.put(key, value);
	}
	
	public void addItem(String key, int value){
		if(inputMap == null)
			newInputMap();
		inputMap.put(key, value);
	}
	
	
	
}
