CREATE TABLE usuario_curso (
                               usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
                               curso_id BIGINT NOT NULL REFERENCES cursos(id),
                               PRIMARY KEY (usuario_id, curso_id)
);