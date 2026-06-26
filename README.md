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

Aplikasi ini membutuhkan *database* MySQL untuk dapat berjalan (koneksi diatur pada `Koneksi.java` ke *database* `db_toko_berkah`). 

**Kredensial Default Login:**
Karena sistem login membaca langsung dari tabel `tb_user`, pastikan Anda sudah memasukkan (*insert*) data pengguna ke *database* lokal Anda. Secara umum, kredensial yang biasa digunakan adalah:
- **Username:** `admin` *(atau sesuaikan dengan database Anda)*
- **Password:** `admin` *(atau sesuaikan dengan database Anda)*

---
*Dibuat untuk keperluan tugas pemrograman aplikasi Point of Sales.*
