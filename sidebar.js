(function () {
    const profileBar = document.getElementById('profile');
    const profileButton = document.getElementById('profile_pic');
    let counter = 1;

    profileButton.addEventListener('click', function () {
        if(counter%2==0){
            profileBar.style.display = 'none';
        }
        else{
            profileBar.style.display = 'flex';
        }

        counter++;
    });

})();