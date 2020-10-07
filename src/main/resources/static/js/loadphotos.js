var photoIdInputs;

$(document).ready(function(){
    photoIdInputs = $("input[id^='photoPath']");
    if(photoIdInputs.length > 0){
        load(0);
    } 
});

function load(index){
    var currentInput = photoIdInputs.eq(index);
    var currentphotoId = currentInput.val();
    jQuery.ajax({	
        type: "GET",
        url: "/recipe/loadPhoto?photoId=" + currentphotoId,
        cache: false,
        xhr:function(){
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        },
        success: function(data){
            var img = document.getElementById('img_' + currentphotoId);
            var url = window.URL || window.webkitURL;
            img.src = url.createObjectURL(data);
            $('#spinner_' + currentphotoId).hide();
            try{
                var carouselImg = document.getElementById('carousel_img_' + currentphotoId);
                carouselImg.src = url.createObjectURL(data);
                $('#carousel_spinner_' + currentphotoId).hide();
            }catch{
                console.log("На страницу отсутсвует элемент карусель");
            }
        },
        complete: function(){
            if(index != photoIdInputs.length - 1){
                index++;
                load(index);
            }
		}
    });
}