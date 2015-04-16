<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/docType.jspf"%>
<%
/* =================================================================
 * 
 * 작성일 : 
 *  
 * 작성자 : 장현석
 * 
 * 상세설명 : 
 *   
 * =================================================================
 * 수정일         작성자             내용     
 * -----------------------------------------------------------------------
 * 
 * =================================================================
 */ 
%>
<html lang="en">
<head>
<title>SVN Explore</title>
<%@ include file="/common/include/head.jspf"%>

<!-- css block -->
<link rel="stylesheet" type="text/css" href="<c:url value="/css/axis/page.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/axis/AXJ.min.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/axis/AXInput.css" />">
<style type="text/css" >
		.wrap-loading
		{ /*화면 전체를 어둡게 합니다.*/
		    position: fixed;
		    left:0;
		    right:0;
		    top:0;
		    bottom:0;
		    background: rgba(0,0,0,0.2); /*not in ie */
		    filter: progid:DXImageTransform.Microsoft.Gradient(startColorstr='#20000000', endColorstr='#20000000');    /* ie */
		}
	    
	    .wrap-loading div{ /*로딩 이미지*/
		        position: fixed;
		        top:50%;
		        left:50%;
		        margin-left: -21px;
		        margin-top: -21px;
		    }
		.display-none{ /*감추기*/
		        display:none;
		}
		.AXInput{

		}
				
        
</style>

<script type="text/javascript" src="<c:url value="/js/axis/AXJ.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="/js/axis/AXTree.js" />" ></script>
<script type="text/javascript" src="<c:url value="/js/axis/AXInput.js" />" ></script>
</head>
<script type="text/javascript">
	var pageID = "AXTree";
	var myTree = new AXTree();
	var Tree = "";
	
	$( document ).ready(function() {
		$("#readMore").click(function () {
			
			var a = document.getElementById("svnUrl").value;
		    var b = document.getElementById("svnUser").value;
		    var c = document.getElementById("svnPassword").value;
		    
		    if (!a || !b || !c) {
		        alert("Fill out every Input Condition");
		        return false;
		    }
			
			var params = $('#svnInfoForm').serialize();
			var url = '<c:url value="/svn/login.do" />';
			
			$.ajax({
					type:"post"		// 포스트방식
					,url:url		// url 주소
					,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
					,dataType:"json"
					,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
						
						Tree = data;
						fnObj.pageStart.delay(0.1);
					}
				    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
				    	
				    	alert(e.responseText);
				    }
				    ,beforeSend:function(){
				        $('.wrap-loading').removeClass('display-none');
				    }
					,complete:function(){
				        $('.wrap-loading').addClass('display-none');
				 
				    }
					,timeout:100000 
				});	
			});
	});
	
	$.fn.sourceClick = function (item){
		
		var url = '<c:url value="/svn/getContents.do" />';
		var params = {"inputData" : item}; 
		
		$.ajax({
				type:"post"		// 포스트방식
				,url:url		// url 주소
				,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
				,dataType:"json"
				,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
					alert(data);
				}
			    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
			    	
			    	alert(e.responseText);
			    }
			    ,beforeSend:function(){
			        $('.wrap-loading').removeClass('display-none');
			    }
				,complete:function(){
			        $('.wrap-loading').addClass('display-none');
			 
			    }
			});	
	};
		
	var fnObj = {
		pageStart: function(){

			fnObj.tree1();
			myTree.setTree(Tree);
			
		},
		tree1: function(){

			myTree.setConfig({
				targetID : "AXTreeTarget",
				theme: "AXTree",
				//height:"auto",
				//width:"auto",
				xscroll:true,
				fitToWidth:true, // 너비에 자동 맞춤
				indentRatio:1,
				reserveKeys:{
					parentHashKey:"pHash", // 부모 트리 포지션
					hashKey:"hash", // 트리 포지션
					openKey:"open", // 확장여부
					subTree:"subTree", // 자식개체키
					displayKey:"display" // 표시여부
				},
				colGroup: [
					{
						key:"name",
						label:"dir",
						width:"200", align:"left",
						indent:true,
						getIconClass: function(){
							//folder, AXfolder, movie, img, zip, file, fileTxt, fileTag
							//var iconNames = "folder, AXfolder, movie, img, zip, file, fileTxt, fileTag".split(/, /g);
							var iconName = "file";
							//if(this.item.type) iconName = iconNames[this.item.type];
							return iconName;
						},
						formatter:function(){
							return "<u>" + this.item.name + "</u>";
						},
						tooltip:function(){
							return this.item.name;
						}
					}
					,{key:"author", label:"commiter", width:"70", align:"center"}
					,{key:"revision", label:"revision", width:"50", align:"center"}
					,{key:"date", label:"date", width:"100", align:"center"}
				],
				colHead: {
					display:true
				},
				body: {
					onclick:function(idx, item){
						//toast.push(Object.toJSON(this.item));
						$.fn.sourceClick(this.item);
					},
					addClass: function(){
						// red, green, blue, yellow
						// 중간에 구분선으로 나오는 AXTreeSplit 도 this.index 가 있습니다. 색 지정 클래스를 추가하는 식을 넣으실때 고려해 주세요.
						/*
						if(this.index % 2 == 0){
							return "green";
						}else{
							return "red";
						}
						*/						
					}
				}
			});

		}
	};
</script>
<body>
<div class="wrap-loading display-none">
    <div><img src="<c:url value="/gif/loading.gif" />" /></div>
</div>   

<div id="AXPage">
	
	<!-- s.AXPageBody -->
	<div id="AXPageBody" class="SampleAXSelect">
        <div id="demoPageTabTarget" class="AXdemoPageTabTarget"></div>
		<div class="AXdemoPageContent">
			<div class="title"><h1>SVN Explorer</h1></div>
			<input type="button" value="View SVN" class="AXButton Red" id="readMore" name="readMore"/>
			<a href="<c:url value="/logout.do" />"><input type="button" value="Logout" class="AXButton Green" id="logout" name="logout" /></a>
			<hr>
			<section class="ax-layer-1">
				<div class="ax-col-3">
					<div class="ax-unit">
						<h2>SVN Information</h2>
						<form id="svnInfoForm" name="svnInfoForm">
							<label>SVN Url</label>
							<input type="text" id="svnUrl" name="svnUrl" value="http://wua.social:7070/subversion" class="AXInput" />
							<label>SVN User Name</label>
							<input type="text" id="svnUser" name="svnUser" value="test" class="AXInput" />
							<label>SVN Password</label>
							<input type="text" id="svnPassword" name="svnPassword" value="1234" class="AXInput" />
						</form>
					</div>	
				</div>		
				<div class="ax-col-10">
					<div class="ax-unit">
						<table cellpadding="0" cellspacing="0" style="table-layout:fixed;width:100%;">
			                <tbody>
			                    <tr>
			                        <td>
			                            <div id="AXTreeTarget" style="height:600px;"></div>
			                        </td>
			                        <td>
			                            <div id="contents" style="height:600px;"></div>
			                        </td>
			                    </tr>
			                </tbody>
			            </table>
		            </div>
				</div>
			</section>
			
			<div class="H10"></div>
            

		</div>
	</div>
	<!-- e.AXPageBody -->
</div>	
</body>
</html>