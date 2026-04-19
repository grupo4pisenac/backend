CREATE TABLE regras_atividade (
                                  id BIGSERIAL PRIMARY KEY,
                                  area VARCHAR(255) NOT NULL,
                                  limite_horas INTEGER NOT NULL,
                                  curso_id BIGINT NOT NULL REFERENCES cursos(id)
);