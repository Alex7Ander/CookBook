$(document).ready(function(){
        loadavatar();  
});

function loadavatar(){
    var currentUserId = $('#user_id').val();
    jQuery.ajax({	
        type: "GET",
        url: "/user/loadAvatar?userId=" + currentUserId,
        cache: false,
        xhr:function(){
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        },
        success: function(data){
            var img = document.getElementById('avatar');
            var url = window.URL || window.webkitURL;
            img.src = url.createObjectURL(data);
        },
        error: function(respond, status, jqXHR) {
            alert(respond.statusText);
        }
    });
}