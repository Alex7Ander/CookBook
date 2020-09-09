let currentlyUploadedPhoto;  //последнее загруженное фото
var photoCount = 0;  //Порядковый индекс загруженной фотографии

//PopUp windows
var ingredientsPopUpWindow; 
var photoUploadPopUpWindow; 
var waitingWindow;

$(document).ready(function(){
	//Скрыть PopUp при загрузке страницы  
	ingredientsPopUpWindow = new IngredientsPopUpWindow("add_ingr_popup", addIngrToTable); 
	photoUploadPopUpWindow = new PhotoUploadPopUpWindow("add_photo_popup"); 
	waitingWindow = new WaitingPopUpWindow("waiting_window");
	ingredientsPopUpWindow.hideWindow();
	photoUploadPopUpWindow.hideWindow();
	waitingWindow.hideWindow();
});

/*
Управление фотографиями
*/
function showPhotoUploadWindow(){
	photoUploadPopUpWindow.showWindow();
}
function hidePhotoUploadWindow(){
	photoUploadPopUpWindow.hideWindow();
}
function getCurrentlyUploadedPhoto(event){
	let photoFileLoader = document.getElementById('photoLoader');
	currentlyUploadedPhoto = photoFileLoader.files[0];
	let output  = document.getElementById('uploadedPhoto');
	output.src = URL.createObjectURL(currentlyUploadedPhoto); 
}

function addPhotoToPhotoList(event){
	event.stopPropagation(); // остановка всех текущих JS событий
	event.preventDefault();  // остановка дефолтного события для текущего элемента
	let photoData = new FormData();
	photoData.append('photo', currentlyUploadedPhoto);
	$.ajax({type: "POST", url: "/recipe/sendPhoto", cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				showUploadedPhotoOnMainPage(respond.code);							
			}
			else{
				console.log(respond.error);
				alert('Ошибка уделнеия фотографии. Повторите попытку.');
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось зашрузить фото на сервер ' + status);
		},
		complete: function(){
			photoUploadPopUpWindow.hideWindow();
		}
	});
}

function showUploadedPhotoOnMainPage(code){
	var element = document.getElementById('photoList');
	var newPhotoCard = document.createElement('div');
	newPhotoCard.id = photoCount;
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
	codeInput.id="hidden_" + photoCount;
	codeInput.className = "hiddenInput";
	codeInput.value = code;
	newPhotoCardBody.append(codeInput);
	// Delete link
	var deleteBtn = document.createElement('input');
	deleteBtn.type="button";
	deleteBtn.value = "Удалить";
	deleteBtn.className = "btn btn-primary";
	//deleteBtn.setAttribute('onclick', 'deletePhotoCard()');
	deleteBtn.id = photoCount;
	deleteBtn.onclick = function(){
		newPhotoCard.remove();
		var photoData = new FormData();
		photoData.append('code', code);		
		$.ajax({type: "POST", url: "/recipe/dropUnsavedPhoto", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {	
					currentPhotoCard.remove();
					alert('Фотография успешно удалена');						
				}
				else {
					alert('Error: ' + respond.data);
				}
			}, 
			error: function(respond, status, jqXHR) {
				alert('Ошибка при удалении фотографии: ' + status);
			}
		});
	};
	newPhotoCardBody.append(deleteBtn);
	photoCount++;
}

function deletePhotoCard(){
	let currentPhotoCard = event.target.parentNode;
	let index = currentPhotoCard.id;
	let currentHiddenInput = document.getElementById("hidden_" + index);
	let code = currentHiddenInput.value;

	let photoData = new FormData();
	photoData.append('code', code);
	
	$.ajax({type: "POST", url: "/recipe/deletePhoto", cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				let parent = document.getElementById('photoList');
				currentPhotoCard.remove();
				alert('Фотография успешно удалена');						
			}
			else {
				alert('Error: ' + respond.data);
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Ошибка при удалении фотографии: ' + status);
		}
	});
}

/*
Управление списком ингредиентов
*/
function showIngredientWindow(){
	ingredientsPopUpWindow.showWindow();
}

function hideIngredientWindow(){
	ingredientsPopUpWindow.hideWindow();
}

function getIngrListFromServer(){
	ingredientsPopUpWindow.getIngrListFromServer();
}

function addNewIngredientToRecipe(){
	ingredientsPopUpWindow.saveNewIngredient();
}

function addExistingIngrToRecipe(){
	ingredientsPopUpWindow.addExistingIngrToRecipe();
}

function addIngrToTable(ingredientVolume){
	var tbody = document.getElementById('ingrTable').getElementsByTagName("TBODY")[0];
	var row = document.createElement("TR");	
	//Поле с итоговой калорийностью для ингредиента
	var resultCalorificValueField = document.createElement('b');
	//Поле для ввода количества ингредиента
	var volumeInput = document.createElement('input'); 
	volumeInput.setAttribute('form', 'saverecipeform');
	volumeInput.setAttribute('name', ingredientVolume.name);
	volumeInput.oninput = function() {
		var resultCalValue = ingredientVolume.calorie * volumeInput.value / 100;
		resultCalorificValueField.innerText = resultCalValue;
	}
	//Кнопка удаления ингредиента
	var deleteBtn = document.createElement('input'); 
	deleteBtn.setAttribute("type", "button");
	deleteBtn.setAttribute("value", "Удалить");
	deleteBtn.setAttribute("onclick", "deleteIngrFromTable()");

	var col1 = document.createElement("TD");
	col1.appendChild(document.createTextNode(ingredientVolume.name));
	var col2 = document.createElement("TD");
	col2.appendChild(document.createTextNode(ingredientVolume.calorie));
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

function deleteIngrFromTable(){
	var currentLine = event.target.parentNode.parentNode;
	currentLine.remove();
}
