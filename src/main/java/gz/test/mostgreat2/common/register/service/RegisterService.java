package gz.test.mostgreat2.common.register.service;

import gz.test.mostgreat2.common.register.dao.RegisterDao;
import gz.test.mostgreat2.common.register.model.RegisterCredentials;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("registerService")
public class RegisterService {
	
	@Resource(name="registerDao")
	private RegisterDao registerDao;
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
	
	public int registerMember(String userId, String userName, String passWd, int age, String gender, String addr) throws Exception{
		
		return registerDao.registerMember(userId, userName, passWd, age, gender, addr);
		
	}
	

	public String register(RegisterCredentials inputData) throws Exception {

		int result = registerDao.registerMember(inputData.getInputId(),
		 						   inputData.getInputName(),
								   inputData.getInputPassWd(),
								   inputData.getInputAge(),
								   inputData.getInputGender(),
								   inputData.getInputCity());
		
		if( result == 1){
			return "Success";
		}else{
			return "Fail";
		}
		
	
	}
	
}
