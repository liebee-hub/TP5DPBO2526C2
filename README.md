# TP5DPBO2526C2

## Janji

Saya Refan Maher Aysel dengan NIM 2509129 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Isi program

Aplikasi desktop CRUD sederhana berbasis **Java Swing** (IntelliJ GUI Designer) untuk mengelola data orang di lingkungan kampus. Pada TP5, data tidak lagi disimpan di `ArrayList` melainkan di database **MySQL** lewat **JDBC**, sehingga data tetap tersimpan setelah program ditutup.

## Struktur Folder

```
TP5DPBO2526C/
├── PeopleData/
│   ├── lib/
│   │   └── mysql-connector-j-26.7.0.jar   # JDBC MySQL connector
│   └── src/
│       ├── Database.java      # koneksi & query ke MySQL
│       ├── Person.java        # model data
│       ├── PeopleMenu.java    # logika GUI + CRUD
│       └── PeopleMenu.form    # desain tampilan (GUI Designer)
├── Dokumentasi/               # screenshot
├── db_people.sql              # skema tabel + data awal
└── README.md
```

## Desain & Fungsi Program

### Tabel `person` (database `db_people`)

| Kolom | Tipe | Komponen di Form | Keterangan |
|---|---|---|---|
| `id` | `VARCHAR(20)` | `JTextField` | Primary key, tidak boleh kembar |
| `nama` | `VARCHAR(100)` | `JTextField` | |
| `thn_lahir` | `INT` | `JTextField` | Harus angka, rentang 1900 – tahun sekarang |
| `kategori` | `VARCHAR(50)` | `JComboBox` | Mahasiswa / Dosen / Tenaga Kependidikan / Alumni |
| `jumlah_kucing` | `INT` | `JSpinner` | Jumlah kucing peliharaan, rentang 0 – 99 |

### Class `Database`

| Method | Fungsi |
|---|---|
| `Database()` | Constructor, membuka koneksi ke `jdbc:mysql://localhost:3306/db_people` |
| `selectQuery(sql, params...)` | Menjalankan `SELECT`, mengembalikan `ResultSet` |
| `insertUpdateDeleteQuery(sql, params...)` | Menjalankan `INSERT`, `UPDATE`, `DELETE`, mengembalikan jumlah baris yang berubah |

Query memakai `PreparedStatement` (parameter `?`) sehingga aman dari SQL injection.

### Komponen Form

- `titleLabel` – judul "Data Orang"
- `idField`, `namaField`, `tahunLahirField` – input teks
- `kategoriComboBox` – pilihan kategori (item pertama `???` = belum memilih)
- `kucingSpinner` – input jumlah kucing peliharaan (0 – 99)
- `addUpdateButton` – berubah teks **Add ⇄ Update** sesuai mode
- `cancelButton` – membersihkan form
- `deleteButton` – hanya tampil saat sebuah baris dipilih
- `peopleTable` – menampilkan seluruh data dari database (kolom: No, ID, Nama, Tahun Lahir, Kategori, Jumlah Kucing; sel tidak bisa diedit langsung)

### Fitur Tambahan

- **Confirmation prompt sebelum delete** (`JOptionPane.showConfirmDialog` Yes/No). Data hanya dihapus jika user memilih **Yes**.
- **Dialog error** pada kondisi berikut:
  - ID, Nama, atau Tahun Lahir kosong saat insert/update
  - Kategori belum dipilih (`???`)
  - ID sudah ada di database saat insert
  - ID diganti menjadi ID milik data lain saat update
  - Tahun lahir bukan angka atau di luar rentang 1900 – tahun sekarang
  - Jumlah kucing bukan angka bulat 0 – 99
  - Gagal terhubung ke database

## Alur Program

1. **Start** – `main()` membuat window `PeopleMenu`. Constructor membuat objek `Database` (koneksi ke `db_people`), lalu menampilkan data di tabel lewat `setTable()` yang menjalankan `SELECT * FROM person ORDER BY id`.
2. **Create (Add)** – Saat tidak ada baris terpilih (`selectedIndex == -1`), tombol berlabel **Add**. Klik → `insertData()`: baca & validasi form → cek ID ke database (`SELECT COUNT(*) ... WHERE id = ?`) → `INSERT INTO person ...` → refresh tabel → `clearForm()`.
3. **Read** – Seluruh isi tabel `person` selalu ditampilkan di tabel. Klik sebuah baris → `selectedIndex` diisi, nilai baris dimasukkan ke form (termasuk combo box & spinner), tombol berubah menjadi **Update**, dan tombol **Delete** muncul.
4. **Update** – Saat baris terpilih, klik **Update** → `updateData()`: baca & validasi form → jika ID diganti, cek ID baru belum dipakai data lain → `UPDATE person SET ... WHERE id = <ID lama>` → refresh tabel → `clearForm()`.
5. **Delete** – Klik **Delete** → muncul dialog konfirmasi. Jika **Yes** → `deleteData()`: `DELETE FROM person WHERE id = ?` → refresh tabel → `clearForm()`. Jika **No** → tidak ada perubahan.
6. **Cancel** – `clearForm()`: kosongkan semua input, kembalikan tombol ke **Add**, sembunyikan **Delete**, `selectedIndex = -1`.

## Dokumentasi Program Dijalankan

Tampilan Awal
<img width="1917" height="1016" alt="tp5 read" src="https://github.com/user-attachments/assets/0f348a20-e761-480a-b058-ed377c9c3c79" />

Create
<img width="1917" height="1016" alt="tp5 add" src="https://github.com/user-attachments/assets/3b926542-e096-430c-8fa0-b42d0aba3bd4" />

Read
<img width="1917" height="1015" alt="tp5 readd" src="https://github.com/user-attachments/assets/3e61a3d9-69a2-4843-9338-f9125f4f4a36" />

Update
<img width="1917" height="1012" alt="tp5 update" src="https://github.com/user-attachments/assets/9b155074-d223-4bb7-b985-0ddb241e9388" />

Delete
<img width="1917" height="1017" alt="tp5 delete" src="https://github.com/user-attachments/assets/a0df6954-937c-4395-9518-f8b8a8869c8d" />

Error Kolom Kosong
<img width="1917" height="1017" alt="tp5 error1" src="https://github.com/user-attachments/assets/0513b060-d823-491d-b160-ba329e34f20c" />

Error Kategori Kosong
<img width="1917" height="1015" alt="tp5 error2" src="https://github.com/user-attachments/assets/5ada10a6-3c00-4f8a-939f-b5e2a29837d0" />

Error ID Sudah Ada
<img width="1917" height="1015" alt="tp5 error3" src="https://github.com/user-attachments/assets/a589e595-731e-4be7-b85c-70575aa6b1df" />

Error Tahun Lahir
<img width="1917" height="1015" alt="tp5 error6" src="https://github.com/user-attachments/assets/5bfa5a84-cace-40aa-a799-6516fdfb566e" />
<img width="1917" height="1016" alt="tp5 error5" src="https://github.com/user-attachments/assets/1f0cfcc6-5964-43f2-8ed0-c3523cedb1b9" />

Data di Database (phpMyAdmin)
<img width="1917" height="961" alt="tp5 phpmyadmin" src="https://github.com/user-attachments/assets/f3a6b35f-89be-48a6-9a6d-d3813e17bec5" />
