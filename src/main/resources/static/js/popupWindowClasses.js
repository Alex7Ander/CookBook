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

    constructor(id, addingToMainTableFunction){
        super(id);
        this.addingFunction = addingToMainTableFunction;
        this.ingrTypeSelect = document.getElementById("ingrType");
        this.ingrListSelect = document.getElementById("ingrName");

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
        $("#select option:first").prop("selected", true);
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
            try{
                this.addingFunction(ingr);
            }
            catch{
                console.log("Отсутствует таблица для сохранения byuhtlbtynf");
            }
            this.hide();
        }
    }
    addExistingIngrToRecipe(){
        var ingr = this.getIngredient();
        this.addingFunction(ingr);
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

class EditRecipeMainInfoPopUpWindow extends PopUpWindow{
    constructor(id){
        super(id);
        this.newNameTextField = document.getElementById("editName");
        this.newTypeTextField = document.getElementById("editType");
        this.newTaglineTextField = document.getElementById("editTagline");
        this.newTextTextField = document.getElementById("editText");
    }

    getNewName(){
        return this.newNameTextField.value;
    }
    getNewType(){
        return this.newTypeTextField.value;
    }
    getNewTagline(){
        return this.newTaglineTextField.value;
    }
    getNewText(){
        return this.newTextTextField.value;
    }

    setRecipeValues(name, type, tagline, text){
        this.newNameTextField.value = name;
        this.newTypeTextField.value = type;
        this.newTaglineTextField.value = tagline;
        this.newTextTextField = text;
    }

    editMainInfo(recipeId){
        var mainInfoData = new FormData();
        mainInfoData.append("id", recipeId);
        mainInfoData.append("name", this.getNewName());
        mainInfoData.append("type", this.getNewType());
        mainInfoData.append("tagline", this.getNewTagline());
        mainInfoData.append("text", this.getNewText());

        $.ajax({type: "POST", url: "/recipe/editMainInfo", cache: false, dataType: 'json', contentType: false, processData: false, data: mainInfoData,
            success: function(respond, status, jqXHR) {
                if (typeof respond.error === 'undefined') {		
                    return true;					
                }
                else {
                    return false;
                }
            }, 
            error: function(respond, status, jqXHR) {
                return false;
            }
        });	
    }

}