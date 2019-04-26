class TaskList extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/taskList.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("taskList");
    }

    selector(){
        return "#taskList";
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

    uploadUserFile(resourceId,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/uploadUserFile",
            data:{
                resourceId:resourceId
            },
            success:function(result){
                callback.success(result);
            },
            error:function(){
                callback.error();
            }
        });
    }

    uploadOrderFile(resourceId,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/uploadOrderFile",
            data:{
                resourceId:resourceId
            },
            success:function(result){
                callback.success(result);
            },
            error:function(){
                callback.error();
            }
        });
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("taskList");
    }
}