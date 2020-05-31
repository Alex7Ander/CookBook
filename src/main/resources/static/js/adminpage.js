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

function loadIngredient(){

}

function loadReview(){

}

function startEdit(id){
    $("#" + id + "TextField").attr('hidden', false);
    $("#" + id + "TextField").prop("value", $("#" + id).text());
    $("#" + id).attr('hidden', true);
}