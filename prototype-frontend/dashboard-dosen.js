const popupOverlayPembimbing = document.getElementById('popupOverlayPembimbing');
const popupContentPembimbing = document.getElementById('popupContentPembimbing');
const closePopupPembimbing = document.getElementById('closePopupPembimbing');

// Fungsi untuk menampilkan popup
function showPopupPembimbing() {
  popupOverlayPembimbing.classList.remove('hidden'); // Menampilkan overlay
  setTimeout(() => {
    popupContentPembimbing.classList.remove('scale-75', 'opacity-0'); // Animasi masuk
    popupContentPembimbing.classList.add('scale-100', 'opacity-100');
  }, 10); // Sedikit delay agar transisi terlihat
}

// Fungsi untuk menutup popup
closePopupPembimbing.addEventListener('click', () => {
  popupContentPembimbing.classList.remove('scale-100', 'opacity-100'); // Animasi keluar
  popupContentPembimbing.classList.add('scale-75', 'opacity-0');
  popupOverlayPembimbing.classList.add('hidden'); // Sembunyikan overlay setelah animasi selesai
});



const popupOverlayPenguji = document.getElementById('popupOverlayPenguji');
const popupContentPenguji = document.getElementById('popupContentPenguji');
const closePopupPenguji = document.getElementById('closePopupPenguji');

// Fungsi untuk menampilkan popup
function showPopupPenguji() {
  popupOverlayPenguji.classList.remove('hidden'); // Menampilkan overlay
  setTimeout(() => {
    popupContentPenguji.classList.remove('scale-75', 'opacity-0'); // Animasi masuk
    popupContentPenguji.classList.add('scale-100', 'opacity-100');
  }, 10); // Sedikit delay agar transisi terlihat
}

// Fungsi untuk menutup popup
closePopupPenguji.addEventListener('click', () => {
  popupContentPenguji.classList.remove('scale-100', 'opacity-100'); // Animasi keluar
  popupContentPenguji.classList.add('scale-75', 'opacity-0');
  popupOverlayPenguji.classList.add('hidden'); // Sembunyikan overlay setelah animasi selesai
});
