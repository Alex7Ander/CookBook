var editRecipeMainInfoWindow;
var addIngredientWindow;
var photoUploadWindow;

$(document).ready(function(){
	editRecipeMainInfoWindow = new EditRecipeMainInfoPopUpWindow("edit_mainInfo_popup");
	addIngredientWindow = new IngredientsPopUpWindow("add_ingr_popup");
	photoUploadWindow = new PhotoUploadPopUpWindow("add_photo_popup");	
	//Hide PopUp windows
	editRecipeMainInfoWindow.hideWindow();
	addIngredientWindow.hideWindow();addIngredientWindow
	photoUploadWindow.hideWindow();
});

/*Working with main info*/
function startEditMainInfo(){
	var name = document.getElementById('name').value;
	var type = document.getElementById('type').innerText;
	var tagline = document.getElementById('tagline').innerText;
	var text = document.getElementById('text').innerText;
	editRecipeMainInfoWindow.setRecipeValues(name, type, tagline, text);
	editRecipeMainInfoWindow.showWindow();
}
function editMainInfo(){	
	var recipeId = document.getElementById('id').value;
	var done = editRecipeMainInfoWindow.editMainInfo(recipeId);
	if (done===true){
		document.getElementById('name').innerText = editRecipeMainInfoWindow.getNewName();
		document.getElementById('type').innerText = editRecipeMainInfoWindow.getNewType();
		document.getElementById('tagline').innerText = editRecipeMainInfoWindow.getNewTagline();
		document.getElementById('text').innerText = editRecipeMainInfoWindow.getNewText();
		alert("Изменения успешно сохранены!");
	}
	else{
		alert("Произошла ошибка при попытке внести изменения!");
	}
	editRecipeMainInfoWindow.hide();
}
function hideEditRecipeMainInfoWindow(){
	this.editRecipeMainInfoWindow.hideWindow();
}



/* Working with ingredients list */
function showAddIngredientWindow(){
	addIngredientWindow.showWindow();
}
function hideAddIngredientWindow(){
	addIngredientWindow.hideWindow();
}
function addExistingIngrToTable(){
	var ingr = addIngredientWindow.getIngredient();
	//Adding line to table
	var tbody = document.getElementById('ingrTable').getElementsByTagName("TBODY")[0];
	var row = document.createElement("TR");	
	//Поле с итоговой калорийностью для ингредиента
	var resultCalorificValueField = document.createElement('b');
	//Поле для ввода количества ингредиента
	var volumeInput = document.createElement('input'); 
	volumeInput.setAttribute('form', 'saverecipeform');
	volumeInput.setAttribute('name', ingr.name);
	volumeInput.oninput = function() {
		var resultCalValue = ingr.getCalorificValue() * volumeInput.value / 100;
		resultCalorificValueField.innerText = resultCalValue;
	}
	//Кнопка удаления ингредиента
	var deleteBtn = document.createElement('input'); 
	deleteBtn.setAttribute("type", "button");
	deleteBtn.setAttribute("value", "Удалить");
	deleteBtn.setAttribute("onclick", "deleteIngrFromTable()");

	var col1 = document.createElement("TD");
	col1.appendChild(document.createTextNode(ingr.name));
	var col2 = document.createElement("TD");
	col2.appendChild(document.createTextNode(ingr.getCalorificValue()));
	var col3 = document.createElement("TD");
	col3.appendChild(volumeInput);
	var col4 = document.createElement("TD");
	col4.appendChild(resultCalorificValueField);
	var col5 = document.createElement("TD");		
	col5.appendChild(deleteBtn);	
	
	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	row.appendChild(col5);
	tbody.appendChild(row);
}
function deleteIngredient(){
	var currentLine = event.target.parentNode.parentNode;
	currentLine.remove();
}
function showVolumeTextField(ingredientName){
	var textField = document.getElementById(ingredientName + "VolumeTextField");
	var label =  document.getElementById(ingredientName + "Volume");
	textField.hidden = false;
	textField.value = label.innerHTML;
	label.hidden = true;
}
function showVolumeSpan(ingredientName){
	var textField = document.getElementById(ingredientName + "VolumeTextField");
	var label =  document.getElementById(ingredientName + "Volume");		
	var newValue = textField.value;
	if (isNaN(newValue)==true){
		alert("Вы ввели значение, не являющееся числом");
	}
	else{
		label.hidden = false;
		label.innerText = newValue;
		textField.hidden = true;
	}
}


/* 
	Working with photos 
*/
function showPhotoUploadWindow(){
	photoUploadWindow.showWindow();
}
function hidePhotoUploadWindow(){
	photoUploadWindow.hideWindow();
}
function addNewPhoto(){
	//
}
function deletePhoto(){

}