var editRecipeMainInfoWindow;
var addIngredientWindow;
var photoUploadWindow;

$(document).ready(function(){
	editRecipeMainInfoWindow = new EditRecipeMainInfoPopUpWindow("edit_mainInfo_popup");
	addIngredientWindow = new IngredientsPopUpWindow("add_ingr_popup", addIngrToRecipe);
	photoUploadWindow = new PhotoUploadPopUpWindow("add_photo_popup");	
	//Hide PopUp windows
	editRecipeMainInfoWindow.hideWindow();
	addIngredientWindow.hideWindow();
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
function addExistingIngrToRecipe(){
	addIngredientWindow.addExistingIngrToRecipe();
}
function addNewIngredienttoRecipe(){
	addIngredientWindow.saveNewIngredient();
}

function addIngrToRecipe(ingr){
	//var ingr = addIngredientWindow.getIngredient();
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
function deleteIngredient(recipeId, ingredientId){	 
	var ingredientData = new FormData();
	ingredientData.append("recipeId", recipeId);
	ingredientData.append("ingredientId", ingredientId);
	$.ajax({type: "POST", url: "/recipe/deleteIngredient", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				var currentLine = event.target.parentNode.parentNode;
				currentLine.remove();
				alert('Игредиент успешно удалена');						
			}
			else {
				alert('Ошибка при удалении ингердиента на сервере: ' + respond.data);
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Ошибка при удалении ингердиента на сервере: ' + status);
		}
	});
}
function showVolumeTextField(ingredientId){
	$("#" + ingredientId + "VolumeTextField").attr('hidden', false);
	var value = $("#" + ingredientId + "Volume").text();
	$("#" + ingredientId + "VolumeTextField").attr('value', value);
	$("#" + ingredientId + "Volume").attr('hidden', true);
}
function changeIngredientVolume(recipeId, ingredientId){	
	var newValue = $("#" + ingredientId + "VolumeTextField").prop("value");
	if (isNaN(newValue)==true){
		alert("Вы ввели значение, не являющееся числом");
	}
	else{
		var ingredientData = new FormData();
		ingredientData.append("recipeId", recipeId);
		ingredientData.append("ingredientId", ingredientId);
		ingredientData.append("newValue", newValue);
		$.ajax({type: "POST", url: "/recipe/editIngredientVolume", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {``
				if (typeof respond.error === 'undefined') {	
					$("#" + ingredientId + "Volume").text(newValue);
					var calorieFactor = $("#" + ingredientId + "CalorieFactor").prop("value");
					var calorieValue = calorieFactor * newValue / 100;
					$("#" + ingredientId + "Calorie").text(calorieValue);						
				}
				else {
					alert('Ошибка при редактирования значения на сервере: ' + respond.data);
				}
			}, 
			error: function(respond, status, jqXHR) {
				alert('Ошибка при редактирования значения на сервере: ' + status);
			},
			complete: function(){
				$("#" + ingredientId + "Volume").attr('hidden', false);
				$("#" + ingredientId + "VolumeTextField").attr('hidden', true);
			}
		});
	}
}
function getIngrListFromServer(){
	addIngredientWindow.getIngrListFromServer();
}
function setTotalCalorie(){

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

