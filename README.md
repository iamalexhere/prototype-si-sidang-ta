# Sistem Informasi Sidang Tugas Akhir

Aplikasi berbasis web untuk persiapan dan pencatatan pelaksanaan sidang Tugas Akhir (TA).

## Project Overview
Aplikasi ini dirancang untuk memudahkan proses persiapan dan pencatatan pelaksanaan sidang Tugas Akhir (TA) dengan fitur-fitur yang lengkap dan mudah digunakan.

## Progress Pengembangan

### Koordinator TA
- ✅ Memasukan data peserta TA
    - ✅ Input judul skripsi
    - ✅ Pilih jenis TA (TA 1 atau 2)
    - ✅ Pilih pembimbing
    - ✅ Input status TA
- ✅ Menentukan jadwal sidang
    - ✅ Set tanggal dan waktu
    - ✅ Set tempat/ruangan
    - ✅ Pilih penguji 1 dan 2
- ⬜ Manajemen BAP (Berita Acara Pelaksanaan)
    - ⬜ Melihat BAP
    - ⬜ Memberikan persetujuan BAP
- ✅ Manajemen Komponen Nilai
    - ✅ Input komponen nilai penguji
    - ✅ Input bobot nilai penguji
    - ✅ Input komponen nilai pembimbing
    - ✅ Input bobot nilai pembimbing

### Penguji
- ✅ Melihat informasi sidang TA
    - ⬜ Lihat jadwal
    - ⬜ Lihat tempat
    - ✅ Lihat mahasiswa
- ⬜ Penilaian
    - ⬜ Input nilai per komponen
    - ⬜ Submit nilai total
- ⬜ BAP
    - ⬜ Melihat BAP
    - ⬜ Memberikan persetujuan BAP

### Pembimbing
- ✅ Melihat informasi sidang TA
    - ⬜ Lihat jadwal
    - ⬜ Lihat tempat
    - ✅ Lihat mahasiswa
- ⬜ Catatan Sidang
    - ⬜ Input catatan revisi
    - ⬜ Edit catatan
    - ⬜ Hapus catatan
- ⬜ Penilaian
    - ⬜ Input nilai per komponen
    - ⬜ Submit nilai total
- ⬜ BAP
    - ⬜ Melihat BAP
    - ⬜ Memberikan persetujuan BAP

### Mahasiswa
- ✅ Melihat informasi TA
    - ✅ Lihat judul
    - ✅ Lihat pembimbing
    - ✅ Lihat status
- ✅ Melihat informasi sidang
    - ✅ Lihat jadwal
    - ✅ Lihat tempat
    - ✅ Lihat penguji
- ⬜ Nilai
    - ⬜ Lihat nilai per komponen
    - ⬜ Lihat nilai total
- ⬜ Catatan Sidang
    - ⬜ Lihat catatan revisi dari pembimbing
- ⬜ BAP
    - ⬜ Melihat BAP
    - ⬜ Memberikan persetujuan BAP

### Fitur Umum
- ⬜ Autentikasi
    - ⬜ Login
    - ⬜ Logout
- ✅ Dashboard
    - ✅ Dashboard Koordinator TA
    - ✅ Dashboard Dosen
    - ✅ Dashboard Mahasiswa

## Tech Stack
- Spring Boot
- Thymeleaf
- MySQL
- Tailwind CSS