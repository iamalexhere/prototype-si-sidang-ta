package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.model.enums.TipePenilai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JdbcNilaiSidangRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private JdbcNilaiSidangRepository repository;

    private NilaiSidang sampleNilaiSidang;
    private Sidang sampleSidang;
    private KomponenNilai sampleKomponenNilai;
    private Dosen sampleDosen;
    private TugasAkhir sampleTA;
    private Semester sampleSemester;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample data
        sampleSemester = new Semester();
        sampleSemester.setSemesterId(1L);

        sampleTA = new TugasAkhir();
        sampleTA.setTaId(1L);
        sampleTA.setSemester(sampleSemester);

        sampleSidang = new Sidang();
        sampleSidang.setSidangId(1L);
        sampleSidang.setTugasAkhir(sampleTA);

        sampleKomponenNilai = new KomponenNilai();
        sampleKomponenNilai.setKomponenId(1L);
        sampleKomponenNilai.setTipePenilai(TipePenilai.penguji);

        sampleDosen = new Dosen();
        sampleDosen.setDosenId(1);

        sampleNilaiSidang = new NilaiSidang();
        sampleNilaiSidang.setNilaiId(1L);
        sampleNilaiSidang.setSidang(sampleSidang);
        sampleNilaiSidang.setKomponenNilai(sampleKomponenNilai);
        sampleNilaiSidang.setDosen(sampleDosen);
        sampleNilaiSidang.setNilai(85.5f);
        sampleNilaiSidang.setCreatedAt(LocalDateTime.now());
        sampleNilaiSidang.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnListOfNilaiSidang() throws SQLException {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(Collections.singletonList(sampleNilaiSidang));

        // Act
        List<NilaiSidang> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleNilaiSidang.getNilaiId(), result.get(0).getNilaiId());
    }

    @Test
    void findById_WhenExists_ShouldReturnNilaiSidang() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(sampleNilaiSidang);

        // Act
        Optional<NilaiSidang> result = repository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleNilaiSidang.getNilaiId(), result.get().getNilaiId());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        Optional<NilaiSidang> result = repository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findBySidang_ShouldReturnListOfNilaiSidang() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(Collections.singletonList(sampleNilaiSidang));

        // Act
        List<NilaiSidang> result = repository.findBySidang(sampleSidang);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleNilaiSidang.getSidang().getSidangId(), result.get(0).getSidang().getSidangId());
    }

    @Test
    void findBySidangAndDosen_ShouldReturnListOfNilaiSidang() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong(), anyInt()))
                .thenReturn(Collections.singletonList(sampleNilaiSidang));

        // Act
        List<NilaiSidang> result = repository.findBySidangAndDosen(sampleSidang, sampleDosen);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleNilaiSidang.getDosen().getDosenId(), result.get(0).getDosen().getDosenId());
    }

    @Test
    void getAverageNilaiBySidang_ShouldReturnAveragesMap() {
        // Arrange
        Map<String, Object> row1 = new HashMap<>();
        row1.put("tipe_penilai", "penguji");  // Using lowercase to match enum
        row1.put("avg_nilai", 85.5);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("tipe_penilai", "pembimbing");  // Using lowercase to match enum
        row2.put("avg_nilai", 90.0);

        when(jdbcTemplate.queryForList(anyString(), anyLong()))
                .thenReturn(Arrays.asList(row1, row2));

        // Act
        Map<TipePenilai, Float> result = repository.getAverageNilaiBySidang(sampleSidang);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(85.5f, result.get(TipePenilai.penguji));
        assertEquals(90.0f, result.get(TipePenilai.pembimbing));
    }

    @Test
    void getFinalNilaiBySidang_ShouldReturnAverageOfPengujiAndPembimbing() {
        // Arrange
        Map<String, Object> row1 = new HashMap<>();
        row1.put("tipe_penilai", "penguji");  // Using lowercase to match enum
        row1.put("avg_nilai", 80.0);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("tipe_penilai", "pembimbing");  // Using lowercase to match enum
        row2.put("avg_nilai", 90.0);

        when(jdbcTemplate.queryForList(anyString(), anyLong()))
                .thenReturn(Arrays.asList(row1, row2));

        // Act
        Float result = repository.getFinalNilaiBySidang(sampleSidang);

        // Assert
        assertEquals(85.0f, result);
    }

    @Test
    void save_WhenInsertingNew_ShouldReturnSavedNilaiSidang() {
        // Arrange
        NilaiSidang newNilaiSidang = new NilaiSidang();
        newNilaiSidang.setSidang(sampleSidang);
        newNilaiSidang.setKomponenNilai(sampleKomponenNilai);
        newNilaiSidang.setDosen(sampleDosen);
        newNilaiSidang.setNilai(85.5f);

        when(jdbcTemplate.queryForObject(eq("SELECT currval('nilai_sidang_nilai_id_seq')"), eq(Long.class)))
                .thenReturn(1L);

        // Act
        NilaiSidang result = repository.save(newNilaiSidang);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNilaiId());
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void save_WhenUpdatingExisting_ShouldReturnUpdatedNilaiSidang() {
        // Act
        NilaiSidang result = repository.save(sampleNilaiSidang);

        // Assert
        assertNotNull(result);
        assertEquals(sampleNilaiSidang.getNilaiId(), result.getNilaiId());
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void deleteById_ShouldExecuteDeleteQuery() {
        // Act
        repository.deleteById(1L);

        // Assert
        verify(jdbcTemplate).update(eq("DELETE FROM nilai_sidang WHERE nilai_id = ?"), eq(1L));
    }

    @Test
    void hasDosenCompletedPenilaian_WhenCompleted_ShouldReturnTrue() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any(), any()))
                .thenReturn(3); // Total components
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any()))
                .thenReturn(3); // Completed components

        // Act
        boolean result = repository.hasDosenCompletedPenilaian(sampleSidang, sampleDosen);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasDosenCompletedPenilaian_WhenNotCompleted_ShouldReturnFalse() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any(), any()))
                .thenReturn(3); // Total components
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any()))
                .thenReturn(2); // Completed components

        // Act
        boolean result = repository.hasDosenCompletedPenilaian(sampleSidang, sampleDosen);

        // Assert
        assertFalse(result);
    }
}
