var basePath = "";
if(!document.getElementsByTagName("base")[0]){
	basePath=parent.document.getElementsByTagName("base")[0].href;
}else{
	basePath=document.getElementsByTagName("base")[0].href;
}

//文本获取焦点、自动全选文字
$(":text").focus(function(){
	  $(this).select();
});


/**
 * 同步ajax
 * @param url
 * @param param
 * @param layerId
 * @param msg
 * @returns
 */
function ajaxSync(url,param) {
//	var layerId ="_commwaitelayer_";
//	var msg="正在努力加载数据，请稍后...";
	var data = null;
	$.ajax({
		"async" : false,
		"url" : basePath+url,
		"data" : param,
		contentType:"application/json",
		"dataType" : "json",
		type : "post",
		beforeSend : function(XMLHttpRequest) {
			//waiting(layerId, msg);
		},
		success : function(d) {
			//waited(layerId);

			if (!d.meta.success ) {
				error_msg(d.meta.message);
				return;
			}  
			data = d.data;
		},
		error : function(error) {
			//waited(layerId);
		}
	});
	return data;
}

/**
 * 异步ajax
 * @param url
 * @param param
 * @param sendFun
 * @param compFun
 * @param callBack
 * @returns
 */
function ajax(url,param, sendFun, compFun, callBack,method) {
	var layerId ="_commwaitelayer2_";
	var msg="正在努力加载数据，请稍后...";
	if(!method)
		method = "post"
	$.ajax({
		"async" : true,
		"url" : basePath+url,
		"data" : param,
		contentType:"application/json",
		"dataType" : "json",
		type : method,
		beforeSend : function(XMLHttpRequest) {
			waiting(layerId, msg);
			sendFun && sendFun();
		},
		complete : function(XMLHttpRequest, textStatus) {
			compFun && compFun();
			waited(layerId);
		}, 
		success : function(d) {
			// var d = JSON.parse(d);
			if (!d.meta.success ) {
				error_msg(d.meta.message);
				return;
			}  
			callBack && callBack(d.data); 
		},
		error : function(error) {
			waited(layerId);
		}
	});
};

function checkMobileNo(mblNo){
	var pattern = /^1[34578]\d{9}$/; 
    return pattern.test(mblNo); 
}

function pageSize(){
	return ajaxSync("/bi/param/pagesize");
} 

function pageSizeApp(){
	return ajaxSync("/bi/param/pagesizeApp");
} 

function isTJsCall(){ 
	return typeof(TJsCall) != "undefined" 
} 
	
function TExit(){
	TJsCall.exit();
}
	
function TMin(){
	TJsCall.doMinSize();
}

/**
 * 
 * @param title  tab标题
 * @param url  tab对应的链接
 * @param tag   唯一的标识，用于生产tab的id
 * @returns
 */
function openTab(title,url,tag){
	parent.addTab(title,url,tag) ;
}

function loadCss(url) {
   var link = document.createElement('link');
   link.type = 'text/css';
   link.rel = 'stylesheet';
   link.href = url;
   document.getElementsByTagName("head")[0].appendChild(link);
}

function loadJs(url,succCall,failCall){
	jQuery.getScript(url).done(function() {
       succCall&&succCall();
    })
    .fail(function() { 
	   failCall&&failCall();
    });
}


function vaildPassWord(password){ 
	var pattern =/^[a-zA-Z0-9]{6,12}$/;
    return pattern.test(password); 
	
}
//时间戳转DateTime 邹庚午
function timeStamp2String (time){
    var datetime = new Date();
     datetime.setTime(time);
     var year = datetime.getFullYear();
     var month = datetime.getMonth() + 1;
     if(month < 10 ) {
    	 month = "0" + month;
     }
     var date = datetime.getDate();
     if(date < 10 ) {
    	 date = "0" + date;
     }
     var hour = datetime.getHours();
     if(hour < 10 ) {
    	 hour = "0" + hour;
     }
     var minute = datetime.getMinutes();
     if(minute < 10 ) {
    	 minute = "0" + minute;
     }
     var second = datetime.getSeconds();
     if(second < 10 ) {
    	 second = "0" + second;
     }
     var mseconds = datetime.getMilliseconds();
     return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second ;
};


/**  
 * 将数值四舍五入(保留2位小数)后格式化成金额形式  
 *  
 * @param num 数值(Number或者String)  
 * @return 金额格式的字符串,如'1,234,567.45'  
 * @type String  
 */    
function formatCurrency(num) {    
    num = num.toString().replace(/\$|\,/g,'');    
    if(isNaN(num))    
    num = "0";    
    sign = (num == (num = Math.abs(num)));    
    num = Math.floor(num*100+0.50000000001);    
    cents = num%100;    
    num = Math.floor(num/100).toString();    
    if(cents<10)    
    cents = "0" + cents;    
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
    num = num.substring(0,num.length-(4*i+3))+','+    
    num.substring(num.length-(4*i+3));    
    return (((sign)?'':'-') + num + '.' + cents);    
}

function mergeObjs(def, obj) {　　
	if (!obj) {　　
		return def;　　
	} else if (!def) {　　
		return obj;　　
	}　　
	for (var i in obj) {　　 // if its an object
		　　
		if (obj[i] != null && obj[i].constructor == Object)　　 {　　
			def[i] = mergeObjs(def[i], obj[i]);　　
		}　　 // if its an array, simple values need to be joined. Object values need to be remerged.
		　　
		else if (obj[i] != null && (obj[i] instanceof Array) && obj[i].length > 0)　　 {　　 // test to see if the first element is an object or not so we know the type of array we're dealing with.
			　　
			if (obj[i][0].constructor == Object)　　 {　　
				var newobjs = [];　　 // create an index of all the existing object IDs for quick access. There is no way to know how many items will be in the arrays.
				　　
				var objids = {}
				if(!def[i])
					def[i]=[];　　
				for (var x = 0, l = def[i].length; x < l; x++)　　 {　　
					objids[def[i][x].id] = x;　　
				}　　 // now walk through the objects in the new array
				　　 // if the ID exists, then merge the objects.
				　　 // if the ID does not exist, push to the end of the def array
				　　
				for (var x = 0, l = obj[i].length; x < l; x++)　　 {　　
					var newobj = obj[i][x];　　
					if (objids[newobj.id] !== undefined)　　 {　　
						def[i][x] = mergeObjs(def[i][x], newobj);　　
					}　　
					else {　　
						newobjs.push(newobj);　　
					}　　
				}　　
				for (var x = 0, l = newobjs.length; x < l; x++) {　　
					def[i].push(newobjs[x]);　　
				}　　
			}　　
			else {　　
				for (var x = 0; x < obj[i].length; x++)　　 {　　
					var idxObj = obj[i][x];　　
					if (def[i].indexOf(idxObj) === -1) {　　
						def[i].push(idxObj);　　
					}　　
				}　　
			}　　
		}　　
		else　　 {　　
			def[i] = obj[i];　　
		}　　
	}　　
	return def;
}

Array.prototype.where = function(f){
	      var fn = f ;
	      // if type of parameter is string         
	      if ( typeof f == "string" )
	         // try to make it into a function
	         if ( ( fn = lambda( fn ) ) == null )
	            // if fail, throw exception
	            throw "Syntax error in lambda string: " + f ;
	      // initialize result array
	      var res = [] ;
	      var l = this.length;
	      // set up parameters for filter function call
	      var p = [ 0, 0, res ] ;
	      // append any pass-through parameters to parameter array               
	      for (var i = 1; i < arguments.length; i++) p.push( arguments[i] );
	      // for each array element, pass to filter function
	      for (var i = 0; i < l ; i++)
	      {
	         // skip missing elements
	         if ( typeof this[ i ] == "undefined" ) continue ;
	         // param1 = array element             
	         p[ 0 ] = this[ i ] ;
	         // param2 = current indeex
	         p[ 1 ] = i ;
	         // call filter function. if return true, copy element to results            
	         if ( !! fn.apply(this, p)  ) res.push(this[i]);
	      }
	      // return filtered result
	      return res ;
}

function lambda( l )
{
   var fn = l.match(/\((.*)\)\s*=>\s*(.*)/) ;
   var p = [] ;
   var b = "" ;
 
   if ( fn.length > 0 ) fn.shift() ;
   if ( fn.length > 0 ) b = fn.pop() ;
   if ( fn.length > 0 ) p = fn.pop()
    .replace(/^\s*|\s(?=\s)|\s*$|,/g, '').split(' ') ;
 
   // prepend a return if not already there.
   fn = ( ( ! /\s*return\s+/.test( b ) ) ? "return " : "" ) + b ;   
 
   p.push( fn ) ;
 
   try
   {
      return Function.apply( {}, p ) ;
   }
   catch(e)
   {
        return null ;
   }
}

//验证电话号码
function checkPhone(tel) {
	var pattern = /^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
	return pattern.test(tel); 
}

//验证身份证
function checkCardId(cardId) {
	if(cardId!="") {     
        //身份证的地区代码对照  
        var aCity = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" };           
        //获取证件号码  
        var person_id=cardId;  
        //合法性验证  
        var sum = 0;  
        //出生日期  
        var birthday;  
        //验证长度与格式规范性的正则  
        var pattern=new RegExp(/(^\d{15}$)|(^\d{17}(\d|x|X)$)/i);         
        if (pattern.exec(person_id)) {  
            //验证身份证的合法性的正则  
            pattern=new RegExp(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/);  
            if(pattern.exec(person_id)) {     
                //获取15位证件号中的出生日期并转位正常日期       
                birthday = "19"+person_id.substring(6,8)+"-"+person_id.substring(8,10)+"-"+person_id.substring(10,12);            
            } else {     
                person_id = person_id.replace(/x|X$/i,"a");                       
                //获取18位证件号中的出生日期  
                birthday =person_id.substring(6,10)+"-"+person_id.substring(10,12)+"-"+person_id.substring(12,14);  
                  
                //校验18位身份证号码的合法性  
                for (var i = 17; i >= 0; i--) {  
                    sum += (Math.pow(2, i) % 11) * parseInt(person_id.charAt(17 - i), 11);  
                }  
                if (sum % 11 != 1) {      
                    error_msg("身份证号码不符合国定标准，请核对！");   
                    return false;  
                }             
            }  
            //检测证件地区的合法性                                  
            if (aCity[parseInt(person_id.substring(0, 2))] == null) {  
                error_msg("证件地区未知，请核对！");        
                return false;  
            }  
            var dateStr = new Date(birthday.replace(/-/g, "/"));                  
              
            //alert(birthday +":"+(dateStr.getFullYear()+"-"+ Append_zore(dateStr.getMonth()+1)+"-"+ Append_zore(dateStr.getDate())))  
            if (birthday != (dateStr.getFullYear()+"-"+ Append_zore(dateStr.getMonth()+1)+"-"+ Append_zore(dateStr.getDate()))) {  
                error_msg("证件出生日期非法！");               
                return false;  
            } 
        } else {          
            error_msg("证件号码格式非法！");  
            return false;  
        }             
    } else {  
    	error_msg("请输入证件号！");  
        return false;     
    } 
	return true;
}

//为值添加0
function Append_zore(temp) {  
    if(temp<10) {  
        return "0"+temp;  
    } else {  
        return temp;  
    }  
} 

//判断字符串长度，包含中文
function getStrLength(str) {
	var l = str.length;
	var blen = 0;
	for(i=0; i<l; i++) {
		if ((str.charCodeAt(i) & 0xff00) != 0) {
			blen ++;
		}
		blen ++;
	}
	
	return blen;
}

//设置图片展示路径
function setPicSrc(path, version, width, height) {
	if(version == undefined) {
		version = "app";
	}
	if(version == "app") {
		if(height == undefined) {
			height = "120";
		}		
		return path + "?x-oss-process=image/resize,m_pad,h_" + height;
	} else if(version == "pc") {
		if(height == undefined) {
			height = "132";
		}
		if(width == undefined) {
			width = "144";
		}		
		return path + "?x-oss-process=image/resize,m_pad,h_" + height + ",w_" + width;
	}
	
	return "";
}

//验证统一社会信用代码
function checkBlicUscc(blicUscc) {
	var pattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{18}$/;
	return pattern.test(blicUscc); 
}

//验证营业执照号码（普通五证类）
function checkBlicCardNoByBLI(blicCardNo) {
	var pattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{15}$/;
	return pattern.test(blicCardNo); 
}

//验证营业执照号码（多证合一非统一社会信用代码类）
function checkBlicCardNoByOCI(blicCardNo) {
	var pattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,50}$/;
	return pattern.test(blicCardNo); 
}

//验证统一社会信用代码
function checkLepCardNo(lepCardNo) {
	var pattern = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,50}$/;
	return pattern.test(lepCardNo); 
}

/**
 * 用来校验组织机构代码是否符合  格式为12345678-9
 * //true 合格  false 不合格
 */
function checkBlicTrcCardNo(value){
	var reg=/^[A-Za-z0-9]{8}-[A-Za-z0-9]/;   /*定义验证表达式*/
	return reg.test(value);
}

//特殊字符验证
function stripscript(obj) { 
	var reg = new RegExp("[@#\$\&|'`\\\\/\"]");
	var s = "";
	for(var i = 0; i < obj.value.length; i++) {
		s = s + obj.value[i].replace(reg,'');
	}
	//obj.value = obj.value.replace(reg,'');
	obj.value = s;
}

function doAuthComponents(){
	if(typeof _components != "undefined"){
		var jsonComponents = JSON.parse(_components);
		var jsonPage = JSON.parse(_pageComponents);
		for(var i=0;i< jsonPage.length;i++){
			var p = jsonPage[i];
			if(jsonComponents.indexOf(p)==-1){
				var o = document.getElementById(p);
				if(o)
					o.style.display="none";
			}
				
		}
	}
}

doAuthComponents();