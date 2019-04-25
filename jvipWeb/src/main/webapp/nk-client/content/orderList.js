class OrderList extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/orderList.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("orderList");
    }

    selector(){
        return "#orderList";
    }

    orderList(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/orderList",
            data:{

            },
            success:function(result){
                callback.success(result);
            }
        });
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("orderList");
    }
}