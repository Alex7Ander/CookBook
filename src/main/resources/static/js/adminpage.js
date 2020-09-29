var newUserPopupWindow;
var waitingWindow;

var currentUploadedIngredient;

$(document).ready(function(){
    $("#spinner_image").hide();
    try{
        newUserPopupWindow = new AddNewUserPopUpWindow("new_user_window");
        newUserPopupWindow.hideWindow();
    }
    catch{}
    waitingWindow = new WaitingPopUpWindow("waiting_window");
    waitingWindow.hideWindow();        
});

function loadUser(id){
    $.ajax({type: "GET", url: "/admin/loadUser?id=" + id, async: false, cache: false, dataType: 'json', contentType: false, processData: false,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте. Идет загрузка информации о пользователе");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
                $("#userId").val(respond.id);
                $("#name").text(respond.name);
                $("#surname").text(respond.surname);
                $("#login").text(respond.login);
                $("#password").text(respond.password);
                $("#city").text(respond.city);
                $("#temperament").text(respond.temperament);
                $("#email").text(respond.email);
                $("#phone").text(respond.phone);
            }
        },
        error: function(respond, status, jqXHR){
            alert("Ошибка при загрузке пользователя: " + status);
        },
        complete: function(){
            waitingWindow.hideWindow();
        }
    });
}
function deleteUser(){
    var id = $('#userId').val();
    var userData = new FormData();
    userData.append("id", id);
    $.ajax({type: "POST", url: "/admin/deleteUser", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: userData,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте. Идет удаление пользователя");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
                waitingWindow.setTitle("Пользователь удален. Страница будет перезагружена для актуализации информации.");
                location.reload();
            }
        },
        error: function(respond, status, jqXHR){
            alert("Ошибка при удалении пользователя: " + status);
        },
        complete: function(){
            waitingWindow.hideWindow();
        }
    });
}
function addNewUser(){
    var userData = new FormData();
    userData.append("login", $("#newUserLogin"));
    userData.append("password", $("#newUserPassword"));
    userData.append("email", $("#newUserEmail"));
    userData.append("role", $('#newUserRole option:selected').text());
    $.ajax({type: "POST", url: "/admin/addNewUser", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: userData,
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
                $("#name").text(respond.name);
                $("#surname").text(respond.surname);
                $("#login").text(respond.login);
                $("#password").text(respond.password);
                $("#city").text(respond.city);
                $("#temperament").text(respond.temperament);
                $("#email").text(respond.email);
                $("#phone").text(respond.phone);
            }
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}

function startEdit(id){
    $("#" + id + "TextField").attr('hidden', false);
    $("#" + id + "TextField").prop("value", $("#" + id).text());
    $("#" + id).attr('hidden', true);
}

function sendEmail(){
    var emailData = new FormData();
    emailData.append("emailTo", $("#emailTo").val());
    emailData.append("message", $("#message").val());
    $.ajax({type: "POST", url: "/admin/sendemail", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: emailData,
        success: function(respond, status, jqXHR){
            alert("Сообщение отправлено");
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}

function sendAnswer(reviewId){
    var answerData = new FormData();
    answerData.append('reviewId', reviewId);
    var answer = $('#answer_' + reviewId).val();
    answerData.append('answer', answer);
    $.ajax({type: "POST", url: "/admin/sendAnswer", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: answerData,
        success: function(respond, status, jqXHR){
            alert("Ответ сохранен");
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}

function deleteReview(reviewId){
    var reviewData = new FormData();
    reviewData.append('reviewId', reviewId);
    $.ajax({type: "POST", url: "/admin/deleteReview", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: reviewData,
        success: function(respond, status, jqXHR){
            alert("Сообщение удалено");
            $('#tr_'+reviewId).remove();
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}

/*
Recipes
*/
function deleteRecipe(){
    var recipeData = new FormData();
    recipeData.append("recipeId", $("#recipeId").val());
    $.ajax({type: "POST", url: "/recipe/delete", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: recipeData,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте. Идет удаление рецепта");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            if(typeof respond.error === 'undefined'){
                waitingWindow.setTitle("Ингредиент удален. Страница будет перезагружена для актуализации информации");
                location.reload();               
            }
            else{
                alert(respond.error);
            }
        },
        error: function(respond, status, jqXHR){
            alert(status);
        },
        complete: function(){
            waitingWindow.hideWindow();
        }
    });
}

function loadRecipe(id){
    $.ajax({type: "GET", url: "/admin/loadRecipe?id="+id, async: false, cache: false, dataType: 'json', contentType: false, processData: false,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте. Идет загрузка рецепта");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            if(typeof respond.error === 'undefined'){
                $("#recipeId").val(respond.id);
                $("#name").text(respond.name);
                $("#type").text(respond.type);
                $("#auther").text("id - " + respond.recipeAuther.id + "; login - " + respond.recipeAuther.login);
                $("#tagline").text(respond.tagline);
                $("#youtubeLink").text(respond.youtubeLink);
                $("#text").text(respond.text);               
            }
            else{
                alert(respond.error);
            }
        },
        error: function(respond, status, jqXHR){
            alert(status);
        },
        complete: function(){
            waitingWindow.hideWindow();
        }
    });
}

/*
Ingredients
*/
function saveIngrBtnClick(){
    var selected = $("input[type='radio'][name='editRb']:checked").val();
    var name = $("#nameEdit").val();
    var type = $("#typeEdit").val();
    var descr = $("#descriptionEdit").val();
    var prot = getFloat('protEdit');   
    var fat = getFloat('fatEdit');    
    var carbo = getFloat('carboEdit');
    if(selected == 1){
        waitingWindow.setTitle("Ожидайте, идет редактирование ингредиента");
        waitingWindow.showWindow();
        var successFinish = function(){
            waitingWindow.setTitle("Изменения сохранены. Страница будет перезагружена для актуализации информации.");
            waitingWindow.hideWindow();
            location.reload();
        }
        var errorFinish = function(){
            alert("Произошла ошибка на сервере. Изменения не сохранены");
            waitingWindow.hideWindow();
        }
        currentUploadedIngredient.edit(name, type,  descr, prot, fat, carbo, successFinish, errorFinish);
    }
    else{
        var newIngredient = new Ingredient();
        newIngredient.name = name;
        newIngredient.type = type;
        newIngredient.descr = descr;
        newIngredient.prot = prot;
        newIngredient.fat = fat;
        newIngredient.carbo = carbo;
        newIngredient.common = true;

        waitingWindow.setTitle("Ожидайте, идет сохранение ингредиента");
        waitingWindow.showWindow();
        var msg = newIngredient.save();
        alert(msg);
        waitingWindow.hideWindow();
        if(msg == "Ингредиент сохранен успешно"){
            location.reload();
        }
    }
}

function loadIngredient(id){
    var name = $("#"+id+"_name").text();
    var type = $("#"+id+"_type").text();
    currentUploadedIngredient = new Ingredient();
    currentUploadedIngredient.getDataFromServer(name, type);
    $("#currentIngrId").val(currentUploadedIngredient.id);
    $("#nameEdit").val(currentUploadedIngredient.name);
    $("#typeEdit").val(currentUploadedIngredient.type);
    $("#protEdit").val(currentUploadedIngredient.prot);
    $("#carboEdit").val(currentUploadedIngredient.carbo);
    $("#fatEdit").val(currentUploadedIngredient.fat);
    $("#descriptionEdit").val(currentUploadedIngredient.descr);

    $("#spinner_image").show();
    currentUploadedIngredient.loadImage("image", finishLoadingImage);
}

function finishLoadingImage(){
    $("#spinner_image").hide();
    $("#image").show();
}

function showNewIngredientPreview(){
	var photoFileLoader = document.getElementById('ingrPhotoLoader');
	var currentlyUploadedPhoto = photoFileLoader.files[0];
	var output  = document.getElementById('image');
    output.src = URL.createObjectURL(currentlyUploadedPhoto); 
    $('#savePreviewBtn').show();
}

function saveNewIngredientPreview(){
	var ingredientData = new FormData();
	var currentIngrId = $('#currentIngrId').val(); 		
	var currentlyUploadedPhoto = $('#ingrPhotoLoader').prop('files')[0];
	ingredientData.append('ingrImage', currentlyUploadedPhoto);
	ingredientData.append('ingrId', currentIngrId);

	$.ajax({type: "POST", url: "/ingredient/saveImage", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте. Идет сохранение фотографии для превью");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {		
                alert("Фото успешно сохранено");
                $('#savePreviewBtn').hide();
			}
			else {
				alert("Ошибка при загрузке изображения: " + respond.error);
			}
		}, 
		error: function(respond, status, jqXHR){
			alert(respond.statusText);
        },
        complete: function(){
            waitingWindow.hideWindow();
        }
	});   
}

function deleteIngr(){
    var id = $("#currentIngrId").val();
    if(id == 0){
        alert("Этот ингредитент еще не сохранен, что бы быть удаленным");
        return;
    }
    var successFinish = function(){
        waitingWindow.setTitle("Ингредиент удален. Страница будет перезагружена для актуализации информации.");
        waitingWindow.hideWindow();
        location.reload();
    }
    var errorFinish = function(){
        alert("Произошла ошибка на сервере. Изменения не сохранены");
        waitingWindow.hideWindow();
    } 
    waitingWindow.setTitle("Ожидайте, идет удаление ингредиента");
    waitingWindow.showWindow();
    currentUploadedIngredient.delete(successFinish, errorFinish);
}

function getFloat(id){
    var initValue = $('#' + id).val();
    var newValue = initValue.replace(/,/, '.');
    return newValue;
}

