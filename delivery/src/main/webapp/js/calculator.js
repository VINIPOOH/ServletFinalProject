window.addEventListener("load", bindActionsToDocument);
function bindActionsToDocument (){
    document.getElementById("localitySandIDSelect").addEventListener("change", function () {
        var selectedIndex =this.selectedIndex;
        if (selectedIndex < 0){
            return;
        }
        var request = new XMLHttpRequest();
        request.open("GET", "http://localhost:8088/delivery/get/localitiesGet/by/localitySend/id?id="
            +this.options[selectedIndex].value,true);

        request.onreadystatechange = function(e) {
            if (this.readyState == 4) {
                var localitiesGetList = JSON.parse(request.responseText);
                var localityGetSelect = document.getElementById("localityGetID");
                localityGetSelect.options.length = 0;
                for (var i =0; i<localitiesGetList.length;i++){
                    localityGetSelect.options.add(new Option(localitiesGetList[i]["name"], localitiesGetList[i]["id"]));
                }
            }
        };
        request.send(null);
    });
}