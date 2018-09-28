/********
脚本 2017-09-07 10:25
********/
/********
俱乐部通用脚本
********/

/** 浏览器环境
browser["b"] 浏览器
browser["v"] 版本
browser["m"] 是否移动端
**/
browser = (function(ua){
  var a=new Object();
  var b = {
    msie: /msie/.test(ua) && !/opera/.test(ua),
    opera: /opera/.test(ua),
    safari: /webkit/.test(ua) && !/chrome/.test(ua),
    firefox: /firefox/.test(ua),
    edge: /edge/.test(ua),
    trident: /trident/.test(ua),
    chrome: /chrome/.test(ua)
  };
  var vMark = "";
  for (var i in b) {
    if (b[i]) { vMark = i; break; }//"safari" == i ? "version" : i;
  }
  b.version = vMark && RegExp("(?:" + vMark + ")[\\/: ]([\\d.]+)").test(ua) ? RegExp.$1 : "0";
  b.ie = b.msie;
  b.ie6 = b.msie && parseInt(b.version, 10) == 6;
  b.ie7 = b.msie && parseInt(b.version, 10) == 7;
  b.ie8 = b.msie && parseInt(b.version, 10) == 8;
  
  //if(vMark == "trident") vMark = "ie11";
  
  a.b=vMark;
  a.v=b.version;
  a.m=/mobile/.test(ua);
  return a;
})(window.navigator.userAgent.toLowerCase());
/** 浏览器环境 结束 **/

//不同设备采用不同动作事件（算了，不用了）
//var click_event = browser["m"]? "touchstart" : "click";
var click_event = "click";




/**
快速闪现提示
UPDATE: 2016/12/30
Powered by Joyton & dline.com.cn
用法：
$.fn.winktips({
  con: "内容",
  delay: 1600 //存活时间，默认1600
});
简单用法：
$.fn.winktips("内容");

**/
(function($){
$.fn.winktips = function( options ) {
  var defaults = {
    con: "(•ᴗ•)و",
    delay: 1600
  };
  if( (typeof options != "object") ) {
    defaults.con = options;
  }
  var settings = $.extend({}, defaults, options);
  
  if( !$("#winktips").length ) {
    $("body").append("<div id=\"winktips\"></div>");
  }
  $("<div class=\"item\"><span>" + settings.con + "</span></div>").appendTo("#winktips").fadeIn(200).delay( settings.delay ).fadeOut(200, function(){
    $(this).remove();
  });
  return this;
  
}})(jQuery);
/** 快速闪现提示插件 结束 **/

/**
弹出提示
UPDATE: 2017/04/12
Powered by Joyton & dline.com.cn
用法：
$.fn.poptips({
  title: "标题", //默认为空
  con: "内容",
  conCenter: false, //文本内容内容是否居中，默认居中
  btnOK: false, //是否显示确认按钮 默认显示
  btnCancel: false, //是否显示取消按钮 默认不显示
  btnOKText: "确认", //确认按钮的文案
  btnCancelText: "取消", //取消按钮的文案
  btnOKClick: function (){	//确认按钮点击事件（没有按钮时遵从此事件）
    console.log("你点了确认");
  },
  btnCancelClick: function (){	//取消按钮点击事件
    console.log("你点了取消");
  }
});
简单用法：
$.fn.poptips("内容");

**/
(function($){
$.fn.poptips = function( options ) {
  var defaults = {
    title: "",
    con: "无内容",
    conCenter: true,
    btnOK: true,
    btnCancel: false,
    btnOKText: "确认",
    btnCancelText: "取消",
    "btnOKClick": function (){},
    "btnCancelClick": function (){}
  };
  
  if( (typeof options != "object") ) {
    defaults.con = options;
  }
  
  var settings = $.extend({}, defaults, options);
  
  var tmp_html = "";
  tmp_html += "<div id=\"poptips\"><div class=\"poptips_box\">"
    +( settings.title ? "<div class=\"title\">" + settings.title + "</div>" : "" )
    + "<div class=\"con "
    +( settings.conCenter ? "center" : "" )
    +"\">" + settings.con 
    + "</div>";
  if( settings.btnCancel || settings.btnOK ) {
    tmp_html += "<div class=\"btn_group\">"
      +( settings.btnCancel ? "<a class=\"btn cancel\">" + settings.btnCancelText + "</a>" : "" )
      +( settings.btnOK ? "<a class=\"btn ok\">" + settings.btnOKText + "</a>" : "" )
      + "</div>";
  }
  tmp_html += "</div></div>";
  
  $("#poptips").remove();
  $("body").append( tmp_html );
    
  try {
    var scrollbarWidth = window.innerWidth - $(window).width();
    if( scrollbarWidth >= 20 || scrollbarWidth < 0 ) {
      scrollbarWidth = 0;
    }
  } catch(e) {
    var scrollbarWidth = 0;
  }

  $("body").addClass( "poptips_show" ).css( "padding-right", scrollbarWidth + "px" );
  
  if( $("#poptips .con").height() > $(window).height() - 100 ) {
    $("#poptips .con").css( "max-height", $(window).height() - 180 + "px" );
  }
  
  $("#poptips .poptips_box").css( "top", ($(window).height() - $("#poptips .poptips_box").height())/2 - 10 + "px" );

  var poptips_scrolltop = 0;
  var tmp_poptips_scrolltop = 0;

  $( "body" ).on({
    "touchstart.poptips": function(e){
      var touch = e.touches[0];
      poptips_scrolltop = $("#poptips .con").scrollTop();
      tmp_poptips_scrolltop = touch.pageY;
    },
    "touchmove.poptips": function(e){
      var e = e || window.event;
      e.preventDefault && e.preventDefault();
      var touch = e.touches[0];
      if( $(e.target).hasClass("con") ) {
        $("#poptips .con").scrollTop( poptips_scrolltop - ( touch.pageY - tmp_poptips_scrolltop ) );
      }
    },
    "touchend.poptips": function(e){
      if( ( poptips_scrolltop - $("#poptips .con").scrollTop() ) > 100 ) {
        $("#poptips .con").animate( {scrollTop: ( $("#poptips .con").scrollTop() - 20 )}, "normal", "swing" );
      } else if( ( $("#poptips .con").scrollTop() - poptips_scrolltop ) > 100 ) {
        $("#poptips .con").animate( {scrollTop: ( $("#poptips .con").scrollTop() + 20 )}, "normal", "swing" );
      }
    }
  });
  
  if( !settings.btnOK && !settings.btnCancel ) {
      $( "body" ).on("click.poptips", "#poptips", function(e){
        var e = e || window.event;
        e.stopPropagation && e.stopPropagation();
        $("body").removeClass( "poptips_show" ).css( "padding-right", "" );
        $("body").off(".poptips", "#poptips .ok")
                 .off(".poptips", "#poptips .cancel")
                 .off(".poptips", "#poptips")
                 .off("touchstart.poptips")
                 .off("touchmove.poptips")
                 .off("touchend.poptips");
        settings.btnOKClick();
      });
      $("#poptips").css("cursor", "pointer");
  } else {
      $("#poptips").css("cursor", "");
      $( "body" ).on("click.poptips", "#poptips .ok", function(e){
        var e = e || window.event;
        e.stopPropagation && e.stopPropagation();
        $("body").removeClass( "poptips_show" ).css( "padding-right", "" );
        $("body").off(".poptips", "#poptips .ok")
                 .off(".poptips", "#poptips .cancel")
                 .off(".poptips", "#poptips")
                 .off("touchstart.poptips")
                 .off("touchmove.poptips")
                 .off("touchend.poptips");
        settings.btnOKClick();
      });
      $( "body" ).on("click.poptips", "#poptips .cancel", function(e){
        var e = e || window.event;
        e.stopPropagation && e.stopPropagation();
        $("body").removeClass( "poptips_show" ).css( "padding-right", "" );
        $("body").off(".poptips", "#poptips .ok")
                 .off(".poptips", "#poptips .cancel")
                 .off(".poptips", "#poptips")
                 .off("touchstart.poptips")
                 .off("touchmove.poptips")
                 .off("touchend.poptips");
        settings.btnCancelClick();
      });
  }

}})(jQuery);
/** 弹出提示插件 结束 **/




//移除特殊字符
function stripscript(s) { 
  var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")  //格式 RegExp("[在中间定义特殊过滤字符]")
  var rs = ""; 
  for (var i = 0; i < s.length; i++) { 
    rs = rs+s.substr(i, 1).replace(pattern, ''); 
  }
  return rs;
}

//获取URL参数
function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  //if (r != null) return unescape(r[2]); return null;
  if (r != null) return decodeURI(r[2]); return null;
}

//汉字转编码
function toUtf8(str) {
  var out, i, len, c;
  out = "";
  len = str.length;
  for(i = 0; i < len; i++) {
    c = str.charCodeAt(i);
    if ((c >= 0x0001) && (c <= 0x007F)) {
      out += str.charAt(i);
    } else if (c > 0x07FF) {
      out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
      out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
      out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
    } else {
      out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
      out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
    }
  }
  return out;
}

/**
 * 用于把用utf16编码的字符转换成实体字符，以供后台存储
 * @param  {string} str 将要转换的字符串，其中含有utf16字符将被自动检出
 * @return {string}     转换后的字符串，utf16字符将被转换成&#xxxx;形式的实体字符
 */
function utf16toEntities(str) {
  var patt=/[\ud800-\udbff][\udc00-\udfff]/g; // 检测utf16字符正则
  str = str.replace(patt, function(char){
    var H, L, code;
    if (char.length===2) {
      H = char.charCodeAt(0); // 取出高位
      L = char.charCodeAt(1); // 取出低位
      code = (H - 0xD800) * 0x400 + 0x10000 + L - 0xDC00; // 转换算法
      return "&#" + code + ";";
    } else {
      return char;
    }
  });
  return str;
}

//转实体字符
function HTMLEnCode(str) {
  var s = "";
  if(str.length == 0) return "";
  s = str.replace(/&/g, "&amp;");
  s = s.replace(/</g, "&lt;");
  s = s.replace(/>/g, "&gt;");
  s = s.replace(/ /g, "&nbsp;");
  s = s.replace(/\'/g, "'");
  s = s.replace(/\"/g, "&quot;");
  s = s.replace(/\n/g, "<br>");
  return s;
}

function HTMLDeCode(str) {
  var s = "";
  if(str.length == 0) return "";
  s = str.replace(/&amp;/g, "&");
  s = s.replace(/&lt;/g, " <");
  s = s.replace(/&gt;/g, ">");
  s = s.replace(/&nbsp;/g, " ");
  s = s.replace(/'/g, "\'");
  s = s.replace(/&quot;/g, "\"");
  s = s.replace(/<br>/g, "\n");
  return s;
}



//生成通用全屏黑幕（黑色遮罩）
(function($){
$.fn.hasShadow = function( options ) {
  if( options == "hide" ) {
    $("#shadow").fadeOut("fast");
    $("body").off(".hasShadow")
             .removeClass( "shadow_show" )
             .css("margin-top", $("body").data("margin-top"))
             .scrollTop( $("body").data("scrollTop") )
             .removeData("margin-top scrollTop");
  } else {
    if( !$("#shadow").length ) {
      $("body").append( "<div id=\"shadow\"></div>" );
    }
    $("#shadow").fadeIn("fast");
    $( "body" ).on("touchmove.hasShadow", "#shadow", function(e){//click
      return false;
    });
    if( !$("body").data("scrollTop") ) {
      $("body").data({"margin-top": $("body").css("margin-top"), "scrollTop": $("body").scrollTop()});
    }
    $("body").css("margin-top", ( $("body").data("margin-top").replace("px","") - $("body").data("scrollTop") ))
             .addClass( "shadow_show" );
  } 
}})(jQuery);



/**
上传并裁切图片
UPDATE: 2016/09/26
先这样，有空再改
用法：
$(".form_imgupload").cropimgupload(function (){});
**/
(function($){
$.fn.cropimgupload = function( options ) {
  var defaults = {
    file_data: "",
    max_side: 640,
    max_height: 1000,
    aspect_ratio: 0,
    btnOKClick: function (){},
    btnCancelClick: function (){}
  };
  var settings = $.extend({}, defaults, options);
  
  //弹出界面
  if( !this[0] ){
    cropimgupload_init();
  } else {
    this.on( "click", function(){
      cropimgupload_init();
    });
  }
  
  var jcrop_api;
  
  function cropimgupload_init() {
    
    
    var imgupload_x, imgupload_y, imgupload_w, imgupload_h;
    var imgupload_scale = 1;
    
    if( $( settings.file_data ).val() ) {
      $("#imgupload_preview").prop( "src", $( settings.file_data ).val() );
      $("#imgupload_file_data").val( $( settings.file_data ).val() );
    } else {
      $("#imgupload_preview").prop( "src", $("#imgupload_preview").data("blank") );
      $("#submit_imgupload").hide();
    }
  
    $.fn.hasShadow();
    $("#part_imgupload").show();
    try {
      jcrop_api.destroy();
    } catch(e) {}


    //获得图片原始尺寸
    function getImgNaturalDimensions(img, callback) {
      var nWidth, nHeight;
      if (img.naturalWidth) { // 现代浏览器
        nWidth = img.naturalWidth;
        nHeight = img.naturalHeight;
      } else { // IE6/7/8
        var image = new Image()
        image.src = img.src;
        image.onload = function() {
          callback(image.width, image.height);
        }
      }
      return [nWidth, nHeight];
    }
    

    
    
    function updateCoords(c) {

      $("#btn_crop_ok").off( ".click_crop_ok");
      $("#btn_crop_cancel").off( ".click_crop_cancel");
      //确认裁切
      $("#btn_crop_ok").on( "click.click_crop_ok", function(){

        imgupload_scale = getImgNaturalDimensions( document.getElementById("imgupload_preview") )[0] / $("#imgupload_preview").width();
        
        imgupload_x = c.x * imgupload_scale;
        imgupload_y = c.y * imgupload_scale;
        imgupload_w = c.w * imgupload_scale;
        imgupload_h = c.h * imgupload_scale;
        
        $.fn.getimgdata({
          img: $("#imgupload_file_data").val(),
          dir: 0,
          crop_x: imgupload_x,
          crop_y: imgupload_y,
          crop_w: imgupload_w,
          crop_h: imgupload_h,
          next: function(data){

            $("#imgupload_preview").prop( "src", data ).css({"width":"","height":""});
          
            $("#imgupload_file_data").val( data );
            
            try {
              jcrop_api.destroy();
            } catch(e) {}
            
            $("#imgupload_preview").Jcrop({
              aspectRatio: settings.aspect_ratio,
              onSelect: updateCoords,
              onRelease: function(){
                $("#btn_crop_ok").off( ".click_crop_ok");
              }
            },function(){
              jcrop_api = this;
            });
            
            //imgupload_natural_w = getImgNaturalDimensions( document.getElementById("imgupload_preview") )[0];
            
            //imgupload_natural_h = getImgNaturalDimensions( document.getElementById("imgupload_preview") )[1];

          }
        });
        
        //$(this).hide();
        $("#crop_bottombar").hide();
        //$("#btn_crop_ok").off( ".click_crop_ok");
      });
      $("#btn_crop_cancel").on( "click.click_crop_cancel", function(){
        try {
          jcrop_api.release();
        } catch(e) {}
        $("#crop_bottombar").hide();
      });
      
      $("#crop_bottombar").show();
      //$("#imgupload_bottombar").hide();
    };
    



    $("#imgupload_file").on("change.change_imgupload_file", function() {
      //var exp = /.jpg$|.gif$|.png$|.bmp$/;
      var expFilter = /^(image\/jpeg|image\/gif|image\/png)$/i;
      var file = this.files[0];
      var orientation = 0;
      
      if( !$(this).val() ){
        //$("#user_avatar_file_preview").css( "background-image", "url( ./carnival/img/photo_upload.png )" );
        return false;
      }
      
      //if( !exp.test(file.value) ){
      if( !expFilter.test(file.type) ){
        $.fn.poptips("图片不支持，仅支持JPG、GIF、PNG图片格式");
        return false;
      }
      
      //$("#user_avatar .user_profile_tip").html( "载入预览中..." );
      
      EXIF.getData( file, function(){
        //orientation = EXIF.getTag( this, "Orientation");
        var file_exif = EXIF.getAllTags(this);
        orientation = file_exif.Orientation;
      });
      

      var reader = new FileReader();
      
      reader.onload = function(e) {

        $.fn.getimgdata({
          img: this.result,
          dir: orientation,
          maxside: settings.max_side,
          maxheight: settings.max_height,
          next: function(data){

            $("#imgupload_preview").prop( "src", data ).css({"width":"","height":""});
          
            $("#imgupload_file_data").val( data );
            
            try {
              jcrop_api.destroy();
            } catch(e) {}

            
            
            $("#imgupload_preview").Jcrop({
              aspectRatio: settings.aspect_ratio,
              onSelect: updateCoords,
              onRelease: function(){
                $("#btn_crop_ok").off( ".click_crop_ok");
                $("#btn_crop_cancel").off( ".click_crop_cancel");
              }
            },function(){
              jcrop_api = this;
              
              //如果限制了比例则自动裁选
              if( settings.aspect_ratio ) {
                var tmp_select_initial = Math.min($("#imgupload_preview").width(), $("#imgupload_preview").height());
                jcrop_api.setSelect([ 0,0,tmp_select_initial,tmp_select_initial ]);
              }

            });
            
            imgupload_scale = getImgNaturalDimensions( document.getElementById("imgupload_preview") )[0] / $("#imgupload_preview").width();

          }
        });
        
        
      }
      
      reader.readAsDataURL( file );
      
      $("#submit_imgupload").show();

    });


    //取消插入图片
    $("#part_imgupload .bottombar_btn_cancel, #part_imgupload .btn_cancel").on( "click.imgupload_cancel", function(){
      $("#part_imgupload").hide();
      $.fn.hasShadow("hide");
      
      $("#crop_bottombar").hide();
      //$("#imgupload_bottombar").show();
      
      $("#btn_crop_ok").off( ".click_crop_ok");
      $("#btn_crop_cancel").off( ".click_crop_cancel");
      $("#imgupload_file").off(".change_imgupload_file").val("");
      $("#part_imgupload .bottombar_btn_cancel, #part_imgupload .btn_cancel").off( ".imgupload_cancel");
      $("#submit_imgupload").off( ".imgupload_ok");
      $("#imgupload_preview").css({"width":"","height":""});
      try {
        jcrop_api.destroy();
      } catch(e) {}
    });
    
    //确认插入图片
    $("#submit_imgupload").on( "click.imgupload_ok", function(){

      if( !$("#imgupload_file_data").val() ){
         $.fn.poptips("请选择需要上传的图片，仅支持JPG、GIF、PNG图片格式");
        return false;
      }
      if( settings.file_data ) {
        $( settings.file_data ).val( $("#imgupload_file_data").val() );
      }
      
      settings.btnOKClick( $("#imgupload_file_data").val() );
      
      $("#part_imgupload").hide();
      $.fn.hasShadow("hide");
      
      $("#crop_bottombar").hide();
      //$("#imgupload_bottombar").show();
      
      $("#btn_crop_ok").off( ".click_crop_ok");
      $("#btn_crop_cancel").off( ".click_crop_cancel");
      $("#imgupload_file").off(".change_imgupload_file").val("");
      $("#part_imgupload .bottombar_btn_cancel, #part_imgupload .btn_cancel").off( ".imgupload_cancel");
      $("#submit_imgupload").off( ".imgupload_ok").attr("style","");
      $("#imgupload_preview").css({"width":"","height":""});
      try {
        jcrop_api.destroy();
      } catch(e) {}
    });
    

    
    
  }


  return this;

}})(jQuery);
//alert( typeof origi_sports_items );
/** 上传并裁切图片 结束 **/
