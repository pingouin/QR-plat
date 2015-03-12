<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=yes" />
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/izzyColor.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
<title>二维码定制生成服务</title>

<script>
function encode(encodeTag) {
	//无法编码一些特殊字符，在这里采用encodeURIComponent方法
	var encodeUrl = "";
	if (encodeTag == "anything") {
		encodeUrl = document.getElementById('requestUrl_1').value;
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "website") {
		encodeUrl = document.getElementById('requestUrl_2').value;
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "mail") {
		encodeUrl = "mailto:" + document.getElementById('requestUrl_3').value;
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "namecard") {
		encodeUrl = "MECARD:N:" + document.getElementById('requestUrl_4_1').value + 
		";ADR:" + document.getElementById('requestUrl_4_2').value + 
		";TEL:" + document.getElementById('requestUrl_4_3').value + 
		";ORG:" + document.getElementById('requestUrl_4_4').value + 
		";DIV:" + document.getElementById('requestUrl_4_5').value + 
		";TIL:" + document.getElementById('requestUrl_4_6').value + 
		";EMAIL:" + document.getElementById('requestUrl_4_7').value + 
		";URL:" + document.getElementById('requestUrl_4_8').value + 
		";NOTE:QQ:" + document.getElementById('requestUrl_4_9').value + ";;";
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "sms") {
		encodeUrl = "smsto:" + document.getElementById('requestUrl_5_1').value + 
		":" + document.getElementById('requestUrl_5_2').value;
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "phonenumber") {
		encodeUrl = "tel:" + document.getElementById('requestUrl_6').value;
		encodeUrl = encodeURIComponent(encodeUrl);
	} else if (encodeTag == "geo") {
		//取坐标使用百度地图，生成google地图链接，因此需要修正坐标偏差
		var logdeviation = 1.0000568461567492425578691530827;//经度偏差 
		var latdeviation = 1.0002012762190961772159526495686;//纬度偏差 
		//var lng = document.getElementById('requestUrl_7_2').value * logdeviation;
		//var lat = document.getElementById('requestUrl_7_1').value * latdeviation;
		//直接生成百度地图链接
		var lng = document.getElementById('requestUrl_7_1').value;
		var lat = document.getElementById('requestUrl_7_2').value;
		//encodeUrl = "http://maps.google.com/?maps?f=q&" + "q=" + lng + "," + lat;
		encodeUrl = "http://api.map.baidu.com/geocoder?location=" + lat + "," + lng + 
			escape("&") + "output=html";
		//encodeUrl = encodeURIComponent(encodeUrl);
	}
	
	var encodeColor = document.getElementById('requestColor').value;
	if (encodeColor.charAt(0) == "#") {encodeColor = encodeColor.substr(1)};
    var encodeLogo = document.getElementById('requestLogo').value;
    var encodeWidth = document.getElementById('encodeWidth').value;
    var encodeHeight = document.getElementById('encodeHeight').value;
    
    var encodeImg = document.getElementById('encodeImg');
    
    if (encodeWidth == "" || encodeWidth == null) {encodeWidth = 300;}
    if (encodeHeight == "" || encodeHeight == null) {encodeHeight = 300;}
    
    if (encodeUrl == "" || encodeUrl == null) {
    	alert("please enter encode content");
    } else {
    	encodeImg.src = "<%= request.getContextPath()%>/" + 
        "generateQRCode.do?requestUrl=" + encodeUrl +
        "&encodeWidth=" + encodeWidth +
        "&encodeHeight=" + encodeHeight +
        "&encodeColor=" + encodeColor +
        "&encodeLogo=" + encodeLogo;
        
        encodeImg.width = encodeWidth;
        encodeImg.height = encodeHeight;
    }
}
</script>

 	<script language="javascript" type="text/javascript">
    function checkFileFormat(){
        //获取id为logoFile的input的值
        var fileName = document.getElementById("logoFile").value;
        //截取文件后缀名
        var fileSuffix = fileName.substr(fileName.length-3);
        //在这里进行判断，如果上传的文件类型不是你所规定的文件后缀类型，则返回false，否则return或者什么也不写，会提交到后台
        if(fileSuffix != "jpg" && fileSuffix != "png" && fileSuffix != "gif"){
            alert("您上传的文件类型不被允许，请重传，只允许上传.png/.jpg/.gif 文件");
            return false;
        }
    }
    </script>
</head>

<style type="text/css">
	html{height:100%}
    body{height:100%;margin:0px;padding:0px}
    #baiduMap{width:90%;height:30%}

	.box {margin:20px;}
	.sub {display:none;}
	.current-sub {display:block;}
	.nav ul li {display:inline-block; float:left; margin-left:20px;}
	.nav ul li a {display:block;}
	.nav ul li a:hover {background:#CCDDCC;}
	.nav ul li a:current {z-index:9999; border-bottom:1px solid #FFF;}
	
	.container button {width:85px; height:35px;}
</style>

<body>
<!-- runs two times, dont know why -->
    <!-- <form name="myform" action="generateQRCode" method="get" enctype="multipart/form-data"> -->
    <div class="box">
    	<div class="nav">
	    	<ul>
	    		<li><a href="javascript:void(0)" class="current"><b>文本</b></a></li>
	    		<li><a href="javascript:void(0)"><b>网址</b></a></li>
	    		<li><a href="javascript:void(0)"><b>电子邮件</b></a></li>
	    		<li><a href="javascript:void(0)"><b>名片</b></a></li>
	    		<li><a href="javascript:void(0)"><b>短信</b></a></li>
	    		<li><a href="javascript:void(0)"><b>电话</b></a></li>
	    		<li><a href="javascript:void(0)"><b>地理位置</b></a></li>
	    	</ul>
    	</div>
    	<br/><br/><br/>
	    <div class="container" style="background-color:#E6E6FA">
	    	<div class="sub current-sub">
	    		<B>任意字符串 (没有前缀)</B>
	    		<br/><br/>
	       		字符:<br/><br/>
	       		<input type="text" id="requestUrl_1" /><br/><br/>
	       		<button type="button" onclick="encode('anything')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>网址</B>
	    		<br/><br/>
	    		URL:<br/><br/>
	       		<input type="text" id="requestUrl_2" value="http://" /><br/><br/>
	       		<button type="button" onclick="encode('website')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>电子邮件</B>
	    		<br/><br/>
	    		邮箱地址:<br/><br/>
	       		<input type="text" id="requestUrl_3" /><br/><br/>
	       		<button type="button" onclick="encode('mail')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>名片</B>
	    		<br/><br/>
	    		姓名:<input type="text" id="requestUrl_4_1" />地址:<input type="text" id="requestUrl_4_2" />电话:<input type="text" id="requestUrl_4_3" />
	    		<br/>
	    		单位:<input type="text" id="requestUrl_4_4" />部门:<input type="text" id="requestUrl_4_5" />职位:<input type="text" id="requestUrl_4_6" />
	    		<br/>
	    		电子邮箱:<input type="text" id="requestUrl_4_7" />网址:<input type="text" id="requestUrl_4_8" />QQ:<input type="text" id="requestUrl_4_9" />
	       		<br/><br/>
	       		<button type="button" onclick="encode('namecard')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>短信</B>
	    		<br/><br/>
	    		号码:<input type="text" id="requestUrl_5_1" />
	    		<br/>
	    		短信内容:<textarea type="text" id="requestUrl_5_2"></textarea>
	    		<br/><br/>
	       		<button type="button" onclick="encode('sms')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>电话</B>
	    		<br/><br/>
	    		号码:<br/><br/>
	       		<input type="text" id="requestUrl_6" /><br/><br/>
	       		<button type="button" onclick="encode('phonenumber')">编码生成</button>
	    	</div>
	    	<div class="sub">
	    		<B>地理位置</B>
	    		<br/>
	    		(暂不支持火狐浏览器)
	    		<br/><br/>
	    		经度:<input type="text" id="requestUrl_7_2" />纬度:<input type="text" id="requestUrl_7_1" />
	    		<br/><br/>
	    		点击地图可获得当前坐标
	    		<br/><br/>
	       		<button type="button" onclick="encode('geo')">编码生成</button>
	       		<br/><br/>
	       		<iframe src="baiduMap.jsp" width="100%" height="400px" ></iframe>
	    	</div>
	    </div>
    </div>
    
        <br/>
        <div>
       		编码宽度:<br/>
	       	<input type="text" id="encodeWidth" /><br/>
	       	编码高度:<br/>
	       	<input type="text" id="encodeHeight" /><br/>
	       	编码颜色: (点击可取色)<br/>
	     	<!-- <input type="text" id="requestColor" class="color {required:false,pickerPosition:'right'}" /><br/> -->
	     	<input type="text" id="requestColor" class="izzyColor" /><br/>
	       	Logo网络地址:(图片大小及格式须符合要求)<br/>
	       	<input type="text" id="requestLogo" /><br/>
	       	<form name="myform" action="upload.do" method="post" enctype="multipart/form-data" target="uploadIframe" onsubmit="return checkFileFormat()" >
	       		上传Logo图片:(图片大小及格式须符合要求)<br/>
	      		<input type="file" name="logoFile" id="logoFile" /><br/>(小于2M)
	      		<input type="submit" name="submit" value="确定上传" />
	       	</form>
	       	<br/>
	       	是否上传成功：<br/>
	       	<iframe name="uploadIframe" width="30%" height="50px" scrolling="true" ></iframe>
	       	<br/>
	       	<br/>
        </div>
        <div style="background-color:#E6E6FA" >
       		<img src="" id="encodeImg" height="" width="" />
        </div>
       <!-- <input type="submit" name="submit" value="Commit"> -->
    <!-- </form> -->
</body>

<script type="text/javascript">
	$(document).ready(function() {
		var intervalId;
		var currentLi;
		$(".nav li a").mouseover(function() {
			currentLi = $(this);
			intervalId = setInterval(onMouseOver, 100);
		});
		
		function onMouseOver() {
			$(".current-sub").removeClass("current-sub");
			$(".sub").eq($(".nav li a").index(currentLi)).addClass("current-sub");
			$(".current").removeClass("current");
			currentLi.addClass("current");
		}
		
		$(".nav li a").mouseout(function() {
			clearInterval(intervalId);
		});
		
		$(".nav li a").click(function() {
			clearInterval(intervalId);
			$(".current-sub").removeClass("current-sub");
			$(".sub").eq($(".nav li a").index(currentLi)).addClass("current-sub");
			$(".current").removeClass("current");
			currentLi.addClass("current");
		});
	});
</script>

</html>
