CREATE EXTENSION IF NOT EXISTS pgcrypto;
DROP FUNCTION IF EXISTS hash_password(text);

CREATE OR REPLACE FUNCTION hash_password(password TEXT)
RETURNS TEXT AS $$
BEGIN
  RETURN crypt(password, gen_salt('bf', 8));
END;
$$ LANGUAGE plpgsql;

-- Dosen CS
INSERT INTO users (username, email, password_hash, role) VALUES
('keenan.leman', 'keenan@unpar.ac.id', hash_password('pass'), 'dosen'),
('maria.veronica', 'maria@unpar.ac.id', hash_password('pass'), 'dosen'),
('lionov', 'lionov@unpar.ac.id', hash_password('pass'), 'dosen'),
('raymond.chandra', 'raymond@unpar.ac.id', hash_password('pass'), 'dosen'),
('husnul.hakim', 'husnul@unpar.ac.id', hash_password('pass'), 'dosen'),
('pascal.nugroho', 'pascal@unpar.ac.id', hash_password('pass'), 'dosen'),
('natalia', 'natalia@unpar.ac.id', hash_password('pass'), 'dosen'),
('mariskha.tri', 'mariskha@unpar.ac.id', hash_password('pass'), 'dosen'),
('vania.natali', 'vania@unpar.ac.id', hash_password('pass'), 'dosen');

INSERT INTO dosen (nip, nama, user_id)
SELECT '2024' || LPAD((ROW_NUMBER() OVER (ORDER BY nama)::INTEGER + 0)::TEXT, 3, '0'), nama, user_id
FROM (
    SELECT 'Keenan Adiwijaya Leman, S.T, M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'keenan.leman') AS user_id UNION ALL
    SELECT 'Maria Veronica, S.T., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'maria.veronica') AS user_id UNION ALL
    SELECT 'Lionov, Ph.D.' AS nama, (SELECT user_id FROM users WHERE username = 'lionov') AS user_id UNION ALL
    SELECT 'Raymond Chandra Putra, S.T., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'raymond.chandra') AS user_id UNION ALL
    SELECT 'Husnul Hakim, S.Kom., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'husnul.hakim') AS user_id UNION ALL
    SELECT 'Pascal A. Nugroho, S.Kom, M.Comp.' AS nama, (SELECT user_id FROM users WHERE username = 'pascal.nugroho') AS user_id UNION ALL
    SELECT 'Natalia, S.Si, M.Si.' AS nama, (SELECT user_id FROM users WHERE username = 'natalia') AS user_id UNION ALL
    SELECT 'Mariskha Tri Adithia, S.Si., M.Sc., PDEng.' AS nama, (SELECT user_id FROM users WHERE username = 'mariskha.tri') AS user_id UNION ALL
    SELECT 'Vania Natali, S.Kom, M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'vania.natali') AS user_id
) as subquery;

-- Dosen DS
INSERT INTO users (username, email, password_hash, role) VALUES
('luciana.abednego', 'luciana@unpar.ac.id', hash_password('pass'), 'dosen'),
('veronica.moertini', 'veronica@unpar.ac.id', hash_password('pass'), 'dosen'),
('gede.karya', 'gede@unpar.ac.id', hash_password('pass'), 'dosen'),
('elisati.hulu', 'elisati@unpar.ac.id', hash_password('pass'), 'dosen'),
('cecilia.nugraheni', 'cecilia@unpar.ac.id', hash_password('pass'), 'dosen'),
('rosa.padmowati', 'rosa@unpar.ac.id', hash_password('pass'), 'dosen');


INSERT INTO dosen (nip, nama, user_id)
SELECT '2024' || LPAD((ROW_NUMBER() OVER (ORDER BY nama)::INTEGER + 9)::TEXT, 3, '0'), nama, user_id
FROM (
    SELECT 'Luciana Abednego, S.Kom., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'luciana.abednego') AS user_id UNION ALL
    SELECT 'Prof. Dr. Ir. Veronica Sri Moertini, M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'veronica.moertini') AS user_id UNION ALL
    SELECT 'Gede Karya, S.T., M.T., CISA' AS nama, (SELECT user_id FROM users WHERE username = 'gede.karya') AS user_id UNION ALL
    SELECT 'Elisati Hulu, S.T., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'elisati.hulu') AS user_id UNION ALL
    SELECT 'Dr.rer.nat.Cecilia Esti Nugraheni, S.T., M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'cecilia.nugraheni') AS user_id UNION ALL
    SELECT 'Dra. Rosa de Lima Endang Padmowati, M.T.' AS nama, (SELECT user_id FROM users WHERE username = 'rosa.padmowati') AS user_id
) AS subquery;

-- Insert Mahasiswa
INSERT INTO users (username, email, password_hash, role) VALUES
('6182201001', '6182201001@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201002', '6182201002@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201003', '6182201003@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201004', '6182201004@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201005', '6182201005@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201006', '6182201006@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201007', '6182201007@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201008', '6182201008@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201009', '6182201009@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201010', '6182201010@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201011', '6182201011@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201012', '6182201012@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201013', '6182201013@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201014', '6182201014@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201015', '6182201015@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201016', '6182201016@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201017', '6182201017@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201018', '6182201018@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201019', '6182201019@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201020', '6182201020@student.unpar.ac.id', hash_password('pass'), 'mahasiswa'),
('6182201021', '6182201021@student.unpar.ac.id', hash_password('pass'), 'mahasiswa');

INSERT INTO mahasiswa (npm, nama, user_id)
SELECT npm, nama, user_id
FROM (
    SELECT '6182201001' AS npm, 'Victor Martins' AS nama, (SELECT user_id FROM users WHERE username = '6182201001') AS user_id UNION ALL
    SELECT '6182201002' AS npm, 'Luke Browning' AS nama, (SELECT user_id FROM users WHERE username = '6182201002') AS user_id UNION ALL
    SELECT '6182201003' AS npm, 'Oliver Bearman' AS nama, (SELECT user_id FROM users WHERE username = '6182201003') AS user_id UNION ALL
    SELECT '6182201004' AS npm, 'Andrea Kimi Antonelli' AS nama, (SELECT user_id FROM users WHERE username = '6182201004') AS user_id UNION ALL
    SELECT '6182201005' AS npm, 'Leonardo Fornaroli' AS nama, (SELECT user_id FROM users WHERE username = '6182201005') AS user_id UNION ALL
    SELECT '6182201006' AS npm, 'Ritomo Miyata' AS nama, (SELECT user_id FROM users WHERE username = '6182201006') AS user_id UNION ALL
    SELECT '6182201007' AS npm, 'Jak Crawford' AS nama, (SELECT user_id FROM users WHERE username = '6182201007') AS user_id UNION ALL
    SELECT '6182201008' AS npm, 'Dennis Hauger' AS nama, (SELECT user_id FROM users WHERE username = '6182201008') AS user_id UNION ALL
    SELECT '6182201009' AS npm, 'Kush Maini' AS nama, (SELECT user_id FROM users WHERE username = '6182201009') AS user_id UNION ALL
    SELECT '6182201010' AS npm, 'Gabriel Bortoleto' AS nama, (SELECT user_id FROM users WHERE username = '6182201010') AS user_id UNION ALL
    SELECT '6182201011' AS npm, 'Richard Verschoor' AS nama, (SELECT user_id FROM users WHERE username = '6182201011') AS user_id UNION ALL
    SELECT '6182201012' AS npm, 'Oliver Goethe' AS nama, (SELECT user_id FROM users WHERE username = '6182201012') AS user_id UNION ALL
    SELECT '6182201013' AS npm, 'Juan Manuel Correa' AS nama, (SELECT user_id FROM users WHERE username = '6182201013') AS user_id UNION ALL
    SELECT '6182201014' AS npm, 'Rafael Villagomez' AS nama, (SELECT user_id FROM users WHERE username = '6182201014') AS user_id UNION ALL
     SELECT '6182201015' AS npm, 'Amaury Cordeel' AS nama, (SELECT user_id FROM users WHERE username = '6182201015') AS user_id UNION ALL
    SELECT '6182201016' AS npm, 'Paul Aron' AS nama, (SELECT user_id FROM users WHERE username = '6182201016') AS user_id UNION ALL
    SELECT '6182201017' AS npm, 'Isack Hadjar' AS nama, (SELECT user_id FROM users WHERE username = '6182201017') AS user_id UNION ALL
    SELECT '6182201018' AS npm, 'Josep Maria Marti' AS nama, (SELECT user_id FROM users WHERE username = '6182201018') AS user_id UNION ALL
    SELECT '6182201019' AS npm, 'Roman Stanek' AS nama, (SELECT user_id FROM users WHERE username = '6182201019') AS user_id UNION ALL
    SELECT '6182201020' AS npm, 'Joshua Duerksen' AS nama, (SELECT user_id FROM users WHERE username = '6182201020') AS user_id UNION ALL
    SELECT '6182201021' AS npm, 'Taylor Barnard' AS nama, (SELECT user_id FROM users WHERE username = '6182201021') AS user_id
) AS subquery;

INSERT INTO semester (tahun_ajaran, periode, is_active) VALUES
('2023/2024', 'ganjil', false),
('2023/2024', 'genap', true);