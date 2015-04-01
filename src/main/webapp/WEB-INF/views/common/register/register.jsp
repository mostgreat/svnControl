<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/docType.jspf"%>
<%@ page isELIgnored ="false" %>
<html xmlns:form="http://www.springframework.org/tags/form">
<head>
<title>Where You At(WUA) Register Page</title>
<%@ include file="/common/include/head.jspf"%>
</head>
<script type="text/javascript">

	$( document ).ready(function() {
		
		$('#InputId').inputmask("d/m/y");
		
	});
	
	$.fn.getCityInfo = function(){
		
		var url = '<c:url value="/selectCommonCodeDetail.do" />';
		var params = { codeId : '00001'};
		
		$.ajax({
			type:"post"		// 포스트방식
			,url:url		// url 주소
			,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
			,dataType:"json"
			,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
				
				var htmlContent = '<div class="form-group"><label for="InputEmail">Enter Your City</label>';
				htmlContent += '<div class="input-group"><select class="form-control" id="InputCity" name="InputCity" placeholder="Enter City" required>';	
				
				for(var i = 0 ; i < data.length ; i++){
					htmlContent += '<option>';
					htmlContent += data[i].value;
					htmlContent += '</option>';
				}
				htmlContent += '</select> <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>';
				htmlContent += '</div></div>';
				
				$('#selectCity').html(htmlContent);
			}
		    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
		    	alert(e.responseText);
		    }
		});		
	};
	
	$.fn.checkSame = function (){
		var password      = document.getElementById("InputPassWd").value;
	    var confirmPassWd = document.getElementById("confirmPassWd").value;
	    
	    if(password == confirmPassWd){
	    	$('#confirmMessage').html('<font color="green"> Same with above Password.</font>');
	    }else{
	    	$('#confirmMessage').html('<font color="red"> Not Same with above Password. Please Type Again.</font>');
	    }
	};
	
	$.fn.registerMember = function () {
		
		var username      = document.getElementById("InputName").value;
	    var userId        = document.getElementById("InputId").value;
	    var password      = document.getElementById("InputPassWd").value;
	    var confirmPassWd = document.getElementById("confirmPassWd").value;
	    var age           = document.getElementById("InputAge").value;
	    var gender        = document.getElementById("InputGender").value;
	    var city          = document.getElementById("InputCity").value;
	    
	    if(password != confirmPassWd){
	    	alert("Please refill password field.");
			return false;
	    }
	    
		if (!username || !password || !userId || !confirmPassWd || !age || !gender || !city) {
			alert("Please Fill up Required Fields All.");
			return false;
		}
		
		
	    
		var url = '<c:url value="/register.do" />';
	    var params = $('#registerForm').serialize(); 
	    
	    $.ajax({
			type:"post"		// 포스트방식
			,url:url		// url 주소
			,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
			,dataType:"json"
			,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
				if(data == 1){
					alert("Register Success");
					var url = '<c:url value="/login.do" />';
					window.location.href = url;				
				}else{
					alert("Register Fail, Try Again");
					var url = '<c:url value="/register.do" />';
					window.location.href = url;	
				}
			}
		    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
		    	alert(e.responseText);
		    }
		});
		
	};


</script>
<body onload="$.fn.getCityInfo()">
	
	<div class="container">
    <div class="row">
        <%-- <form action="<c:out value="register.do" />" method="post" id="registerForm" name="registerForm"> --%>
        <form action="${flowExecutionUrl}&_eventId=registerCredentialsEntered" method="post" id="registerForm" name="registerForm">
            <div class="col-lg-6">
                <div class="well well-sm"><strong><span class="glyphicon glyphicon-asterisk"></span>(Required Field) <font color="red"> Please Fill up Required Fields All.</font></strong></div>
                
                <div class="form-group">
                    <label for="InputName">Enter Name</label>
                    <div class="input-group">
                        <input type="text" class="form-control" name="InputName" id="InputName" placeholder="Enter Name" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="InputEmail">Enter ID</label>
                    <div class="input-group">
                        <input type="email" class="form-control" id="InputId" name="InputId" placeholder="Enter E-mail" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="InputEmail">Enter Password</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="InputPassWd" name="InputPassWd" placeholder="Enter Password" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="InputEmail">Confirm Password</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="confirmPassWd" name="confirmPassWd" placeholder="Confirm Password" required onkeyup="$.fn.checkSame();">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                
                <div class="well well-sm">
                	<strong><span class="glyphicon glyphicon-asterisk"></span>(Password Check)<div id="confirmMessage" name="confirmMessage" ></div>
                	</strong>
                </div>
                
                <div class="form-group">
                    <label for="InputEmail">Enter Age</label>
                    <div class="input-group">
                        <input type="number" class="form-control" id="InputAge" name="InputAge" placeholder="Enter Age" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="InputEmail">Enter Gender</label>
                    <div class="input-group">
                        <!-- <input type="text" class="form-control" id="InputGender" name="InputGender" placeholder="Enter Gender" required> -->
                        <select class="form-control" id="InputGender" name="InputGender" placeholder="Enter Gender" required>
						  <option>M</option>
						  <option>F</option>
						</select>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div id="selectCity"/>
            </div>
            <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
        </form>
        <input type="button" value="Submit" class="btn btn-info pull-right" onclick="$.fn.registerMember()">
    </div>
</div>
</body>
</html>