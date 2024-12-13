package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.model.enums.StatusTA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JdbcBAPRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcBAPRepository repository;

    private BAP sampleBAP;
    private Sidang sampleSidang;
    private TugasAkhir sampleTA;
    private Users sampleUser;
    private Set<Users> sampleApprovers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JdbcBAPRepository();
        
        // Use reflection to set jdbcTemplate
        try {
            Field field = JdbcBAPRepository.class.getDeclaredField("jdbcTemplate");
            field.setAccessible(true);
            field.set(repository, jdbcTemplate);
        } catch (Exception e) {
            fail("Failed to set jdbcTemplate: " + e.getMessage());
        }

        // Setup sample data
        sampleTA = new TugasAkhir();
        sampleTA.setTaId(1L);
        sampleTA.setStatus(StatusTA.draft);

        sampleSidang = new Sidang();
        sampleSidang.setSidangId(1L);
        sampleSidang.setTugasAkhir(sampleTA);
        sampleSidang.setStatusSidang(StatusSidang.terjadwal);

        sampleBAP = new BAP();
        sampleBAP.setBapId(1L);
        sampleBAP.setSidang(sampleSidang);
        sampleBAP.setCatatanTambahan("Catatan sidang");
        sampleBAP.setCreatedAt(LocalDateTime.now());
        sampleBAP.setPersetujuan(new HashSet<>());

        sampleUser = new Users();
        sampleUser.setUserId(1);
    }

    @Test
    void findById_WhenExists_ShouldReturnBAP() {
        // Arrange
        when(jdbcTemplate.queryForObject(
            anyString(), 
            any(RowMapper.class), 
            eq(1L)
        )).thenReturn(sampleBAP);
        
        when(jdbcTemplate.query(
            anyString(), 
            any(RowMapper.class), 
            eq(1L)
        )).thenReturn(new ArrayList<>(sampleBAP.getPersetujuan()));

        // Act
        Optional<BAP> result = repository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleBAP.getBapId(), result.get().getBapId());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(999L)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        Optional<BAP> result = repository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(999L));
    }

    @Test
    void findBySidang_WhenExists_ShouldReturnBAP() {
        // Arrange
        when(jdbcTemplate.queryForObject(
            anyString(), 
            any(RowMapper.class), 
            eq(1L)
        )).thenReturn(sampleBAP);
        
        when(jdbcTemplate.query(
            anyString(), 
            any(RowMapper.class), 
            eq(1L)
        )).thenReturn(new ArrayList<>(sampleBAP.getPersetujuan()));

        // Act
        Optional<BAP> result = repository.findBySidang(sampleSidang);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleBAP.getBapId(), result.get().getBapId());
    }

    @Test
    void findAll_ShouldReturnListOfBAP() {
        // Arrange
        when(jdbcTemplate.query(
            anyString(), 
            any(RowMapper.class)
        )).thenAnswer(invocation -> {
            RowMapper<BAP> rowMapper = invocation.getArgument(1);
            ResultSet mockRs = mock(ResultSet.class);
            
            // First call for BAP data
            when(mockRs.getLong("bap_id")).thenReturn(1L);
            when(mockRs.getLong("sidang_id")).thenReturn(1L);
            when(mockRs.getString("catatan_tambahan")).thenReturn("Catatan sidang");
            when(mockRs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
            when(mockRs.getInt("user_id")).thenReturn(1);
            when(mockRs.wasNull()).thenReturn(false);
            
            rowMapper.mapRow(mockRs, 0);
            return Collections.singletonList(sampleBAP);
        });

        // Act
        List<BAP> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        BAP resultBAP = result.get(0);
        assertEquals(sampleBAP.getBapId(), resultBAP.getBapId());
        assertEquals(sampleBAP.getSidang().getSidangId(), resultBAP.getSidang().getSidangId());
        assertEquals(sampleBAP.getCatatanTambahan(), resultBAP.getCatatanTambahan());
    }

    @Test
    void save_WhenInsertingNew_ShouldReturnSavedBAP() {
        // Arrange
        BAP newBAP = new BAP();
        newBAP.setSidang(sampleSidang);
        newBAP.setCatatanTambahan("New BAP");
        newBAP.setPersetujuan(sampleBAP.getPersetujuan());

        when(jdbcTemplate.queryForObject(eq("SELECT currval('bap_bap_id_seq')"), eq(Long.class)))
                .thenReturn(2L);

        // Act
        BAP result = repository.save(newBAP);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getBapId());
        verify(jdbcTemplate).update(
            anyString(),
            eq(sampleSidang.getSidangId()),
            eq("New BAP"),
            any(LocalDateTime.class)
        );
    }

    @Test
    void save_WhenUpdating_ShouldUpdateExistingBAP() {
        // Arrange
        sampleBAP.setCatatanTambahan("Updated BAP");

        // Act
        BAP result = repository.save(sampleBAP);

        // Assert
        assertNotNull(result);
        assertEquals("Updated BAP", result.getCatatanTambahan());
        verify(jdbcTemplate).update(
            anyString(),
            eq(sampleSidang.getSidangId()),
            eq("Updated BAP"),
            eq(1L)
        );
    }

    @Test
    void deleteById_ShouldDeleteBAPAndPersetujuan() {
        // Act
        repository.deleteById(1L);

        // Assert
        verify(jdbcTemplate).update("DELETE FROM persetujuan_bap WHERE bap_id = ?", 1L);
        verify(jdbcTemplate).update("DELETE FROM bap WHERE bap_id = ?", 1L);
    }

    @Test
    void findRequiredApprovers_ShouldReturnAllRequiredUsers() {
        // Arrange
        Users adminUser = new Users();
        adminUser.setUserId(1);
        
        Users pengujiUser = new Users();
        pengujiUser.setUserId(2);
        
        Users pembimbingUser = new Users();
        pembimbingUser.setUserId(3);
        
        List<Users> allUsers = Arrays.asList(adminUser, pengujiUser, pembimbingUser);
        
        when(jdbcTemplate.query(
            contains("SELECT DISTINCT u.*"),
            any(RowMapper.class),
            eq(1L),
            eq(1L),
            eq(1L)
        )).thenReturn(allUsers);

        // Act
        Set<Users> result = repository.findRequiredApprovers(sampleBAP);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(adminUser));
        assertTrue(result.contains(pengujiUser));
        assertTrue(result.contains(pembimbingUser));
    }

    @Test
    void findApprovers_ShouldReturnCurrentApprovers() {
        // Arrange
        when(jdbcTemplate.query(
            anyString(), 
            any(RowMapper.class), 
            eq(1L)
        )).thenReturn(Collections.singletonList(sampleUser));

        // Act
        Set<Users> result = repository.findApprovers(sampleBAP);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void isFullyApproved_WhenAllApproved_ShouldReturnTrue() {
        // Arrange
        Users approver1 = new Users();
        approver1.setUserId(1);
        
        Users approver2 = new Users();
        approver2.setUserId(2);
        
        Set<Users> requiredApprovers = new HashSet<>(Arrays.asList(approver1, approver2));
        Set<Users> actualApprovers = new HashSet<>(Arrays.asList(approver1, approver2));
        
        // Mock findRequiredApprovers
        when(jdbcTemplate.query(
            contains("SELECT DISTINCT u.*"),
            any(RowMapper.class),
            eq(1L),
            eq(1L),
            eq(1L)
        )).thenReturn(new ArrayList<>(requiredApprovers));
        
        // Mock findApprovers
        when(jdbcTemplate.query(
            contains("SELECT u.* FROM users u JOIN persetujuan_bap pb"),
            any(RowMapper.class),
            eq(1L)
        )).thenReturn(new ArrayList<>(actualApprovers));

        // Act
        boolean result = repository.isFullyApproved(sampleBAP);

        // Assert
        assertTrue(result);
    }

    @Test
    void addApprover_WhenNewApprover_ShouldInsertApproval() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L), eq(1)))
                .thenReturn(0);

        // Act
        repository.addApprover(sampleBAP, sampleUser);

        // Assert
        verify(jdbcTemplate).update(
            eq("INSERT INTO persetujuan_bap (bap_id, user_id, is_approved, approved_at) VALUES (?, ?, true, ?)"),
            eq(1L),
            eq(1),
            any(LocalDateTime.class)
        );
    }

    @Test
    void addApprover_WhenExistingApprover_ShouldUpdateApproval() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L), eq(1)))
                .thenReturn(1);

        // Act
        repository.addApprover(sampleBAP, sampleUser);

        // Assert
        verify(jdbcTemplate).update(
            eq("UPDATE persetujuan_bap SET is_approved = true, approved_at = ? WHERE bap_id = ? AND user_id = ?"),
            any(LocalDateTime.class),
            eq(1L),
            eq(1)
        );
    }

    @Test
    void removeApprover_ShouldUpdateApprovalStatus() {
        // Act
        repository.removeApprover(sampleBAP, sampleUser);

        // Assert
        verify(jdbcTemplate).update(
            eq("UPDATE persetujuan_bap SET is_approved = false, approved_at = null WHERE bap_id = ? AND user_id = ?"),
            eq(1L),
            eq(1)
        );
    }
}
