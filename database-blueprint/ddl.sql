-- Create custom enum types
CREATE TYPE user_role AS ENUM ('admin', 'mahasiswa', 'dosen');
CREATE TYPE status_ta AS ENUM ('draft', 'diajukan', 'diterima', 'ditolak', 'dalam_pengerjaan', 'selesai');
CREATE TYPE periode AS ENUM ('ganjil', 'genap');
CREATE TYPE jenis_ta AS ENUM ('skripsi', 'tesis');
CREATE TYPE status_sidang AS ENUM ('terjadwal', 'berlangsung', 'selesai', 'dibatalkan');
CREATE TYPE peran_penguji AS ENUM ('ketua', 'anggota');
CREATE TYPE tipe_penilai AS ENUM ('pembimbing', 'penguji');

-- Create tables
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true
);

CREATE TABLE mahasiswa (
    mahasiswa_id SERIAL PRIMARY KEY,
    npm VARCHAR(20) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    status_ta status_ta DEFAULT 'draft',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dosen (
    dosen_id SERIAL PRIMARY KEY,
    nip VARCHAR(20) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    is_koordinator BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE semester (
    semester_id SERIAL PRIMARY KEY,
    tahun_ajaran VARCHAR(9) NOT NULL,
    periode periode NOT NULL,
    is_active BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tahun_ajaran, periode)
);

CREATE TABLE tugas_akhir (
    ta_id SERIAL PRIMARY KEY,
    mahasiswa_id INTEGER NOT NULL REFERENCES mahasiswa(mahasiswa_id),
    semester_id INTEGER NOT NULL REFERENCES semester(semester_id),
    judul TEXT NOT NULL,
    topik VARCHAR(255) NOT NULL,
    jenis_ta jenis_ta NOT NULL,
    status status_ta DEFAULT 'draft',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pembimbing_ta (
    ta_id INTEGER REFERENCES tugas_akhir(ta_id),
    dosen_id INTEGER REFERENCES dosen(dosen_id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ta_id, dosen_id)
);

CREATE TABLE sidang (
    sidang_id SERIAL PRIMARY KEY,
    ta_id INTEGER NOT NULL UNIQUE REFERENCES tugas_akhir(ta_id),
    jadwal TIMESTAMP NOT NULL,
    ruangan VARCHAR(50) NOT NULL,
    status_sidang status_sidang DEFAULT 'terjadwal',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE penguji_sidang (
    sidang_id INTEGER REFERENCES sidang(sidang_id),
    dosen_id INTEGER REFERENCES dosen(dosen_id),
    peran_penguji peran_penguji NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (sidang_id, dosen_id)
);

CREATE TABLE komponen_nilai (
    komponen_id SERIAL PRIMARY KEY,
    semester_id INTEGER NOT NULL REFERENCES semester(semester_id),
    nama_komponen VARCHAR(100) NOT NULL,
    bobot FLOAT NOT NULL CHECK (bobot >= 0 AND bobot <= 100),
    tipe_penilai tipe_penilai NOT NULL,
    deskripsi TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE nilai_sidang (
    nilai_id SERIAL PRIMARY KEY,
    sidang_id INTEGER NOT NULL REFERENCES sidang(sidang_id),
    komponen_id INTEGER NOT NULL REFERENCES komponen_nilai(komponen_id),
    dosen_id INTEGER NOT NULL REFERENCES dosen(dosen_id),
    nilai FLOAT NOT NULL CHECK (nilai >= 0 AND nilai <= 100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(sidang_id, komponen_id, dosen_id)
);

CREATE TABLE catatan_revisi (
    catatan_id SERIAL PRIMARY KEY,
    sidang_id INTEGER NOT NULL REFERENCES sidang(sidang_id),
    dosen_id INTEGER NOT NULL REFERENCES dosen(dosen_id),
    isi_catatan TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bap (
    bap_id SERIAL PRIMARY KEY,
    sidang_id INTEGER NOT NULL UNIQUE REFERENCES sidang(sidang_id),
    catatan_tambahan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE persetujuan_bap (
    bap_id INTEGER REFERENCES bap(bap_id),
    user_id INTEGER REFERENCES users(user_id),
    is_approved BOOLEAN DEFAULT false,
    approved_at TIMESTAMP,
    PRIMARY KEY (bap_id, user_id)
);

-- Create indexes for foreign keys and frequently queried columns
CREATE INDEX idx_mahasiswa_user_id ON mahasiswa(user_id);
CREATE INDEX idx_dosen_user_id ON dosen(user_id);
CREATE INDEX idx_tugas_akhir_mahasiswa_id ON tugas_akhir(mahasiswa_id);
CREATE INDEX idx_tugas_akhir_semester_id ON tugas_akhir(semester_id);
CREATE INDEX idx_sidang_ta_id ON sidang(ta_id);
CREATE INDEX idx_nilai_sidang_sidang_id ON nilai_sidang(sidang_id);
CREATE INDEX idx_catatan_revisi_sidang_id ON catatan_revisi(sidang_id);
CREATE INDEX idx_persetujuan_bap_user_id ON persetujuan_bap(user_id);

-- Add trigger for updating updated_at columns
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_sidang_updated_at
    BEFORE UPDATE ON sidang
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_nilai_sidang_updated_at
    BEFORE UPDATE ON nilai_sidang
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
