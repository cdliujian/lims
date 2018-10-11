var path = document.getElementsByTagName("base")[0].href;  
if(path){
	path=path.split("://")[1]; 
}
var websocket;  
var heartTimer;
 
var initWs=function(){ 
    if ('WebSocket' in window) {  
        websocket = new WebSocket("wss://" + path + "bi/ws");  
    } else if ('MozWebSocket' in window) {  
        websocket = new MozWebSocket("wss://" + path + "bi/ws");  
    } else {  
        websocket = new SockJS("https://" + path + "bi/ws/sockjs");  
    }  
	
    websocket.onopen = function(event) {  
        console.log("WebSocket:已连接");  
    	//heartTimer=setInterval(function () {
        //	keepHeart()
      //  }, 150000);    //150s发送一次心跳 150000 
    };  
    websocket.onmessage = function(event) {   
    	if(!heartPackage(event.data)){
    		showMessage(event.data);
    	} 
    };  
    websocket.onerror = function(event) {  
        console.log("WebSocket:发生错误 ");  
        console.log(event)  
    };  
    websocket.onclose = function(event) {  
        console.log("WebSocket:已关闭"); 
    } 
}

function keepHeart(){
   if(websocket==null || websocket.readyState !== 1){
      if(websocket!=null){
    	  websocket.close(); 
	  }
      initWs();
    } 
    websocket.send('{"c":"~"}'); 
} 
   
function heartPackage(packageMsg){
   if(packageMsg==null){
	   return false;
   }
   return packageMsg=='{"c":"~"}';
}
    
 function showMessage(data){
	 var dataJson = JSON.parse(data);  
	 if(dataJson.type==0){  //文本消息
		 if(dataJson.message){ 
			
		 } 
	 }else{
		 if(dataJson.message){
			 if(dataJson.message.grade==1){   //加急
				 bombNotice(dataJson.message); 
			 }else{
				 if(dataJson.message instanceof Array && dataJson.pro=='y'){
					// userMessagePro(dataJson); 
				 }else{
					 //调用子页面的方法 渲染消息列表
//					 window.frames["name_ifr_notice"].innerNotices(dataJson); 
					 addNoticePro(dataJson);
				 } 
			 }
		 }
	 }
 }
 
