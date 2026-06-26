# 🛒 Toko Berkah Jaya (Point of Sales)

Toko Berkah Jaya adalah aplikasi kasir / *Point of Sales* (POS) berbasis Desktop yang dibangun menggunakan **Java Swing**. Aplikasi ini didesain dengan antarmuka yang modern, elegan, dan *user-friendly* (didukung oleh FlatLaf).

## ✨ Fitur Utama

- **📦 Manajemen Barang:** Mengelola data barang, termasuk stok awal, satuan, harga beli (modal), dan harga jual.
- **👥 Manajemen Pengguna:** Mengatur *user* (kasir atau admin) yang dapat masuk ke dalam sistem.
- **🤝 Manajemen Customer:** Mendata pelanggan toko.
- **💳 Sistem Transaksi Kasir:**
  - Antarmuka keranjang belanja yang interaktif.
  - Perhitungan otomatis untuk *Grand Total*, nilai tunai (pembayaran), dan uang kembalian.
- **🛡️ Validasi Input Pintar (UX Interaktif):**
  - Dilengkapi dengan *Custom Toast Notification* (pesan error melayang yang hilang secara otomatis).
  - Indikator *border* merah sementara jika pengguna memasukkan karakter yang salah (misalnya, mengetik huruf pada kolom harga/angka).

## 💻 Teknologi yang Digunakan

- **Bahasa Pemrograman:** Java (JDK)
- **GUI Framework:** Java Swing
- **Tema (Look and Feel):** [FlatLaf](https://www.formdev.com/flatlaf/)
- **IDE / Build Tools:** NetBeans IDE (Apache Ant)

## 🚀 Cara Menjalankan Aplikasi

1. Clone atau *download* repositori ini ke komputer Anda.
   ```bash
   git clone https://github.com/pramadiant/TokoBerkahJayaa.git
   ```
2. Buka **NetBeans IDE**.
3. Pilih **File** > **Open Project...** lalu arahkan ke folder `TokoBerkahJaya`.
4. Pastikan *library* UI (`flatlaf-3.4.1.jar`) yang berada di dalam folder `lib/` sudah terdeteksi di bagian *Libraries* *project* Anda.
5. Klik kanan pada *project* `TokoBerkahJaya` dan pilih **Run** (atau tekan F6).

## 🗄️ Setup Database & Kredensial Login

Aplikasi ini membutuhkan *database* MySQL lokal (menggunakan XAMPP, Laragon, dll). File *export database* telah disertakan di dalam *project* ini.

**Langkah-langkah Import Database:**
1. Nyalakan server MySQL Anda (melalui XAMPP Control Panel atau Laragon).
2. Buka phpMyAdmin, HeidiSQL, DBeaver, atau MySQL *client* favorit Anda.
3. Lakukan **Import** (atau eksekusi/jalankan) file `databasenetbeans.sql` yang terletak di folder utama (*root*) *project* ini.
   *(Catatan: File tersebut sudah berisi perintah otomatis untuk membuat database `db_toko_berkah` sekaligus tabel dan isinya).*
4. Secara *default*, koneksi aplikasi Java sudah diarahkan ke:
   - **Nama Database:** `db_toko_berkah`
   - **User MySQL:** `root`
   - **Password MySQL:** *(kosong)*
   > **Penting:** Jika komputer Anda (atau dosen penilai) menggunakan *password* untuk user `root`, silakan sesuaikan pengaturannya di dalam file `src/config/Koneksi.java` pada baris ke-15 sampai 17.

**Kredensial Login Aplikasi:**
Karena seluruh data *dummy* otomatis ikut masuk saat proses *import* selesai, Anda bisa langsung *login* ke dalam aplikasi menggunakan salah satu akun berikut:
- **Hak Akses Admin:** Username: `admin` | Password: `123`
- **Hak Akses Kasir:** Username: `kasir1` | Password: `123`

---
*Dibuat untuk keperluan tugas pemrograman aplikasi Point of Sales.*
