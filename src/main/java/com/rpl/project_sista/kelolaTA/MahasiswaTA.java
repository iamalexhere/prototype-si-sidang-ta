package com.rpl.project_sista.kelolaTA;

import lombok.Data;

@Data
public class MahasiswaTA {
    private String namaMhs;
    private String judul;
    private String namapem;
    private String[] listpenguji;

    public String info() {
        return namaMhs + " " + judul + "\n" + namapem + " " + listpenguji[0] + " " + listpenguji[1];
    }
}
