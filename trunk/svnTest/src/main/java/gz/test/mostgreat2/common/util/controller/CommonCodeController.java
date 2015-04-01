package gz.test.mostgreat2.common.util.controller;

import gz.test.mostgreat2.common.util.service.CommonCodeService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CommonCodeController {
	
	@Resource(name="commonCodeService")
	private CommonCodeService commonCodeService;
	
	private static final Logger logger = LoggerFactory.getLogger(CommonCodeController.class);
	
	@RequestMapping(value = "/selectCommonCode.do")
	public @ResponseBody List selectCommonCode( 
			@RequestParam("codeId") String codeId,
			Model model
			)	{
		
		logger.debug("selectCommonCode");
		return commonCodeService.selectCommonCode(codeId);

	}
	
	@RequestMapping(value = "/selectCommonCodeDetail.do")
	public @ResponseBody List selectCommonCodeDetail(
			@RequestParam("codeId") String codeId,
			Model model
			)	{
		
		logger.debug("selectCommonCodeDetail");
		return commonCodeService.selectCommonCodeDetail(codeId);
	}
	
}
