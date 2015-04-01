package gz.test.mostgreat2.common.register.controller;

import gz.test.mostgreat2.common.register.service.RegisterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class RegisterController {

	@Resource(name = "registerService")
	private RegisterService registerService;

	public static final int KEY_SIZE = 1024;

	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@RequestMapping(value = "/registerPage.do")
	public String registerPage(HttpServletRequest req) throws Exception {
		
		
		return "common/register/register";
				
	}
	
	@RequestMapping(value = "/register.do", method = RequestMethod.GET)
	public String register(HttpServletRequest req,
						   Model model) throws Exception {
		
		return "redirect:/registerPage.do";
				
	}

	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public @ResponseBody Integer register(HttpServletRequest req,
						   @RequestParam("InputName")   String userName,
						   @RequestParam("InputId")     String userId, 
						   @RequestParam("InputPassWd") String passWd,
						   @RequestParam("InputAge")    int age,
						   @RequestParam("InputGender") String gender,
						   @RequestParam("InputCity") String addr,
						   Model model) throws Exception {
		
		return registerService.registerMember(userId, userName, passWd, age, gender, addr);
				
	}

}
