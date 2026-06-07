-- ─────────────────────────────────────────
-- INSERÇÃO DE COORDENADORES
-- ─────────────────────────────────────────
INSERT INTO usuarios (nome, email, senha, perfil, semestre_atual)
VALUES
    ('Daniela Vasconcelos de Oliveira', 'fac-design@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Luiz Clério Duarte Júnior', 'luizduarte@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Lorena Bezerra de Sousa', 'lorenasousa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Robson Luis Trindade Lustosa', 'robsonlustosa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Denise Lins Farias dos Santos', 'denisesantos@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Andressa Mendonça da Costa Brito', 'andressabrito@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Ameliara Freire Santos de Miranda', 'ameliaramiranda@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Over Manuel Montes Causil', 'overcausil@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1),
    ('Bruno Felipe Novaes de Souza', 'brunonovaes@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR', 1)
    ON CONFLICT (email) DO NOTHING;

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
    ('Enfermagem', 10)
    ON CONFLICT (nome) DO NOTHING;

-- ─────────────────────────────────────────
-- ASSOCIAÇÃO COORDENADOR <-> CURSO
-- ─────────────────────────────────────────
INSERT INTO usuario_curso (usuario_id, curso_id)
VALUES
    ((SELECT id FROM usuarios WHERE email = 'fac-design@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Moda' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'fac-design@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Interiores' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'luizduarte@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Design de Moda' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'lorenasousa@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'robsonlustosa@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'denisesantos@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Gastronomia' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'andressabrito@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Estética e Cosmética' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'andressabrito@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Enfermagem' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'ameliaramiranda@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'ameliaramiranda@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Jogos Digitais' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'overcausil@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Logística' LIMIT 1)),
((SELECT id FROM usuarios WHERE email = 'brunonovaes@pe.senac.br'), (SELECT id FROM cursos WHERE nome = 'Enfermagem' LIMIT 1))
ON CONFLICT DO NOTHING;