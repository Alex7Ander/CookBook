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
function deleteUser(){
    var id = $('#id').prop("value");
    var userData = new FormData();
    userData.append("id", id);
    $.ajax({type: "POST", url: "/admin/deleteUser", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: userData,
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
                $("#name").text("-");
                $("#surname").text("-");
                $("#login").text("-");
                $("#password").text("-");
                $("#city").text("-");
                $("#temperament").text("-");
                $("#email").text("-");
                $("#phone").text("-");
            }
        },
        error: function(respond, status, jqXHR){
            alert(status);
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
Ingredients
*/
function saveIngrBtnClick(){
    var selected = $("input[type='radio'][name='editRb']:checked").val();
    var name = $("#nameEdit").val();
    var type = $("#typeEdit").val();
    var descr = $("#descriptionEdit").val();
    var prot = $("#protEdit").val();
    var fat = $("#fatEdit").val();
    var carbo = $("#carboEdit").val();
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
        //waitingWindow.setTitle("Ожидайте, идет сохранение ингредиента");
        newIngredient.save();
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


