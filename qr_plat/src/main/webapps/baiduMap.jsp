	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Hello, World</title>
    <style type="text/css">
    html{height:100%}
    body{height:100%;margin:0px;padding:0px}
    #container{height:100%}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
    </head>
     
    <body>
    <div>
    	</br>
    	模糊搜索 (北京限定):
	    <input type="text" id="searchKeyword"/><input value="搜索" type="button" onclick="baiduMapSearch();" />
	    </br>
	        <span style="color:red;">得到搜索结果后，请点击地图上的所需位置, 以获得坐标</span>
	    </br>
	    <input type="button" onclick="map.clearOverlays();mLocalSearch.clearResults();" value="清除地图标记" />
	    </br>
    </div>
    <div id="container" style="width:78%;height:100%;float:left"></div>
    <div id="searchResult" style="width:18%;height:100%;float:left"></div>
    </body>
    </html>
    
    <script type="text/javascript">
    var map = new BMap.Map("container");  // 创建地图实例
    map.centerAndZoom("北京",12);  // 初始化地图,设置城市和地图级别。
    map.enableScrollWheelZoom();
    map.addControl(new BMap.NavigationControl());  //添加默认平移缩放控件
    
    var marker = new BMap.Marker(new BMap.Point(map.getCenter().lng, map.getCenter().lat));  // 创建当前中心点标注
    var gc = new BMap.Geocoder();
    
    var options = {renderOptions: {map: map, panel: "searchResult"}};
    var mLocalSearch = new BMap.LocalSearch(map,options);
    
    map.addEventListener("click", function(e){
    	var lng = e.point.lng;
    	var lat = e.point.lat;
    	
    	marker.hide();
    	marker = new BMap.Marker(new BMap.Point(lng, lat));
    	map.addOverlay(marker);  // 将标注添加到地图中
        marker.setAnimation(BMAP_ANIMATION_BOUNCE);  //跳动的动画
        //marker.enableDragging();  //可拖拽
    	
        gc.getLocation(e.point, function (rs) {
        	var addressComp = rs.addressComponents;
        	var infoWindow = new BMap.InfoWindow(addressComp.province + "  " + addressComp.city + "  " + addressComp.district + "  " + addressComp.street + "  " + addressComp.streetNumber);
        	marker.openInfoWindow(infoWindow);
        });
        
    	parent.document.getElementById("requestUrl_7_1").value = lng;
    	parent.document.getElementById("requestUrl_7_2").value = lat;
    	
    	marker.show();
    });
    
    function baiduMapSearch() {
    	var keyword = document.getElementById("searchKeyword").value;
    	mLocalSearch.search(keyword);
    }
	</script>