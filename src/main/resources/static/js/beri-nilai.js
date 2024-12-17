const form = document.querySelector("form");
const confirmationPopup = document.getElementById("confirmationPopup");
const successPopup = document.getElementById("successPopup");
const confirmYes = document.getElementById("confirmYes");
const confirmNo = document.getElementById("confirmNo");
const successOk = document.getElementById("successOk");
const popupOverlay = document.getElementById("popupOverlay");

function showPopup(event) {
    event.preventDefault();
    popupOverlay.classList.remove('hidden');
    confirmationPopup.classList.remove("hidden");
}

form.addEventListener("submit", showPopup);

function confirmYesFunc(){
    confirmationPopup.classList.add("hidden");
    successPopup.classList.remove("hidden");
    form.submit();
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
