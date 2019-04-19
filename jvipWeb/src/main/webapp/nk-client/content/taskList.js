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


    init(params){
        super.init(params);
        loader.getBase().setMode("taskList");
    }
}