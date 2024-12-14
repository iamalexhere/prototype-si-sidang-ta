insert into penguji_sidang(sidang_id,dosen_id,peran_penguji)
--values(1,7,'penguji2')
--values(2,3,'penguji1')
--values(3,4,'penguji2')
--values(4,6,'penguji2')
--values(5,1,'penguji1')
--values(6,3,'penguji2')
--values(7,4,'penguji1')
--values(8,5,'penguji2')
--values(9,1,'penguji1')
--values(10,3,'penguji2')
--values(11,7,'penguji1')
--values(12,4,'penguji2')
--values(13,5,'penguji1')

insert into nilai_sidang(nilai_id,sidang_id,komponen_id,dosen_id,nilai)
values(1,2,1,1,80),
(2,2,2,1,60),
(3,2,3,1,70),
(4,2,4,1,75),
(5,2,5,1,90),

(6,2,1,3,90),
(7,2,2,3,70),
(8,2,3,3,80),
(9,2,4,3,65),
(10,2,5,3,80),


(11,2,6,2,80),
(12,2,7,2,60),
(13,2,8,2,70),
(14,2,9,2,75)

CREATE TABLE bobot_role_per_semester (
    bobot_id SERIAL PRIMARY KEY,
    role tipe_penilai NOT NULL,
    bobot FLOAT NOT NULL,
    semester_id INTEGER REFERENCES semester(semester_id)
);

INSERT INTO bobot_role_per_semester (role, bobot, semester_id)
VALUES
    ('pembimbing', 20, 1),
    ('penguji', 35, 1)






