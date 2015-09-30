package gz.test.mostgreat2.common.login.controller;

import gz.test.mostgreat2.common.login.service.LoginService;
import gz.test.mostgreat2.common.session.sessionVo.SessionVo;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {

	@Resource(name = "loginService")
	private LoginService loginService;

	public static final int KEY_SIZE = 1024;

	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest req) throws Exception {

		HttpSession sess = req.getSession();
		SessionVo sessVo = (SessionVo) sess.getAttribute("loginUser");

		if (sessVo == null) {
			logger.debug("You are not logged at the moment. no session");

			try {
				KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(KEY_SIZE);

				KeyPair keyPair = generator.genKeyPair();
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");

				PublicKey publicKey = keyPair.getPublic();
				PrivateKey privateKey = keyPair.getPrivate();
				
				sess.setAttribute("__rsaPrivateKey__", privateKey);
 
				RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

				String publicKeyModulus = publicSpec.getModulus().toString(16);
				String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
				
				req.setAttribute("publicKeyModulus", publicKeyModulus);
				req.setAttribute("publicKeyExponent", publicKeyExponent);

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage(), ex);
			}

			return "common/login/login";
		} 
		
		logger.debug(sessVo.getUserId() + " is being logged");
		return "common/main/main";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> login(HttpServletRequest req,
			@RequestParam("securedUsername") String userId,
			@RequestParam("securedPassword") String passWd, Model model)
			throws Exception {

		HttpSession sess = req.getSession(false);
		
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("code", "");

		PrivateKey privateKey = (PrivateKey) sess.getAttribute("__rsaPrivateKey__");
		sess.removeAttribute("__rsaPrivateKey__"); 
		
		if (privateKey == null) {
			logger.debug("No Private Key");
			sess.invalidate();
			result.put("result", "Please Try Again.");
			return result;
		}
		
		String username = decryptRsa(privateKey, userId);
		String password = decryptRsa(privateKey, passWd);
		
		Map resultLogin = loginService.readMember(username, password);

		logger.debug("====Login Result==== : " + resultLogin);

		/*if (resultLogin == null) {
			
			logger.debug("Id와 Password가 맞지 않습니다.");
			result.put("result", "Id/Password is wrong, Please Try Again.");
			return result;
		}*/

		logger.debug("user Id = " + userId);
		logger.debug("user password = " + passWd);

		SessionVo sessVo = new SessionVo();

		sessVo.setUserId(userId);
		sessVo.setPassWd(passWd);

		sess.setAttribute("loginUser", sessVo);

		result.put("result", "success");
		result.put("code", "1");
		return result;
	}

	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest req) {

		HttpSession sess = req.getSession(false);

		if (sess == null) {

		} else {
			sess.invalidate();
		}

		return "redirect:/login.do";
	}

	private String decryptRsa(PrivateKey privateKey, String securedValue)
			throws Exception {
		logger.debug("will decrypt : " + securedValue);
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] encryptedBytes = hexToByteArray(securedValue);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		String decryptedValue = new String(decryptedBytes, "utf-8"); 
		return decryptedValue;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

	@Deprecated
	public static byte[] hexToByteArrayBI(String hexString) {
		return new BigInteger(hexString, 16).toByteArray();
	}

	public static String base64Encode(byte[] data) throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = encoder.encode(data);
		return encoded;
	}

	public static byte[] base64Decode(String encryptedData) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decoded = decoder.decodeBuffer(encryptedData);
		return decoded;
	}

}
