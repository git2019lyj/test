﻿/**
 * jQuery EasyUI 1.4.5
 * 
 * Copyright (c) 2009-2016 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
(function($){
function _1(_2){
$(_2).addClass("validatebox-text");
};
function _3(_4){
var _5=$.data(_4,"validatebox");
_5.validating=false;
if(_5.timer){
clearTimeout(_5.timer);
}
$(_4).tooltip("destroy");
$(_4).unbind();
$(_4).remove();
};
function _6(_7){
var _8=$.data(_7,"validatebox").options;
$(_7).unbind(".validatebox");
if(_8.novalidate||_8.disabled){
return;
}
for(var _9 in _8.events){
$(_7).bind(_9+".validatebox",{target:_7},_8.events[_9]);
}
};
function _a(e){
var _b=e.data.target;
var _c=$.data(_b,"validatebox");
var _d=_c.options;
if($(_b).attr("readonly")){
return;
}
_c.validating=true;
_c.value=_d.val(_b);
(function(){
if(_c.validating){
var _e=_d.val(_b);
if(_c.value!=_e){
_c.value=_e;
if(_c.timer){
clearTimeout(_c.timer);
}
_c.timer=setTimeout(function(){
$(_b).validatebox("validate");
},_d.delay);
}else{
if(_c.message){
_d.err(_b,_c.message);
}
}
setTimeout(arguments.callee,_d.interval);
}
})();
};
function _f(e){
var _10=e.data.target;
var _11=$.data(_10,"validatebox");
var _12=_11.options;
_11.validating=false;
if(_11.timer){
clearTimeout(_11.timer);
_11.timer=undefined;
}
if(_12.validateOnBlur){
$(_10).validatebox("validate");
}
_12.err(_10,_11.message,"hide");
};
function _13(e){
var _14=e.data.target;
var _15=$.data(_14,"validatebox");
_15.options.err(_14,_15.message,"show");
};
function _16(e){
var _17=e.data.target;
var _18=$.data(_17,"validatebox");
if(!_18.validating){
_18.options.err(_17,_18.message,"hide");
}
};
function _19(_1a,_1b,_1c){
var _1d=$.data(_1a,"validatebox");
var _1e=_1d.options;
var t=$(_1a);
if(_1c=="hide"||!_1b){
t.tooltip("hide");
}else{
if(t.is(":focus")||_1c=="show"){
t.tooltip($.extend({},_1e.tipOptions,{content:_1b,position:_1e.tipPosition,deltaX:_1e.deltaX})).tooltip("show");
}
}
};
function _1f(_20){
var _21=$.data(_20,"validatebox");
var _22=_21.options;
var box=$(_20);
_22.onBeforeValidate.call(_20);
var _23=_24();
_23?box.removeClass("validatebox-invalid"):box.addClass("validatebox-invalid");
_22.err(_20,_21.message);
_22.onValidate.call(_20,_23);
return _23;
function _25(msg){
_21.message=msg;
};
function _26(_27,_28){
var _29=_22.val(_20);
var _2a=/([a-zA-Z_]+)(.*)/.exec(_27);
var _2b=_22.rules[_2a[1]];
if(_2b&&_29){
var _2c=_28||_22.validParams||eval(_2a[2]);
if(!_2b["validator"].call(_20,_29,_2c)){
var _2d=_2b["message"];
if(_2c){
for(var i=0;i<_2c.length;i++){
_2d=_2d.replace(new RegExp("\\{"+i+"\\}","g"),_2c[i]);
}
}
_25(_22.invalidMessage||_2d);
return false;
}
}
return true;
};
function _24(){
_25("");
if(!_22._validateOnCreate){
setTimeout(function(){
_22._validateOnCreate=true;
},0);
return true;
}
if(_22.novalidate||_22.disabled){
return true;
}
if(_22.required){
if(_22.val(_20)==""){
_25(_22.missingMessage);
return false;
}
}
if(_22.validType){
if($.isArray(_22.validType)){
for(var i=0;i<_22.validType.length;i++){
if(!_26(_22.validType[i])){
return false;
}
}
}else{
if(typeof _22.validType=="string"){
if(!_26(_22.validType)){
return false;
}
}else{
for(var _2e in _22.validType){
var _2f=_22.validType[_2e];
if(!_26(_2e,_2f)){
return false;
}
}
}
}
}
return true;
};
};
function _30(_31,_32){
var _33=$.data(_31,"validatebox").options;
if(_32!=undefined){
_33.disabled=_32;
}
if(_33.disabled){
$(_31).addClass("validatebox-disabled").attr("disabled","disabled");
}else{
$(_31).removeClass("validatebox-disabled").removeAttr("disabled");
}
};
function _34(_35,_36){
var _37=$.data(_35,"validatebox").options;
_37.readonly=_36==undefined?true:_36;
if(_37.readonly||!_37.editable){
$(_35).addClass("validatebox-readonly").attr("readonly","readonly");
}else{
$(_35).removeClass("validatebox-readonly").removeAttr("readonly");
}
};
$.fn.validatebox=function(_38,_39){
if(typeof _38=="string"){
return $.fn.validatebox.methods[_38](this,_39);
}
_38=_38||{};
return this.each(function(){
var _3a=$.data(this,"validatebox");
if(_3a){
$.extend(_3a.options,_38);
}else{
_1(this);
_3a=$.data(this,"validatebox",{options:$.extend({},$.fn.validatebox.defaults,$.fn.validatebox.parseOptions(this),_38)});
}
_3a.options._validateOnCreate=_3a.options.validateOnCreate;
_30(this,_3a.options.disabled);
_34(this,_3a.options.readonly);
_6(this);
_1f(this);
});
};
$.fn.validatebox.methods={options:function(jq){
return $.data(jq[0],"validatebox").options;
},destroy:function(jq){
return jq.each(function(){
_3(this);
});
},validate:function(jq){
return jq.each(function(){
_1f(this);
});
},isValid:function(jq){
return _1f(jq[0]);
},enableValidation:function(jq){
return jq.each(function(){
$(this).validatebox("options").novalidate=false;
_6(this);
_1f(this);
});
},disableValidation:function(jq){
return jq.each(function(){
$(this).validatebox("options").novalidate=true;
_6(this);
_1f(this);
});
},resetValidation:function(jq){
return jq.each(function(){
var _3b=$(this).validatebox("options");
_3b._validateOnCreate=_3b.validateOnCreate;
_1f(this);
});
},enable:function(jq){
return jq.each(function(){
_30(this,false);
_6(this);
_1f(this);
});
},disable:function(jq){
return jq.each(function(){
_30(this,true);
_6(this);
_1f(this);
});
},readonly:function(jq,_3c){
return jq.each(function(){
_34(this,_3c);
_6(this);
_1f(this);
});
}};
$.fn.validatebox.parseOptions=function(_3d){
var t=$(_3d);
return $.extend({},$.parser.parseOptions(_3d,["validType","missingMessage","invalidMessage","tipPosition",{delay:"number",interval:"number",deltaX:"number"},{editable:"boolean",validateOnCreate:"boolean",validateOnBlur:"boolean"}]),{required:(t.attr("required")?true:undefined),disabled:(t.attr("disabled")?true:undefined),readonly:(t.attr("readonly")?true:undefined),novalidate:(t.attr("novalidate")!=undefined?true:undefined)});
};
$.fn.validatebox.defaults={required:false,validType:null,validParams:null,delay:200,interval:200,missingMessage:"This field is required.",invalidMessage:null,tipPosition:"right",deltaX:0,novalidate:false,editable:true,disabled:false,readonly:false,validateOnCreate:true,validateOnBlur:false,events:{focus:_a,blur:_f,mouseenter:_13,mouseleave:_16,click:function(e){
var t=$(e.data.target);
if(t.attr("type")=="checkbox"||t.attr("type")=="radio"){
t.focus().validatebox("validate");
}
}},val:function(_3e){
return $(_3e).val();
},err:function(_3f,_40,_41){
_19(_3f,_40,_41);
},tipOptions:{showEvent:"none",hideEvent:"none",showDelay:0,hideDelay:0,zIndex:"",onShow:function(){
$(this).tooltip("tip").css({color:"#000",borderColor:"#CC9933",backgroundColor:"#FFFFCC"});
},onHide:function(){
$(this).tooltip("destroy");
}},rules:{},onBeforeValidate:function(){
},onValidate:function(_4a){
}};
})(jQuery);

