function getIngrListFromServer(){
    var selectedType = $("#ingrType").val();
    var element = $("#ingrList");
    $("#ingrList").load("/user/getIngrList", {type: selectedType});
    $("#select option:first").prop("selected", true);
}