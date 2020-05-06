let currentlyUploadedPhoto;  //последнее загруженное фото
var photoCount = 0;  //Порядковый индекс загруженной фотографии

$(document).ready(function(){
    //Скрыть PopUp при загрузке страницы    
    PopUpHide("add_ingr_popup");
	PopUpHide("add_photo_popup");
});

/*
Управление фотографиями
*/
function getCurrentLyUploadedPhoto(event){
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
function getIngrListFromServer(){
    req = new asyncRequest();
    element = document.getElementById('ingrList');
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
            element.innerHTML = req.responseText;
            if(!req.status == 200) {
                alert("Error: " + req.status);
            }
        }
    }
    var select = document.getElementById("ingrType");
    var selectedValue = select.value;
    var body = "?type=" + selectedValue;			
    req.open("GET", "/user/getIngrList" + body);
    req.send();
    element.innerHTML = "Список ингредиентов загружается...";
}

function saveNewIngredient(){
    var name = document.getElementById("newIngrName").value;
    var type = document.getElementById("newIngrType").value; 
    var descr = document.getElementById("newIngrDesription").value;
    var prot = document.getElementById("newIngrProt").value;
    var fat = document.getElementById("newIngrFat").value;
    var carbo = document.getElementById("newIngrCarbo").value;
    var ingr = new ingredient(name, type, descr, prot, fat, carbo);
    ingr.save();
    if (ingr.isSaved() == true) {
        ingr.addToTable('ingrTable');
        PopUpHide('add_ingr_popup');
    }
}

function addExistingIngrToTable(){
    try{
		var ingr = new ingredient();
		var selectType = document.getElementById("ingrType");
		var cType = selectType.value;
		var selectName = document.getElementById("ingrName");
		var cName = selectName.value;
		ingr.getDataFromServer(cName, cType);

		//Adding line to table
		var tbody = document.getElementById('ingrTable').getElementsByTagName("TBODY")[0];
		var row = document.createElement("TR");	

		//Поле с итоговой калорийностью для ингредиента
		var resultCalorificValueField = document.createElement('b'); //span

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
		deleteBtn.type = 'button';
		deleteBtn.value= 'Удалить';
		deleteBtn.class= 'dws-btn';
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
    PopUpHide('add_ingr_popup');		
}

function deleteIngrFromTable(){
	var currentLine = event.target.parentNode.parentNode;
	var currentIngrName = currentLine.childNodes[0].innerText;
	currentLine.remove();
}
