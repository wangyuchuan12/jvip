class Util{
    elEquals(el1,el2){
        var id1 = el1.attr("id");
        var id2 = el2.attr("id");
        if(id1!=id2){
            return false;
        }else if(!id1&&!id2){
           try{
            return el1.get(0).outerHTML==el2.get(0).outerHTML;
           }catch(e){
                /*alert(JSON.stringify(el1));
                alert(id1);
                alert(id2);*/
           }
            
        }else{
            return true;
        }
    }

    clone(obj){
        var b = {};
        $.extend(b,obj);
        return b;
        let temp = null;
        if(obj instanceof Array){
            temp = obj.concat();
        }else if(obj instanceof Function){
            //函数是共享的是无所谓的，js也没有什么办法可以在定义后再修改函数内容
            temp = obj;
        }else{
            temp = new Object();
            for(let item in obj){
                let val = obj[item];
                temp[item] = typeof val == 'object'?this.clone(val):val; //这里也没有判断是否为函数，因为对于函数，我们将它和一般值一样处理
            }
        }
        return temp;
    }

    S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }


    timestampToDate(shijianchuo)
    {
        if(!shijianchuo){
            return "";
        }
        function add0(m){return m<10?'0'+m:m }
        var time = new Date(shijianchuo);
        var y = time.getFullYear();
        var m = time.getMonth()+1;
        var d = time.getDate();
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
    }
      
    guid() {
        return (
          this.S4() +
          this.S4() +
          "-" +
          this.S4() +
          "-" +
          this.S4() +
          "-" +
          this.S4() +
          "-" +
          this.S4() +
          this.S4() +
          this.S4()
        );
      }

    portIsOccupied (port,callback) {
        var net = require('net')
        // 创建服务并监听该端口
        var server = net.createServer().listen(port)
      
        server.on('listening', function () {
          server.close() // 关闭服务
          callback.free();
          
        })
      
        server.on('error', function (err) {
          if (err.code === 'EADDRINUSE') { // 端口已经被使用
            callback.occupy();
          }
          server.close();
        })
    }

    getFreePort(port,callback){
        var that = this;
		this.portIsOccupied(port,{
            free:function(){
                callback.success(port);
            },
            occupy:function(){
                that.getFreePort(port+1,callback);
            }
        });
	}
}

//搜索器
class Searcher{
    constructor(){

    }
    addElement(key,element){
        var array = this.array;
        if(!array){
            array = new Array();
            this.array = array;
        }

        for(var i=0;i<array.length;i++){
            var obj = array[i];
            if(this.equals(key,obj.key)){
                return;
            }
        }
        array.push({
            key:key,
            element:element
        });
    }

    getElement(key){
        var array = this.array;
        if(!array){
            array = new Array();
            this.array = array;
        }

        for(var i=0;i<array.length;i++){
            var obj = array[i];
            if(this.equals(key,obj.key)){
                return obj.element;
            }
        }

    }
}

class ElSearcher extends Searcher{
    equals(el,el2){
        var util = new Util();
        return util.elEquals(el,el2);
    }
}
