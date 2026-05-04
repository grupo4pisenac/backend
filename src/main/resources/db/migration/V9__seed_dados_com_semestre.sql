-- Atualiza total_semestres nos cursos
UPDATE cursos SET total_semestres = 5 WHERE nome = 'Análise e Desenvolvimento de Sistemas';
UPDATE cursos SET total_semestres = 5 WHERE nome = 'Gestão de Tecnologia da Informação';
UPDATE cursos SET total_semestres = 5 WHERE nome = 'Redes de Computadores';
UPDATE cursos SET total_semestres = 5 WHERE nome = 'Ciência de Dados';
UPDATE cursos SET total_semestres = 5 WHERE nome = 'Segurança da Informação';

-- Atualiza semestre_atual dos alunos
UPDATE usuarios SET semestre_atual = 1 WHERE email = 'ana.paula@senac.com';
UPDATE usuarios SET semestre_atual = 2 WHERE email = 'bruno.costa@senac.com';
UPDATE usuarios SET semestre_atual = 1 WHERE email = 'camila.rocha@senac.com';
UPDATE usuarios SET semestre_atual = 2 WHERE email = 'diego.ferreira@senac.com';
UPDATE usuarios SET semestre_atual = 1 WHERE email = 'eduarda.martins@senac.com';
UPDATE usuarios SET semestre_atual = 2 WHERE email = 'felipe.santos@senac.com';
UPDATE usuarios SET semestre_atual = 1 WHERE email = 'gabriela.alves@senac.com';
UPDATE usuarios SET semestre_atual = 2 WHERE email = 'henrique.nunes@senac.com';
UPDATE usuarios SET semestre_atual = 1 WHERE email = 'isabella.pereira@senac.com';
UPDATE usuarios SET semestre_atual = 2 WHERE email = 'joao.vitor@senac.com';

-- Atualiza limite_horas_semestral nas regras de atividade
UPDATE regras_atividade SET limite_horas_semestral = 25 WHERE area = 'Extensão' AND curso_id = (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas');
UPDATE regras_atividade SET limite_horas_semestral = 15 WHERE area = 'Pesquisa'  AND curso_id = (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas');
UPDATE regras_atividade SET limite_horas_semestral = 12 WHERE area = 'Ensino'    AND curso_id = (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Cultura'   AND curso_id = (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas');

UPDATE regras_atividade SET limite_horas_semestral = 20 WHERE area = 'Extensão' AND curso_id = (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 15 WHERE area = 'Pesquisa'  AND curso_id = (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 12 WHERE area = 'Ensino'    AND curso_id = (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Cultura'   AND curso_id = (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação');

UPDATE regras_atividade SET limite_horas_semestral = 25 WHERE area = 'Extensão' AND curso_id = (SELECT id FROM cursos WHERE nome = 'Redes de Computadores');
UPDATE regras_atividade SET limite_horas_semestral = 12 WHERE area = 'Pesquisa'  AND curso_id = (SELECT id FROM cursos WHERE nome = 'Redes de Computadores');
UPDATE regras_atividade SET limite_horas_semestral = 12 WHERE area = 'Ensino'    AND curso_id = (SELECT id FROM cursos WHERE nome = 'Redes de Computadores');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Cultura'   AND curso_id = (SELECT id FROM cursos WHERE nome = 'Redes de Computadores');

UPDATE regras_atividade SET limite_horas_semestral = 20 WHERE area = 'Extensão' AND curso_id = (SELECT id FROM cursos WHERE nome = 'Ciência de Dados');
UPDATE regras_atividade SET limite_horas_semestral = 20 WHERE area = 'Pesquisa'  AND curso_id = (SELECT id FROM cursos WHERE nome = 'Ciência de Dados');
UPDATE regras_atividade SET limite_horas_semestral = 12 WHERE area = 'Ensino'    AND curso_id = (SELECT id FROM cursos WHERE nome = 'Ciência de Dados');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Cultura'   AND curso_id = (SELECT id FROM cursos WHERE nome = 'Ciência de Dados');

UPDATE regras_atividade SET limite_horas_semestral = 25 WHERE area = 'Extensão' AND curso_id = (SELECT id FROM cursos WHERE nome = 'Segurança da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 15 WHERE area = 'Pesquisa'  AND curso_id = (SELECT id FROM cursos WHERE nome = 'Segurança da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Ensino'    AND curso_id = (SELECT id FROM cursos WHERE nome = 'Segurança da Informação');
UPDATE regras_atividade SET limite_horas_semestral = 8  WHERE area = 'Cultura'   AND curso_id = (SELECT id FROM cursos WHERE nome = 'Segurança da Informação');

-- Atualiza semestre nas solicitações existentes com base no semestre_atual do aluno
UPDATE solicitacoes s
SET semestre = u.semestre_atual
    FROM usuarios u
WHERE s.aluno_id = u.id AND s.semestre IS NULL;