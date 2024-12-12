document.addEventListener('DOMContentLoaded', function () {

});


(function () {
    const filterForm = document.querySelector('form');
    const xButton = document.getElementsByName('remove')[0];
    const inputs = filterForm.querySelectorAll('input, select');
    const tahun_ajaran = document.getElementsByName("tahun_ajaran")[0];

    function checkFilters() {
        let isFormFilled = false;
        inputs.forEach(input => {
            if ((input.type === 'text' && input.value.trim() !== '') ||
                (input.type === 'radio' && input.checked) ||
                (input.type === 'select-one' && input.value !== '')) {
                isFormFilled = true;
            }
        });

        if (isFormFilled) {
            xButton.classList.remove("hidden");
        } else {
            xButton.classList.add("hidden");
        }
    }

    inputs.forEach(input => {
        input.addEventListener('input', checkFilters);
        input.addEventListener('change', checkFilters);
    });

    tahun_ajaran.addEventListener('change', function () {
        if (tahun_ajaran.value !== '') {
            tahun_ajaran.classList.replace("text-gray-400", "text-black");
        }
    });

    xButton.addEventListener('click', function (event) {
        event.preventDefault();
        inputs.forEach(input => {
            if (input.type === 'text') {
                input.value = '';
            } else if (input.type === 'radio') {
                input.checked = false;
            } else if (input.tagName.toLowerCase() === 'select') {
                input.selectedIndex = 0;
            }
        });

        filterForm.submit;
        checkFilters();
    });

    checkFilters();
})();