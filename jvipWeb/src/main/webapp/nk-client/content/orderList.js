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


    init(params){
        super.init(params);
        loader.getBase().setMode("orderList");
    }
}