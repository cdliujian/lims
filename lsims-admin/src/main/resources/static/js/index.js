var basePath=document.getElementsByTagName("base")[0].href;
var tabCount=0; 


$(document).ready(function() { 
	if(!isTJsCall()){
		$("#btn_min").addClass("am-hide");
		$("#btn_exit").addClass("am-hide"); 
	}   
	
    // 移除标签页
	$('#pageTabs').find('.am-tabs-nav').on('click', '.tab-close', function() {
    	var $tab = $('#pageTabs');
        var $nav = $tab.find('.am-tabs-nav');   //tab页容器
        var $bd = $tab.find('.am-tabs-bd');     //iframe页容器
        var $item = $(this).closest('li');
        var index = $nav.children('li').index($item); 
        $item.remove(); 
        $bd.find('.am-tab-panel').eq(index).remove();
        
        var newIndex=index > 0 ? index - 1 : index + 1; 
        $tab.tabs('open', newIndex);
        $bd.find('.am-tab-panel').eq(newIndex).addClass("am-active"); 
        $bd.find('.am-tab-panel').eq(newIndex).parent().addClass("am-active"); 
        $tab.tabs('refresh');
        tabCount--;
    });
	
	 $("#modifyLoginPWDClose").click(
		 function(){
		     $(".am-field-error").each(function(index, item) {
		         $(item).popover('close');
		      });
		        	
		      closeModifyLoginPWD();
		    }		
	 );
	 $("#modifyPwdCommit").click(function() {
		$.when($('#modifyLoginPWDForm').validator('isFormValid')).then(
		    function() {
		    	modifyCommit();
		    }, 
		    function() {
		       // 验证错误的逻辑
		       $(".am-field-error").eq(0).focus();
		       $(".am-field-error").each(function(index, item) {
		           $(item).popover('close');
		       });
		        $(".am-field-error").eq(0).popover('open');
		 })
	  });
	 ajax("api/smComponentService/userRes",JSON.stringify({"kv":" " }),null,null,buildMenu); 
     //window.setTimeout('initWs()',900);  
	 window.setTimeout('passwordValidator()',700); 
	 window.setTimeout(function(){
		var userInfo= ajaxSync("api/currentUser",null);  
	    var userHtml=template("tplunameInfo", {"userName" : userInfo.cname});
		$("#userInfo").html(userHtml);  
		if(userInfo.dept){
			var u_dept_code=null;
			if(userInfo.dept.code){
				u_dept_code=userInfo.dept.code; 
				$("#divHyh").show();
				$("#hycode").text(u_dept_code); 
			}  
			var deptName=userInfo.deptFullName; 
			var deptHtml=template("tpldeptInfo", {"deptName":deptName}); 
			$("#deptInfo").html(deptHtml);
			var floorLineNums= Math.floor(deptName.length/12);
			//$("#deptInfo").parent().parent().height(42+floorLineNums*25);
			//生成二维码
			var url = document.getElementsByTagName("base")[0].href + "web/marketing/Guide.jsp?" + getParam();
			$("#qrcode").empty();
	    	$("#qrcode").qrcode({width:136,height:122,text:url});
		} 
	},1000); 
	window.setTimeout('loadNoticePro()',1700);  
	/**window.setTimeout(function() {
		// Set up tour
	    $('body').pagewalkthrough({
	        name: 'introduction',
	        steps: [{
	        	wrapper: '#userInfo',
	            popup: {
	                content: '#walkthrough-2',
	                type: 'tooltip',
	                position: 'bottom'
	            }
	        }]
	    });

	    // Show the tour
    	$('body').pagewalkthrough('show');
    }, 2000);  */
//	window.setTimeout(function() {
//		var data1 = ajaxSync("bi/MarketingHbSendinfoAction/queryHbByUserId",null);
//		if(data1 != null && data1.status == 1) {
//	    	$("#pageWindow1").modal({width: "406", height: "526",closeViaDimmer: false});
//	    	$("#pageWindow1").modal("open");
//	    	
//	    	$("#qrcode1").empty();
//	    	$("#qrcode1").qrcode({width:245,height:245,text:data1.message});
//		}
//    }, 2500)
   
});


function getParam(){
	var data = ajaxSync("bi/AppOrgRegisterAction/createUrl",null);
	if(data == null) {
		return "";
	}
		
	var param = "promoterId=" + data;
	return param;
	
}

/**
 * 关闭修改密码的窗口
 * @returns
 */
function closeModifyLoginPWD(){
	$("#modifyOrginPwd").val("");
	$("#modifyNewPwd").val("");
	$("#modifyNewPwdAgain").val(""); 
	
	var $modal = $('#modifyLoginPWD');
    if($modal){
	    $modal.modal('close');
    }
}
 
function buildMenu(menuTree){
	var menuHtml=template("tplMenuTree", {"menus" : menuTree});
	$("#menuTree").html(menuHtml);  
	firstTab(); 
}
 
function addTab(title,url,code,$this,evt) {  
	if(evt){
		evt.stopPropagation();
	}  
	url = 'template/wait?targetUrl='+encodeURIComponent(url);
	 
	
    if(isCurrent(code)){
    	$("#name_ifr_"+code).attr("src",url);   //重新加载下页面，防止历史数据
    	return;
    }
    var $tab = $('#pageTabs'); 
    var $nav = $tab.find('.am-tabs-nav'); 
     
	if(isOpen(code)){   //已经打开 
		$tab.find('.am-tabs-bd').find('.am-active').find('div').removeClass("am-active");
		$tab.find('.am-tabs-bd').find('.am-active').removeClass("am-active");
	    $nav.find('.am-active').removeClass("am-active");
		$('#ifr-'+code).addClass("am-active"); 
		$("#ifr-pdiv-"+code).addClass("am-active"); 
 		$nav.find('#tab-'+code).addClass("am-active"); 
 		menuSelectCss($this);
 		$("#name_ifr_"+code).attr("src",url); //重新加载下页面，防止历史数据 
		return; 
	}
	
	if(tabCount>10){
		error_msg("您打开的页面过多，请先关闭其他页面"); 
		return;
	}
	
    var $bd = $tab.find('.am-tabs-bd');  
    $bd.find('.am-active').find('div.am-active').removeClass("am-active");
    $bd.find('.am-active').removeClass("am-active");
    $nav.find('.am-active').removeClass("am-active");
    var nav = template("tplTabLable", {"code" : code,"name":title}); 
    var content=template("tplTabContent", {"url": url,"code" : code}); 
    $nav.append(nav);
    $bd.append(content); 
    $tab.tabs('refresh');
    menuSelectCss($this); 
    $("#ifr-pdiv-"+code).css('height',$(document).height()-68); 
    tabCount++; 
}
/**
 * 菜单选中样式处理
 * @returns
 */
function menuSelectCss(obj){
	var  menus=$("ul.menu-list li");
	for(var i=0;i<menus.length;i++){
		var $menuItem=$(menus[i]);
		if($menuItem.hasClass("menu-active")){
			$menuItem.removeClass("menu-active")
		}
	}
	$(obj).addClass("menu-active"); 
}


/**
 * 判断菜单是否打开
 * @param code
 * @returns
 */
function isOpen(code){
	return $('#pageTabs').find("#tab-"+code).length>0;
}
/**
 * 判断是否当前菜单
 * @returns
 */
function isCurrent(code){
	return $('#pageTabs').find('.am-tabs-nav').find('#tab-'+code).hasClass('am-active')>0;
}

function oprTreeTop(ele,event){
	if($(ele).hasClass("am-active")){
		$(ele).removeClass("am-active");
		$(ele).find("dd").removeClass("am-in"); 
	}else{
		$(ele).siblings().each(
		   function(){
			   if($(this).hasClass("am-active")){
				   $(this).removeClass("am-active");
				   $(this).find("dd").removeClass("am-in"); 
				}
		   }		
		);   
		$(ele).addClass("am-active");
		$(ele).find("dd").addClass("am-in");
	}
	event.stopPropagation();
}

function collapse(evt,eleId,$this){
	evt.stopPropagation(); 
	if(!$($this).hasClass("am-active")){ 
		//收起打开的兄弟节点
		var silbs=$($this).parent().parent().parent().parent().siblings();
		for(var i=0;i<silbs.length;i++){
			var $_e=$(silbs[i]).find('h4.am-active');
			if($_e.length>0){
				$_e.attr("class","am-panel-title am-collapsed");
				$($_e.parent()).find("div.am-panel-collapse").collapse('toggle');  
			} 
		} 
		$($this).attr("class","am-panel-title am-active");
	}else{
		$($this).attr("class","am-panel-title am-collapsed");
	}
	$("#"+eleId).collapse('toggle'); 
}  

function clickTab(code){ 
	if(isCurrent(code)){
    	return;
    }
	
	var secondMenu=$("#mli_"+code).closest('.am-panel-collapse').siblings('h4'); 
	if(secondMenu.length>0){
		secondMenu=secondMenu[0]; 
		if(!$(secondMenu).hasClass("am-active")){
			$(secondMenu).click();
		}
	}
	
	var topMenu=$("#mli_"+code).closest('.am-accordion-item');  //找到顶级菜单 closest是个好东西
	if(topMenu.length>0){
		topMenu=topMenu[0]; 
		if(!$(topMenu).hasClass("am-active")){
			$(topMenu).click();
		}
	} 
	
    var $tab = $('#pageTabs'); 
    var $nav = $tab.find('.am-tabs-nav'); 
    $tab.find('.am-tabs-bd').find('.am-active').find('div').removeClass("am-active"); 
    $tab.find('.am-tabs-bd').find('.am-active').removeClass("am-active");
    $nav.find('.am-active').removeClass("am-active");
	$('#ifr-'+code).addClass("am-active"); 
    $nav.find('#tab-'+code).addClass("am-active"); 
    $("#ifr-pdiv-"+code).addClass("am-active"); 
	menuSelectCss($("#mli_"+code)); 
}

function firstTab(){
	/*
    var indexurl= ajaxSync("/bi/Sm.IndexPageService/index",JSON.stringify({"terminal":"1" }));  
    if(indexurl==null||indexurl==""||indexurl=="none"){
    	indexurl="web/index.jsp";
    	$("#ifr-first").find('iframe')[0].src=indexurl;
    }else{
    */
    	$("#ifr-first").find('iframe')[0].src='template/wait?targetUrl='+"com.lanshan.web.admin.sm.view.UrlManage.d"; 
    //} 
    $("#ifr-first").css('height',$(document).height()-68); 
    
}
 

function moreNotice(){ 
	addTab('消息中心','web/notice/message.jsp','notice') 
}

function readNotice(recv_name,serino,title,sendTime,content,isRead){ 
	var notice={};
	notice.title=title;
	notice.sendTime=sendTime;
	notice.content=content; 
	bombNotice(notice);  
	if(serino!="" && recv_name!=""&&isRead!='1'){
		reduceNoticeCount();
		ajaxSync("bi/notice/read",JSON.stringify({"recv_name":recv_name,"serino":serino}));
	}
}
/**
 * 加载消息概要
 * @returns
 */
function loadNoticePro(){
	 //ajax("/bi/noticeService/notices",JSON.stringify({"pro":"y"}),null,null,noticePro); 
}

/**
 * 页面首次加载用户消息处理
 * @param noticesPro
 * @returns
 */
function noticePro(noticesPro){
	 $("#unreadCnt").text(noticesPro.unreadCount); 
	 var noticeHtml=template("tplnoticepro", {"notices" : noticesPro.notices});
	 $("#noticespro").html(noticeHtml);  
}

function reduceNoticeCount(){
	var count=$("#unreadCnt").text(); 
	var c=parseInt(count)-1;
	$("#unreadCnt").text(c<0?0:c); 
}

/**
 * 接收到新消息处理
 * @returns
 */
function addNoticePro(notice){
	popNotice(notice.message);
	loadNoticePro();
//	var count=$("#unreadCnt").text(); 
//	$("#unreadCnt").text(parseInt(count)+1); 
//	if(!notice.message.serino){
//		notice.message.serino=-1;
//	}
//	
//	var noticeHtml=template("tplnoticeItem", {"notice" : notice.message}); 
//	$(noticeHtml).insertAfter("#stationNotice"); 
//	var sibls =$("#stationNotice").siblings(); 
//	if(sibls.length>4){   //消息列表大于3，移除后面的，保证最多三个
//		for(var i=3;i<sibls.length-1;i++){
//			$(sibls[i]).remove();
//		}
//	} 
} 


function bombNotice(notice){  
	$("#bombTitle").text(notice.title);
	$("#bombSendTime").text(notice.sendTime);
	$("#bombContent").html(notice.content);  
	$("#messageBomb").modal({'closeViaDimmer':false}); 
	loadNoticePro();
}

function popNotice(notice){
	$("#popTitle").text(notice.title); 
	$("#popContent").html(notice.content);  
	$('#messagePop').modal({
	   dimmer: false
	});
}

function logOut(){ 
	confirm_msg("您是否确定要退出登录？",
	   function(){
		 location.href =document.getElementsByTagName("base")[0].href+"bi/form/logout"; 
	   }		
	); 
}
function TMin(){
	TJsCall.doMinSize();
}
function TExit(){
	confirm_msg("程序即将关闭，您确定要退出系统?", function(){ 
		TJsCall.exit();
	}); 
}

function modifyCommit(){
	var modifyOrginPwd=$("#modifyOrginPwd").val();
	var modifyNewPwd=$("#modifyNewPwd").val();
	var modifyNewPwdAgain=$("#modifyNewPwdAgain").val(); 
	
	ajax("/bi/Sm.UserService/updatePasswordBySelf",
			JSON.stringify({"orginPwd":modifyOrginPwd,"newPwd":modifyNewPwd,"againPwd":modifyNewPwdAgain}), null, null, 
			function(){
		      closeModifyLoginPWD(); 
		      succ_msg("您的登录密码修改成功");
	        }
	); 
}


function passwordValidator() {
    var msg = "";
    var $form = $('#modifyLoginPWDForm');
    $form.validator({
        onValid: function(validity) {
            //关闭校验元素的提示
            $(validity.field).popover('close');
        },
        onInValid: function(validity) { 
            //首先关闭所有的校验提示信息，然后再打开本身的错误提示
            $(".am-field-error").each(function(index,item){
                 $(item).popover('close');
             });
            $(validity.field).popover('setContent',msg).popover('open');
        },
        validate: function(validity) {
            //给每一个进校校验的元素加上提示，内容为空，不展示
            if ($.isEmptyObject($(validity.field).data()['amui.popover'])) {
                $(validity.field).popover({
                    content: "",
                    theme: 'danger sm'
                  });
            }
            var value = $(validity.field).val();
            var id = $(validity.field).attr("id");
            
            
            if (id == "modifyOrginPwd") {
                // 判断数量是不是正浮点数
                if (!value||value.trim()=="") {
                    msg = "请输入原密码";
                    validity.valid = false;
                } else {
                	validity.valid = true;
                }
            }
            
            if (id == "modifyNewPwd" || id == "modifyNewPwdAgain" ) {
                // 判断数量是不是正浮点数
                if (!vaildPassWord(value)) {
                    msg = "请按照正确的格式输入密码";
                    validity.valid = false;
                } else {
                	validity.valid = true;
                }
            }
            return validity;
        },
        alwaysRevalidate:true,  //很重要，不然校验第一次之后不会校验第二次
        keyboardEvents: 'focusout', //触发校验元素的事件
        submit: function(e){  
            //阻止原生的action提交，而是用手动来校验数据
            return false;
        }
    });
    $form.on('focusin', 'input', function(e) {
		if($(this).hasClass("am-field-error")){
			$(this).popover('open');
		}
		
    });
}
