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
        this.ingrTypeSelect = $('#ingrType');
        this.ingrListSelect = $('#ingrName');

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
        return this.newIngrProtTextField.value;
    }

    getNewIngrFat(){
        return this.newIngrFatTextField.value;
    }

    getNewIngrCarbo(){
        return this.newIngrCarboTextField.value;
    }

    getIngrListFromServer(){
        var selectedType = $('#ingrType').val();
        var requestedIngredientsList = new Array();
        $.ajax({type: "GET", url: "/ingredient/getIngredients?ingrType=" + selectedType, async: false, cache: false, dataType: 'json', contentType: false, processData: false,
            success: function(respond, status, jqXHR){
                if (typeof respond.error === 'undefined') {
                    requestedIngredientsList = respond;
                }
            }
        });
        
        $('#ingrName').empty();	
        $('#ingrName').append('<option> </option>');
        $.each(requestedIngredientsList, function(index, value){
            $('#ingrName').append('<option>'+requestedIngredientsList[index].name+'</option>');
        });
    }

    saveNewIngredient(){
        var name = this.getNewIngrName();
        var type = this.getNewIngrType(); 
        var descr = this.getNewIngrDescription();
        var prot = this.getNewIngrProt();
        var fat = this.getNewIngrFat();
        var carbo = this.getNewIngrCarbo();
        if(isNaN(prot) || isNaN(fat) || isNaN(carbo)){
            alert("Одно из введенных Вами значений БЖУ не является числом")
        }
        else{
            var ingr = new ingredient("-", name, type, descr, prot, fat, carbo);
            var savingMessage = ingr.save();
            if (ingr.isSaved()) {
                try{
                    this.addingFunction(ingr);
                }
                catch{
                    console.log("Отсутствует таблица для сохранения");
                }
                this.hideWindow();
            } else {
                alert(savingMessage);
            }
        }
    }
    addExistingIngrToRecipe(){
        var ingr = new ingredient();
        var selectedType = $('#ingrType').val();
        var selectedName = $('#ingrName').val();
        ingr.getDataFromServer(selectedName, selectedType);
        var ingredientVolume = new IngredientVolume(ingr);
        this.addingFunction(ingredientVolume);
        this.hideWindow();
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
        this.newYoutubeLinkTextField = document.getElementById("editYoutubeLink");
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
    getNewYoutubeLink(){
        return this.newYoutubeLinkTextField.value;
    }
    getNewText(){
        return this.newTextTextField.value;
    }

    setRecipeValues(name, type, tagline, youtubeLink, text){
        this.newNameTextField.value = name;
        this.newTypeTextField.value = type;
        this.newTaglineTextField.value = tagline;
        this.newYoutubeLinkTextField.value = youtubeLink;
        this.newTextTextField.value = text;
    }

    editMainInfo(recipeId){
        var mainInfoData = new FormData();
        mainInfoData.append("id", recipeId);
        mainInfoData.append("name", this.getNewName());
        mainInfoData.append("type", this.getNewType());
        mainInfoData.append("tagline", this.getNewTagline());
        mainInfoData.append("youtubeLink", this.getNewYoutubeLink());
        mainInfoData.append("text", this.getNewText());

        var done = false;
        $.ajax({type: "POST", url: "/recipe/editMainInfo", async:false, cache: false, dataType: 'json', contentType: false, processData: false, data: mainInfoData,
            success: function(respond, status, jqXHR) {
                if (typeof respond.error === 'undefined') {		
                    done = true;					
                }
                else {
                    done = false;
                }
            }, 
            error: function(respond, status, jqXHR) {
                done = false;
            }
        });
        return done;	
    }

}

class CarouselPopUpWindow extends PopUpWindow{
    constructor(id){
        super(id);
    }
}


class AddNewUserPopUpWindow extends PopUpWindow{
    constructor(id){
        super(id);      
    }
}

class WaitingPopUpWindow extends PopUpWindow{
    constructor(id){
        super(id);
        this.title = $('#waitingWindowTitle');
    }

    setTitle(titleText){
        this.title.text(titleText);
    }
}