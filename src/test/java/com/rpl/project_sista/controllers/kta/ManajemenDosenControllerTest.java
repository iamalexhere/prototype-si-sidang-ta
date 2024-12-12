package com.rpl.project_sista.controllers.kta;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.repository.DosenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManajemenDosenController.class)
class ManajemenDosenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DosenRepository dosenRepository;

    private Dosen testDosen;

    @BeforeEach
    void setUp() {
        testDosen = new Dosen();
        testDosen.setUserId(1);
        testDosen.setNip("198501012010121001");
        testDosen.setNama("Dr. Test Dosen");
        testDosen.setEmail("test.dosen@university.ac.id");
        testDosen.setPasswordHash("hashedpassword123");
        testDosen.setRole(UserRole.dosen);
        testDosen.setCreatedAt(LocalDateTime.now());
        testDosen.setUsername("testdosen");
    }

    @Test
    void listDosen_ShouldReturnDosenListPage() throws Exception {
        when(dosenRepository.findAll()).thenReturn(Arrays.asList(testDosen));

        mockMvc.perform(get("/kta/dosen"))
                .andExpect(status().isOk())
                .andExpect(view().name("kta/dosen/index"))
                .andExpect(model().attributeExists("dosenList"))
                .andExpect(model().attributeExists("pageTitle"));
    }

    @Test
    void showAddDosenForm_ShouldReturnAddDosenPage() throws Exception {
        mockMvc.perform(get("/kta/dosen/tambah"))
                .andExpect(status().isOk())
                .andExpect(view().name("kta/dosen/tambah-dosen"))
                .andExpect(model().attributeExists("dosen"))
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(model().attributeExists("title"));
    }

    @Test
    void showUpdateDosenForm_WithValidId_ShouldReturnUpdateDosenPage() throws Exception {
        when(dosenRepository.findById(1)).thenReturn(Optional.of(testDosen));

        mockMvc.perform(get("/kta/dosen/ubah").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("kta/dosen/ubah-dosen"))
                .andExpect(model().attributeExists("dosen"))
                .andExpect(model().attributeExists("pageTitle"))
                .andExpect(model().attributeExists("title"));
    }

    @Test
    void showUpdateDosenForm_WithInvalidId_ShouldRedirectWithError() throws Exception {
        when(dosenRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/kta/dosen/ubah").param("id", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/kta/dosen?error=Dosen tidak ditemukan"));
    }

    @Test
    void addDosen_WithValidData_ShouldRedirectToIndex() throws Exception {
        when(dosenRepository.save(any(Dosen.class))).thenReturn(testDosen);

        mockMvc.perform(post("/kta/dosen/tambah")
                .param("nip", "198501012010121001")
                .param("nama", "Dr. Test Dosen")
                .param("username", "testdosen")
                .param("email", "test.dosen@university.ac.id")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/kta/dosen"));
    }

    @Test
    void updateDosen_WithValidData_ShouldRedirectToIndex() throws Exception {
        when(dosenRepository.findById(1)).thenReturn(Optional.of(testDosen));
        when(dosenRepository.save(any(Dosen.class))).thenReturn(testDosen);

        mockMvc.perform(post("/kta/dosen/ubah")
                .param("id", "1")
                .param("nip", "198501012010121001")
                .param("nama", "Dr. Test Dosen Updated")
                .param("username", "testdosen")
                .param("email", "test.dosen@university.ac.id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/kta/dosen"));
    }

    @Test
    void deleteDosen_WithValidId_ShouldRedirectToIndex() throws Exception {
        when(dosenRepository.findById(1)).thenReturn(Optional.of(testDosen));

        mockMvc.perform(post("/kta/dosen/hapus").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/kta/dosen"));
    }
}
