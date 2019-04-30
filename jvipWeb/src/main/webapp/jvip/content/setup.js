class Setup extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/setup.html";
    }

    parentSelector(){
        return "#dialogContent";
    }

    dialogParentSelector(){
        return "#dialogContent";
    }

    dialogWidth(){
        return 1000;
    }

    dialogHeight(){
        return 500;
    }

    showView() {
        super.showView();
        loader.getBase().setMode("setup");
    }

    showDialog(callback){
        this.title = "设置配置信息";
        super.showDialog();
        this.callback = callback;
    }

    selector(){
        return "#setup";
    }

    taskList(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/taskList",
            data:{

            },
            success:function(result){
                callback.success(result);
            }
        });
    }

    onOpen(){

    }

    onClose(){

    }

    submitGradeConfig(configs,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/flushGradeConfig",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: "POST",
            data:JSON.stringify(configs),
            success:function(result){
                callback.success(result);
            }
        });
    }

    submitIntegralConfig(configs,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/flushIntegralConfig",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: "POST",
            data:JSON.stringify(configs),
            success:function(result){
                callback.success(result);
            }
        });
    }

    submitPurchaseConfig(configs,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/flushPurchaseConfig",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            type: "POST",
            data:JSON.stringify(configs),
            success:function(result){
                callback.success(result);
            }
        });
    }

    requestGradeConfig(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/gradeConfig",
            data:{

            },
            success:function(result){
                callback.success(result);
            }
        });
    }

    requestPurchaseConfigs(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/purchaseConfigs",
            data:{

            },
            success:function(result){
                callback.success(result);
            }
        });
    }

    requestIntegralConfigs(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/integralConfigs",
            data:{

            },
            success:function(result){
                callback.success(result);
            }
        });
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("taskList");
        params.render();
        //this.callback.onRender();
    }
}