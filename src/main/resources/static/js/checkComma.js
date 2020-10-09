function checkComma(id){
	let value = $("#"+id).val();
	let commaIndex = value.indexOf(',');
	if(commaIndex != -1){
		let newValue = value.replace(',','.');
		$("#"+id).val(newValue);
	}
}