function sumTime(){
	var total = 0;
	for (i=0;i<10;i++){
		var tv = document.getElementById("time" + String(i)).value;
		total += Number(tv);
	}
	document.getElementById("time_total").value = total.toFixed(2);
	var seisu = Math.floor(total);
	var shosu = parseFloat("0."+(String(total)).split(".")[1]);
	document.getElementById("time_remark").value = seisu + " 時間 " + shosu * 60 + " 分です。";
}