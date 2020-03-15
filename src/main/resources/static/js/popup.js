$(document).ready(function(){
    //Скрыть PopUp при загрузке страницы    
    PopUpHide("add_ingr_popup");
	PopUpHide("add_photo_popup");
});
//Функция отображения PopUp
function PopUpShow(id){
    $("#"+id).show();
}
//Функция скрытия PopUp
function PopUpHide(id){
    $("#"+id).hide();
}