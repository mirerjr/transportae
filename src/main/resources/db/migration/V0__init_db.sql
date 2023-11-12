CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;
CREATE SEQUENCE IF NOT EXISTS usuario_seq INCREMENT 50;

CREATE TABLE IF NOT EXISTS public.usuario
(
    ativo boolean,
    data_nascimento date,
    id bigint NOT NULL,
    data_cadasto timestamp(6) without time zone,
    data_primeiro_acesso timestamp(6) without time zone,
    cpf character varying(11) NOT NULL,
    matricula character varying(50),
    email character varying(150) NOT NULL,
    nome character varying(255) NOT NULL,
    perfil character varying(255),
    senha character varying(255) NOT NULL,
    CONSTRAINT usuario_pkey PRIMARY KEY (id),
    CONSTRAINT usuario_perfil_check CHECK (perfil::text = ANY (ARRAY[
        'ALUNO'::character varying, 
        'MOTORISTA'::character varying, 
        'ADMIN'::character varying
    ]::text[]))
);