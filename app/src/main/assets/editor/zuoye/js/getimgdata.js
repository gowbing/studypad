//2016-10-10 17:00
// @param {string} img 图片的base64
// @param {int} dir exif获取的方向信息
// @param {function} next 回调方法，返回校正方向后的base64
// max_side 最大边长，0不限制，默认1024
// encoder 图片质量，0~1，默认0.92
(function($){
$.fn.getimgdata = function( options ){

  var defaults = {
    img: "",
    dir: 0,
    max_side: 1024,
    max_height: 1,
    encoder: 0.92,
    crop_x: 0,
    crop_y: 0,
    crop_w: 0,
    crop_h: 0,
    next: function (){}
  };
  
  var settings = $.extend({}, defaults, options);
  
  
  var image=new Image();
  image.onload=function(){
    var degree=0,drawWidth,drawHeight,width,height;
    if( settings.crop_w && settings.crop_h ) {
      drawWidth=settings.crop_w;
      drawHeight=settings.crop_h;
    } else {
      drawWidth=this.naturalWidth;
      drawHeight=this.naturalHeight;
    }
//console.log( settings.encoder );
    //以下改变一下图片大小
    if( settings.max_side != 0 ) {
      if( settings.max_height > settings.max_side && drawHeight > drawWidth ) {
        var tmp_drawWidth = settings.max_height * drawWidth / drawHeight;
        if (tmp_drawWidth > settings.max_side) {
          drawWidth = settings.max_side;
          drawHeight = settings.max_side * drawHeight / drawWidth;
        } else {
          drawWidth = tmp_drawWidth;
          drawHeight = settings.max_height;
        }
      } else {
        var maxSide = Math.max(drawWidth, drawHeight);
        if (maxSide > settings.max_side) {
            var minSide = Math.min(drawWidth, drawHeight);
            minSide = minSide / maxSide * settings.max_side;
            maxSide = settings.max_side;
            if (drawWidth > drawHeight) {
                drawWidth = maxSide;
                drawHeight = minSide;
            } else {
                drawWidth = minSide;
                drawHeight = maxSide;
            }
        }
      }
    }

    var canvas=document.createElement('canvas');
    canvas.width=width=drawWidth;
    canvas.height=height=drawHeight; 
    var context=canvas.getContext('2d');
    //判断图片方向，重置canvas大小，确定旋转角度，iphone默认的是home键在右方的横屏拍摄方式
    switch(settings.dir){
       //iphone横屏拍摄，此时home键在左侧
        case 3:
            degree=180;
            drawWidth=-width;
            drawHeight=-height;
            break;
        //iphone竖屏拍摄，此时home键在下方(正常拿手机的方向)
        case 6:
            canvas.width=height;
            canvas.height=width; 
            degree=90;
            drawWidth=width;
            drawHeight=-height;
            break;
        //iphone竖屏拍摄，此时home键在上方
        case 8:
            canvas.width=height;
            canvas.height=width; 
            degree=270;
            drawWidth=-width;
            drawHeight=height;
            break;
    }
    //使用canvas旋转校正
    context.rotate(degree*Math.PI/180);
    
    if( settings.crop_w && settings.crop_h ) {
      context.drawImage(this,settings.crop_x,settings.crop_y,drawWidth,drawHeight,0,0,drawWidth,drawHeight);
    } else {
      context.drawImage(this,0,0,drawWidth,drawHeight);
    }

    //返回校正图片
    settings.next(canvas.toDataURL("image/jpeg", settings.encoder));
    
 }
  image.src=settings.img;
}})(jQuery);