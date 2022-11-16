function changeGroupCombo(){
	document.forms[0].CMD.value='init';
	document.forms[0].submit();
	return false;
}

function allcheck( tf ) {
	var ElementsCount = document.forms[0].elements.length; // チェックボックスの数
	for( i=0 ; i<ElementsCount ; i++ ) {
		// ON・OFFを切り替え
		document.forms[0].elements[i].checked = tf;
	}
}
