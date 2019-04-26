class ContentPageParser extends BasePageParser{
    constructor(el){
        super();
        this.el = el;
    }

    getRawValue(){
        var rawValue = this.el.text();
        return rawValue;
    }

    doUpdateData(value){
        this.el.text(value);
    }
}