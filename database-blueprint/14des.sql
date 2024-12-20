PGDMP  ;    &                |            project_sista    17.2    17.2 �    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16677    project_sista    DATABASE     �   CREATE DATABASE project_sista WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_Indonesia.1252';
    DROP DATABASE project_sista;
                     postgres    false                        3079    34087    pgcrypto 	   EXTENSION     <   CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;
    DROP EXTENSION pgcrypto;
                        false            �           0    0    EXTENSION pgcrypto    COMMENT     <   COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';
                             false    2            �           1247    34154    jenis_ta    TYPE     >   CREATE TYPE public.jenis_ta AS ENUM (
    'ta1',
    'ta2'
);
    DROP TYPE public.jenis_ta;
       public               postgres    false            �           1247    34170    peran_penguji    TYPE     M   CREATE TYPE public.peran_penguji AS ENUM (
    'penguji1',
    'penguji2'
);
     DROP TYPE public.peran_penguji;
       public               postgres    false            �           1247    34148    periode    TYPE     B   CREATE TYPE public.periode AS ENUM (
    'ganjil',
    'genap'
);
    DROP TYPE public.periode;
       public               postgres    false            �           1247    34160    status_sidang    TYPE     r   CREATE TYPE public.status_sidang AS ENUM (
    'terjadwal',
    'berlangsung',
    'selesai',
    'dibatalkan'
);
     DROP TYPE public.status_sidang;
       public               postgres    false            �           1247    34134 	   status_ta    TYPE     �   CREATE TYPE public.status_ta AS ENUM (
    'draft',
    'diajukan',
    'diterima',
    'ditolak',
    'dalam_pengerjaan',
    'selesai'
);
    DROP TYPE public.status_ta;
       public               postgres    false            �           1247    34176    tipe_penilai    TYPE     M   CREATE TYPE public.tipe_penilai AS ENUM (
    'pembimbing',
    'penguji'
);
    DROP TYPE public.tipe_penilai;
       public               postgres    false            �           1247    34126 	   user_role    TYPE     T   CREATE TYPE public.user_role AS ENUM (
    'admin',
    'mahasiswa',
    'dosen'
);
    DROP TYPE public.user_role;
       public               postgres    false            !           1255    34412    hash_password(text)    FUNCTION     �   CREATE FUNCTION public.hash_password(password text) RETURNS text
    LANGUAGE plpgsql
    AS $$
BEGIN
  RETURN crypt(password, gen_salt('bf', 8));
END;
$$;
 3   DROP FUNCTION public.hash_password(password text);
       public               postgres    false                        1255    16962    update_updated_at_column()    FUNCTION     �   CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        NEW.updated_at = CURRENT_TIMESTAMP;
        RETURN NEW;
    END;
    $$;
 1   DROP FUNCTION public.update_updated_at_column();
       public               postgres    false            �            1259    34370    bap    TABLE     �   CREATE TABLE public.bap (
    bap_id integer NOT NULL,
    sidang_id integer NOT NULL,
    catatan_tambahan text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.bap;
       public         heap r       postgres    false            �            1259    34369    bap_bap_id_seq    SEQUENCE     �   CREATE SEQUENCE public.bap_bap_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.bap_bap_id_seq;
       public               postgres    false    239            �           0    0    bap_bap_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.bap_bap_id_seq OWNED BY public.bap.bap_id;
          public               postgres    false    238            �            1259    34350    catatan_revisi    TABLE     �   CREATE TABLE public.catatan_revisi (
    catatan_id integer NOT NULL,
    sidang_id integer NOT NULL,
    dosen_id integer NOT NULL,
    isi_catatan text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 "   DROP TABLE public.catatan_revisi;
       public         heap r       postgres    false            �            1259    34349    catatan_revisi_catatan_id_seq    SEQUENCE     �   CREATE SEQUENCE public.catatan_revisi_catatan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.catatan_revisi_catatan_id_seq;
       public               postgres    false    237            �           0    0    catatan_revisi_catatan_id_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.catatan_revisi_catatan_id_seq OWNED BY public.catatan_revisi.catatan_id;
          public               postgres    false    236            �            1259    34211    dosen    TABLE     �   CREATE TABLE public.dosen (
    dosen_id integer NOT NULL,
    nip character varying(20) NOT NULL,
    nama character varying(100) NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.dosen;
       public         heap r       postgres    false            �            1259    34210    dosen_dosen_id_seq    SEQUENCE     �   CREATE SEQUENCE public.dosen_dosen_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.dosen_dosen_id_seq;
       public               postgres    false    223            �           0    0    dosen_dosen_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.dosen_dosen_id_seq OWNED BY public.dosen.dosen_id;
          public               postgres    false    222            �            1259    34307    komponen_nilai    TABLE     �  CREATE TABLE public.komponen_nilai (
    komponen_id integer NOT NULL,
    semester_id integer NOT NULL,
    nama_komponen character varying(100) NOT NULL,
    bobot double precision NOT NULL,
    tipe_penilai public.tipe_penilai NOT NULL,
    deskripsi text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT komponen_nilai_bobot_check CHECK (((bobot >= (0)::double precision) AND (bobot <= (100)::double precision)))
);
 "   DROP TABLE public.komponen_nilai;
       public         heap r       postgres    false    925            �            1259    34306    komponen_nilai_komponen_id_seq    SEQUENCE     �   CREATE SEQUENCE public.komponen_nilai_komponen_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.komponen_nilai_komponen_id_seq;
       public               postgres    false    233            �           0    0    komponen_nilai_komponen_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.komponen_nilai_komponen_id_seq OWNED BY public.komponen_nilai.komponen_id;
          public               postgres    false    232            �            1259    34195 	   mahasiswa    TABLE     =  CREATE TABLE public.mahasiswa (
    mahasiswa_id integer NOT NULL,
    npm character varying(20) NOT NULL,
    nama character varying(100) NOT NULL,
    user_id integer NOT NULL,
    status_ta public.status_ta DEFAULT 'draft'::public.status_ta,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.mahasiswa;
       public         heap r       postgres    false    910    910            �            1259    34194    mahasiswa_mahasiswa_id_seq    SEQUENCE     �   CREATE SEQUENCE public.mahasiswa_mahasiswa_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.mahasiswa_mahasiswa_id_seq;
       public               postgres    false    221            �           0    0    mahasiswa_mahasiswa_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.mahasiswa_mahasiswa_id_seq OWNED BY public.mahasiswa.mahasiswa_id;
          public               postgres    false    220            �            1259    34323    nilai_sidang    TABLE     �  CREATE TABLE public.nilai_sidang (
    nilai_id integer NOT NULL,
    sidang_id integer NOT NULL,
    komponen_id integer NOT NULL,
    dosen_id integer NOT NULL,
    nilai double precision NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT nilai_sidang_nilai_check CHECK (((nilai >= (0)::double precision) AND (nilai <= (100)::double precision)))
);
     DROP TABLE public.nilai_sidang;
       public         heap r       postgres    false            �            1259    34322    nilai_sidang_nilai_id_seq    SEQUENCE     �   CREATE SEQUENCE public.nilai_sidang_nilai_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.nilai_sidang_nilai_id_seq;
       public               postgres    false    235            �           0    0    nilai_sidang_nilai_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.nilai_sidang_nilai_id_seq OWNED BY public.nilai_sidang.nilai_id;
          public               postgres    false    234            �            1259    34257    pembimbing_ta    TABLE     �   CREATE TABLE public.pembimbing_ta (
    ta_id integer NOT NULL,
    dosen_id integer NOT NULL,
    assigned_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 !   DROP TABLE public.pembimbing_ta;
       public         heap r       postgres    false            �            1259    34290    penguji_sidang    TABLE     �   CREATE TABLE public.penguji_sidang (
    sidang_id integer NOT NULL,
    dosen_id integer NOT NULL,
    peran_penguji public.peran_penguji NOT NULL,
    assigned_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 "   DROP TABLE public.penguji_sidang;
       public         heap r       postgres    false    922            �            1259    34386    persetujuan_bap    TABLE     �   CREATE TABLE public.persetujuan_bap (
    bap_id integer NOT NULL,
    user_id integer NOT NULL,
    is_approved boolean DEFAULT false,
    approved_at timestamp without time zone
);
 #   DROP TABLE public.persetujuan_bap;
       public         heap r       postgres    false            �            1259    34226    semester    TABLE       CREATE TABLE public.semester (
    semester_id integer NOT NULL,
    tahun_ajaran character varying(9) NOT NULL,
    periode public.periode NOT NULL,
    is_active boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.semester;
       public         heap r       postgres    false    913            �            1259    34225    semester_semester_id_seq    SEQUENCE     �   CREATE SEQUENCE public.semester_semester_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.semester_semester_id_seq;
       public               postgres    false    225            �           0    0    semester_semester_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.semester_semester_id_seq OWNED BY public.semester.semester_id;
          public               postgres    false    224            �            1259    34274    sidang    TABLE     �  CREATE TABLE public.sidang (
    sidang_id integer NOT NULL,
    ta_id integer NOT NULL,
    jadwal timestamp without time zone NOT NULL,
    ruangan character varying(50) NOT NULL,
    status_sidang public.status_sidang DEFAULT 'terjadwal'::public.status_sidang,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.sidang;
       public         heap r       postgres    false    919    919            �            1259    34273    sidang_sidang_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sidang_sidang_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.sidang_sidang_id_seq;
       public               postgres    false    230            �           0    0    sidang_sidang_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.sidang_sidang_id_seq OWNED BY public.sidang.sidang_id;
          public               postgres    false    229            �            1259    34237    tugas_akhir    TABLE     u  CREATE TABLE public.tugas_akhir (
    ta_id integer NOT NULL,
    mahasiswa_id integer NOT NULL,
    semester_id integer NOT NULL,
    judul text NOT NULL,
    topik character varying(255) NOT NULL,
    jenis_ta public.jenis_ta NOT NULL,
    status public.status_ta DEFAULT 'draft'::public.status_ta,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.tugas_akhir;
       public         heap r       postgres    false    910    916    910            �            1259    34236    tugas_akhir_ta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tugas_akhir_ta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tugas_akhir_ta_id_seq;
       public               postgres    false    227            �           0    0    tugas_akhir_ta_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.tugas_akhir_ta_id_seq OWNED BY public.tugas_akhir.ta_id;
          public               postgres    false    226            �            1259    34182    users    TABLE     U  CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    role public.user_role NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_active boolean DEFAULT true
);
    DROP TABLE public.users;
       public         heap r       postgres    false    907            �            1259    34181    users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public               postgres    false    219            �           0    0    users_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;
          public               postgres    false    218            �           2604    34373 
   bap bap_id    DEFAULT     h   ALTER TABLE ONLY public.bap ALTER COLUMN bap_id SET DEFAULT nextval('public.bap_bap_id_seq'::regclass);
 9   ALTER TABLE public.bap ALTER COLUMN bap_id DROP DEFAULT;
       public               postgres    false    238    239    239            �           2604    34353    catatan_revisi catatan_id    DEFAULT     �   ALTER TABLE ONLY public.catatan_revisi ALTER COLUMN catatan_id SET DEFAULT nextval('public.catatan_revisi_catatan_id_seq'::regclass);
 H   ALTER TABLE public.catatan_revisi ALTER COLUMN catatan_id DROP DEFAULT;
       public               postgres    false    237    236    237            �           2604    34214    dosen dosen_id    DEFAULT     p   ALTER TABLE ONLY public.dosen ALTER COLUMN dosen_id SET DEFAULT nextval('public.dosen_dosen_id_seq'::regclass);
 =   ALTER TABLE public.dosen ALTER COLUMN dosen_id DROP DEFAULT;
       public               postgres    false    223    222    223            �           2604    34310    komponen_nilai komponen_id    DEFAULT     �   ALTER TABLE ONLY public.komponen_nilai ALTER COLUMN komponen_id SET DEFAULT nextval('public.komponen_nilai_komponen_id_seq'::regclass);
 I   ALTER TABLE public.komponen_nilai ALTER COLUMN komponen_id DROP DEFAULT;
       public               postgres    false    233    232    233            �           2604    34198    mahasiswa mahasiswa_id    DEFAULT     �   ALTER TABLE ONLY public.mahasiswa ALTER COLUMN mahasiswa_id SET DEFAULT nextval('public.mahasiswa_mahasiswa_id_seq'::regclass);
 E   ALTER TABLE public.mahasiswa ALTER COLUMN mahasiswa_id DROP DEFAULT;
       public               postgres    false    220    221    221            �           2604    34326    nilai_sidang nilai_id    DEFAULT     ~   ALTER TABLE ONLY public.nilai_sidang ALTER COLUMN nilai_id SET DEFAULT nextval('public.nilai_sidang_nilai_id_seq'::regclass);
 D   ALTER TABLE public.nilai_sidang ALTER COLUMN nilai_id DROP DEFAULT;
       public               postgres    false    234    235    235            �           2604    34229    semester semester_id    DEFAULT     |   ALTER TABLE ONLY public.semester ALTER COLUMN semester_id SET DEFAULT nextval('public.semester_semester_id_seq'::regclass);
 C   ALTER TABLE public.semester ALTER COLUMN semester_id DROP DEFAULT;
       public               postgres    false    225    224    225            �           2604    34277    sidang sidang_id    DEFAULT     t   ALTER TABLE ONLY public.sidang ALTER COLUMN sidang_id SET DEFAULT nextval('public.sidang_sidang_id_seq'::regclass);
 ?   ALTER TABLE public.sidang ALTER COLUMN sidang_id DROP DEFAULT;
       public               postgres    false    229    230    230            �           2604    34240    tugas_akhir ta_id    DEFAULT     v   ALTER TABLE ONLY public.tugas_akhir ALTER COLUMN ta_id SET DEFAULT nextval('public.tugas_akhir_ta_id_seq'::regclass);
 @   ALTER TABLE public.tugas_akhir ALTER COLUMN ta_id DROP DEFAULT;
       public               postgres    false    227    226    227            �           2604    34185    users user_id    DEFAULT     n   ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);
 <   ALTER TABLE public.users ALTER COLUMN user_id DROP DEFAULT;
       public               postgres    false    219    218    219            �          0    34370    bap 
   TABLE DATA           N   COPY public.bap (bap_id, sidang_id, catatan_tambahan, created_at) FROM stdin;
    public               postgres    false    239   9�       �          0    34350    catatan_revisi 
   TABLE DATA           b   COPY public.catatan_revisi (catatan_id, sidang_id, dosen_id, isi_catatan, created_at) FROM stdin;
    public               postgres    false    237   V�       �          0    34211    dosen 
   TABLE DATA           I   COPY public.dosen (dosen_id, nip, nama, user_id, created_at) FROM stdin;
    public               postgres    false    223   s�       �          0    34307    komponen_nilai 
   TABLE DATA           }   COPY public.komponen_nilai (komponen_id, semester_id, nama_komponen, bobot, tipe_penilai, deskripsi, created_at) FROM stdin;
    public               postgres    false    233   ,�       �          0    34195 	   mahasiswa 
   TABLE DATA           \   COPY public.mahasiswa (mahasiswa_id, npm, nama, user_id, status_ta, created_at) FROM stdin;
    public               postgres    false    221   I�       �          0    34323    nilai_sidang 
   TABLE DATA           q   COPY public.nilai_sidang (nilai_id, sidang_id, komponen_id, dosen_id, nilai, created_at, updated_at) FROM stdin;
    public               postgres    false    235   �       �          0    34257    pembimbing_ta 
   TABLE DATA           E   COPY public.pembimbing_ta (ta_id, dosen_id, assigned_at) FROM stdin;
    public               postgres    false    228   1�       �          0    34290    penguji_sidang 
   TABLE DATA           Y   COPY public.penguji_sidang (sidang_id, dosen_id, peran_penguji, assigned_at) FROM stdin;
    public               postgres    false    231   ��       �          0    34386    persetujuan_bap 
   TABLE DATA           T   COPY public.persetujuan_bap (bap_id, user_id, is_approved, approved_at) FROM stdin;
    public               postgres    false    240   ��       �          0    34226    semester 
   TABLE DATA           ]   COPY public.semester (semester_id, tahun_ajaran, periode, is_active, created_at) FROM stdin;
    public               postgres    false    225   ��       �          0    34274    sidang 
   TABLE DATA           j   COPY public.sidang (sidang_id, ta_id, jadwal, ruangan, status_sidang, created_at, updated_at) FROM stdin;
    public               postgres    false    230   ��       �          0    34237    tugas_akhir 
   TABLE DATA           s   COPY public.tugas_akhir (ta_id, mahasiswa_id, semester_id, judul, topik, jenis_ta, status, created_at) FROM stdin;
    public               postgres    false    227   
�       �          0    34182    users 
   TABLE DATA           e   COPY public.users (user_id, username, email, password_hash, role, created_at, is_active) FROM stdin;
    public               postgres    false    219   ��       �           0    0    bap_bap_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.bap_bap_id_seq', 1, false);
          public               postgres    false    238            �           0    0    catatan_revisi_catatan_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.catatan_revisi_catatan_id_seq', 1, false);
          public               postgres    false    236            �           0    0    dosen_dosen_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.dosen_dosen_id_seq', 15, true);
          public               postgres    false    222            �           0    0    komponen_nilai_komponen_id_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.komponen_nilai_komponen_id_seq', 1, false);
          public               postgres    false    232            �           0    0    mahasiswa_mahasiswa_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.mahasiswa_mahasiswa_id_seq', 21, true);
          public               postgres    false    220            �           0    0    nilai_sidang_nilai_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.nilai_sidang_nilai_id_seq', 1, false);
          public               postgres    false    234            �           0    0    semester_semester_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.semester_semester_id_seq', 2, true);
          public               postgres    false    224            �           0    0    sidang_sidang_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.sidang_sidang_id_seq', 8, true);
          public               postgres    false    229            �           0    0    tugas_akhir_ta_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tugas_akhir_ta_id_seq', 8, true);
          public               postgres    false    226            �           0    0    users_user_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.users_user_id_seq', 36, true);
          public               postgres    false    218                       2606    34378    bap bap_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.bap
    ADD CONSTRAINT bap_pkey PRIMARY KEY (bap_id);
 6   ALTER TABLE ONLY public.bap DROP CONSTRAINT bap_pkey;
       public                 postgres    false    239                       2606    34380    bap bap_sidang_id_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.bap
    ADD CONSTRAINT bap_sidang_id_key UNIQUE (sidang_id);
 ?   ALTER TABLE ONLY public.bap DROP CONSTRAINT bap_sidang_id_key;
       public                 postgres    false    239                       2606    34358 "   catatan_revisi catatan_revisi_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.catatan_revisi
    ADD CONSTRAINT catatan_revisi_pkey PRIMARY KEY (catatan_id);
 L   ALTER TABLE ONLY public.catatan_revisi DROP CONSTRAINT catatan_revisi_pkey;
       public                 postgres    false    237            �           2606    34219    dosen dosen_nip_key 
   CONSTRAINT     M   ALTER TABLE ONLY public.dosen
    ADD CONSTRAINT dosen_nip_key UNIQUE (nip);
 =   ALTER TABLE ONLY public.dosen DROP CONSTRAINT dosen_nip_key;
       public                 postgres    false    223            �           2606    34217    dosen dosen_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.dosen
    ADD CONSTRAINT dosen_pkey PRIMARY KEY (dosen_id);
 :   ALTER TABLE ONLY public.dosen DROP CONSTRAINT dosen_pkey;
       public                 postgres    false    223                       2606    34316 "   komponen_nilai komponen_nilai_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.komponen_nilai
    ADD CONSTRAINT komponen_nilai_pkey PRIMARY KEY (komponen_id);
 L   ALTER TABLE ONLY public.komponen_nilai DROP CONSTRAINT komponen_nilai_pkey;
       public                 postgres    false    233            �           2606    34204    mahasiswa mahasiswa_npm_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.mahasiswa
    ADD CONSTRAINT mahasiswa_npm_key UNIQUE (npm);
 E   ALTER TABLE ONLY public.mahasiswa DROP CONSTRAINT mahasiswa_npm_key;
       public                 postgres    false    221            �           2606    34202    mahasiswa mahasiswa_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.mahasiswa
    ADD CONSTRAINT mahasiswa_pkey PRIMARY KEY (mahasiswa_id);
 B   ALTER TABLE ONLY public.mahasiswa DROP CONSTRAINT mahasiswa_pkey;
       public                 postgres    false    221                       2606    34331    nilai_sidang nilai_sidang_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.nilai_sidang
    ADD CONSTRAINT nilai_sidang_pkey PRIMARY KEY (nilai_id);
 H   ALTER TABLE ONLY public.nilai_sidang DROP CONSTRAINT nilai_sidang_pkey;
       public                 postgres    false    235                       2606    34333 <   nilai_sidang nilai_sidang_sidang_id_komponen_id_dosen_id_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.nilai_sidang
    ADD CONSTRAINT nilai_sidang_sidang_id_komponen_id_dosen_id_key UNIQUE (sidang_id, komponen_id, dosen_id);
 f   ALTER TABLE ONLY public.nilai_sidang DROP CONSTRAINT nilai_sidang_sidang_id_komponen_id_dosen_id_key;
       public                 postgres    false    235    235    235                       2606    34262     pembimbing_ta pembimbing_ta_pkey 
   CONSTRAINT     k   ALTER TABLE ONLY public.pembimbing_ta
    ADD CONSTRAINT pembimbing_ta_pkey PRIMARY KEY (ta_id, dosen_id);
 J   ALTER TABLE ONLY public.pembimbing_ta DROP CONSTRAINT pembimbing_ta_pkey;
       public                 postgres    false    228    228                       2606    34295 "   penguji_sidang penguji_sidang_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.penguji_sidang
    ADD CONSTRAINT penguji_sidang_pkey PRIMARY KEY (sidang_id, dosen_id);
 L   ALTER TABLE ONLY public.penguji_sidang DROP CONSTRAINT penguji_sidang_pkey;
       public                 postgres    false    231    231                       2606    34391 $   persetujuan_bap persetujuan_bap_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.persetujuan_bap
    ADD CONSTRAINT persetujuan_bap_pkey PRIMARY KEY (bap_id, user_id);
 N   ALTER TABLE ONLY public.persetujuan_bap DROP CONSTRAINT persetujuan_bap_pkey;
       public                 postgres    false    240    240            �           2606    34233    semester semester_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.semester
    ADD CONSTRAINT semester_pkey PRIMARY KEY (semester_id);
 @   ALTER TABLE ONLY public.semester DROP CONSTRAINT semester_pkey;
       public                 postgres    false    225            �           2606    34235 *   semester semester_tahun_ajaran_periode_key 
   CONSTRAINT     v   ALTER TABLE ONLY public.semester
    ADD CONSTRAINT semester_tahun_ajaran_periode_key UNIQUE (tahun_ajaran, periode);
 T   ALTER TABLE ONLY public.semester DROP CONSTRAINT semester_tahun_ajaran_periode_key;
       public                 postgres    false    225    225                       2606    34282    sidang sidang_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.sidang
    ADD CONSTRAINT sidang_pkey PRIMARY KEY (sidang_id);
 <   ALTER TABLE ONLY public.sidang DROP CONSTRAINT sidang_pkey;
       public                 postgres    false    230            
           2606    34284    sidang sidang_ta_id_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.sidang
    ADD CONSTRAINT sidang_ta_id_key UNIQUE (ta_id);
 A   ALTER TABLE ONLY public.sidang DROP CONSTRAINT sidang_ta_id_key;
       public                 postgres    false    230                       2606    34246    tugas_akhir tugas_akhir_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tugas_akhir
    ADD CONSTRAINT tugas_akhir_pkey PRIMARY KEY (ta_id);
 F   ALTER TABLE ONLY public.tugas_akhir DROP CONSTRAINT tugas_akhir_pkey;
       public                 postgres    false    227            �           2606    34193    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public                 postgres    false    219            �           2606    34189    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    219            �           2606    34191    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public                 postgres    false    219                       1259    34408    idx_catatan_revisi_sidang_id    INDEX     \   CREATE INDEX idx_catatan_revisi_sidang_id ON public.catatan_revisi USING btree (sidang_id);
 0   DROP INDEX public.idx_catatan_revisi_sidang_id;
       public                 postgres    false    237            �           1259    34403    idx_dosen_user_id    INDEX     F   CREATE INDEX idx_dosen_user_id ON public.dosen USING btree (user_id);
 %   DROP INDEX public.idx_dosen_user_id;
       public                 postgres    false    223            �           1259    34402    idx_mahasiswa_user_id    INDEX     N   CREATE INDEX idx_mahasiswa_user_id ON public.mahasiswa USING btree (user_id);
 )   DROP INDEX public.idx_mahasiswa_user_id;
       public                 postgres    false    221                       1259    34407    idx_nilai_sidang_sidang_id    INDEX     X   CREATE INDEX idx_nilai_sidang_sidang_id ON public.nilai_sidang USING btree (sidang_id);
 .   DROP INDEX public.idx_nilai_sidang_sidang_id;
       public                 postgres    false    235                       1259    34409    idx_persetujuan_bap_user_id    INDEX     Z   CREATE INDEX idx_persetujuan_bap_user_id ON public.persetujuan_bap USING btree (user_id);
 /   DROP INDEX public.idx_persetujuan_bap_user_id;
       public                 postgres    false    240                       1259    34406    idx_sidang_ta_id    INDEX     D   CREATE INDEX idx_sidang_ta_id ON public.sidang USING btree (ta_id);
 $   DROP INDEX public.idx_sidang_ta_id;
       public                 postgres    false    230                        1259    34404    idx_tugas_akhir_mahasiswa_id    INDEX     \   CREATE INDEX idx_tugas_akhir_mahasiswa_id ON public.tugas_akhir USING btree (mahasiswa_id);
 0   DROP INDEX public.idx_tugas_akhir_mahasiswa_id;
       public                 postgres    false    227                       1259    34405    idx_tugas_akhir_semester_id    INDEX     Z   CREATE INDEX idx_tugas_akhir_semester_id ON public.tugas_akhir USING btree (semester_id);
 /   DROP INDEX public.idx_tugas_akhir_semester_id;
       public                 postgres    false    227            1           2620    34411 +   nilai_sidang update_nilai_sidang_updated_at    TRIGGER     �   CREATE TRIGGER update_nilai_sidang_updated_at BEFORE UPDATE ON public.nilai_sidang FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();
 D   DROP TRIGGER update_nilai_sidang_updated_at ON public.nilai_sidang;
       public               postgres    false    235    288            0           2620    34410    sidang update_sidang_updated_at    TRIGGER     �   CREATE TRIGGER update_sidang_updated_at BEFORE UPDATE ON public.sidang FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();
 8   DROP TRIGGER update_sidang_updated_at ON public.sidang;
       public               postgres    false    230    288            -           2606    34381    bap bap_sidang_id_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.bap
    ADD CONSTRAINT bap_sidang_id_fkey FOREIGN KEY (sidang_id) REFERENCES public.sidang(sidang_id);
 @   ALTER TABLE ONLY public.bap DROP CONSTRAINT bap_sidang_id_fkey;
       public               postgres    false    4872    239    230            +           2606    34364 +   catatan_revisi catatan_revisi_dosen_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.catatan_revisi
    ADD CONSTRAINT catatan_revisi_dosen_id_fkey FOREIGN KEY (dosen_id) REFERENCES public.dosen(dosen_id);
 U   ALTER TABLE ONLY public.catatan_revisi DROP CONSTRAINT catatan_revisi_dosen_id_fkey;
       public               postgres    false    4858    237    223            ,           2606    34359 ,   catatan_revisi catatan_revisi_sidang_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.catatan_revisi
    ADD CONSTRAINT catatan_revisi_sidang_id_fkey FOREIGN KEY (sidang_id) REFERENCES public.sidang(sidang_id);
 V   ALTER TABLE ONLY public.catatan_revisi DROP CONSTRAINT catatan_revisi_sidang_id_fkey;
       public               postgres    false    230    4872    237                       2606    34220    dosen dosen_user_id_fkey    FK CONSTRAINT     |   ALTER TABLE ONLY public.dosen
    ADD CONSTRAINT dosen_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);
 B   ALTER TABLE ONLY public.dosen DROP CONSTRAINT dosen_user_id_fkey;
       public               postgres    false    223    4847    219            '           2606    34317 .   komponen_nilai komponen_nilai_semester_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.komponen_nilai
    ADD CONSTRAINT komponen_nilai_semester_id_fkey FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);
 X   ALTER TABLE ONLY public.komponen_nilai DROP CONSTRAINT komponen_nilai_semester_id_fkey;
       public               postgres    false    225    4861    233                       2606    34205     mahasiswa mahasiswa_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.mahasiswa
    ADD CONSTRAINT mahasiswa_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);
 J   ALTER TABLE ONLY public.mahasiswa DROP CONSTRAINT mahasiswa_user_id_fkey;
       public               postgres    false    221    4847    219            (           2606    34344 '   nilai_sidang nilai_sidang_dosen_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.nilai_sidang
    ADD CONSTRAINT nilai_sidang_dosen_id_fkey FOREIGN KEY (dosen_id) REFERENCES public.dosen(dosen_id);
 Q   ALTER TABLE ONLY public.nilai_sidang DROP CONSTRAINT nilai_sidang_dosen_id_fkey;
       public               postgres    false    4858    223    235            )           2606    34339 *   nilai_sidang nilai_sidang_komponen_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.nilai_sidang
    ADD CONSTRAINT nilai_sidang_komponen_id_fkey FOREIGN KEY (komponen_id) REFERENCES public.komponen_nilai(komponen_id);
 T   ALTER TABLE ONLY public.nilai_sidang DROP CONSTRAINT nilai_sidang_komponen_id_fkey;
       public               postgres    false    235    4878    233            *           2606    34334 (   nilai_sidang nilai_sidang_sidang_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.nilai_sidang
    ADD CONSTRAINT nilai_sidang_sidang_id_fkey FOREIGN KEY (sidang_id) REFERENCES public.sidang(sidang_id);
 R   ALTER TABLE ONLY public.nilai_sidang DROP CONSTRAINT nilai_sidang_sidang_id_fkey;
       public               postgres    false    235    4872    230            "           2606    34268 )   pembimbing_ta pembimbing_ta_dosen_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pembimbing_ta
    ADD CONSTRAINT pembimbing_ta_dosen_id_fkey FOREIGN KEY (dosen_id) REFERENCES public.dosen(dosen_id);
 S   ALTER TABLE ONLY public.pembimbing_ta DROP CONSTRAINT pembimbing_ta_dosen_id_fkey;
       public               postgres    false    223    4858    228            #           2606    34263 &   pembimbing_ta pembimbing_ta_ta_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pembimbing_ta
    ADD CONSTRAINT pembimbing_ta_ta_id_fkey FOREIGN KEY (ta_id) REFERENCES public.tugas_akhir(ta_id);
 P   ALTER TABLE ONLY public.pembimbing_ta DROP CONSTRAINT pembimbing_ta_ta_id_fkey;
       public               postgres    false    4867    227    228            %           2606    34301 +   penguji_sidang penguji_sidang_dosen_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.penguji_sidang
    ADD CONSTRAINT penguji_sidang_dosen_id_fkey FOREIGN KEY (dosen_id) REFERENCES public.dosen(dosen_id);
 U   ALTER TABLE ONLY public.penguji_sidang DROP CONSTRAINT penguji_sidang_dosen_id_fkey;
       public               postgres    false    231    223    4858            &           2606    34296 ,   penguji_sidang penguji_sidang_sidang_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.penguji_sidang
    ADD CONSTRAINT penguji_sidang_sidang_id_fkey FOREIGN KEY (sidang_id) REFERENCES public.sidang(sidang_id);
 V   ALTER TABLE ONLY public.penguji_sidang DROP CONSTRAINT penguji_sidang_sidang_id_fkey;
       public               postgres    false    231    230    4872            .           2606    34392 +   persetujuan_bap persetujuan_bap_bap_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.persetujuan_bap
    ADD CONSTRAINT persetujuan_bap_bap_id_fkey FOREIGN KEY (bap_id) REFERENCES public.bap(bap_id);
 U   ALTER TABLE ONLY public.persetujuan_bap DROP CONSTRAINT persetujuan_bap_bap_id_fkey;
       public               postgres    false    239    240    4888            /           2606    34397 ,   persetujuan_bap persetujuan_bap_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.persetujuan_bap
    ADD CONSTRAINT persetujuan_bap_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);
 V   ALTER TABLE ONLY public.persetujuan_bap DROP CONSTRAINT persetujuan_bap_user_id_fkey;
       public               postgres    false    219    240    4847            $           2606    34285    sidang sidang_ta_id_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.sidang
    ADD CONSTRAINT sidang_ta_id_fkey FOREIGN KEY (ta_id) REFERENCES public.tugas_akhir(ta_id);
 B   ALTER TABLE ONLY public.sidang DROP CONSTRAINT sidang_ta_id_fkey;
       public               postgres    false    230    227    4867                        2606    34247 )   tugas_akhir tugas_akhir_mahasiswa_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tugas_akhir
    ADD CONSTRAINT tugas_akhir_mahasiswa_id_fkey FOREIGN KEY (mahasiswa_id) REFERENCES public.mahasiswa(mahasiswa_id);
 S   ALTER TABLE ONLY public.tugas_akhir DROP CONSTRAINT tugas_akhir_mahasiswa_id_fkey;
       public               postgres    false    227    221    4854            !           2606    34252 (   tugas_akhir tugas_akhir_semester_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tugas_akhir
    ADD CONSTRAINT tugas_akhir_semester_id_fkey FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);
 R   ALTER TABLE ONLY public.tugas_akhir DROP CONSTRAINT tugas_akhir_semester_id_fkey;
       public               postgres    false    225    4861    227            �      x������ � �      �      x������ � �      �   �  x�}��n�0 �y�y :�C��!@KTQ�8�2M�d��^9I+޾&5�e��!�C<_f&c)�(N�H�m�讂-]�B�;S�xD1�=��I& �E4Y$	J���� �B,vJiҰ,��ѕ`�j�7��!�F�=m>����F1
%%>)�LpR�hΩ���8$��0��R-ߺhK{(�]H�}F1�/N��Tݑ�`�P���NEJMN,^��5��?�ƬL��$�<4�t��.`U�.,Aڵ�q>I�{h.N�݄�{���C��������ŕ�ٍ6M�}�T*�e�`]RzV:�^MCP(�s�T]�>CJEm>���d�W��MōK�mWu�5�ѿ�\��\-;�ׇQa��-�n��;rq��L�`��
��柋�G�?ǯ���5?����=�K�[�Q�e�gD�;�����u3��      �      x������ � �      �   �  x����n�0���S�Z���8�%-�.m�!rڅ��X�,��"{�1�0���~��"��h]`Q �1rP���#`m�S]���V
�]Q�U�-b���B'^������?���2�%|q��	jk(��y�J|�C��Vm|do������e�YaO�e����]�wԩ����<�z����U�W��^��8qhA�y�It�{;�'��&�.��u���<��8`��]ͳX��%>ҏ`�S[���z�с�ey�N>@L��̐a"&%�s�M��1�(q7��%�If�� ^������t"��9:�`~�ΐ��7M�r}Ck��2�DL*J�J�S���1y(��H�^Dj�)@��!&%�x4?����ߣe���l��g��-�7=�>��DyB7�z�L�G#;ȰQ'%~�������1h��P���b���E�      �      x������ � �      �   r   x�e��1Cѳ�",� �M-鿎�>��aa�y>������Q�$[|��ac�@Un�o�a*���?J�:.��c@��-us�Qm���5?��[�����:���U��N-u      �   �   x�}�=ND1F�:^��N�Z��@4��AFJyt�/������������؅�����96�����v�� ���\nOg�b�־�.ʹ@o�.7�"��[9n{�+�ldד:�Uu�kמ��$���b��R��k��.{@�68�;N�Wgm�p����T��$�ʼ�,����~j[e-�)ؾ�՘���x)�#      �      x������ � �      �   E   x�3�4202�&��yY�9�i !]C#]CC#+3+=CC#S.#d��y��%����qqq )�b      �   �   x���KNC1E��*��Z����Z�T�	b���>�U�EA�H����*Ҥ	�!G�2ᓹX�}�����?Oo��y��8�LW�ubK+�hXq]�r���@.���0
!]�m�������>E	[Qn�i�*û��v,�8��tPf�ק�v�B�Һ7_z����\�b��#6mP�bX�7y�+�}���l��(�;E�飍+�q�N���kzL$���vJG���r�E��6Y���%��A X�?Q���]��      �   i  x����N!��5</ �>��؅U�i����9�X�3�Pf��˴՘��L�$,>?q$�e�mmg}��#�K�#��5�nؘ۸�W�����a:Y4%�h����M��:��+�E��Z��(n�@×��+7�i�!&�Y�F���?����8gLa�dF�۹��x�g�"�]ߑ��m�d#��>N�GxM��������
�߁_�˒��y�m�6����w�?��@R��k�O�#�4D��oC���GI!k�i!�P� 3.��,�@l�Q?�k���f��F���?�N���dC
���J�KU-�Ҫ�%*s�����6d+ ��㣵���'�J�T��HY+M9�1��b�?>���      �   �  x������FFcݧ����Aȑ��uR@�Ạ����}��>5{WRK��
|�%���?�?��-B$�6��7��m���Q�>�w�.�;G5D!��&��C.�5�rz�+O7�6Q��rC`�;N��S���?0��Bp�b	z�� 6hS�����4���H^xq�3���oK="���a��q}=�]�8l4��M&G��A 7yZ�����n��a���'�*���2s�����t����ƈ��lxV�gi���5{S��EUFH��2j���6�u�T\ă��z���-���("�}�lp�$�N�Y4��kk(�M2t�#	x��������\�{�$���	�M�cWe����i�g���>~T�g����ԠA��C�VI��}�@x�lw�����:d��m�!$�a��3.��EN�Id9F��u�e�vS��)��|��ޗ����]|��D�&Y׃z_A�H-�j���ꑽ��A�m����m~�H ҷ��;CR�F�\yN?�ݕN� W�/�/�c�8^A�m��خb�mFP~�?����@(���өpeK,"'�䣎y9�Ӎ��48��5�Ԓ�	�UW} �m�!LA	���q�k�Ǒ@w�� <�S�k�H�z0QI�=��UB��"��w�r�մ}����
$��`ۧe���;��!����Oe-�"���$2��tEIS��d�h*����b�	�H�M#�<@;���wۻY|�1G��p�O48G��X	#!����kȄcϭlZI���K���<�@�"ɐ��w�����Rٛ�3��B���o�m�+k�q���Ѝ��,\E�j{b�	a����+@����+�@X�?��!jm��r��r��w�wo�+v�5�;,L8���Iȳ�IeV�Л�� R�����c��};�Iۗ���?����bU� f�l�>8�+!}AW���ٸ�&�ƻ���B`6��c�(���!�e���a��طy�p���z�@��Ѕ��ΐȭ!�
��;f�pN��� J@�v�?@�o
bQ~��cT�9O����~���$m1���1�P���#��'��[���� �' �)ҲK�s`�vFN�k�љ�Gџ�g�.ao2G���u�Ν�j��7�(?Q��G�቞�� �����<�Ù�����x��cy�Ki�\D`����T4z�O�C��⮼��<��}����*Ǵ^_Y�m��N�Z�]De5�����'���Ʋ	�n%������u�_ң쯻�7��F�{��a����@�~o?I:�����˄C��M�{�N3y�kĜ�Y&�)���8�,V;D,L^��~�Q"`���mk�\^�T?�i�hM�:q&� �od4>ʯK,h]άZ���{�	��\a�*�wy�[�S�L�V#Y<�I�>y��m�b��2H�&V}��c���]w�`�d�V������}���T��\�=�LG��Y~�a5з�8�(?:$�ڇ�s���L��w��W�L�����DI'_'�^6�qHW}�����{A5Z�Y/-��:7�U9�&-&��z�����)�ة� g&}��o�qrQ~j0�Ũs�����/,8��#,�N$-|�E7�YD����'ȭ�o�qjQ~"PH�xZ�칃g�����#�R��?��L��S��:�;����\����cR34�$�8�{�#�܆�n1[�औ�����>&~��iXE�Z����cR��YFK3aw_�h�p����}8����Bw*�i4#�z.V-L^��1��ƞI/d��ӐwJ3�!B1Ϻ�r�y��8���H56����"&/����/���;�V���j[7�n\�B�S2����v���Sxb�n�}�\����cR���,���P�b��>�WH����x����0��LgB2K���'�m��&B|L�����v�Ё�#�����KOt������� ���U1��Y�m��&B|Lꔥ� EH�R]���
U#�=\'L*��j��������o��"?~��{���     