package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.jdbcrepository.JdbcCatatanRevisiRepository;
import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.TugasAkhir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JdbcCatatanRevisiRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcCatatanRevisiRepository repository;

    private CatatanRevisi sampleCatatanRevisi;
    private Sidang sampleSidang;
    private Dosen sampleDosen;
    private TugasAkhir sampleTA;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JdbcCatatanRevisiRepository();
        // Use reflection to set jdbcTemplate
        try {
            java.lang.reflect.Field field = JdbcCatatanRevisiRepository.class.getDeclaredField("jdbcTemplate");
            field.setAccessible(true);
            field.set(repository, jdbcTemplate);
        } catch (Exception e) {
            fail("Failed to set jdbcTemplate: " + e.getMessage());
        }

        // Setup sample data
        sampleTA = new TugasAkhir();
        sampleTA.setTaId(1L);

        sampleSidang = new Sidang();
        sampleSidang.setSidangId(1L);
        sampleSidang.setTugasAkhir(sampleTA);

        sampleDosen = new Dosen();
        sampleDosen.setDosenId(1);
        sampleDosen.setNama("Dr. John Doe");

        sampleCatatanRevisi = new CatatanRevisi();
        sampleCatatanRevisi.setCatatanId(1L);
        sampleCatatanRevisi.setSidang(sampleSidang);
        sampleCatatanRevisi.setDosen(sampleDosen);
        sampleCatatanRevisi.setIsiCatatan("Revisi bab 1: Perbaiki latar belakang");
        sampleCatatanRevisi.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnListOfCatatanRevisi() {
        // Arrange
        List<CatatanRevisi> expectedList = Arrays.asList(sampleCatatanRevisi);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedList);

        // Act
        List<CatatanRevisi> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCatatanRevisi.getCatatanId(), result.get(0).getCatatanId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnCatatanRevisi() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(sampleCatatanRevisi);

        // Act
        Optional<CatatanRevisi> result = repository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleCatatanRevisi.getCatatanId(), result.get().getCatatanId());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(999L)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        Optional<CatatanRevisi> result = repository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(999L));
    }

    @Test
    void findBySidang_ShouldReturnListOfCatatanRevisi() {
        // Arrange
        List<CatatanRevisi> expectedList = Arrays.asList(sampleCatatanRevisi);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(expectedList);

        // Act
        List<CatatanRevisi> result = repository.findBySidang(sampleSidang);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCatatanRevisi.getCatatanId(), result.get(0).getCatatanId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void findByDosen_ShouldReturnListOfCatatanRevisi() {
        // Arrange
        List<CatatanRevisi> expectedList = Arrays.asList(sampleCatatanRevisi);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(expectedList);

        // Act
        List<CatatanRevisi> result = repository.findByDosen(sampleDosen);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCatatanRevisi.getCatatanId(), result.get(0).getCatatanId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1));
    }

    @Test
    void findBySidangAndDosen_ShouldReturnListOfCatatanRevisi() {
        // Arrange
        List<CatatanRevisi> expectedList = Arrays.asList(sampleCatatanRevisi);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1)))
                .thenReturn(expectedList);

        // Act
        List<CatatanRevisi> result = repository.findBySidangAndDosen(sampleSidang, sampleDosen);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCatatanRevisi.getCatatanId(), result.get(0).getCatatanId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1L), eq(1));
    }

    @Test
    void save_WhenInsertingNew_ShouldReturnSavedCatatanRevisi() {
        // Arrange
        CatatanRevisi newCatatan = new CatatanRevisi();
        newCatatan.setSidang(sampleSidang);
        newCatatan.setDosen(sampleDosen);
        newCatatan.setIsiCatatan("New Revision Note");

        when(jdbcTemplate.queryForObject(eq("SELECT currval('catatan_revisi_catatan_id_seq')"), eq(Long.class)))
                .thenReturn(2L);

        // Act
        CatatanRevisi result = repository.save(newCatatan);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getCatatanId());
        assertNotNull(result.getCreatedAt());
        verify(jdbcTemplate).update(
            anyString(),
            eq(sampleSidang.getSidangId()),
            eq(sampleDosen.getDosenId()),
            eq("New Revision Note"),
            any(LocalDateTime.class)
        );
    }

    @Test
    void save_WhenUpdating_ShouldUpdateExistingCatatanRevisi() {
        // Arrange
        sampleCatatanRevisi.setIsiCatatan("Updated Revision Note");

        // Act
        CatatanRevisi result = repository.save(sampleCatatanRevisi);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Revision Note", result.getIsiCatatan());
        verify(jdbcTemplate).update(
            anyString(),
            eq(sampleSidang.getSidangId()),
            eq(sampleDosen.getDosenId()),
            eq("Updated Revision Note"),
            eq(1L)
        );
    }

    @Test
    void deleteById_ShouldExecuteDeleteQuery() {
        // Act
        repository.deleteById(1L);

        // Assert
        verify(jdbcTemplate).update("DELETE FROM catatan_revisi WHERE catatan_id = ?", 1L);
    }

    @Test
    void countBySidang_ShouldReturnTotalCount() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq(1L)))
                .thenReturn(5L);

        // Act
        long count = repository.countBySidang(sampleSidang);

        // Assert
        assertEquals(5L, count);
        verify(jdbcTemplate).queryForObject(
            eq("SELECT COUNT(*) FROM catatan_revisi WHERE sidang_id = ?"),
            eq(Long.class),
            eq(1L)
        );
    }
}
