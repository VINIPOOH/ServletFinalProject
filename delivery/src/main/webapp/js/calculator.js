window.addEventListener("load", bindActionsToDocument);
alert("test");
function bindActionsToDocument (){
    document.getElementById("localitySandID").addEventListener("change", function () {
        var selectedIndex =this.selectedIndex;
        if (selectedIndex < 0){
            return;
        }
        alert(this.options[selectedIndex].value);
    });
};