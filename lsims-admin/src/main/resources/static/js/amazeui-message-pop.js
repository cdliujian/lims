var basePath;
if(!document.getElementsByTagName("base")[0]){
	basePath=parent.document.getElementsByTagName("base")[0].href;
}else{
	basePath=document.getElementsByTagName("base")[0].href;
}
var succ_html=
              '<div class="am-modal" tabindex="-1" id="succ_msg_1">'
	         +'<div class="am-modal-dialog">'
	         +'<div class="am-modal-bd am-padding-0 am-margin-0">'
             +'<div class="am-text-sm bg-white am-text-center pbottom10">'
             +'<div class="wl-tipss"><img class="am-padding-right-sm" src="'+basePath+'/images/right2.png"> #msg#</div>'
             +'<div class="am-divider-default mbottom10 pbottom10"></div>'      
             +'<a id="btn_okHide" class="am-btn am-btn-sm btn-blue am-padding-left-xl am-padding-right-xl" href="javascript:void(0)">确定</a></div></div></div></div>' 
 /**
 弹出成功提示
**/ 
function succ_msg(msg,callBack){
	var hasCallBack=arguments.length>1;
    if($('#succ_msg_1'))	
	   $('#succ_msg_1').remove(); 
	$("body").append(succ_html.replace("#msg#",msg).replace('#id#','\'succ_msg_1\''));
	$("#btn_okHide").click(function(){
		close_layer("succ_msg_1"); 
		if(hasCallBack){
			callBack&&callBack(); 
		} 
	}); 
	
	var $modal = $('#succ_msg_1');
	$modal.modal({'closeViaDimmer':false});
}

function  close_layer(id, btnId){
	var $modal = $('#'+id);
	if($modal){
		if(btnId != 'undefined') {
			$("#"+btnId).button('reset');
		}
		$modal.modal('close');
	}
	 
} 

var error_html='<div class="am-modal" tabindex="-1" id="error_pop_1">'
              +'<div class="am-modal-dialog">'
              +'<div class="am-modal-bd am-padding-0 am-margin-0">'
              +'<div class="am-text-sm bg-white am-text-center pbottom10">'
              +'<div class="wl-tipss">'
              +'<img class="am-text-top am-padding-top-xs" src="'+basePath+'/images/wrong.png"> <span>操作失败！<br><span class="am-text-xs c999">#error#</span></span></div>'
              +'<div class="am-divider-default mbottom10 pbottom10"></div>'
              +'<a class="am-btn am-btn-sm btn-blue am-padding-left-xl am-padding-right-xl" id="btn_errHide">确定</a>'  
              +'</div></div></div></div>' 
 /**
 弹出异常提示
**/ 
function error_msg(error,callback){
	var hasCallBack=arguments.length>1;
	if($('#error_pop_1'))	
	   $('#error_pop_1').remove();
	$("body").append(error_html.replace("#error#",error));
	$("#btn_errHide").click(function(){
		close_layer("error_pop_1"); 
		if(hasCallBack){
			callback(); 
		} 
	}); 
	$('#btn_errHide').bind('keypress',function(event){  
		 if(event.keyCode == "13"){  
			 close_layer("error_pop_1"); 
			 if(hasCallBack){
				 callback(); 
			 } 
        }  
    });
	var $modal = $('#error_pop_1'); 
	$modal.modal({ 
        'closeViaDimmer':false
    });  
    
}       
var confirm_html='<div class="am-modal" tabindex="-1" id="doc-modal-confirm">'
	        +'<div class="am-modal-dialog"><div class="am-modal-bd am-padding-0 am-margin-0">'
            +'<div class="am-text-sm bg-white am-text-center pbottom10"><div class="wl-tipss">' 
            +'<img class="am-text-top am-padding-top-xs" src="'+basePath+'/images/tan2.png"/><span>#tips#</span> </div>'
            +'<div class="am-divider-default mbottom10 pbottom10"></div>'
            +'<a class="am-btn am-btn-sm btn-blue am-margin-right-xs" id="btn_ok">确定</a>'
            +'<a class="am-btn am-btn-sm btn-gray" onclick="close_layer(#id#,#btnId#)" data-am-modal-close>取消</a>'   
            +'</div></div></div></div>' 

function  confirm_msg(tips,callback,btnId){
	if($('#doc-modal-confirm'))	
	   $('#doc-modal-confirm').remove();
	$("body").append(confirm_html.replace("#tips#",tips).replace("#id#",'\'doc-modal-confirm\'').replace("#btnId#",'\'' + btnId + '\''));
	$("#btn_ok").click(function(){
		close_layer('doc-modal-confirm');
		callback && callback();
	});
	var $modal = $('#doc-modal-confirm');
	$modal.modal({
        width : 370,
        'closeViaDimmer':false
    });
}

function waiting(id,msg){
	if($("#l_"+id).length==0){
		var html = '<div id="l_' + id + '"><div class="am-modal am-modal-loading am-modal-no-btn am-modal-active" tabindex="-1" style="display: block; margin-top: -50px;"><div class="am-modal-dialog"><div class="am-modal-hd">#msg#</div><div class="am-modal-bd"><span class="am-icon-spinner am-icon-spin"></span></div></div></div><div class="am-dimmer am-active" data-am-dimmer="" style="display: block;background-color: rgba(0,0,0,0.4);"></div></div>';
		$("body").append(html.replace("#msg#",msg));
	} 
}

function waited(id)
{
	if($("#l_"+id).length>0){
	   $("#l_" + id).remove();
	}
} 

var prompt_html = '<div class="am-modal am-modal-prompt" tabindex="-1" id="my-prompt">'
 +' <div class="am-modal-dialog">'
 +' <div class="am-modal-hd">' 
 +'  <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>'
 +'  </div>' 
 +' <div class="am-modal-bd">' 
 +'   <span>#tips#</span> '
 +'   <input type="text" class="am-modal-prompt-input" value="#defautValue#">'
 +'  </div>'
 +'  <div class="am-modal-footer">'
 +' <span class="am-modal-btn" data-am-modal-cancel  onclick="close_layer(#id#)">关闭</span>'
 +' <span class="am-modal-btn" data-am-modal-confirm>确认</span>'
 +'  </div>'
 +'  </div>'
 +'  </div>';

function  prompt_msg(tips, defautValue ,callback){
	 if($('#my-prompt'))	
		   $('#my-prompt').remove();
		$("body").append(prompt_html.replace("#tips#",tips).replace("#defautValue#",defautValue).replace("#id#",'\'my-prompt\''));
	 $('#my-prompt').modal({ 
	        relatedTarget: this,
	        width : 370,
	        'closeViaDimmer':false,
	        onConfirm: function(e) {
	          count =  e.data; 
	          callback && callback(count);
	        } ,
	        onCancel: function(e) {
	            
	          }
	 }); 
}
