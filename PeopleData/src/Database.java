import java.sql.*;

public class Database {
    private Connection connection;

    // constructor: buka koneksi ke MySQL
    public Database() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_people",
                    "root",
                    "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // isi parameter (?) pada PreparedStatement
    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    // digunakan untuk SELECT
    public ResultSet selectQuery(String sql, Object... params) {
        try {
            PreparedStatement ps = prepare(sql, params);
            ps.closeOnCompletion(); // statement ikut tertutup saat ResultSet ditutup
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // digunakan untuk INSERT, UPDATE, DELETE
    public int insertUpdateDeleteQuery(String sql, Object... params) {
        try (PreparedStatement ps = prepare(sql, params)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
