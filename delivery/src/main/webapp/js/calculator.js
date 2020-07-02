window.addEventListener("load", bindActionsToDocument);
function bindActionsToDocument (){
    document.getElementById("localitySandIDSelect").addEventListener("change", function () {
        var selectedIndex =this.selectedIndex;
        if (selectedIndex < 0){
            return;
        }
        alert(this.options[selectedIndex].value);
    });
}