package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DosenRepositoryTest {

    @Autowired
    private JdbcDosenRepository dosenRepository;

    private Dosen testDosen;

    @BeforeEach
    void setUp() {
        // Setup test data
        testDosen = new Dosen();
        testDosen.setNip("198501012010121001");
        testDosen.setNama("Dr. Test Dosen");
        testDosen.setEmail("test.dosen@university.ac.id");
        testDosen.setPasswordHash("password123");
        testDosen.setRole(UserRole.dosen);
        testDosen.setCreatedAt(LocalDateTime.now());
        testDosen.setUsername("testdosen");
    }

    @Test
    void findAll_ShouldReturnAllDosen() {
        // Given
        dosenRepository.save(testDosen);

        // When
        List<Dosen> result = dosenRepository.findAll();

        // Then
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(d -> d.getNip().equals(testDosen.getNip())));
    }

    @Test
    void findById_WithValidId_ShouldReturnDosen() {
        // Given
        Dosen savedDosen = dosenRepository.save(testDosen);

        // When
        Optional<Dosen> result = dosenRepository.findById(savedDosen.getUserId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(testDosen.getNip(), result.get().getNip());
        assertEquals(testDosen.getNama(), result.get().getNama());
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        // When
        Optional<Dosen> result = dosenRepository.findById(-1);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldCreateNewDosen() {
        // When
        Dosen savedDosen = dosenRepository.save(testDosen);

        // Then
        assertNotNull(savedDosen.getUserId());
        assertEquals(testDosen.getNip(), savedDosen.getNip());
        assertEquals(testDosen.getNama(), savedDosen.getNama());
    }

    @Test
    void findByName_ShouldReturnMatchingDosen() {
        // Given
        dosenRepository.save(testDosen);

        // When
        List<Dosen> result = dosenRepository.findByName("Test");

        // Then
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(d -> d.getNama().contains("Test")));
    }

    @Test
    void findPaginated_ShouldReturnCorrectPage() {
        // Given
        dosenRepository.save(testDosen);
        int page = 0;
        int size = 10;
        String filter = "";

        // When
        List<Dosen> result = dosenRepository.findPaginated(page, size, filter);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= size);
    }

    @Test
    void count_ShouldReturnTotalCount() {
        // Given
        dosenRepository.save(testDosen);
        String filter = "";

        // When
        int count = dosenRepository.count(filter);

        // Then
        assertTrue(count > 0);
    }

    @Test
    void deleteById_ShouldRemoveDosen() {
        // Given
        Dosen savedDosen = dosenRepository.save(testDosen);

        // When
        dosenRepository.deleteById(savedDosen.getUserId());
        Optional<Dosen> result = dosenRepository.findById(savedDosen.getUserId());

        // Then
        assertTrue(result.isEmpty());
    }
}
