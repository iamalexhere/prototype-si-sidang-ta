(function () {
    const profileBar = document.getElementById('profile');
    const profileButton = document.getElementById('profile_pic');
    let counter = 0;

    profileButton.addEventListener('click', function () {
        if(counter%2==0){
            profileBar.classList.replace("flex", "hidden");
            counter = 1;
        }
        else{
            profileBar.classList.replace("hidden", "flex");
            counter = 0;
        }
    });

})();