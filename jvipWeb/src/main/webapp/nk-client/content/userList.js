class UserList extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/userList.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("userList");
    }

    selector(){
        return "#userList";
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("userList");
    }
}