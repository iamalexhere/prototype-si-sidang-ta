-- Insert additional users for new mahasiswa
INSERT INTO users (username, email, password_hash, role) VALUES
('gabriel', 'gabriel@student.unpar.ac.id', 'pass', 'mahasiswa'),
('isack', 'isack@student.unpar.ac.id', 'pass', 'mahasiswa'),
('paul', 'paul@student.unpar.ac.id', 'pass', 'mahasiswa'),
('zane', 'zane@student.unpar.ac.id', 'pass', 'mahasiswa'),
('jak', 'jak@student.unpar.ac.id', 'pass', 'mahasiswa'),
('andrea', 'andrea@student.unpar.ac.id', 'pass', 'mahasiswa'),
('victor', 'victor@student.unpar.ac.id', 'pass', 'mahasiswa'),
('richard', 'richard@student.unpar.ac.id', 'pass', 'mahasiswa'),
('franco', 'franco@student.unpar.ac.id', 'pass', 'mahasiswa'),
('joshua', 'joshua@student.unpar.ac.id', 'pass', 'mahasiswa');

-- Insert additional mahasiswa
INSERT INTO mahasiswa (npm, nama, user_id) VALUES
('6182201004', 'Gabriel Bortoleto', 7),
('6182201005', 'Isack Hadjar', 8),
('6182201006', 'Paul Aron', 9),
('6182201007', 'Zane Maloney', 10),
('6182201008', 'Jak Crawford', 11),
('6182201009', 'Andrea Kimi Antonelli', 12),
('6182201010', 'Victor Martins', 13),
('6182201011', 'Richard Verschoor', 14),
('6182201012', 'Franco Colapinto', 15),
('6182201013', 'Joshua Dürksen', 16);

-- Insert additional dosen users
INSERT INTO users (username, email, password_hash, role) VALUES
('bambang', 'bambang@unpar.ac.id', 'pass', 'dosen'),
('sri', 'sri@unpar.ac.id', 'pass', 'dosen'),
('widodo', 'widodo@unpar.ac.id', 'pass', 'dosen'),
('retno', 'retno@unpar.ac.id', 'pass', 'dosen'),
('agus', 'agus@unpar.ac.id', 'pass', 'dosen');

-- Insert additional dosen
INSERT INTO dosen (nip, nama, user_id) VALUES
('2024003', 'Bambang Suryadi', 7),
('2024004', 'Sri Wahyuni', 8),
('2024005', 'Widodo Pranowo', 9),
('2024006', 'Retno Kusumaningrum', 10),
('2024007', 'Agus Setiawan', 11);

-- Insert additional tugas akhir for the new mahasiswa
INSERT INTO tugas_akhir (mahasiswa_id, semester_id, judul, topik, jenis_ta, status) VALUES
(4, 1, 'Analisis Performa Sistem Embedded IoT untuk Monitoring Lingkungan', 'MTA5802DCS', 'ta1', 'draft'),
(5, 1, 'Pengembangan Algoritma Machine Learning untuk Prediksi Cuaca', 'ELH5803ECS', 'ta1', 'draft'),
(6, 1, 'Desain Sistem Keamanan Jaringan Berbasis Blockchain', 'MTA5804FCS', 'ta1', 'draft'),
(7, 1, 'Implementasi Sistem Manajemen Energi Cerdas pada Gedung Perkantoran', 'ELH5805GCS', 'ta1', 'draft'),
(8, 1, 'Pengembangan Aplikasi Mobile untuk Pemantauan Kesehatan Berbasis AI', 'MTA5806HCS', 'ta1', 'draft'),
(9, 1, 'Optimasi Routing Protocol untuk Jaringan Ad-Hoc Mobile', 'ELH5807ICS', 'ta1', 'draft'),
(10, 1, 'Analisis Keamanan Sistem Informasi Menggunakan Metode Penetration Testing', 'MTA5808JCS', 'ta1', 'draft'),
(11, 1, 'Pengembangan Sistem Rekomendasi Berbasis Kecerdasan Buatan', 'ELH5809KCS', 'ta1', 'draft'),
(12, 1, 'Desain Arsitektur Mikroservis untuk Aplikasi Enterprise', 'MTA5810LCS', 'ta1', 'draft'),
(13, 1, 'Implementasi Sistem Deteksi Anomali Jaringan Menggunakan Deep Learning', 'ELH5811MCS', 'ta1', 'draft');

-- Insert pembimbing for the new tugas akhir
INSERT INTO pembimbing_ta (ta_id, dosen_id) VALUES
(4, 3),
(5, 4),
(6, 5),
(7, 1),
(8, 2),
(9, 3),
(10, 4),
(11, 5),
(12, 1),
(13, 2);

-- Insert sidang for the new tugas akhir
INSERT INTO sidang (ta_id, jadwal, ruangan, status_sidang) VALUES
(4, '2024-02-01 10:00:00', '91123', 'terjadwal'),
(5, '2024-02-01 11:00:00', '91124', 'terjadwal'),
(6, '2024-02-01 13:00:00', '91125', 'terjadwal'),
(7, '2024-02-01 14:00:00', '91126', 'terjadwal'),
(8, '2024-02-01 15:00:00', '91127', 'terjadwal'),
(9, '2024-02-02 10:00:00', '91128', 'terjadwal'),
(10, '2024-02-02 11:00:00', '91129', 'terjadwal'),
(11, '2024-02-02 13:00:00', '91130', 'terjadwal'),
(12, '2024-02-02 14:00:00', '91131', 'terjadwal'),
(13, '2024-02-02 15:00:00', '91132', 'terjadwal');

-- Insert penguji for the new sidang
INSERT INTO penguji_sidang (sidang_id, dosen_id, peran_penguji) VALUES
(4, 4, 'penguji1'),
(5, 5, 'penguji2'),
(6, 1, 'penguji1'),
(7, 2, 'penguji2'),
(8, 3, 'penguji1'),
(9, 4, 'penguji2'),
(10, 5, 'penguji1'),
(11, 1, 'penguji2'),
(12, 2, 'penguji1'),
(13, 3, 'penguji2');