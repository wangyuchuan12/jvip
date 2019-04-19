class UserInfo extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/userInfo.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("userInfo");
    }

    selector(){
        return "#userInfo";
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("userInfo");
    }
}