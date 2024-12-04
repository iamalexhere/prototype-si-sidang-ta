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

-- Insert data into 'komponen_nilai'
INSERT INTO komponen_nilai (semester_id, nama_komponen, bobot, tipe_penilai, deskripsi) VALUES
(1, 'Proposal Presentation', 40, 'pembimbing', 'Assessment of the proposal presentation'),
(1, 'Final Report', 60, 'penguji', 'Assessment of the final report quality');

-- Insert data into 'nilai_sidang'
INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, nilai) VALUES
(1, 1, 1, 85),
(1, 2, 1, 90),
(2, 1, 2, 80),
(2, 2, 2, 88);

-- Insert data into 'catatan_revisi'
INSERT INTO catatan_revisi (sidang_id, dosen_id, isi_catatan) VALUES
(1, 1, 'Clarify the methodology in section 3.'),
(2, 2, 'Revise the conclusion for better clarity.');

-- Insert data into 'bap'
INSERT INTO bap (sidang_id, catatan_tambahan) VALUES
(1, 'Overall good work, minor revisions needed.'),
(2, 'Well-documented, but requires additional references.');

-- Insert data into 'persetujuan_bap'
INSERT INTO persetujuan_bap (bap_id, user_id, is_approved, approved_at) VALUES
(1, 1, true, '2024-01-16 10:00:00'),
(2, 5, true, '2024-01-16 11:00:00');
