CREATE TABLE solicitacoes (
                              id BIGSERIAL PRIMARY KEY,
                              descricao TEXT NOT NULL,
                              area VARCHAR(255) NOT NULL,
                              horas_solicitadas INTEGER NOT NULL,
                              data_criacao TIMESTAMP NOT NULL,
                              status VARCHAR(50) NOT NULL,
                              aluno_id BIGINT NOT NULL REFERENCES usuarios(id),
                              curso_id BIGINT NOT NULL REFERENCES cursos(id)
);