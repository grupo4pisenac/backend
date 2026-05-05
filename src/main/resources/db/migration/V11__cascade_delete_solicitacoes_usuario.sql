-- Cascade ao deletar usuário (aluno com solicitações)
ALTER TABLE solicitacoes DROP CONSTRAINT solicitacoes_aluno_id_fkey;
ALTER TABLE solicitacoes ADD CONSTRAINT solicitacoes_aluno_id_fkey
    FOREIGN KEY (aluno_id) REFERENCES usuarios(id) ON DELETE CASCADE;

-- Cascade ao deletar curso (regras vinculadas)
ALTER TABLE regras_atividade DROP CONSTRAINT regras_atividade_curso_id_fkey;
ALTER TABLE regras_atividade ADD CONSTRAINT regras_atividade_curso_id_fkey
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE;

-- Cascade ao deletar curso (solicitações vinculadas)
ALTER TABLE solicitacoes DROP CONSTRAINT solicitacoes_curso_id_fkey;
ALTER TABLE solicitacoes ADD CONSTRAINT solicitacoes_curso_id_fkey
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE;