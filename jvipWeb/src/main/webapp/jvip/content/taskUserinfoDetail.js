class TaskUserinfoDetail extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/taskUserinfoDetail.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("taskUserinfoDetail");
    }

    loadData(resourceId,type,callback){
        this.resourceId = resourceId;
        this.type = type;
        this.params.doLoadData();
        this.callback = callback;
    }

    getCallback(){
        return this.callback;
    }

    getType(){
        return this.type;
    }

    selector(){
        return "#taskUserinfoDetail";
    }

    requestTaskOrderDetailByResourceId(callback){
        var resourceId = this.resourceId;
        var base = loader.getBase();
        base.request({
            url:"/api/taskOrderDetailByResourceId",
            data:{
                resourceId:resourceId
            },
            success:function(result){
                if(result.success){
                    callback.success(result.data);
                }else{
                    callback.fail();
                }
            },
            error:function(){
                callback.error();
            }
        });
    }

    requestTaskUserinfoDetailByResourceId(callback){
        var resourceId = this.resourceId;
        var base = loader.getBase();
        base.request({
            url:"/api/taskUserinfoDetailByResourceId",
            data:{
                resourceId:resourceId
            },
            success:function(result){
                if(result.success){
                    callback.success(result.data);
                }else{
                    callback.fail();
                }
            },
            error:function(){
                callback.error();
            }
        });
    }

    getResourceId(){
        return this.resourceId;
    }

    init(params){
        super.init(params);
        this.params = params;
        loader.getBase().setMode("taskUserinfoDetail");
    }
}