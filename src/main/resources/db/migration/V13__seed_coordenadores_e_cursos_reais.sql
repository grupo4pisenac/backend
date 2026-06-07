-- ─────────────────────────────────────────
-- INSERÇÃO DE COORDENADORES
-- ─────────────────────────────────────────
INSERT INTO usuarios (nome, email, senha, perfil, semestre_atual)
VALUES
-- Design de Moda / Design de Interiores
('Daniela Vasconcelos de Oliveira', 'fac-design@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
('Luiz Clério Duarte Júnior', 'luizduarte@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
-- Gastronomia
('Lorena Bezerra de Sousa', 'lorenasousa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
('Robson Luis Trindade Lustosa', 'robsonlustosa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
('Denise Lins Farias dos Santos', 'denisesantos@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
-- Estética e Cosmética / Enfermagem
('Andressa Mendonça da Costa Brito', 'andressabrito@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
-- Análise e Desenvolvimento de Sistemas / Jogos Digitais
('Ameliara Freire Santos de Miranda', 'ameliaramiranda@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
-- Logística
('Over Manuel Montes Causil', 'overcausil@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
-- Enfermagem
('Bruno Felipe Novaes de Souza', 'brunonovaes@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1);

-- ─────────────────────────────────────────
-- INSERÇÃO DE CURSOS
-- ─────────────────────────────────────────
INSERT INTO cursos (nome, total_semestres)
VALUES
    ('Design de Moda', 5),
    ('Gastronomia', 5),
    ('Estética e Cosmética', 5),
    ('Análise e Desenvolvimento de Sistemas', 5),
    ('Design de Interiores', 5),
    ('Logística', 4),
    ('Jogos Digitais', 5),
    ('Enfermagem', 10);

-- ─────────────────────────────────────────
-- ASSOCIAÇÃO COORDENADOR <-> CURSO
-- ─────────────────────────────────────────
INSERT INTO usuario_curso (usuario_id, curso_id)
VALUES
-- Daniela → Design de Moda
((SELECT id FROM usuarios WHERE email = 'fac-design@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Moda')),
-- Daniela → Design de Interiores
((SELECT id FROM usuarios WHERE email = 'fac-design@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Interiores')),
-- Luiz → Design de Moda
((SELECT id FROM usuarios WHERE email = 'luizduarte@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Moda')),
-- Lorena → Gastronomia
((SELECT id FROM usuarios WHERE email = 'lorenasousa@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia')),
-- Robson → Gastronomia
((SELECT id FROM usuarios WHERE email = 'robsonlustosa@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia')),
-- Denise → Gastronomia
((SELECT id FROM usuarios WHERE email = 'denisesantos@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia')),
-- Andressa → Estética e Cosmética
((SELECT id FROM usuarios WHERE email = 'andressabrito@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Estética e Cosmética')),
-- Andressa → Enfermagem
((SELECT id FROM usuarios WHERE email = 'andressabrito@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Enfermagem')),
-- Ameliara → Análise e Desenvolvimento de Sistemas
((SELECT id FROM usuarios WHERE email = 'ameliaramiranda@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas')),
-- Ameliara → Jogos Digitais
((SELECT id FROM usuarios WHERE email = 'ameliaramiranda@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Jogos Digitais')),
-- Over → Logística
((SELECT id FROM usuarios WHERE email = 'overcausil@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Logística')),
-- Bruno → Enfermagem
((SELECT id FROM usuarios WHERE email = 'brunonovaes@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Enfermagem'));