package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.TugasAkhirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.entity.Semester;


// Repository untuk mengelola data tugas akhir menggunakan JDBC.
@Repository
public class JdbcTugasAkhirRepository implements TugasAkhirRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data tugas akhir dari database.
    @Override
    public List<TugasAkhir> findAll() {
         String sql = "SELECT ta.*, m.*, s.* FROM tugas_akhir ta " +
                       "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                        "JOIN semester s ON ta.semester_id = s.semester_id";
       return jdbcTemplate.query(sql,this::mapRowToTugasAkhir);
    }

    // Mengambil data tugas akhir berdasarkan ID.
    @Override
    public Optional<TugasAkhir> findById(Integer id) {
          String sql = "SELECT ta.*, m.*, s.* FROM tugas_akhir ta " +
                        "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                       "JOIN semester s ON ta.semester_id = s.semester_id "+
                        "WHERE ta.ta_id = ?";
        try {
            TugasAkhir tugasAkhir = jdbcTemplate.queryForObject(
               sql, this::mapRowToTugasAkhir, id);
            return Optional.ofNullable(tugasAkhir);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
      @Override
      public List<TugasAkhir> findByMahasiswaId(Integer mahasiswaId) {
        String sql = "SELECT ta.*, m.*, s.* FROM tugas_akhir ta " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
             "JOIN semester s ON ta.semester_id = s.semester_id "+
            "WHERE ta.mahasiswa_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToTugasAkhir, mahasiswaId);
      }


    // Menyimpan atau memperbarui data tugas akhir.
    @Override
     public TugasAkhir save(TugasAkhir tugasAkhir) {
       if (tugasAkhir.getTaId() == null) {
          return insert(tugasAkhir);
        }else{
            update(tugasAkhir);
            return tugasAkhir;
         }
    }
  private TugasAkhir insert(TugasAkhir tugasAkhir) {
      String sql = "INSERT INTO tugas_akhir (mahasiswa_id, semester_id, judul, topik, jenis_ta, status, created_at) VALUES (?, ?, ?, ?, ?::jenis_ta, ?::status_ta, ?)";

     KeyHolder keyHolder = new GeneratedKeyHolder();
     jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setObject(1, tugasAkhir.getMahasiswa().getMahasiswaId());
        ps.setObject(2, tugasAkhir.getSemester().getSemesterId());
        ps.setString(3, tugasAkhir.getJudul());
        ps.setString(4, tugasAkhir.getTopik());
        ps.setString(5, tugasAkhir.getJenisTA().toString());
        ps.setString(6, tugasAkhir.getStatus().toString());
         ps.setObject(7, LocalDateTime.now());
        return ps;
     }, keyHolder);
      tugasAkhir.setTaId(keyHolder.getKey().longValue());
    return tugasAkhir;
 }

 private void update(TugasAkhir tugasAkhir) {
    String sql = "UPDATE tugas_akhir SET mahasiswa_id = ?, semester_id = ?, judul = ?, topik = ?, jenis_ta = ?::jenis_ta, status = ?::status_ta WHERE ta_id = ?";
        jdbcTemplate.update(sql,
            tugasAkhir.getMahasiswa().getMahasiswaId(),
            tugasAkhir.getSemester().getSemesterId(),
            tugasAkhir.getJudul(),
            tugasAkhir.getTopik(),
            tugasAkhir.getJenisTA().toString(),
            tugasAkhir.getStatus().toString(),
             tugasAkhir.getTaId()
     );
    }
    

    // Menghapus data tugas akhir berdasarkan ID.
    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM tugas_akhir WHERE ta_id = ?", id);
    }
     @Override
  public boolean updateStatus(Long taId, StatusTA newStatus){
      int updated = jdbcTemplate.update("UPDATE tugas_akhir SET status = ?::status_ta WHERE ta_id = ?", newStatus.toString(), taId);
      return updated > 0;
   }
   @Override
    public List<TugasAkhir> findByStatus(StatusTA status) {
           String sql = "SELECT ta.*, m.*, s.* FROM tugas_akhir ta " +
                      "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                        "JOIN semester s ON ta.semester_id = s.semester_id "+
                        "WHERE ta.status = ?::status_ta";
           return jdbcTemplate.query(sql, this::mapRowToTugasAkhir, status.toString());
     }

     @Override
      public List<TugasAkhir> findByMahasiswaIdAndStatus(Integer mahasiswaId, StatusTA status) {
         String sql = "SELECT ta.*, m.*, s.* FROM tugas_akhir ta " +
                       "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                        "JOIN semester s ON ta.semester_id = s.semester_id "+
                       "WHERE ta.mahasiswa_id = ? AND ta.status = ?::status_ta";
        return jdbcTemplate.query(sql, this::mapRowToTugasAkhir, mahasiswaId, status.toString());
      }
    private TugasAkhir mapRowToTugasAkhir(ResultSet rs, int rowNum) throws SQLException {
    TugasAkhir tugasAkhir = new TugasAkhir();
    tugasAkhir.setTaId(rs.getLong("ta_id"));

    // Mapping Mahasiswa data
      Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setMahasiswaId(rs.getInt("mahasiswa_id"));
        mahasiswa.setNpm(rs.getString("npm"));
        mahasiswa.setNama(rs.getString("nama"));
        tugasAkhir.setMahasiswa(mahasiswa);

     // Mapping Semester data
      Semester semester = new Semester();
        semester.setSemesterId(rs.getLong("semester_id"));
        semester.setTahunAjaran(rs.getString("tahun_ajaran"));
         semester.setPeriode(com.rpl.project_sista.model.enums.Periode.valueOf(rs.getString("periode").toLowerCase()));
         tugasAkhir.setSemester(semester);

     tugasAkhir.setJudul(rs.getString("judul"));
     tugasAkhir.setTopik(rs.getString("topik"));
     tugasAkhir.setJenisTA(JenisTA.valueOf(rs.getString("jenis_ta").toLowerCase()));
     tugasAkhir.setStatus(StatusTA.valueOf(rs.getString("status").toLowerCase()));
     tugasAkhir.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return tugasAkhir;
    }
}