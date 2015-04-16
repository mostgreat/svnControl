<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/docType.jspf"%>
<%
    String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
    String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
            
%>
<html>
<head>
<title>Login Page</title>
<%@ include file="/common/include/head.jspf"%>
<link rel="stylesheet" type="text/css" href="http://cdn.axisj.com/axicon/axicon.min.css" />
<script type="text/javascript" src="<c:url value="/js/rsa/jsbn.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/rsa.js"  />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/prng4.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/rng.js"  />"></script>
</head>
<script type="text/javascript">

	function onload(){
		var result = '<c:out value="${loginResult }" />'; 
		if(result.length > 0){
			alert(result);
		}

	}
	
	function validateEncryptedForm() {
		
	    var username = document.getElementById("userId").value;
	    var password = document.getElementById("passWd").value;
	    
	    if (!username || !password) {
	        alert("ID/비밀번호를 입력해주세요.");
	        return false;
	    }

	    try {
	        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
	        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
	        submitEncryptedForm(username,password, rsaPublicKeyModulus, rsaPublicKeyExponent);
	        
	    } catch(err) {
	        alert(err);
	    }
	    return false;
	}

	function submitEncryptedForm(username, password, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
	    var rsa = new RSAKey();
	    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

	    // 사용자ID와 비밀번호를 RSA로 암호화한다.
	    var securedUsername = rsa.encrypt(username);
	    var securedPassword = rsa.encrypt(password);
	    
	    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
	    var securedLoginForm = document.getElementById("securedLoginForm");
	    securedLoginForm.securedUsername.value = securedUsername;
	    securedLoginForm.securedPassword.value = securedPassword;
	    
	    var url = '<c:url value="/login.do" />';
		var params = $('#securedLoginForm').serialize(); 
		
		$.ajax({
			type:"post"		// 포스트방식
			,url:url		// url 주소
			,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
			,dataType:"json"
			,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
				if(data.code != '1'){
					alert(data.result);
				}
				var url = '<c:url value="/login.do" />';
				window.location.href = url;				
			}
		    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
		    	
		    	alert(e.responseText);
		    }
		});	
	    
	}
	
	
</script>
<body onload="onload()">

	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">
				<!-- <h1 class="text-center login-title">Sign in Bootsnipp</h1> -->
				<div class="account-wall">

					<input type="text" id="userId" name="userId" class="form-control"
						placeholder="Email" required autofocus> <input
						type="password" id="passWd" name="passWd" class="form-control"
						placeholder="Password" required> <input type="hidden"
						id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" /> <input
						type="hidden" id="rsaPublicKeyExponent"
						value="<%=publicKeyExponent%>" />
						<button class="btn btn-lg btn-primary btn-block" type="button" onclick="validateEncryptedForm()">Sign in</button>
						<label class="checkbox pull-left"> <input type="checkbox"
							value="remember-me"> Remember me
						</label>  
						<span class="clearfix"></span>
					<form class="form-signin" action="<c:url value="/login.do"/>" method="post" id="securedLoginForm" name="securedLoginForm"> 
						<input type="hidden" name="securedUsername" id="securedUsername" value="" /> 
						<input type="hidden" name="securedPassword" id="securedPassword" value="" />
					</form>
				</div>
				<a href="<c:url value="/register?registerView"/>" class="text-center new-account">Create an account </a>
			</div>
		</div>
	</div>

</body>

</html>