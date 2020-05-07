let currentlyUploadedPhoto;  //последнее загруженное фото
var photoCount = 0;  //Порядковый индекс загруженной фотографии

//PopUp windows
var ingredientsPopUpWindow; //= new IngredientsPopUpWindow("add_ingr_popup");
var photoUploadPopUpWindow; //= new PhotoUploadPopUpWindow("add_photo_popup");

$(document).ready(function(){
	//Скрыть PopUp при загрузке страницы  
	ingredientsPopUpWindow = new IngredientsPopUpWindow("add_ingr_popup"); 
	photoUploadPopUpWindow = new PhotoUploadPopUpWindow("add_photo_popup"); 
	ingredientsPopUpWindow.hideWindow();
	photoUploadPopUpWindow.hideWindow();
});

/*
Управление фотографиями
*/
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
	$.ajax({type: "POST", url: "/user/addRecipePhoto", cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
		success: function(respond, status, jqXHR){
			if( typeof respond.error === 'undefined' ){
				showUploadedPhotoOnMainPage(respond);							
			}
			else{
				alert('Error: ' + respond.data);
			}
		}, 
		error: function(respond, status, jqXHR){
			alert('Не удалось загрузить фото на сервер: ' + status);
		},
		complete: function(){
			PopUpHide('add_photo_popup');
		}
	});
}

function showUploadedPhotoOnMainPage(code){
	var element = document.getElementById('photoList');
	var newPhotoCard = document.createElement('div');
	newPhotoCard.id = photoCount;
	newPhotoCard.className = "card";
	element.append(newPhotoCard);	
	// Image
	var image = document.createElement('img');
	var photoPath = document.getElementById('uploadedPhoto').src;
	image.src = photoPath;
	image.className = "imageContainer";
	newPhotoCard.append(image);
	// Hidden input with hashcode
	var codeInput = document.createElement('input');
	codeInput.type = "hidden";
	codeInput.id="hidden_" + photoCount;
	codeInput.className = "hiddenInput";
	codeInput.value = code;
	newPhotoCard.append(codeInput);
	// Delete link
	var deleteBtn = document.createElement('input');
	deleteBtn.type="button";
	deleteBtn.value = "Удалить";
	deleteBtn.className = "btn btn-primary";
	deleteBtn.setAttribute('onclick', 'deletePhotoCard()');
	deleteBtn.id = photoCount;
	newPhotoCard.append(deleteBtn);
	photoCount++;
}

function deletePhotoCard(){
	let currentPhotoCard = event.target.parentNode;
	let index = currentPhotoCard.id;
	let currentHiddenInput = document.getElementById("hidden_" + index);
	let code = currentHiddenInput.value;

	let photoData = new FormData();
	photoData.append('code', code);
	
	$.ajax({type: "POST", url: "/user/deleteRecipePhoto", cache: false, dataType: 'json', contentType: false, processData : false, data: photoData,
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

function saveNewIngredient(){
	ingredientsPopUpWindow.saveNewIngredient();
}

function addExistingIngrToTable(){
    try{
		var ingr = ingredientsPopUpWindow.getIngredient();
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
		var deleteBtn = document.createElement('a'); 
		deleteBtn.value= 'Удалить';
		deleteBtn.setAttribute('onclick', 'deleteIngrFromTable()');

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
    catch(e){
		alert("Ошибка при попытке добавить ингредиент");
    }
    //Closing pop-up window
    ingredientsPopUpWindow.hide();		
}

function deleteIngrFromTable(){
	var currentLine = event.target.parentNode.parentNode;
	currentLine.remove();
}
