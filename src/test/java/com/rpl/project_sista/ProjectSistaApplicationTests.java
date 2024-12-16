import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.TipePenilai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectSistaApplicationTests {

    private NilaiSidang nilaiSidang;
    private Sidang sidang;
    private KomponenNilai komponenNilai;
	private List<KomponenNilai> listKomponenNilai;
	private List<NilaiSidang> listNilaiSidang;
    private Dosen dosen;

    @BeforeEach
	@SuppressWarnings("removal")
    void setUp() {
        // Create mock objects for related entities
        sidang = new Sidang();
        sidang.setSidangId(new Long(1));

		listKomponenNilai = new ArrayList<KomponenNilai>();
		listNilaiSidang = new ArrayList<NilaiSidang>();

        komponenNilai = new KomponenNilai();
        komponenNilai.setKomponenId(new Long(1));
		komponenNilai.setBobot(new Float(0.2));
		komponenNilai.setTipePenilai(TipePenilai.pembimbing);
		komponenNilai.setNamaKomponen("Tata Tulis Laporan");
		listKomponenNilai.add(komponenNilai);
		// Create a NilaiSidang instance
        nilaiSidang = new NilaiSidang();
		nilaiSidang.setKomponenNilai(komponenNilai);
		nilaiSidang.setNilai(new Float(90));
		listNilaiSidang.add(nilaiSidang);


		komponenNilai = new KomponenNilai();
		komponenNilai.setKomponenId(new Long(2));
		komponenNilai.setBobot(new Float(0.2));
		komponenNilai.setTipePenilai(TipePenilai.pembimbing);
		komponenNilai.setNamaKomponen("Kelengkapan Materi");
		listKomponenNilai.add(komponenNilai);
		// Create a NilaiSidang instance
        nilaiSidang = new NilaiSidang();
		nilaiSidang.setKomponenNilai(komponenNilai);
		nilaiSidang.setNilai(new Float(90));
		listNilaiSidang.add(nilaiSidang);


		komponenNilai = new KomponenNilai();
		komponenNilai.setKomponenId(new Long(3));
		komponenNilai.setBobot(new Float(0.3));
		komponenNilai.setTipePenilai(TipePenilai.pembimbing);
		komponenNilai.setNamaKomponen("Proses Bimbingan");
		listKomponenNilai.add(komponenNilai);
		// Create a NilaiSidang instance
        nilaiSidang = new NilaiSidang();
		nilaiSidang.setKomponenNilai(komponenNilai);
		nilaiSidang.setNilai(new Float(90));
		listNilaiSidang.add(nilaiSidang);


		komponenNilai = new KomponenNilai();
		komponenNilai.setKomponenId(new Long(4));
		komponenNilai.setBobot(new Float(0.3));
		komponenNilai.setTipePenilai(TipePenilai.pembimbing);
		komponenNilai.setNamaKomponen("Penguasaan Materi");
		listKomponenNilai.add(komponenNilai);
		// Create a NilaiSidang instance
        nilaiSidang = new NilaiSidang();
		nilaiSidang.setKomponenNilai(komponenNilai);
		nilaiSidang.setNilai(new Float(90));
		listNilaiSidang.add(nilaiSidang);

        dosen = new Dosen();
        dosen.setDosenId(1);
    }

	@Test
	void testNilaiAkhir(){
		double expectedNilaiAkhir = (0.3*90) + (0.3*90) + (0.2*90) + (0.2*90);
		double nilaiAkhir = 0;

		for(int i=0;i<4;i++){
			double bobot = listKomponenNilai.get(i).getBobot();
			double nilai = listNilaiSidang.get(i).getNilai();



			nilaiAkhir += nilai * bobot;
		}

		nilaiAkhir = Math.round(nilaiAkhir * 100) / 100;

		assertEquals(expectedNilaiAkhir, nilaiAkhir);
	}

    @Test
	@SuppressWarnings("removal")
    void testConstructorAndSetters() {
        // Test all-args constructor
		NilaiSidang fullNilaiSidang = new NilaiSidang(
            new Long(1), sidang, komponenNilai, dosen, 85.5f, 
            LocalDateTime.now(), LocalDateTime.now()
        );

        assertNotNull(fullNilaiSidang);
        assertEquals(new Long(1), fullNilaiSidang.getNilaiId());
        assertEquals(sidang, fullNilaiSidang.getSidang());
        assertEquals(komponenNilai, fullNilaiSidang.getKomponenNilai());
        assertEquals(dosen, fullNilaiSidang.getDosen());
        assertEquals(85.5f, fullNilaiSidang.getNilai());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        nilaiSidang.setNilaiId(1L);
        nilaiSidang.setSidang(sidang);
        nilaiSidang.setKomponenNilai(komponenNilai);
        nilaiSidang.setDosen(dosen);
        nilaiSidang.setNilai(90.0f);

        // Verify getters
        assertEquals(1L, nilaiSidang.getNilaiId());
        assertEquals(sidang, nilaiSidang.getSidang());
        assertEquals(komponenNilai, nilaiSidang.getKomponenNilai());
        assertEquals(dosen, nilaiSidang.getDosen());
        assertEquals(90.0f, nilaiSidang.getNilai());
    }

    @Test
    void testNullValues() {
        // Verify handling of null values
        nilaiSidang.setSidang(null);
        nilaiSidang.setKomponenNilai(null);
        nilaiSidang.setDosen(null);

        assertNull(nilaiSidang.getSidang());
        assertNull(nilaiSidang.getKomponenNilai());
        assertNull(nilaiSidang.getDosen());
    }

    @Test
    void testEqualsAndHashCode() {
        NilaiSidang nilaiSidang1 = new NilaiSidang();
        nilaiSidang1.setNilaiId(1L);

        NilaiSidang nilaiSidang2 = new NilaiSidang();
        nilaiSidang2.setNilaiId(1L);

        NilaiSidang nilaiSidang3 = new NilaiSidang();
        nilaiSidang3.setNilaiId(2L);

        // These are generated by Lombok's @Data annotation
        assertEquals(nilaiSidang1, nilaiSidang2);
        assertNotEquals(nilaiSidang1, nilaiSidang3);
        assertEquals(nilaiSidang1.hashCode(), nilaiSidang2.hashCode());
    }

}