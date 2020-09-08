var newUserPopupWindow;
var waitingWindow;

$(document).ready(function(){
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

function loadRecipe(){

}

function loadReview(){

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

function loadIngredient(id){
    var name = $("#"+id+"_name").text;
    var type = $("#"+id+"_type").text;
    $.ajax({type: "GET", url: "/ingredient/getProperties?type="+type+"&name="+name, async: false, cache: false, dataType: 'json', contentType: false, processData: false,
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
                $("#nameEdit").val(respond.name);
                $("#protEdit").val(respond.prot);
                $("#carboEdit").val(respond.carbo);
                $("#fatEdit").val(respond.fat);
                $("#descriptionEdit").val(respond.descr);            
            }
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}

function saveIngredient(){
    var ingrData = new FormData();
    var id = $("#currentIngrId").val();
    if(id != 0){
        ingrData.append('id', id);
    }
    ingrData.append('name', $("#nameEdit").val());
    ingrData.append('type', $("#typeEdit").val());
    ingrData.append('descr', $("#descriptionEdit").val());
    ingrData.append('prot', $("#protEdit").val());
    ingrData.append('fat', $("#fatEdit").val());
    ingrData.append('carbo', $("#carboEdit").val());
    $.ajax({type: "POST", url: "/admin/addIngredient", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: ingrData,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте, идет добавление ингредиента");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            alert("Ингредиент добавлен");
        },
        error: function(respond, status, jqXHR){
            alert(status);
        },
        complete: function(){
			waitingWindow.hideWindow();			
		}
    });
}

function deleteIngr(){
    var ingrData = new FormData();
    var id = $("#currentIngrId").val();
    if(id == 0){
        alert("Этот ингредитент еще не сохранен, что бы быть удаленным");
        return;
    }   
    ingrData.append('id', id);
    $.ajax({type: "POST", url: "/admin/deleteIngredient", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: ingrData,
        beforeSend: function(){
            waitingWindow.setTitle("Ожидайте, идет удаление ингредиента");
            waitingWindow.showWindow();
        },
        success: function(respond, status, jqXHR){
            alert("Ингредиент удален");
        },
        error: function(respond, status, jqXHR){
            alert(status);
        },
        complete: function(){
			waitingWindow.hideWindow();			
		}
    });
}
