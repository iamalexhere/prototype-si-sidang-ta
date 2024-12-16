document.addEventListener('DOMContentLoaded', function(){
  const popupOverlayPembimbing = document.getElementById('popupOverlayPembimbing');
  const popupContentPembimbing = document.getElementById('popupContentPembimbing');
  const closePopupPembimbing = document.getElementById('closePopupPembimbing');
  const popupOverlayPenguji = document.getElementById('popupOverlayPenguji');
  const popupContentPenguji = document.getElementById('popupContentPenguji');
  const closePopupPenguji = document.getElementById('closePopupPenguji');
  const infoButtonPembimbing = document.getElementById('infoButtonPembimbing');
  const infoButtonPenguji = document.getElementById('infoButtonPenguji');

  infoButtonPembimbing.addEventListener('click', function(){
      popupOverlayPembimbing.classList.remove('hidden'); // Menampilkan overlay
      setTimeout(() => {
        popupContentPembimbing.classList.remove('scale-75', 'opacity-0'); // Animasi masuk
        popupContentPembimbing.classList.add('scale-100', 'opacity-100');
      }, 10); // Sedikit delay agar transisi terlihat
  });

  // Fungsi untuk menutup popup
  closePopupPembimbing.addEventListener('click', () => {
    popupContentPembimbing.classList.remove('scale-100', 'opacity-100'); // Animasi keluar
    popupContentPembimbing.classList.add('scale-75', 'opacity-0');
    popupOverlayPembimbing.classList.add('hidden'); // Sembunyikan overlay setelah animasi selesai
  });

  infoButtonPenguji.addEventListener('click', function(){
    popupOverlayPenguji.classList.remove('hidden'); // Menampilkan overlay
    setTimeout(() => {
      popupContentPenguji.classList.remove('scale-75', 'opacity-0'); // Animasi masuk
      popupContentPenguji.classList.add('scale-100', 'opacity-100');
    }, 10); // Sedikit delay agar transisi terlihat
});

// Fungsi untuk menutup popup
closePopupPenguji.addEventListener('click', () => {
  popupContentPenguji.classList.remove('scale-100', 'opacity-100'); // Animasi keluar
  popupContentPenguji.classList.add('scale-75', 'opacity-0');
  popupOverlayPenguji.classList.add('hidden'); // Sembunyikan overlay setelah animasi selesai
});

})

