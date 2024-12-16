const saveButton = document.getElementById("saveButton");
const confirmationPopup = document.getElementById("confirmationPopup");
const successPopup = document.getElementById("successPopup");
const confirmYes = document.getElementById("confirmYes");
const confirmNo = document.getElementById("confirmNo");
const successOk = document.getElementById("successOk");
const popupOverlay = document.getElementById("popupOverlay");
let valid = false;



if(tataTulis.value != '' && kelengkapanMateri.value != '' && prosesBimbingan.value != '' && penguasaanMateri.value != ''){
    valid = true;
}


function showPopup() {
    popupOverlay.classList.remove('hidden');
    confirmationPopup.classList.remove("hidden");
}

saveButton.addEventListener("click", showPopup);

function confirmYesFunc(){
    confirmationPopup.classList.add("hidden");
    successPopup.classList.remove("hidden"); 
}

confirmYes.addEventListener("click", confirmYesFunc);

function successOkFunc(){
    successPopup.classList.add("hidden"); 
    popupOverlay.classList.add("hidden");
}

successOk.addEventListener("click", successOkFunc); 

function successNoFunc(){
    confirmationPopup.classList.add("hidden"); 
    popupOverlay.classList.add("hidden");

}

confirmNo.addEventListener("click", successNoFunc);
