import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.Year;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(700, 600);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris tabel yang sedang dipilih (-1 = tidak ada)
    private int selectedIndex = -1;
    // koneksi ke database MySQL (sumber data tabel)
    private Database database;

    // component hasil binding dari PeopleMenu.form
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JSpinner kucingSpinner;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;

    // constructor
    public PeopleMenu() {
        // koneksi ke database
        try {
            database = new Database();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal terhubung ke database:\n" + ex.getMessage(),
                    "Error Koneksi", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }

        // isi tabel person
        peopleTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Mahasiswa", "Dosen", "Tenaga Kependidikan", "Alumni" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur spinner jumlah kucing (nilai awal 0, minimal 0, maksimal 99, naik 1)
        kucingSpinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan -> minta konfirmasi dulu
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                int pilihan = JOptionPane.showConfirmDialog(
                        PeopleMenu.this,
                        "Yakin ingin menghapus data \"" + nama + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (pilihan == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                int row = peopleTable.getSelectedRow();
                if (row == -1) {
                    return;
                }
                selectedIndex = row;

                // simpan value textfield, combo box, dan spinner
                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curKucing = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield, combo box, dan spinner
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                kucingSpinner.setValue(Integer.parseInt(curKucing));

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Jumlah Kucing" };

        // buat objek tabel dengan kolom yang sudah dibuat
        // (sel dibuat tidak bisa diedit langsung, edit lewat form)
        DefaultTableModel tmp = new DefaultTableModel((Object[][]) null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // isi tabel dengan hasil query
        try (ResultSet rs = database.selectQuery("SELECT * FROM person ORDER BY id")) {
            int i = 0;
            while (rs.next()) {
                Object[] row = { ++i,
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getInt("thn_lahir"),
                        rs.getString("kategori"),
                        rs.getInt("jumlah_kucing")
                };
                tmp.addRow(row);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return tmp;
    }

    public void insertData() {
        // ambil & validasi value dari form
        Person person = buildPersonFromForm();
        if (person == null) {
            return;
        }

        // ID tidak boleh kembar
        if (isIdUsed(person.getId())) {
            showError("ID '" + person.getId() + "' sudah digunakan!");
            return;
        }

        // tambahkan data ke database
        database.insertUpdateDeleteQuery(
                "INSERT INTO person (id, nama, thn_lahir, kategori, jumlah_kucing) VALUES (?, ?, ?, ?, ?)",
                person.getId(), person.getNama(), person.getTahunLahir(),
                person.getKategori(), person.getJumlahKucing());

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
    }

    public void updateData() {
        // ambil & validasi data dari form
        Person person = buildPersonFromForm();
        if (person == null) {
            return;
        }

        // ID lama (dari baris tabel yang dipilih) dipakai sebagai kunci WHERE
        String oldId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();

        // kalau ID diganti, ID baru tidak boleh sama dengan data lain
        if (!person.getId().equalsIgnoreCase(oldId) && isIdUsed(person.getId())) {
            showError("ID '" + person.getId() + "' sudah digunakan!");
            return;
        }

        // ubah data di database
        database.insertUpdateDeleteQuery(
                "UPDATE person SET id = ?, nama = ?, thn_lahir = ?, kategori = ?, jumlah_kucing = ? WHERE id = ?",
                person.getId(), person.getNama(), person.getTahunLahir(),
                person.getKategori(), person.getJumlahKucing(), oldId);

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil diubah");
    }

    public void deleteData() {
        // ambil ID baris yang dipilih, lalu hapus dari database
        String id = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
        database.insertUpdateDeleteQuery("DELETE FROM person WHERE id = ?", id);

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua textfield, combo box, dan spinner
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        kucingSpinner.setValue(0);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // hilangkan highlight baris tabel
        peopleTable.clearSelection();

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    // ---------- method bantu ----------

    // baca isi form, validasi, lalu kembalikan sebagai object Person (null jika tidak valid)
    private Person buildPersonFromForm() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunText = tahunLahirField.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || tahunText.isEmpty()) {
            showError("ID, Nama, dan Tahun Lahir tidak boleh kosong!");
            return null;
        }

        int tahunLahir;
        try {
            tahunLahir = Integer.parseInt(tahunText);
        } catch (NumberFormatException ex) {
            showError("Tahun lahir harus berupa angka!");
            return null;
        }

        int tahunSekarang = Year.now().getValue();
        if (tahunLahir < 1900 || tahunLahir > tahunSekarang) {
            showError("Tahun lahir harus antara 1900 dan " + tahunSekarang + "!");
            return null;
        }

        if (kategoriComboBox.getSelectedIndex() <= 0) {
            showError("Silakan pilih kategori!");
            return null;
        }
        String kategori = kategoriComboBox.getSelectedItem().toString();

        // pastikan teks yang diketik di spinner sudah diproses menjadi nilai
        try {
            kucingSpinner.commitEdit();
        } catch (ParseException ex) {
            showError("Jumlah kucing harus berupa angka bulat 0 - 99!");
            return null;
        }
        int jumlahKucing = (Integer) kucingSpinner.getValue();

        return new Person(id, nama, tahunLahir, kategori, jumlahKucing);
    }

    // cek apakah ID sudah ada di database
    private boolean isIdUsed(String id) {
        try (ResultSet rs = database.selectQuery("SELECT COUNT(*) FROM person WHERE id = ?", id)) {
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
