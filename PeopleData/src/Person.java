public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    private int jumlahKucing; // atribut baru

    // Constructor
    public Person() {
    }

    public Person(String id, String nama, int tahunLahir, String kategori, int jumlahKucing) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.jumlahKucing = jumlahKucing;
    }

    // Getter dan Setter untuk id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getter dan Setter untuk nama
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    // Getter dan Setter untuk tahun lahir
    public int getTahunLahir() { return tahunLahir; }
    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }

    // Getter dan Setter untuk kategori
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    // Getter dan Setter untuk jumlah kucing
    public int getJumlahKucing() { return jumlahKucing; }
    public void setJumlahKucing(int jumlahKucing) { this.jumlahKucing = jumlahKucing; }
}
