package gz.test.mostgreat2.common.interceptor;

import gz.test.mostgreat2.common.session.sessionVo.SessionVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(LoginCheckInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse response, Object handler) throws Exception {
		
		HttpSession sess = req.getSession(false);
		
		try{
			
			//accessible page before login
			if(req.getRequestURI().indexOf("register") > 0 ||
			   req.getRequestURI().indexOf("selectCommonCode") > 0 ||
			   req.getRequestURI().indexOf("test") > 0 ||
			   req.getRequestURI().indexOf("selectCommonCodeDetail") > 0 || 
			   req.getRequestURI().indexOf("greeting") > 0
			   ){
				return true;
			}
			
			
			//Login Check
			SessionVo sessVo = (SessionVo)sess.getAttribute("loginUser");
			if(sessVo.getUserId() != null){
				return true;
			}
			
		}catch(Exception e){
			logger.debug(e.getMessage());
			response.sendRedirect("/login.do");
			return false;
		}

		return true;
	}
	
}
