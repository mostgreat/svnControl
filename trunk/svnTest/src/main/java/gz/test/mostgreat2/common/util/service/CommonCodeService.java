package gz.test.mostgreat2.common.util.service;

import gz.test.mostgreat2.common.util.dao.CommonCodeDao;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("commonCodeService")
public class CommonCodeService {
	
	@Resource(name="commonCodeDao")
	private CommonCodeDao commonCodeDao;
	
	private static final Logger logger = LoggerFactory.getLogger(CommonCodeService.class);
	
	public List selectCommonCode(String codeId){
		
		return commonCodeDao.selectCommonCode(codeId);
		
	}
	
	public List selectCommonCodeDetail(String codeId){
		
		return commonCodeDao.selectCommonCodeDetail(codeId);
	}
	
	
	
}
