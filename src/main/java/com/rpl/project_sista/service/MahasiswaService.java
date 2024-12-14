package com.rpl.project_sista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rpl.project_sista.jdbcrepository.JdbcMahasiswaRepository;
import com.rpl.project_sista.jdbcrepository.JdbcTugasAkhirRepository;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.entity.TugasAkhir;
import java.util.List;

@Service
public class MahasiswaService {
    
    private static final Logger logger = LoggerFactory.getLogger(MahasiswaService.class);
    
    @Autowired
    private JdbcMahasiswaRepository mahasiswaRepository;
    
    @Autowired
    private JdbcTugasAkhirRepository tugasAkhirRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void deleteMahasiswa(Integer mahasiswaId) {
        try {
            logger.info("Attempting to delete mahasiswa with mahasiswa_id: {}", mahasiswaId);
            
            // 1. Get user_id from mahasiswa_id
            Integer userId = jdbcTemplate.queryForObject(
                "SELECT user_id FROM mahasiswa WHERE mahasiswa_id = ?", 
                Integer.class, 
                mahasiswaId
            );
            
            if (userId == null) {
                throw new RuntimeException("Mahasiswa tidak ditemukan");
            }
            
            logger.info("Found user_id: {} for mahasiswa_id: {}", userId, mahasiswaId);

            // 2. Delete from penguji_sidang first
            jdbcTemplate.update(
                "DELETE FROM penguji_sidang WHERE sidang_id IN " +
                "(SELECT s.sidang_id FROM sidang s " +
                "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                "WHERE ta.mahasiswa_id = ?)", 
                mahasiswaId
            );
            logger.info("Deleted related penguji_sidang records");
            
            // 3. Delete from nilai_sidang
            jdbcTemplate.update(
                "DELETE FROM nilai_sidang WHERE sidang_id IN " +
                "(SELECT s.sidang_id FROM sidang s " +
                "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                "WHERE ta.mahasiswa_id = ?)", 
                mahasiswaId
            );
            logger.info("Deleted related nilai_sidang records");

            // 4. Delete from sidang
            jdbcTemplate.update(
                "DELETE FROM sidang WHERE ta_id IN " +
                "(SELECT ta_id FROM tugas_akhir WHERE mahasiswa_id = ?)", 
                mahasiswaId
            );
            logger.info("Deleted related sidang records");
            
            // 5. Delete from pembimbing_ta
            jdbcTemplate.update(
                "DELETE FROM pembimbing_ta WHERE ta_id IN " +
                "(SELECT ta_id FROM tugas_akhir WHERE mahasiswa_id = ?)", 
                mahasiswaId
            );
            logger.info("Deleted related pembimbing_ta records");
            
            // 6. Delete from tugas_akhir
            jdbcTemplate.update("DELETE FROM tugas_akhir WHERE mahasiswa_id = ?", mahasiswaId);
            logger.info("Deleted related tugas_akhir records");
            
            // 7. Delete from mahasiswa
            jdbcTemplate.update("DELETE FROM mahasiswa WHERE mahasiswa_id = ?", mahasiswaId);
            logger.info("Deleted mahasiswa record");
            
            // 8. Delete from users
            jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
            logger.info("Successfully deleted mahasiswa and all related data");
            
        } catch (Exception e) {
            logger.error("Error deleting mahasiswa: ", e);
            throw e;
        }
    }
    
    public Mahasiswa getMahasiswaById(Integer id) {
        return mahasiswaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));
    }
}
