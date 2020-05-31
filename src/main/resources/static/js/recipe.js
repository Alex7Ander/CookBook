var editRecipeMainInfoWindow;
var addIngredientWindow;
var photoUploadWindow;
var carouselWindow;

var currentCaruselActiveSlide;

$(document).ready(function(){
	editRecipeMainInfoWindow = new EditRecipeMainInfoPopUpWindow("edit_mainInfo_popup");
	addIngredientWindow = new IngredientsPopUpWindow("add_ingr_popup", addIngrToTable);
	photoUploadWindow = new PhotoUploadPopUpWindow("add_photo_popup");	
	carouselWindow = new CarouselPopUpWindow("carousel_recipe_photos");
	//Hide PopUp windows
	editRecipeMainInfoWindow.hideWindow();
	addIngredientWindow.hideWindow();
	photoUploadWindow.hideWindow();
	carouselWindow.hideWindow();
});

/*Working with main info*/
function startEditMainInfo(){
	var name = document.getElementById('name').innerText;
	var type = document.getElementById('type').innerText;
	var tagline = document.getElementById('tagline').innerText;
	var youtubeLink = document.getElementById('youtubeLink').href;
	var text = document.getElementById('text').innerText;
	editRecipeMainInfoWindow.setRecipeValues(name, type, tagline, youtubeLink, text);
	editRecipeMainInfoWindow.showWindow();
}
function editMainInfo(){	
	var recipeId = document.getElementById('recipeId').value;
	var done = editRecipeMainInfoWindow.editMainInfo(recipeId);
	if (done===true){
		document.getElementById('name').innerText = editRecipeMainInfoWindow.getNewName();
		document.getElementById('type').innerText = editRecipeMainInfoWindow.getNewType();
		document.getElementById('tagline').innerText = editRecipeMainInfoWindow.getNewTagline();
		document.getElementById('youtubeLink').href = editRecipeMainInfoWindow.getNewYoutubeLink();
		document.getElementById('text').innerText = editRecipeMainInfoWindow.getNewText();
		alert("Изменения успешно сохранены!");
	}
	else{
		alert("Произошла ошибка при попытке внести изменения!");
	}
	editRecipeMainInfoWindow.hideWindow();
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

function addIngrToTable(ingredientVolume){ 
	var tbody = document.getElementById('ingrTable').getElementsByTagName("TBODY")[0];
	var row = document.createElement("TR");	

	//Поле с итоговой калорийностью для ингредиента
	var resultCalorificValueField = document.createElement('b');

	//Поле с количеством ингредиента и поле для редактирования
	var volumeTextField = document.createElement('input');
	volumeTextField.setAttribute("type", "text");
	volumeTextField.id = ingredientVolume.ingrId + "VolumeTextField";
	volumeTextField.oninput = function() {
		resultCalorificValueField.innerText = ingredientVolume.calorie * volumeTextField.value / 100;
	}
	var volumeLabel = document.createElement('b');
	volumeLabel.hidden= true;
	volumeLabel.id = ingredientVolume.ingrId + "Volume";

	//Кнопка удаления ингредиента
	var deleteBtn = document.createElement('input'); 
	deleteBtn.setAttribute("type", "button");
	deleteBtn.setAttribute("value", "Удалить");
	deleteBtn.setAttribute("id", ingredientVolume.ingrId + "DeleteBtn");
	deleteBtn.onclick = function(){
		event.target.parentNode.parentNode.remove();
	}

	//Кнопка сохранения
	var saveBtn = document.createElement('input'); 
	saveBtn.setAttribute("type", "button");
	saveBtn.setAttribute("value", "Сохранить");
	var recipeId = document.getElementById("recipeId").value;
	saveBtn.onclick = function(){
		saveIngredientInRecipe(recipeId, ingredientVolume.ingrId, volumeTextField.value);
		event.target.parentNode.remove();		
	}

	var col1 = document.createElement("TD");
	col1.appendChild(document.createTextNode(ingredientVolume.name));
	var col2 = document.createElement("TD");
	col2.appendChild(volumeTextField);
	var col3 = document.createElement("TD");
	col3.appendChild(resultCalorificValueField);
	var col4 = document.createElement("TD");
	col4.appendChild(deleteBtn);
	var col5 = document.createElement("TD");		
	col5.appendChild(saveBtn);	
	
	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	row.appendChild(col5);
	tbody.appendChild(row);
}
function saveIngredientInRecipe(recipeId, ingredientId, volume){
	var ingredientData = new FormData();
	ingredientData.append("recipeId", recipeId);
	ingredientData.append("ingredientId", ingredientId);
	ingredientData.append("volume", volume);
	$.ajax({type: "POST", url: "/recipe/addExistingIngredient", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				//Переназначение кнопок
				var ingredientVolumeId = respond.ingredientVolumeId;
				var deleteBtn = $("#" + ingredientVolumeId + "DeleteBtn");
				deleteBtn.bind('click', deleteIngredient);
				$("#" + ingredientVolumeId + "Volume").attr('hidden', false);
				$("#" + ingredientVolumeId + "Volume").bind('click', showVolumeTextField);
				$("#" + ingredientVolumeId + "VolumeTxtField").attr('hidden', true);
				$("#" + ingredientVolumeId + "VolumeTxtField").bind('change', changeIngredientVolume);
				alert('Игредиент успешно добавлен');						
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert(respond.responseText);
			alert('Ошибка при добавлении ингердиента на сервере: ' + status);
		}
	});
}
function deleteIngredient(recipeId, ingredientVolumeId){	 
	var ingredientData = new FormData();
	ingredientData.append("recipeId", recipeId);
	ingredientData.append("ingredientVolumeId", ingredientVolumeId);
	var response = $.ajax({type: "POST", url: "/recipe/deleteIngredient", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {
				$("#" + ingredientVolumeId).closest("tr").remove();
				alert('Игредиент успешно удален');						
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Ошибка при удалении ингердиента на сервере: ' + status);
		}
	});
	var recipeInfo = JSON.parse(response.responseText);

}
function showVolumeTextField(ingredientVolumeId){
	$("#" + ingredientVolumeId + "VolumeTextField").attr('hidden', false);
	var value = $("#" + ingredientVolumeId + "Volume").text();
	$("#" + ingredientVolumeId + "VolumeTextField").attr('value', value);
	$("#" + ingredientVolumeId + "Volume").attr('hidden', true);
}
function changeIngredientVolume(recipeId, ingredientVolumeId){	
	var newValue = $("#" + ingredientVolumeId + "VolumeTextField").prop("value");
	if (isNaN(newValue)==true){
		alert("Вы ввели значение, не являющееся числом");
	}
	else{
		var ingredientData = new FormData(); 
		ingredientData.append("recipeId", recipeId);
		ingredientData.append("ingredientVolumeId", ingredientVolumeId);
		ingredientData.append("newValue", newValue);
		$.ajax({type: "POST", url: "/recipe/editIngredientVolume", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {``
				if (typeof respond.error === 'undefined') {	
					$("#" + ingredientVolumeId + "Volume").text(newValue);
					var calorieFactor = $("#" + ingredientVolumeId + "CalorieFactor").prop("value");
					var calorieValue = calorieFactor * newValue / 100;
					$("#" + ingredientVolumeId + "Calorie").text(calorieValue);						
				}
				else {
					alert('Ошибка при редактирования значения на сервере: ' + respond.data);
				}
			}, 
			error: function(respond, status, jqXHR) {
				alert('Ошибка при редактирования значения на сервере: ' + status);
			},
			complete: function(){
				$("#" + ingredientVolumeId + "Volume").attr('hidden', false);
				$("#" + ingredientVolumeId + "VolumeTextField").attr('hidden', true);
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

function showCarouselWindow(photoId){
	carouselWindow.showWindow();
	currentCaruselActiveSlide = $("#" + photoId + "Slide");
	currentCaruselActiveSlide.addClass("active");
}
function hideCarouselWindow(){
	carouselWindow.hideWindow();
	currentCaruselActiveSlide.removeClass("active");	
	currentCaruselActiveSlide = null;
}