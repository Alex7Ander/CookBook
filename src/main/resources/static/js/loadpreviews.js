var recipeIdInputs;

$(document).ready(function(){
    recipeIdInputs = $("input[id^='recipeId']");
    if(recipeIdInputs.length > 0){
        load(0);
    }    
});

function load(index){
    var currentInput = recipeIdInputs.eq(index);
    var currentRecipeId = currentInput.val();
    jQuery.ajax({	
        type: "GET",
        url: "/cookbook/loadPreview?recipeId=" + currentRecipeId,
        cache: false,
        xhr:function(){
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        },
        success: function(data){
            var img = document.getElementById('img_' + currentRecipeId);
            var url = window.URL || window.webkitURL;
            img.src = url.createObjectURL(data);
            $('#spinner_' + currentRecipeId).hide();
        },
        complete: function(){
            if(index != recipeIdInputs.length - 1){
                index++;
                load(index);
            }
		}
    });
}