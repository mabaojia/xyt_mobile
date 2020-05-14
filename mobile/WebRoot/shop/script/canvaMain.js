;	

var canvasMain = {};

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
const getCanvasBg = function(type,json,cb){

	//console.log(json);

	json.objects.forEach((item,index)=>{
		if(item.mediaType == 'camera'){
			json.objects.splice(index,1);
		}
		if(type==1){
			if(item.mediaType == 'bg'){
				// item.fill = 'rgb(255, 255, 255,0)'
			}
		}else{
			if(item.mediaType == 'bg'){
				json.objects.splice(index,1);
			}
		}
	})

	console.log(json)
	typeof cb == "function" && cb(json);
}


//把当前画布保存成图片
canvasMain.returnCanvasImg = function(width){
	var that= this;
	canvasMain.outputWidth = width || 500 ;
	canvasMain.outputHeight = canvasMain.outputWidth/canvasMain.plateRATIO;
	//新的缩放比例
	var afterCutRATIO = canvasMain.outputWidth/canvasMain.oldCutWidth;
	//执行新的缩放
	that.mainCanvas.zoomToPoint(new fabric.Point(canvasMain.width/2,canvasMain.height/2),afterCutRATIO);
	//生成剪切图片
	var dataURL = that.mainCanvas.toDataURL({
		  format: 'png',
		  left: canvasMain.width/2 -canvasMain.outputWidth/2 ,
		  top: 	canvasMain.height/2 - canvasMain.outputHeight/2,
		  width: canvasMain.outputWidth,
		  height: canvasMain.outputHeight
	});
	return dataURL;
}



canvasMain.mainCanvas = new fabric.Canvas('diy',{
	selectionColor: 'rgba(0,0,0,0.1)',
	controlsAboveOverlay:true,
	preserveObjectStacking:true,
	backgroundColor:'rgba(255,255,255,0)',
	selctionBorderColor:'#000',
	selectionDashArray:[3,6],
	selectionLineWidth: 1,
	width:100,
	height:100
});


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

var bgRect = '';

canvasMain.loadFromJSON = function (type,json,cb){

	canvasMain.plateRATIO = json.plateRATIO;
	canvasMain.oldCutWidth = json.oldCutWidth;
	canvasMain.oldCutHeight = json.oldCutHeight;
	canvasMain.width = json.width;
	canvasMain.height = json.height;
	
	getCanvasBg(type,json,function(json){
		
		canvasMain.mainCanvas.loadFromJSON(json,function(){

			if(type==1){
				canvasMain.mainCanvas.clipPath = bgRect;
			}

			canvasMain.mainCanvas.width = 1;
			canvasMain.mainCanvas.height = 1;
			canvasMain.mainCanvas.setBackgroundColor('rgba(255, 255, 255, 1)',canvasMain.mainCanvas.renderAll.bind(canvasMain.mainCanvas));
			
			canvasMain.mainCanvas.renderAll.bind(canvasMain.mainCanvas);
			
	
			var imgUrlBlob = dataURLToBlob(canvasMain.returnCanvasImg(fabric.util.parseUnit(json.origWidth+'cm')*300/96));
			var imgUrl = URL.createObjectURL(imgUrlBlob);
	
			obj ={};
			obj.blob = imgUrlBlob;
			obj.url = imgUrl;
			console.log(obj);

			typeof cb=="function" && cb(obj);
	
		},function(o,rect){

			if(rect && rect.mediaType){
	            if(rect.mediaType=='bg'){
	            	console.log(rect)
	              bgRect = rect;
	            }
        	}

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

