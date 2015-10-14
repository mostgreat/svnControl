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
		    background: rgba(255,255,255,0.2); /*not in ie */
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
	var myTree = "";
	var myTree_target = "";
	var Tree = "";
	var Tree_target = "";
	
	var sourceUrl = "";
	var targetUrl = "";
	var sourceRevision = "";
	var targetRevision = "";
	
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
			
			Tree = "";
			myTree = new AXTree();
			
			$.ajax({
					type:"post"		// 포스트방식
					,url:url		// url 주소
					,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
					,dataType:"json"
					,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
						
						Tree = data.svnList;
						$('#svnRootDir').html('');
						for(var i = 0 ; i < data.rootList.length; i++){
							if(data.rootList[i] != 'trunk'){
								$('#svnRootDir').append('<option value=\"' + data.rootList[i] + '\">' + data.rootList[i] + '</option>');	
							}
						}
						
						$('#deployCheckdList').removeClass('display-none');
						$('#svnRootDir').removeClass('display-none');
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
		
		$("#readMore2").click(function () {
			
			var a = document.getElementById("svnUrl2").value;
		    var b = document.getElementById("svnUser2").value;
		    var c = document.getElementById("svnPassword2").value;
		    
		    if (!a || !b || !c) {
		        alert("Fill out every Input Condition");
		        return false;
		    }
			
			var params = $('#svnInfoForm2').serialize();
			var url = '<c:url value="/svn/login.do" />';
			Tree_target = "";
			myTree_target = new AXTree();
			
			
			$.ajax({
					type:"post"		// 포스트방식
					,url:url		// url 주소
					,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
					,dataType:"json"
					,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
						
						Tree_target = data.svnList;;
						fnObj2.pageStart.delay(0.1);
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
	
		$("#readDiff").click(function () {
			
			var params = { "sourceUrl"           : sourceUrl ,
						   "destinationUrl"      : targetUrl,
						   "sourceRevision"      : sourceRevision,
						   "destinationRevision" : targetRevision};
			
			var url = '<c:url value="/svn/getDiff.do" />';
			
			$.ajax({
					type:"post"		// 포스트방식
					,url:url		// url 주소
					,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
					,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
						$('#readDiffcontents').html('<xmp>' + data + '</xmp>');
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
		
		
	  $("#deployCheckdList").click(function () {
			
		    var result = confirm($('#svnRootDir option:selected').val() + '로 커밋하시겠습니까?');
		    if(!result)
		    	return false;
		  	var url = '<c:url value="/svn/deploy.do" />';
			var selectedList = myTree.getCheckedList(0);
			var params = "{ \"deploys\" : [";
			
			for(var i = 0 ; i < selectedList.length; i++){
				
				if(selectedList[i].file){
					if(params.indexOf("filePath") > -1){
						params += ",";	
					}
					//console.log(selectedList[i].path + selectedList[i].name + "," +  selectedList[i].revision);
					params += "{ \"filePath\" : \"" + selectedList[i].path
							 + "\" , \"fileName\" : \"" + selectedList[i].name 
							 + "\" , \"revision\" : \"" + selectedList[i].revision 
							 + "\" , \"file\" : \""     + selectedList[i].file + "\"}"
				}
			}
			
			params += "], \"deployDir\" : \"" + $('#svnRootDir option:selected').val() + "\" }";
			$.ajax({
					type:"post"		// 포스트방식
					,url:url		// url 주소
					,contentType: "application/json"
					,dataType:"json"
					,data: params
					,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
						alert(data.result);
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
		});
		
	});
	
	
	$.fn.sourceClick = function (item){
		
		var url = '<c:url value="/svn/getContent.do" />';
		
		var params = {"name" : item.name
				     ,"path": item.path}; 
		
		console.log(item.path + item.name + item.revision);
		
		$.ajax({
				type:"post"		// 포스트방식
				,url:url		// url 주소
				,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
				,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
					$('#sourceContent').html('<xmp>' + data + '</xmp>');
				}
			    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
			    	
			    	alert("error" + e.responseText);
			    }
			    ,beforeSend:function(){
			        $('.wrap-loading').removeClass('display-none');
			    }
				,complete:function(){
			        $('.wrap-loading').addClass('display-none');
			 
			    }
			});	
	};
	
	$.fn.sourceClick_target = function (item){
		
		var url = '<c:url value="/svn/getContent.do" />';
		var params = {"name" : item.name
				     ,"path": item.path}; 
		
		$.ajax({
				type:"post"		// 포스트방식
				,url:url		// url 주소
				,data:params	//  요청에 전달되는 프로퍼티를 가진 객체
				,success:function(data){	//응답이 성공 상태 코드를 반환하면 호출되는 함수
					$('#sourceContent_target').html('<xmp>' + data + '</xmp>');
				}
			    ,error:function(e) {	// 이곳의 ajax에서 에러가 나면 얼럿창으로 에러 메시지 출력
			    	
			    	alert("error" + e.responseText);
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
				xscroll:false,
				//fitToWidth:true, // 너비에 자동 맞춤
				//indentRatio:1,
				checkboxRelationFixed:true,
				reserveKeys:{
					parentHashKey:"pHash", // 부모 트리 포지션
					hashKey:"hash", // 트리 포지션
					openKey:"openStatus", // 확장여부
					subTree:"subTree", // 자식개체키
					displayKey:"display" // 표시여부
				},
				colGroup: [
					{
                        key:"chk", label:"checkbox", width:"30", align:"center", formatter:"checkbox", 
                        disabled:function(){
                            return false;
                        },
                        // checkbox 개체를 checked 된 상태로 만들기
                        checked:function(){
                        	return (this.item.chk == true)? true:false;
                        }
                    },
					{
						key:"name",
						label:"dir",
						width:"200", align:"left",
						indent:true,
						getIconClass: function(){
							//folder, AXfolder, movie, img, zip, file, fileTxt, fileTag
							var iconNames = "folder, AXfolder, movie, img, zip, file, fileTxt, fileTag".split(/, /g);
							var iconName = "file";
							if(this.item.type) iconName = iconNames[this.item.type];
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
							if(this.item.file){
							$.fn.sourceClick(this.item);
							sourceUrl = '/' + this.item.path + this.item.name;
							sourceRevision = this.item.revision;
							}
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
	
	var fnObj2 = {
			pageStart: function(){
				
				fnObj2.tree1();
				myTree_target.setTree(Tree_target);
				
			},
			tree1: function(){

			myTree_target.setConfig({
					targetID : "AXTreeTarget_target",
					theme: "AXTree",
					//height:"auto",
					//width:"auto",
					xscroll:true,
					fitToWidth:true, // 너비에 자동 맞춤
					indentRatio:1,
					reserveKeys:{
						parentHashKey:"pHash", // 부모 트리 포지션
						hashKey:"hash", // 트리 포지션
						openKey:"openStatus", // 확장여부
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
								var iconNames = "folder, AXfolder, movie, img, zip, file, fileTxt, fileTag".split(/, /g);
								var iconName = "file";
								if(this.item.type) iconName = iconNames[this.item.type];
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
							if(this.item.file){
								$.fn.sourceClick_target(this.item);
								targetUrl = '/' + this.item.path + this.item.name;
								targetRevision = this.item.revision;
								}
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
			<a href="<c:url value="/logout.do" />"><input type="button" value="Logout" class="AXButton Green" id="logout" name="logout" /></a>
			<hr>
			
			
			
			<section class="ax-layer-1">
						
				<div class="ax-col-5">
				<div class="ax-unit">
					<input type="button" value="View Source SVN" class="AXButton Red" id="readMore" name="readMore"/>
					<input type="button" value="Deploy" class="AXButton Red display-none" id="deployCheckdList" name="deployCheckdList"/>
					<select class="AXSelect display-none" id="svnRootDir" name="svnRootDir"> 
	                </select>
					<form id="svnInfoForm" name="svnInfoForm">
						<label>SVN Url</label>
						<input type="text" id="svnUrl" name="svnUrl" value="svn://wua.social:3690" class="AXInput" />
						<label>SVN User Name</label>
						<input type="text" id="svnUser" name="svnUser" value="mostgreat" class="AXInput" />
						<label>SVN Password</label>
						<input type="password" id="svnPassword" name="svnPassword" value="1234" class="AXInput" />
					</form>
					
					<table cellpadding="0" cellspacing="0" style="table-layout:fixed;width:100%;">
			                <tbody>
			                    <tr>
			                        <td>
			                            <div id="AXTreeTarget" style="height:500px;"></div>
			                        </td>
			                        
			                    </tr>
			                </tbody>
			            </table>
					
					<h2>Source Code</h2>
					<div id="sourceContent" style="height:400px; widows: 500px; overflow: auto;" ></div>
					
					</div>
			</div>
			
			<div class="ax-col-5">
					<input type="button" value="View Target SVN" class="AXButton Red" id="readMore2" name="readMore2"/>
					<form id="svnInfoForm2" name="svnInfoForm2">
						<label>SVN Url</label>
						<input type="text" id="svnUrl2" name="svnUrl" value="svn://wua.social:3690" class="AXInput" />
						<label>SVN User Name</label>
						<input type="text" id="svnUser2" name="svnUser" value="mostgreat" class="AXInput" />
						<label>SVN Password</label>
						<input type="password" id="svnPassword2" name="svnPassword" value="1234" class="AXInput" />
					</form>
					
					<table cellpadding="0" cellspacing="0" style="table-layout:fixed;width:100%;">
			                <tbody>
			                    <tr>
			                        <td>
			                            <div id="AXTreeTarget_target" style="height:500px;"></div>
			                        </td>
			                        
			                    </tr>
			                </tbody>
			            </table>
					
					<h2>Source Code</h2>
					<div id="sourceContent_target" style="height:400px; widows: 500px; overflow: auto;" ></div>
					
				</div>
				</div>
			</section>
			
			<div class="ax-col-10">
			<center>
				<input type="button" value="View Difference" class="AXButton Red" id="readDiff" name="readDiff"/>
			</center>
			</div>

			</div>
			
			<div class="ax-col-10">
				<div id="readDiffcontents" style="height:400px; widows: 500px; overflow: auto;" ></div>
			</div>

		</div>
	</div>
	<!-- e.AXPageBody -->
</div>	
</body>
</html>