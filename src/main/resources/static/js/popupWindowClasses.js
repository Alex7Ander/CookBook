class PopUpWindow{
    constructor(id){
        this.id = id;
    }
    showWindow(){
        $("#"+this.id).show();
    }
    hideWindow(){
        $("#"+this.id).hide();
    }
}


class IngredientsPopUpWindow extends PopUpWindow{

    constructor(id){
        super(id);
        this.ingrTypeSelect = document.getElementById("ingrType");
        this.ingrListSelect = document.getElementById('ingrList');

        this.newIngrNameTextField = document.getElementById("newIngrName");
        this.newIngrTypeTextField = document.getElementById("newIngrType");
        this.newIngrDescriptionTextField = document.getElementById("newIngrDesription");
        this.newIngrProtTextField = document.getElementById("newIngrProt");
        this.newIngrFatTextField = document.getElementById("newIngrFat");
        this.newIngrCarboTextField = document.getElementById("newIngrCarbo");
    }

    getSelectedIngrType(){
        return this.ingrTypeSelect.value;
    } 

    getSelectedIngrName(){
        return this.ingrListSelect.value;
    }

    getNewIngrName(){
        return this.newIngrNameTextField.value;
    }

    getNewIngrType(){
        return this.newIngrTypeTextField.value;
    }

    getNewIngrDescription(){
        return this.newIngrDescriptionTextField.value;
    }

    getNewIngrProt(){
        return this.getNewIngrProtTextField.value;
    }

    getNewIngrFat(){
        return this.newIngrFatTextField.value;
    }

    getNewIngrCarbo(){
        return this.newIngrCarboTextField.value;
    }

    getIngrListFromServer(){
        var selectedType = this.getSelectedIngrType();
        var element = this.ingrListSelect;
        $(element).load("/user/getIngrList", {type: selectedType});
    }

    saveNewIngredient(){
        var name = this.getNewIngrName();
        var type = this.getNewIngrType(); 
        var descr = this.getNewIngrDescription();
        var prot = this.getNewIngrProt();
        var fat = this.getNewIngrFat();
        var carbo = this.getNewIngrCarbo();
        var ingr = new ingredient(name, type, descr, prot, fat, carbo);
        ingr.save();
        if (ingr.isSaved() == true) {
            ingr.addToTable('ingrTable');
            this.hide();
        }
    }

    getIngredient(){
        var ingr = new ingredient();
		var selectedType = this.getSelectedIngrType();
		var selectedName = this.getSelectedIngrName();
        ingr.getDataFromServer(selectedName, selectedType);
        return ingr;
    }
    

}

class PhotoUploadPopUpWindow extends PopUpWindow{
    constructor(id){
        super(id);
    }
}