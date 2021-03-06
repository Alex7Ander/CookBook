var newPhotoCount = 0;
var newIngrCount = 0;

var editRecipeMainInfoWindow;
var addIngredientWindow;
var photoUploadWindow;
var carouselWindow;
var waitingWindow;

var currentCaruselActiveSlide;

$(document).ready(function(){
	editRecipeMainInfoWindow = new EditRecipeMainInfoPopUpWindow("edit_mainInfo_popup");
	addIngredientWindow = new IngredientsPopUpWindow("add_ingr_popup", addIngrToTable);
	photoUploadWindow = new PhotoUploadPopUpWindow("add_photo_popup");	
	carouselWindow = new CarouselPopUpWindow("carousel_recipe_photos");
	waitingWindow = new WaitingPopUpWindow("waiting_window");
	//Hide PopUp windows
	editRecipeMainInfoWindow.hideWindow();
	addIngredientWindow.hideWindow();
	photoUploadWindow.hideWindow();
	carouselWindow.hideWindow();
	waitingWindow.hideWindow();

	$("input[id$='VolumeTextField']").each(function(){$(this).hide();});
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

function addIngrToTable(ingredient){ 
	newIngrCount++;
	var tbody = document.getElementById('ingrTable').getElementsByTagName("TBODY")[0];
	var row = document.createElement("TR");	

	//Поле с количеством ингредиента и поле для редактирования
	var volumeTextField = document.createElement('input');
	volumeTextField.setAttribute("type", "text");
	volumeTextField.id = "new" + newIngrCount + "VolumeTextField";
	var volumeLabel = document.createElement('b');
	volumeLabel.id = "new" + newIngrCount + "Volume";

	//Кнопка удаления ингредиента
	var deleteBtn = document.createElement('input'); 
	deleteBtn.setAttribute("type", "button");
	deleteBtn.setAttribute("value", "Удалить");
	deleteBtn.setAttribute("id", "new" + newIngrCount + "DeleteBtn");
	deleteBtn.onclick = function(event){
		event.target.parentNode.parentNode.remove();
	}

	//Кнопка сохранения
	var saveBtn = document.createElement('input'); 
	saveBtn.setAttribute("type", "button");
	saveBtn.setAttribute("value", "Сохранить");
	var recipeId = document.getElementById("recipeId").value;
	saveBtn.onclick = function(event){
		saveIngredientInRecipe(newIngrCount, recipeId, ingredient.ingrId, volumeTextField.value);
		event.target.parentNode.remove();		
	}

	var col1 = document.createElement("TD");
	col1.appendChild(document.createTextNode(ingredient.name));
	var col2 = document.createElement("TD");
	col2.appendChild(volumeTextField);
	col2.appendChild(volumeLabel);
	var col3 = document.createElement("TD");
	col3.appendChild(deleteBtn);
	var col4 = document.createElement("TD");		
	col4.appendChild(saveBtn);	
	
	row.appendChild(col1);
	row.appendChild(col2);
	row.appendChild(col3);
	row.appendChild(col4);
	tbody.appendChild(row);
}

function saveIngredientInRecipe(newIngrIndex, recipeId, ingredientId, volume){
	var ingredientData = new FormData();
	ingredientData.append("recipeId", recipeId);
	ingredientData.append("ingredientId", ingredientId);
	ingredientData.append("volume", volume);
	$.ajax({type: "POST", url: "/recipe/addExistingIngredient", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет добавление ингредиента");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				//Переназначение кнопок
				var ingredientVolumeId = respond.ingredientVolumeId;
				//кнопка удаление
				var deleteBtn = $("#new" + newIngrIndex + "DeleteBtn");
				deleteBtn.on('click', function(){
					deleteIngredient(ingredientVolumeId);
				});
				deleteBtn.attr('id', ingredientVolumeId + "DeleteBtn");
				//текстовое поле с количеством ингредиента
				var volumeTxtField=$("#new" + newIngrIndex + "VolumeTextField");
				volumeTxtField.on('change', function(){
					changeIngredientVolume(ingredientVolumeId);
				});	
				volumeTxtField.attr('id', ingredientVolumeId + "VolumeTextField");
				volumeTxtField.hide();
				//кол-во ингредиента в теге b
				var volumeLabel = $("#new" + newIngrIndex + "Volume");
				volumeLabel.on('click', function(){
					showVolumeTextField(ingredientVolumeId);
				});
				volumeLabel.attr('id', ingredientVolumeId + "Volume");
				volumeLabel.text(volumeTxtField.val());
				volumeLabel.attr('hidden', false);					
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert(respond.responseText);
			alert('Ошибка при добавлении ингердиента на сервере: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();			
		}
	});
}
function deleteIngredient(ingredientVolumeId){	 
	var ingredientData = new FormData();
	ingredientData.append("ingredientVolumeId", ingredientVolumeId);
	var response = $.ajax({type: "POST", url: "/recipe/deleteIngredient", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет удаление ингредиента");
			waitingWindow.showWindow();
		},	
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {
				$("#" + ingredientVolumeId).closest("tr").remove();						
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Ошибка при удалении ингердиента на сервере: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();			
		}
	});
}
function showVolumeTextField(ingredientVolumeId){
	$("#" + ingredientVolumeId + "VolumeTextField").show();
	var value = $("#" + ingredientVolumeId + "Volume").text();
	$("#" + ingredientVolumeId + "VolumeTextField").attr('value', value);
	$("#" + ingredientVolumeId + "Volume").hide();
}
function changeIngredientVolume(ingredientVolumeId){	
	var newValue = $("#" + ingredientVolumeId + "VolumeTextField").prop("value");
	var re = '/./g';
	newValue.replace(re, '.');
	var ingredientData = new FormData(); 
	ingredientData.append("ingredientVolumeId", ingredientVolumeId);
	ingredientData.append("newValue", newValue);
	$.ajax({type: "POST", url: "/recipe/editIngredientVolume", cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет редактирование количества ингредиента");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				$("#" + ingredientVolumeId + "Volume").text(newValue);
				$("#" + ingredientVolumeId + "Calorie").text(respond.calorie);								
			}
			else {
				alert('Ошибка при редактирования значения на сервере: ' + respond.data);
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Ошибка при редактирования значения на сервере: ' + status);
		},
		complete: function(){
			$("#" + ingredientVolumeId + "VolumeTextField").hide();
			$("#" + ingredientVolumeId + "Volume").show();
			waitingWindow.hideWindow();	
		}
	});
}
function getIngrListFromServer(){
	addIngredientWindow.getIngrListFromServer();
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

function getCurrentlyUploadedPhoto(){
	var photoFileLoader = document.getElementById('photoLoader');
	currentlyUploadedPhoto = photoFileLoader.files[0];
	var output = document.getElementById('uploadedPhoto');
	output.src = URL.createObjectURL(currentlyUploadedPhoto); 
}

function addPhotoToPhotoList(event){
	if(currentlyUploadedPhoto.size >= 10 * 1048576){
		alert("Размер фото превышает максимально допустимый (10 мб)");
		return;
	}
	event.stopPropagation(); // остановка всех текущих JS событий
	event.preventDefault();  // остановка дефолтного события для текущего элемента
	let photoData = new FormData();
	photoData.append('photo', currentlyUploadedPhoto);
	$.ajax({type: "POST", url: "/recipe/sendPhoto", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет отправка фотографии");
			photoUploadWindow.hideWindow();
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				newPhotoCount++;
				showUploadedPhotoOnMainPage(respond.code);							
			}
			else{
				console.log(respond.error);
				alert('Загрузки фотографии. Повторите попытку.');
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось загрузить фото: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();			
		}
	});
} 

function showUploadedPhotoOnMainPage(code){
	var element = document.getElementById('photoList');

	var newPhotoCard = document.createElement('div');
	newPhotoCard.id = "photoCard_" + code;
	newPhotoCard.className = "card";
	element.append(newPhotoCard);	

	var newPhotoCardBody = document.createElement('div');
	newPhotoCardBody.className = "card-body";
	newPhotoCard.append(newPhotoCardBody);

	// Image
	var image = document.createElement('img');
	var photoPath = document.getElementById('uploadedPhoto').src;
	image.src = photoPath;
	image.className = "d-block w-100";
	newPhotoCardBody.append(image);

	// Hidden input with hashcode
	var codeInput = document.createElement('input');
	codeInput.type = "hidden";
	codeInput.id = "hidden_" + newPhotoCount;
	codeInput.className = "hiddenInput";
	codeInput.value = code;
	newPhotoCard.append(codeInput);

	//Save link
	var saveBtn = document.createElement('input');
	saveBtn.type = "button";
	saveBtn.value = "Сохранить";
	saveBtn.id = "saveBtn" + code;
	saveBtn.className = "btn btn-primary";
	saveBtn.onclick = function(){
		saveNewPhoto(code);
	}
	newPhotoCardBody.append(saveBtn);

	// Delete link
	var deleteBtn = document.createElement('input');
	deleteBtn.type = "button";
	deleteBtn.value = "Отмена";
	deleteBtn.id = "deleteBtn" + code;
	deleteBtn.className = "btn btn-primary";
	deleteBtn.onclick = function(){
		var photoData = new FormData();
		photoData.append("code", code);
		$.ajax({type: "POST", url: "/recipe/dropUnsavedPhoto", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
			beforeSend: function(){
				waitingWindow.setTitle("Ожидайте, идет удаление ингредиента");
				waitingWindow.showWindow();
			},	
			success: function(respond, status, jqXHR){
				if( typeof respond.error === 'undefined' ){
					newPhotoCard.remove();					
				}
				else {
					console.log(respond.error);
				}
			}, 
			error: function(respond, status, jqXHR){
				alert('Не удалось удалить фото: ' + status);
			},
			complete: function(){
				waitingWindow.hideWindow();
			}
		});
	}
	newPhotoCardBody.append(deleteBtn);
}

function saveNewPhoto(code){
	var photoData = new FormData();
	photoData.append("recipeId", $("#recipeId").val());
	photoData.append("code", code);
	$.ajax({type: "POST", url: "/recipe/saveUnsavedePhoto", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет сохранение новой фотографии");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				alert("Фото успешно сохранено");
				var newPhotoId = respond.id;
				
				$("#saveBtn"+code).remove();
				var previewLabel = document.createElement('label');
				var previewInput = document.createElement('input');
				previewInput.type = "radio";
				previewInput.value = code;
				previewInput.name = "previewRb";	
				previewInput.onclick = function(){
					setPhotoAsPreviewForRecipe(respond.id);
				}
				previewLabel.append(previewInput);
				previewLabel.append("Установить как обложку для рецепта");
				$("#photoCard_" + code).append(previewLabel);

				$("#deleteBtn" + code).remove();
				var newDeleteBtn = document.createElement('input');
				newDeleteBtn.type = "button";
				newDeleteBtn.value = "Удалить";
				newDeleteBtn.id = "deleteBtn" + code;
				newDeleteBtn.className = "btn btn-primary";
				newDeleteBtn.onclick = function(){
					deletePhoto(newPhotoId);
				};
				$("#photoCard_" + code).append(newDeleteBtn);
				$("#photoCard_" + code).attr("id", "photoCard_" + newPhotoId);						
			}
			else {
				console.log(respond.error);
				alert('Ошибка сохранения фотографии:' + respond.error + '.\nПовторите попытку.');
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось сохранить фото: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();
		}
	});
}

function deletePhoto(photoId){
	var photoData = new FormData();
	photoData.append("recipeId", $("#recipeId").val());
	photoData.append("photoId", photoId);
	$.ajax({type: "POST", url: "/recipe/deletePhoto", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет удаление фотографии");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				alert("Фото успешно удалено");
				$("#photoCard_" + photoId).remove();							
			}
			else{
				alert('Ошибка удаления фотографии:' + respond.error + '.\nПовторите попытку.');
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось удалить фото: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();
		}
	});
}

function setPhotoAsPreviewForRecipe(id){
	var photoData = new FormData();
	photoData.append('recipeId',  $("#recipeId").val());
	photoData.append('photoId', id);
	$.ajax({type: "POST", url: "/recipe/setPreview", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет установка превью для рецепта");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR){
			if(! typeof respond.error === 'undefined' ){
				alert('Не удалось установить превью для рецепта: ' + respond.error);
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось установить превью для рецепта: ' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();
		}
	});
}

function showCarouselWindow(photoId){
	carouselWindow.showWindow();
	currentCaruselActiveSlide = $("#slide_" + photoId);
	currentCaruselActiveSlide.addClass("active");
}
function hideCarouselWindow(){
	carouselWindow.hideWindow();
	currentCaruselActiveSlide.removeClass("active");	
	currentCaruselActiveSlide = null;
}

function deleteRecipe(){
	var recipeData = new FormData();
	recipeData.append("recipeId", $("#recipeId").val());
	$.ajax({type: "POST", url: "/recipe/delete", async: false, cache: false, dataType: 'json', contentType: false, processData : false, data: recipeData,
		beforeSend: function(){
			waitingWindow.setTitle("Ожидайте, идет удаление рецепта");
			waitingWindow.showWindow();
		},
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				alert("Рецепт успешно удален");
				$(location).attr('href', '/cookbook/showCookbook');						
			}
			else{
				console.log(respond.error);
				alert('Ошибка удаления рецепта:\n' + respond.error);
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось удалить рецепт:\n' + status);
		},
		complete: function(){
			waitingWindow.hideWindow();
		}
	});
}