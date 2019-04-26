class TaskUpload extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/taskUpload.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("taskUpload");
    }

    selector(){
        return "#taskUpload";
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("taskUpload");
    }
}