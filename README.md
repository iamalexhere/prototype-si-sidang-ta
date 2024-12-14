# Sistem Informasi Sidang Tugas Akhir

Aplikasi berbasis web untuk persiapan dan pencatatan pelaksanaan sidang Tugas Akhir (TA).

## Project Overview
Aplikasi ini dirancang untuk memudahkan proses persiapan dan pencatatan pelaksanaan sidang Tugas Akhir (TA) dengan fitur-fitur yang lengkap dan mudah digunakan.

## Progress Pengembangan

### Koordinator TA
- [x] Memasukan data peserta TA
  - [x] Input judul skripsi
  - [x] Pilih jenis TA (TA 1 atau 2)
  - [x] Pilih pembimbing
  - [x] Input status TA
- [x] Menentukan jadwal sidang
  - [x] Set tanggal dan waktu
  - [x] Set tempat/ruangan
  - [x] Pilih penguji 1 dan 2
- [ ] Manajemen BAP (Berita Acara Pelaksanaan)
  - [ ] Melihat BAP
  - [ ] Memberikan persetujuan BAP
- [ ] Manajemen Komponen Nilai
  - [ ] Input komponen nilai penguji
  - [ ] Input bobot nilai penguji
  - [ ] Input komponen nilai pembimbing
  - [ ] Input bobot nilai pembimbing

### Penguji
- [x] Melihat informasi sidang TA
  - [] Lihat jadwal
  - [] Lihat tempat
  - [x] Lihat mahasiswa
- [ ] Penilaian
  - [ ] Input nilai per komponen
  - [ ] Submit nilai total
- [ ] BAP
  - [ ] Melihat BAP
  - [ ] Memberikan persetujuan BAP

### Pembimbing
- [x] Melihat informasi sidang TA
  - [] Lihat jadwal
  - [] Lihat tempat
  - [x] Lihat mahasiswa
- [ ] Catatan Sidang
  - [ ] Input catatan revisi
  - [ ] Edit catatan
  - [ ] Hapus catatan
- [ ] Penilaian
  - [ ] Input nilai per komponen
  - [ ] Submit nilai total
- [ ] BAP
  - [ ] Melihat BAP
  - [ ] Memberikan persetujuan BAP

### Mahasiswa
- [x] Melihat informasi TA
  - [x] Lihat judul
  - [x] Lihat pembimbing
  - [x] Lihat status
- [x] Melihat informasi sidang
  - [x] Lihat jadwal
  - [x] Lihat tempat
  - [x] Lihat penguji
- [ ] Nilai
  - [ ] Lihat nilai per komponen
  - [ ] Lihat nilai total
- [ ] Catatan Sidang
  - [ ] Lihat catatan revisi dari pembimbing
- [ ] BAP
  - [ ] Melihat BAP
  - [ ] Memberikan persetujuan BAP

### Fitur Umum
- [] Autentikasi
  - [] Login
  - [] Logout
- [x] Dashboard
  - [x] Dashboard Koordinator TA
  - [x] Dashboard Dosen
  - [x] Dashboard Mahasiswa

## Tech Stack
- Spring Boot
- Thymeleaf
- MySQL
- Tailwind CSS