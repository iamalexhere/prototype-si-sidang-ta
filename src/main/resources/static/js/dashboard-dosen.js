document.addEventListener('DOMContentLoaded', function() {
    // Pembimbing popup functionality
    const infoButtonsPembimbing = document.querySelectorAll('#infoButtonPembimbing');
    const popupOverlayPembimbing = document.getElementById('popupOverlayPembimbing');
    const closeButtonPembimbing = document.getElementById('closeButtonPembimbing');

    infoButtonsPembimbing.forEach(button => {
        button.addEventListener('click', () => {
            popupOverlayPembimbing.classList.remove('hidden');
        });
    });

    if (closeButtonPembimbing) {
        closeButtonPembimbing.addEventListener('click', () => {
            popupOverlayPembimbing.classList.add('hidden');
        });
    }

    // Close on outside click for pembimbing popup
    popupOverlayPembimbing.addEventListener('click', (e) => {
        if (e.target === popupOverlayPembimbing) {
            popupOverlayPembimbing.classList.add('hidden');
        }
    });

    // Penguji popup functionality
    const infoButtonsPenguji = document.querySelectorAll('#infoButtonPenguji');
    const popupOverlayPenguji = document.getElementById('popupOverlayPenguji');
    const closeButtonPenguji = document.getElementById('closeButtonPenguji');

    infoButtonsPenguji.forEach(button => {
        button.addEventListener('click', () => {
            popupOverlayPenguji.classList.remove('hidden');
        });
    });

    if (closeButtonPenguji) {
        closeButtonPenguji.addEventListener('click', () => {
            popupOverlayPenguji.classList.add('hidden');
        });
    }

    // Close on outside click for penguji popup
    popupOverlayPenguji.addEventListener('click', (e) => {
        if (e.target === popupOverlayPenguji) {
            popupOverlayPenguji.classList.add('hidden');
        }
    });
});
