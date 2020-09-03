function sendReview(){
    var reviewData = new FormData();
    var reviewText = $('#review').val();
    reviewData.append('reviewText', reviewText);
    $.ajax({type: "POST", url: "/user/saveReview", async: false, cache: false, dataType: 'json', contentType: false, processData: false, data: reviewData,
        success: function(respond, status, jqXHR){
            alert("Ваше сообщение успешно отправлено.");
            $('#reviews').append("<div class='row'><div class='col-md-6'><b class = 'label'>Вопрос:<span> "+reviewText+" </span> </b><br></div><div class='col-md-6'><b class = 'label'>Ответ:</b><br></div></div>");
        },
        error: function(respond, status, jqXHR){
            alert(status);
        }
    });
}