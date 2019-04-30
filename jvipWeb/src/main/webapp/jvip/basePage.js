class BasePageSearcher{
    constructor(){
        var elSearcher = new ElSearcher();
        this.elSearcher = elSearcher;

    }

    getElement(el){
        var element = this.elSearcher.getElement(el); 
        if(!element){
            element = new Object();
            this.elSearcher.addElement(el,element);
        }
        return element;
    }
    setContentPageParser(el,contentPageParser){
        this.getElement(el).contentPageParser = contentPageParser;
    }

    addAttrPageParser(el,attrPageParser){
        var attrPageParsers = this.getElement(el).attrPageParsers;
        if(!attrPageParsers){
            attrPageParsers = new Array();
            this.getElement(el).attrPageParsers = attrPageParsers;
        }

        attrPageParsers.push(attrPageParser);

    }

    getContentPageParser(el){
        return this.getElement(el).contentPageParser;
    }

    getAttrPageParsers(el){
        var attrPageParsers = this.getElement(el).attrPageParsers;
        if(!attrPageParsers){
            attrPageParsers = new Array();
            this.attrPageParsers = attrPageParsers;
        }
        return attrPageParsers;
    }
}

class BasePage{
    constructor(){
        var basePageSearcher = new BasePageSearcher();
        this.basePageSearcher = basePageSearcher;


        var attrPageParserSearcher = new ElSearcher();
        var contentPageParserSearcher = new ElSearcher();
        this.attrPageParserSearcher = attrPageParserSearcher;
        this.contentPageParserSearcher = contentPageParserSearcher;
    }
    load(el,url,callback){
		$(el).load(url,function(){
			if(callback){
				callback.success();
			}
		});
    }

    initDatagrid(selector,params){
        $(selector).datagrid(params);
    }

    gridLoadData(selector,data){
        $(selector).datagrid("loadData",data);
    }

    initTextbox(selector,params){
        $(selector).textbox(params);
    }

    initPagination(selector,params){
        $(selector).pagination(params);
    }

    initDatebox(selector,params){
        $(selector).datebox(params);
    }

    getValueFromTextbox(selector){
        return $(selector).textbox("getValue");
    }

    setValueToTextbox(selector,value){
        $(selector).textbox("setValue",value);
    }

    hide(selector){
        if(!selector){
            $(this.selector()).css("display","none");
        }else{
            $(selector).css("display","none");
        }
        
    }

    show(selector){
        if(!selector){
            $(this.selector()).css("display","block");
        }else{
            $(selector).css("display","block");
        }
        
    }

    showView(){
        var that = this;
        $(this.parentSelector()).empty();
        this.load(this.parentSelector(),this.url(),{
            success:function(){
                that.doInit();
                var selector = that.selector();
                that.initEl($(selector),1);
            }
        });
    }

    showDialog(){
        var params = new Object();
        params.width = this.dialogWidth();
        params.height = this.dialogHeight();
        params.title = this.title;
        params.cache = false;
        params.modal = true;
        params.empty = true;
        params.onClose = this.onClose;
        params.onOpen = this.onOpen;
        params.parentSelector = this.dialogParentSelector();
        this.doShowDialog(params);
    }

    closeDialog(){
        $(this.dialogParentSelector()).dialog("close");
    }

    doShowDialog(params){
        var width = params.width;
        var height = params.height;
        var title = params.title;
        var cache = params.cache;
        var modal = params.modal;
        var empty = params.empty;
        var onOpen = params.onOpen;
        var onClose = params.onClose;
        if(!cache){
            cache = false;
        }
        if(!modal){
            modal = false;
        }
        var url = this.url();

        var that = this;

        var onOpen = function(){
            params.onOpen();
        }

        $(params.parentSelector).dialog({
            title:title,
            width: width,
            height:height,
            cache: false,
            toolbar:[],
            closable:true,
            href: url,
            modal: true,
            onOpen:onOpen,
            onClose:onClose
        });
    }

    setData(data){
        if(!this.data){
            this.data = {};
        }
        this.changeData = {};
        this.oldData = {};
        /*for(var i in this.data){
            this.oldData[i] = this.data[i];
        }
        for(var name in data){
            if(data[name]!=this.data[name]){
                this.changeData[name] = data[name];
                this.data[name] = data[name];
            }
        }*/
        this.initData(data);
        this.initEl($(this.selector()),0);
        this.render(this.data,this.changeData,this.oldData);
    }

    initData(newData,parent,name){
        
        var type = typeof newData;
        if(!parent){
            parent = this;
            name = "data";
        }
        
        if(type=="object"&&!Array.isArray(newData)){
            var thisParent;
            if(!parent[name]){
                parent[name] = new Object();
            }
            thisParent = parent[name];
            for(var name in newData){
                this.initData(newData[name],thisParent,name)
            }    
        }else{
            parent[name] = newData;
            
        }
    }

    init(params){
        var that = this;
        var render = params.render;
        params.render = function(data,changeData,oldData){
            render.call(that,data,changeData,oldData);
        }
        this.params = params;
        for(var i in params){
            this[i] = params[i];
        }
    }


    dw_reflushGridData(el){
        $(el).attr("dw_initialized",false);
        this.dw_gridData(el);
    }

    dw_gridData(el){
        var rawValue = $(el).attr("dw_gridData");
        var regexp = /{{[a-z A-Z]{1,20}}}/;
        if(regexp.test(rawValue)){
            var prefixIndex = 0;
            var suffixIndex = rawValue.indexOf("}}",prefixIndex)
            var name = rawValue.substring(prefixIndex+2,suffixIndex);

            var splitNames = name.split(".");
            var data = this.data;
            for(var i = 0;i<splitNames.length;i++){
                data = data[splitNames[i]];
                if(!data){
                    data = new Object();
                    break;
                }
            }
            var initialized = $(el).attr("dw_initialized");
            if(!initialized){
                this.initDatagrid($(el),data);
                $(el).attr("dw_initialized",true);
            }else{
                this.gridLoadData($(el),data.data);
            }
            
        }
    }
       

    doInit(){
        var params = this.params;
        var data = params.data;
        if(!data){
            data = {};
        }
        
        if(params){
            
            for(var name in params){
                this[name] = params[name];
            }
           
        }
        this.data = data;
        var selector = this.selector();
        //this.initEl($(selector),1);
        
        this.render(this.data,this.changeData,this.oldData);
        
    }

    initEl(el,onInit){
        
        var el = $(el);
        var rawValue = el.attr("dw_gridData");
        if(el){
            this.dw_gridData(el);
        }
        if(onInit){
            if(el.attr("onInit")){
                this[el.attr("onInit")].call(this,null);
            }
        }

        var children = el.children();
        this.initElAttrValue(el);
        if(children&&children.length>0){
            for(var i=0;i<children.length;i++){
                this.initEl(children[i],onInit);
            }
        }else{
            this.initElTextValue(el);
        }
    }


    initElAttrValue(el){
        var attrPageParserSearcher = this.attrPageParserSearcher;
        var attrPageParsers = attrPageParserSearcher.getElement(el);
        if(!attrPageParsers){
            attrPageParsers = new Array();
            attrPageParserSearcher.addElement(el,attrPageParsers);
            var attrnames = el.get(0).getAttributeNames();
            for(var i = 0;i<attrnames.length;i++){
                if(!attrnames[i].startsWith("dw_")){
                    var attrPageParser = new AttrPageParser(el,attrnames[i]);
                    attrPageParsers.push(attrPageParser);
                    attrPageParser.parseValue();
                }
            }
        }

        for(var i=0;i<attrPageParsers.length;i++){
            attrPageParsers[i].updateData(this.data);
        }
        
    }

    initElTextValue(el){
        var contentPageParser = this.contentPageParserSearcher.getElement(el);
        if(!contentPageParser){
            contentPageParser = new ContentPageParser(el);
            this.contentPageParserSearcher.addElement(el,contentPageParser);
            contentPageParser.parseValue();
        }
        contentPageParser.updateData(this.data);
    }


    /*initElAttrValue(el){
        var attrPageParsers = this.basePageSearcher.getAttrPageParsers(el);
        if(!attrPageParsers||attrPageParsers.length==0){
            var attrnames = el.get(0).getAttributeNames();
            for(var i = 0;i<attrnames.length;i++){
                if(!attrnames[i].startsWith("dw_")){
                    var attrPageParser = new AttrPageParser(el,attrnames[i]);
                    attrPageParser.parseValue();
                }
            }
        }

        for(var i=0;i<attrPageParsers.length;i++){
            attrPageParsers[i].updateData(this.data);
        }
        
    }

    initElTextValue(el){
        var contentPageParser = this.basePageSearcher.getContentPageParser(el);
        if(!contentPageParser){
            contentPageParser = new ContentPageParser(el);
            this.basePageSearcher.setContentPageParser(contentPageParser);
            contentPageParser.parseValue();
        }
        contentPageParser.updateData(this.data);
    }*/
}