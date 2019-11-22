package org.edb.main;

import javafx.beans.property.*;

public class ExternalServiceDetail {
    private StringProperty if_achieve;
    private StringProperty name;
    private IntegerProperty idx;
    private BooleanProperty check;

    /*
    *  int if_achieve;
    int external_service_detail_idx;
    String name;*/

    public ExternalServiceDetail(int idx, String  name, int if_achieve) {
        this.idx = new SimpleIntegerProperty(idx);
        this.name = new SimpleStringProperty(name);
        if(if_achieve == 1){
            this.if_achieve = new SimpleStringProperty("달성완료");
        }else{
            this.if_achieve = new SimpleStringProperty("달성해주세요");
        }
        this.check = new SimpleBooleanProperty(false);

    }

    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty ifachieveProperty(){
        return if_achieve;
    }

    public IntegerProperty idxProperty(){return idx;}

    public BooleanProperty checkProperty() { return check; }
    public BooleanProperty getCheck() {return check; }

    public void setCheck(Boolean check) {
        this.check = new SimpleBooleanProperty(check);
    }
}
