document.addEventListener('DOMContentLoaded', function(){
    const infoButtonsPembimbing = document.querySelectorAll('.infoButtonPembimbing');
    const infoButtonsPenguji = document.querySelectorAll('.infoButtonPenguji');

    infoButtonsPembimbing.forEach(button => {
        button.addEventListener('click', function(){
            const row = this.closest('tr');
            const popupOverlay = row.querySelector('.popupOverlayPembimbing');
            const popupContent = row.querySelector('.popupContentPembimbing');
            popupOverlay.classList.remove('hidden');
            setTimeout(() => {
                popupContent.classList.remove('scale-75', 'opacity-0');
                popupContent.classList.add('scale-100', 'opacity-100');
            }, 10);
        });
    });

    const closeButtonsPembimbing = document.querySelectorAll('.closePopupPembimbing');
    closeButtonsPembimbing.forEach(button => {
        button.addEventListener('click', function(){
            const row = this.closest('tr');
             const popupOverlay = row.querySelector('.popupOverlayPembimbing');
            const popupContent = row.querySelector('.popupContentPembimbing');
            popupContent.classList.remove('scale-100', 'opacity-100');
            popupContent.classList.add('scale-75', 'opacity-0');
            popupOverlay.classList.add('hidden');
        });
    });

    infoButtonsPenguji.forEach(button => {
        button.addEventListener('click', function(){
            const row = this.closest('tr');
            const popupOverlay = row.querySelector('.popupOverlayPenguji');
            const popupContent = row.querySelector('.popupContentPenguji');
            popupOverlay.classList.remove('hidden');
            setTimeout(() => {
                popupContent.classList.remove('scale-75', 'opacity-0');
                popupContent.classList.add('scale-100', 'opacity-100');
            }, 10);
        });
    });

    const closeButtonsPenguji = document.querySelectorAll('.closePopupPenguji');
    closeButtonsPenguji.forEach(button => {
        button.addEventListener('click', function(){
            const row = this.closest('tr');
            const popupOverlay = row.querySelector('.popupOverlayPenguji');
            const popupContent = row.querySelector('.popupContentPenguji');
            popupContent.classList.remove('scale-100', 'opacity-100');
            popupContent.classList.add('scale-75', 'opacity-0');
            popupOverlay.classList.add('hidden');
        });
    });
})
