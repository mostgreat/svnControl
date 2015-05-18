package gz.test.mostgreat2.common.main.controller;

import gz.test.mostgreat2.common.main.service.MainService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {
	
	@Resource(name="mainService")
	private MainService service;
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/main.do")
	public String main() {
		
		logger.debug("main Conroller");
		return "common/main/main";
	}
	
}
