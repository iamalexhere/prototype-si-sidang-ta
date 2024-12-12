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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        sampleKomponenNilai.setTipePenilai(TipePenilai.PENGUJI);

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
        List<Map<String, Object>> mockResults = new ArrayList<>();
        
        Map<String, Object> pengujiResult = new HashMap<>();
        pengujiResult.put("tipe_penilai", "PENGUJI"); 
        pengujiResult.put("avg_nilai", new BigDecimal("85.5")); 
        mockResults.add(pengujiResult);

        Map<String, Object> pembimbingResult = new HashMap<>();
        pembimbingResult.put("tipe_penilai", "PEMBIMBING"); 
        pembimbingResult.put("avg_nilai", new BigDecimal("90.0")); 
        mockResults.add(pembimbingResult);

        when(jdbcTemplate.queryForList(anyString(), eq(sampleSidang.getSidangId())))
                .thenReturn(mockResults);

        // Act
        Map<TipePenilai, Float> result = repository.getAverageNilaiBySidang(sampleSidang);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(85.5f, result.get(TipePenilai.PENGUJI), 0.001f);
        assertEquals(90.0f, result.get(TipePenilai.PEMBIMBING), 0.001f);
    }

    @Test
    void getFinalNilaiBySidang_ShouldReturnAverageOfPengujiAndPembimbing() {
        // Arrange
        List<Map<String, Object>> mockResults = new ArrayList<>();
        
        Map<String, Object> pengujiResult = new HashMap<>();
        pengujiResult.put("tipe_penilai", "PENGUJI"); 
        pengujiResult.put("avg_nilai", new BigDecimal("80.0")); 
        mockResults.add(pengujiResult);

        Map<String, Object> pembimbingResult = new HashMap<>();
        pembimbingResult.put("tipe_penilai", "PEMBIMBING"); 
        pembimbingResult.put("avg_nilai", new BigDecimal("90.0")); 
        mockResults.add(pembimbingResult);

        when(jdbcTemplate.queryForList(anyString(), eq(sampleSidang.getSidangId())))
                .thenReturn(mockResults);

        // Act
        Float result = repository.getFinalNilaiBySidang(sampleSidang);

        // Assert
        assertEquals(85.0f, result, 0.001f);
    }

    @Test
    void save_WhenInsertingNew_ShouldReturnSavedNilaiSidang() {
        // Arrange
        NilaiSidang newNilaiSidang = new NilaiSidang();
        newNilaiSidang.setSidang(sampleSidang);
        newNilaiSidang.setKomponenNilai(sampleKomponenNilai);
        newNilaiSidang.setDosen(sampleDosen);
        newNilaiSidang.setNilai(85.5f);
        newNilaiSidang.setCreatedAt(LocalDateTime.now());
        newNilaiSidang.setUpdatedAt(LocalDateTime.now());

        // Mock the key holder behavior
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("nilai_id", 1L);
        keyHolder.getKeyList().add(keyMap);

        doAnswer(invocation -> {
            PreparedStatementCreator psc = invocation.getArgument(0);
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().addAll(keyHolder.getKeyList());
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // Act
        NilaiSidang result = repository.save(newNilaiSidang);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNilaiId());
        assertEquals(85.5f, result.getNilai());
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
    void deleteById_ShouldDeleteNilaiSidang() {
        // Act
        repository.deleteById(1L);

        // Verify
        verify(jdbcTemplate).update("DELETE FROM nilai_sidang WHERE nilai_id = ?", 1L);
    }

    @Test
    void hasDosenCompletedPenilaian_WhenNotCompleted_ShouldReturnFalse() {
        // Arrange
        when(jdbcTemplate.queryForObject(contains("SELECT UPPER(role::TEXT)"), eq(String.class), anyInt()))
                .thenReturn("PENGUJI");
        when(jdbcTemplate.queryForObject(contains("SELECT COUNT(DISTINCT kn.komponen_id)"), eq(Integer.class), anyLong(), anyString()))
                .thenReturn(3); // Total required components
        when(jdbcTemplate.queryForObject(contains("SELECT COUNT(*) FROM nilai_sidang"), eq(Integer.class), anyLong(), anyInt()))
                .thenReturn(2); // Only 2 components rated

        // Act
        boolean result = repository.hasDosenCompletedPenilaian(sampleSidang, sampleDosen);

        // Assert
        assertFalse(result);
    }

    @Test
    void hasDosenCompletedPenilaian_WhenCompleted_ShouldReturnTrue() {
        // Arrange
        when(jdbcTemplate.queryForObject(contains("SELECT UPPER(role::TEXT)"), eq(String.class), anyInt()))
                .thenReturn("PENGUJI");
        when(jdbcTemplate.queryForObject(contains("SELECT COUNT(DISTINCT kn.komponen_id)"), eq(Integer.class), anyLong(), anyString()))
                .thenReturn(3); // Total required components
        when(jdbcTemplate.queryForObject(contains("SELECT COUNT(*) FROM nilai_sidang"), eq(Integer.class), anyLong(), anyInt()))
                .thenReturn(3); // All components rated

        // Act
        boolean result = repository.hasDosenCompletedPenilaian(sampleSidang, sampleDosen);

        // Assert
        assertTrue(result);
    }
}
