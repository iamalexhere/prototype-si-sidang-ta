package com.rpl.project_sista;

import com.rpl.project_sista.jdbcrepository.JdbcKomponenNilaiRepository;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.model.enums.TipePenilai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JdbcKomponenNilaiRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcKomponenNilaiRepository repository;

    private KomponenNilai sampleKomponenNilai;
    private Semester sampleSemester;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JdbcKomponenNilaiRepository(jdbcTemplate);

        // Setup sample data
        sampleSemester = new Semester();
        sampleSemester.setSemesterId(1L);
        sampleSemester.setTahunAjaran("2023/2024");
        sampleSemester.setPeriode(Periode.ganjil);
        sampleSemester.setIsActive(true);
        sampleSemester.setCreatedAt(LocalDateTime.now());

        sampleKomponenNilai = new KomponenNilai();
        sampleKomponenNilai.setKomponenId(1L);
        sampleKomponenNilai.setSemester(sampleSemester);
        sampleKomponenNilai.setNamaKomponen("Presentasi");
        sampleKomponenNilai.setBobot(0.4f);
        sampleKomponenNilai.setTipePenilai(TipePenilai.pembimbing);
        sampleKomponenNilai.setDeskripsi("Penilaian presentasi");
        sampleKomponenNilai.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnListOfKomponenNilai() {
        // Arrange
        List<KomponenNilai> expectedList = Arrays.asList(sampleKomponenNilai);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedList);

        // Act
        List<KomponenNilai> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleKomponenNilai.getKomponenId(), result.get(0).getKomponenId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnKomponenNilai() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(sampleKomponenNilai);

        // Act
        Optional<KomponenNilai> result = repository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleKomponenNilai.getKomponenId(), result.get().getKomponenId());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void findBySemester_ShouldReturnListOfKomponenNilai() {
        // Arrange
        List<KomponenNilai> expectedList = Arrays.asList(sampleKomponenNilai);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(expectedList);

        // Act
        List<KomponenNilai> result = repository.findBySemester(sampleSemester);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleKomponenNilai.getKomponenId(), result.get(0).getKomponenId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void findBySemesterAndTipePenilai_ShouldReturnFilteredList() {
        // Arrange
        List<KomponenNilai> expectedList = Arrays.asList(sampleKomponenNilai);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), anyString()))
                .thenReturn(expectedList);

        // Act
        List<KomponenNilai> result = repository.findBySemesterAndTipePenilai(
                sampleSemester, TipePenilai.pembimbing);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TipePenilai.pembimbing, result.get(0).getTipePenilai());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1L), anyString());
    }

    @Test
    void save_WhenInsertingNew_ShouldReturnSavedKomponenNilai() {
        // Arrange
        KomponenNilai newKomponen = new KomponenNilai();
        newKomponen.setSemester(sampleSemester);
        newKomponen.setNamaKomponen("New Komponen");
        newKomponen.setBobot(0.3f);
        newKomponen.setTipePenilai(TipePenilai.penguji);
        newKomponen.setDeskripsi("New Description");

        when(jdbcTemplate.queryForObject(
            anyString(),
            eq(Long.class),
            eq(sampleSemester.getSemesterId()),
            eq("New Komponen"),
            eq(0.3f),
            eq("penguji"),
            eq("New Description"),
            any(LocalDateTime.class)
        )).thenReturn(2L);

        // Act
        KomponenNilai result = repository.save(newKomponen);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getKomponenId());
        verify(jdbcTemplate).queryForObject(
            anyString(),
            eq(Long.class),
            eq(sampleSemester.getSemesterId()),
            eq("New Komponen"),
            eq(0.3f),
            eq("penguji"),
            eq("New Description"),
            any(LocalDateTime.class)
        );
    }

    @Test
    void save_WhenUpdating_ShouldUpdateExistingKomponenNilai() {
        // Arrange
        sampleKomponenNilai.setNamaKomponen("Updated Name");

        // Act
        KomponenNilai result = repository.save(sampleKomponenNilai);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getNamaKomponen());
        verify(jdbcTemplate).update(
            anyString(),
            eq(sampleSemester.getSemesterId()),
            eq("Updated Name"),
            eq(0.4f),
            eq("pembimbing"),
            eq("Penilaian presentasi"),
            eq(1L)
        );
    }

    @Test
    void deleteById_ShouldExecuteDeleteQuery() {
        // Act
        repository.deleteById(1L);

        // Assert
        verify(jdbcTemplate).update(anyString(), eq(1L));
    }

    @Test
    void count_ShouldReturnTotalCount() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class)))
                .thenReturn(5L);

        // Act
        long count = repository.count();

        // Assert
        assertEquals(5L, count);
        verify(jdbcTemplate).queryForObject(anyString(), eq(Long.class));
    }
}
