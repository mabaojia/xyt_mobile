;	
var canvasMain = {};


//解决图片上传旋转问题
canvasMain.returnIMgFile = function(file,cb){

	EXIF.getData(file, function () {
	    var Orientation = EXIF.getTag(this, "Orientation");
	    //console.log("Orientation>>>>>>", Orientation);

	    //转换成base64
	    const reader = new FileReader();
	    reader.readAsDataURL(file);

	    reader.onload = function(e){

	        // if (Orientation == 1 || Orientation==undefined) {
	        //     //console.log("图片无需处理");
	        //     typeof cb=="function" && cb(file);
	        // } else {

            var uploadBase64 = new Image();
            uploadBase64.src = e.target.result;

            uploadBase64.onload = function () {
                //修正旋转图片
                var expectWidth = uploadBase64.width;
                var expectHeight = uploadBase64.height;

                var canvas = document.createElement("canvas"),
                    ctx = canvas.getContext("2d");
                canvas.width = expectWidth;
                canvas.height = expectHeight;

                ctx.drawImage(uploadBase64, 0, 0, expectWidth, expectHeight);
                var base64 = null;

                if (Orientation !== "" && Orientation != 1) {
                    switch (Orientation) {
                        case 6:
                            //console.log("顺时针旋转270度");
                            rotateImg(uploadBase64, "left", canvas);
                            break;
                        case 8:
                            //console.log("顺时针旋转90度");
                            rotateImg(uploadBase64, "right", canvas);
                            break;
                        case 3:
                            //console.log("顺时针旋转180度");
                            rotateImg(uploadBase64, "horizen", canvas);
                            break;
                    }
                }


                //输出转换后的base64图片
                var base64 = canvas.toDataURL(file.type, 1);

                yasuo(base64,file.type,function(imgUrl){

            	   var newBlob = dataURLToBlob(imgUrl, file.type);
	               var blobImgUrl = URL.createObjectURL(newBlob);
	               
	               typeof cb=="function" && cb(newBlob,imgUrl,blobImgUrl);

                });

            

            };

	    };
	})
}

function yasuo(imgUrl,type,cb){

	var canvas = document.createElement("canvas"),
        ctx = canvas.getContext("2d");

    var imgElement = new Image();
     imgElement.src = imgUrl;

    imgElement.onload = function () {

    	var expectWidth = imgElement.width;
        var expectHeight = imgElement.height;

        var bili = expectWidth/expectHeight;

        if(expectWidth>1000){
          expectWidth = 1000;
          expectHeight = expectWidth/bili;
        }

        canvas.width = expectWidth;
        canvas.height = expectHeight;
        ctx.drawImage(imgElement, 0, 0, expectWidth, expectHeight);

        var base64 = canvas.toDataURL(type, 1);
        typeof cb=="function" && cb(base64);

    }
}

//对图片旋转处理
function rotateImg(img, direction, canvas) {
    //console.log("开始旋转图片");
    //图片旋转4次后回到原方向
    if (img == null) return;
    var height = img.height;
    var width = img.width;
    var step = 2;

    if (direction == "right") {
        step++;
    } else if (direction == "left") {
        step--;
    } else if (direction == "horizen") {
        step = 2; //不处理
      }
    //旋转角度以弧度值为参数
    var degree = step * 90 * Math.PI / 180;
    var ctx = canvas.getContext("2d");
    switch (step) {
        case 0:
            canvas.width = width;
            canvas.height = height;
            ctx.drawImage(img, 0, 0);
            break;
        case 1:
            canvas.width = height;
            canvas.height = width;
            ctx.rotate(degree);
            ctx.drawImage(img, 0, -height);
            break;
        case 2:
            canvas.width = width;
            canvas.height = height;
            ctx.rotate(degree);
            ctx.drawImage(img, -width, -height);
            break;
        case 3:
            canvas.width = height;
            canvas.height = width;
            ctx.rotate(degree);
            ctx.drawImage(img, -width, 0);
            break;
    }
    //console.log("结束旋转图片");
}

//检查图片类型提供相应函数
const checkImgUrl = function(url,cb){

  var result=/\.svg/g.test(url);
  if(result){
    fabric.loadSVGFromURL(url, function (objects, options) {
      var obj = fabric.util.groupSVGElements(objects, options);
       typeof cb == "function" && cb(obj);
    });
  }else{
    fabric.Image.fromURL(url,cb);
  }
}

//解析背景图片
const getCanvasBg = function(json,cb){
	//console.log(json)
	if(json.objects[0].mediaType=='bg'&&json.objects[0].bgImg){
	
	 var bgImg = json.objects[0].bgImg;
	 fabric.bgWidth = json.objects[0].width;
	
	 checkImgUrl(bgImg.src,(img)=>{
	   fabric.bgImg = img;
	   typeof cb == "function" && cb();
	 })
	
	}else{
	 typeof cb == "function" && cb();
	}
}


//把当前画布保存成图片
canvasMain.returnCanvasImg = function(width){

	var outputWidth = width || 500 ;
    var outputHeight = outputWidth/canvasMain.plateRATIO;

    var canvas = canvasMain.mainCanvas;

    canvas.renderAll();
    //新的缩放比例
    var afterCutRATIO = outputWidth/canvasMain.cutWidth;
    //之前缩放比例
    var zoomNum = canvas.getZoom();
    var points = canvas.getCenter(); 

    //执行新的缩放
    canvas.zoomToPoint(new fabric.Point(points.left,points.top),afterCutRATIO);

    var x= canvas.viewportTransform[4];
    var y= canvas.viewportTransform[5];


    var dataURL = canvas.toDataURL({
      format: 'png',
      left:canvasMain.pageWidth*afterCutRATIO/2+ x - outputWidth/2,
      top: canvasMain.pageHeight*afterCutRATIO/2+y - outputHeight/2,
      width: outputWidth,
      height: outputHeight
    });

    //缩放回原来大小
    canvas.zoomToPoint(new fabric.Point(points.left,points.top),zoomNum);
    canvas.renderAll();

  	var imgUrlBlob = dataURLToBlob(dataURL);
	var imgUrl = URL.createObjectURL(imgUrlBlob);
	
	obj ={};
	obj.blob = imgUrlBlob;
	obj.url = imgUrl;

	return obj;
}



canvasMain.createCanvas = function(key,pageWidth,pageHeight,cutWidth,cutHeight){

	canvasMain.mainCanvas = new fabric.Canvas(key,{
		selectionColor: 'rgba(0,0,0,0.1)',
		controlsAboveOverlay:true,
		preserveObjectStacking:true,
		backgroundColor:'#ffffff',
		selctionBorderColor:'#000',
		selectionDashArray:[3,6],
		selectionLineWidth: 1,
		width:100,
		height:100
	});

	canvasMain.pageWidth = pageWidth;
	canvasMain.pageHeight = pageHeight;

	canvasMain.oldCutWidth = cutWidth;
	canvasMain.oldCutHeight = cutHeight;

	canvasMain.plateRATIO = (cutWidth/cutHeight).toFixed(2);

	canvasMain.mainCanvas.setWidth(pageWidth);
    canvasMain.mainCanvas.setHeight(pageHeight);

    cutArea(pageWidth,pageHeight);
}


//返回元素最合理尺寸
const returnObjsMaxWidth = function(obj,bili,h){

    //图片宽高比
    var imgRATIO = obj.width/obj.height;

    var maxBili = bili || 0.8;

    //图片最大尺寸
    var maxWidthImg = canvasMain.cutWidth*maxBili;
    var maxHeightImg = canvasMain.cutHeight*maxBili;
    var imgWidth,imgHeight;
    
    if(h){
      imgHeight = maxHeightImg;
    }else{
      if(maxHeightImg*imgRATIO >= maxWidthImg ){
        imgWidth = maxHeightImg*imgRATIO;
        imgHeight = maxHeightImg;
      }else{

        imgWidth = maxWidthImg;
        imgHeight = maxWidthImg/imgRATIO;

      }
    }


    if(h){
      return imgHeight.toFixed(0);
    }else{
      return imgWidth.toFixed(0);
    }
}

//添加照片
canvasMain.addImg = (url,cb,objects)=>{

	canvasMain.mainCanvas.clear();
  checkImgUrl(url,function(designImg){

      designImg.set({
        borderColor:'#f44',
		cornerSize:18,
		cornerStyle:"circle",
		cornerStrokeColor:'#f44',
		rotatingPointOffset:20,
		borderDashArray:[],
		transparentCorners:false,
		cornerColor:"#f44",
        ...objects
      });


      designImg.scaleToWidth(returnObjsMaxWidth(designImg,1));
      
      //centerObj(designImg);
      canvasMain.mainCanvas.add(designImg).setActiveObject(designImg).centerObject(designImg);

      designImg.setControlVisible('ml',true);
      designImg.setControlVisible('mr',true);
      designImg.setControlVisible('mt',true);
      designImg.setControlVisible('mb',true);

      canvasMain.mainCanvas.renderAll();
      canvasMain.mainCanvas.calcOffset();

      typeof cb=="function" && cb(designImg);


  },{'crossOrigin':'Anonymous'})   
}


function touchFunc(){

  var that = this;
  var canvas = canvasMain.mainCanvas;
  var target = canvas.getActiveObject();
  var baseInfo = {};

  var div_canvas = document.getElementById('div_canvas');

  touch.on(div_canvas, 'pinchstart', function (e) {
     
     if(target && target.mediaType!='bg'){
        //baseInfo = JSON.parse(JSON.stringify(that.objCur));
        baseInfo = {
          scaleX:that.objCur.scaleX,
          scaleY:that.objCur.scaleY,
          angle:that.objCur.angle
        }
     } 
  })

  touch.on(div_canvas, 'pinch', function (e) {
      var scale = e.scale;
      target.scale(baseInfo.scaleX*scale);
      canvas.renderAll();
  })

  touch.on(div_canvas, 'rotate', function (e) {
    var rotation = e.rotation;
    if(target && target.mediaType!='bg'){
        target.rotate(baseInfo.angle+ rotation); 
        canvas.renderAll();
    }
  })

}

const cutArea = (pageWidth,pageHeight)=>{

	var cutCanvas = canvasMain.cutCanvas;

	if(cutCanvas){
	var m = document.getElementById("cutCanvas");
	m.parentNode.removeChild(m);
	}

	canvasMain.cutCanvas = document.createElement('canvas');
	canvasMain.cutCanvas.width = pageWidth;
	canvasMain.cutCanvas.height= pageHeight;
	canvasMain.cutCanvas.id = 'cutCanvas';

	canvasMain.cutCanvas.style.position = 'absolute';
	canvasMain.cutCanvas.style.top = 0;
	canvasMain.cutCanvas.style.left = 0;
	canvasMain.cutCanvas.style.width = pageWidth;
	canvasMain.cutCanvas.style.height = pageHeight;
	  
	var cutCtx = canvasMain.cutCanvas.getContext('2d');

	cutCtx.fillStyle = 'rgba(0,0,0,0.4)';
	cutCtx.fillRect(0,0,pageWidth,pageHeight);
    
	//剪切区域的最大高度 和宽度
	var maxWidth = pageWidth*0.7;
	var maxHeight = pageHeight*0.7;


	if(maxHeight*canvasMain.plateRATIO < maxWidth){
		canvasMain.cutWidth = maxHeight*canvasMain.plateRATIO;
		canvasMain.cutHeight = maxHeight;
	}else {
		canvasMain.cutWidth = maxWidth;
		canvasMain.cutHeight = maxWidth/canvasMain.plateRATIO;
	}

 	cutCtx.clearRect((pageWidth-canvasMain.cutWidth)/2,(pageHeight-canvasMain.cutHeight)/2, canvasMain.cutWidth, canvasMain.cutHeight);
  	
  	$("#diy_canvas").after(canvasMain.cutCanvas);
}


function dataURLToBlob(dataurl){
	var arr = dataurl.split(',');
	var mime = arr[0].match(/:(.*?);/)[1];
	var bstr = atob(arr[1]);
	var n = bstr.length;
	var u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new Blob([u8arr], {type:mime});
}


canvasMain.loadFromJSON = function (json,cb){

	canvasMain.plateRATIO = json.plateRATIO;
	canvasMain.oldCutWidth = json.oldCutWidth;
	canvasMain.oldCutHeight = json.oldCutHeight;
	canvasMain.width = json.width;
	canvasMain.height = json.height;
	
	getCanvasBg(json,function(){
		
		canvasMain.mainCanvas.loadFromJSON(json,function(obj){
	
			canvasMain.mainCanvas.width = 1;
			canvasMain.mainCanvas.height = 1;
			canvasMain.mainCanvas.setBackgroundColor('rgba(255, 255, 255, 1)',canvasMain.mainCanvas.renderAll.bind(canvasMain.mainCanvas));
			
			canvasMain.mainCanvas.renderAll.bind(canvasMain.mainCanvas);
	
			var imgUrlBlob = dataURLToBlob(canvasMain.returnCanvasImg(fabric.util.parseUnit(json.origWidth+'cm')*300/96));
			var imgUrl = URL.createObjectURL(imgUrlBlob);
	
			obj ={};
			obj.blob = imgUrlBlob;
			obj.url = imgUrl;
	
			typeof cb=="function" && cb(obj);
	
		});
	})
	
}

//批量加载字体
canvasMain.getBatchFontFace = function(data,cb){

	$youzikuClient.getBatchFontFace(data, function (res) {
		//alert(852)
		typeof cb == "function" && cb(res);
		console.log(res)
		var list = res.FontfaceList;

		for(var i=0;i<list.length;i++){
			if(list[i].Code!=200){
				console.log(list[i].ErrorMessage);
			}
		}
	})

}

//加载字体
canvasMain.loadFont = function(json,cb){

  var webFontFamilyList ={
    Tags:[]
  }

  var englishArr = [];

  json.objects.forEach(function(item){

    if(item.mediaType=='textbox'){
      if(item.AccessKey){
        var data = {
          AccessKey:item.AccessKey,
          Content:item.text
        }
        webFontFamilyList.Tags.push(data);
      }else{

        if(item.textLoad){
          var data = {
            FontFamily:item.fontFamily
          }
          englishArr.push(data);
        }

      }
    }

  });

  canvasMain.fontArrLoad(englishArr,function(){
    if(webFontFamilyList.Tags.length>0){
        canvasMain.getBatchFontFace(webFontFamilyList,function(res){
          var FontfaceList = res.FontfaceList;
          canvasMain.fontArrLoad(FontfaceList,cb);
        });
     }else{
      typeof cb == "function" && cb();
     }
  })
}


//字体加载异步监控
canvasMain.fontArrLoad = function(FontfaceList,cb){

    if(FontfaceList.length<=0){
      typeof cb == "function" && cb();
      return;
    }
    var fontObj = {};
      FontfaceList.forEach((item,index)=>{
      fontObj['font'+index] = new FontFaceObserver(item.FontFamily);
    });

    var arr = [];
    for(var i in fontObj){
      arr.push(fontObj[i].load());
    }

    Promise.all(arr)
    .then(function (res) {
      typeof cb == "function" && cb();
      console.log('Family loaded');
    })
    .catch(function(err){
      console.log('字体加载失败');
      typeof cb == "function" && cb();
    })

}




