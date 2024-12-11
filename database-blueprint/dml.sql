-- Insert data into 'users'
INSERT INTO users (username, email, password_hash, role) VALUES
('kta', 'kta@unpar.ac.id', 'pass', 'admin'),
('hans', 'hans@student.unpar.ac.id', 'pass', 'mahasiswa'),
('manuel', 'manuel@student.unpar.ac.id', 'pass', 'mahasiswa'),
('elvin', 'elvin@student.unpar.ac.id', 'pass', 'mahasiswa'),
('eli', 'eli@unpar.ac.id', 'pass', 'dosen'),
('mariskha', 'mariskha@unpar.ac.id', 'pass', 'dosen');

-- Insert data into 'mahasiswa'
INSERT INTO mahasiswa (npm, nama, user_id) VALUES
('6182201001', 'Hans Stellarblade', 2),
('6182201002', 'Manuel Hamenang', 3),
('6182201003', 'Elvin Pisau', 4);

-- Insert data into 'dosen'
INSERT INTO dosen (nip, nama, user_id) VALUES
('2024001', 'Elisati Hulu', 5),
('2024002', 'Mariskha Tri Adithia', 6);

-- Insert data into 'semester'
INSERT INTO semester (tahun_ajaran, periode, is_active) VALUES
('2023/2024', 'ganjil', true),
('2023/2024', 'genap', false);

-- Insert data into 'tugas_akhir'
INSERT INTO tugas_akhir (mahasiswa_id, semester_id, judul, topik, jenis_ta, status) VALUES
(1, 1, 'Implementasi protokol transfer data multiple frame/packet yang reliable di Wireless Sensor Network bertopologi flat', 'ELH5801ACS', 'ta1', 'draft'),
(2, 1, 'Visualisasi Graf Interaksi Pengguna Twitter', 'MTA5801BCS', 'ta1', 'draft'),
(3, 1, 'Pengembangan aplikasi pemantauan getaran menggunakan Eclipse Paho MQTT Python client library', 'ELH5806CCS', 'ta1', 'draft');

-- Insert data into 'pembimbing_ta'
INSERT INTO pembimbing_ta (ta_id, dosen_id) VALUES
(1, 1),
(2, 2),
(3, 1);

-- Insert data into 'sidang'
INSERT INTO sidang (ta_id, jadwal, ruangan, status_sidang) VALUES
(1, '2024-01-15 10:00:00', '91120', 'terjadwal'),
(2, '2024-01-15 10:00:00', '91121', 'terjadwal'),
(3, '2024-01-15 10:00:00', '91122', 'terjadwal');

-- Insert data into 'penguji_sidang'
INSERT INTO penguji_sidang (sidang_id, dosen_id, peran_penguji) VALUES
(1, 2, 'penguji1'),
(2, 1, 'penguji2'),
(3, 2, 'penguji1');

-- Insert data into 'komponen_nilai' for PENGUJI
INSERT INTO komponen_nilai (semester_id, nama_komponen, bobot, tipe_penilai, deskripsi) VALUES
(1, 'Tata tulis laporan', 15, 'penguji', 'Penilaian tata tulis dan format laporan'),
(1, 'Kelengkapan materi', 20, 'penguji', 'Penilaian kelengkapan isi materi'),
(1, 'Pencapaian tujuan', 25, 'penguji', 'Penilaian pencapaian tujuan penelitian'),
(1, 'Penguasaan materi', 30, 'penguji', 'Penilaian penguasaan materi oleh mahasiswa'),
(1, 'Presentasi', 10, 'penguji', 'Penilaian kemampuan presentasi');

-- Insert data into 'komponen_nilai' for PEMBIMBING
INSERT INTO komponen_nilai (semester_id, nama_komponen, bobot, tipe_penilai, deskripsi) VALUES
(1, 'Tata tulis laporan', 20, 'pembimbing', 'Penilaian tata tulis dan format laporan'),
(1, 'Kelengkapan materi', 20, 'pembimbing', 'Penilaian kelengkapan isi materi'),
(1, 'Proses bimbingan', 30, 'pembimbing', 'Penilaian proses bimbingan dan progress'),
(1, 'Penguasaan materi', 30, 'pembimbing', 'Penilaian penguasaan materi oleh mahasiswa');
