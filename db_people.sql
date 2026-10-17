-- Database untuk TP5 DPBO (kolom disesuaikan dengan TP4: ada jumlah_kucing)
CREATE DATABASE IF NOT EXISTS db_people;
USE db_people;

CREATE TABLE IF NOT EXISTS person (
    id            VARCHAR(20)  NOT NULL,
    nama          VARCHAR(100) NOT NULL,
    thn_lahir     INT          NOT NULL,
    kategori      VARCHAR(50)  NOT NULL,
    jumlah_kucing INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

INSERT INTO person (id, nama, thn_lahir, kategori, jumlah_kucing) VALUES
('P001', 'Ucup Santoso', 2004, 'Mahasiswa', 2),
('P002', 'Alucard', 2003, 'Mahasiswa', 0),
('P003', 'Alok', 1985, 'Dosen', 1),
('P004', 'Mas Rusdi', 1990, 'Dosen', 3),
('P005', 'Si Imut', 1995, 'Tenaga Kependidikan', 0),
('P006', 'Wednesday', 1998, 'Tenaga Kependidikan', 5),
('P007', 'Heri Puter', 2001, 'Alumni', 1),
('P008', 'Sepidermen', 2000, 'Alumni', 0),
('P009', 'Om Gatot', 2005, 'Mahasiswa', 4),
('P010', 'Titut', 2002, 'Mahasiswa', 2);
