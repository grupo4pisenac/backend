-- ─────────────────────────────────────────
-- INSERÇÃO DE COORDENADORES
-- ─────────────────────────────────────────
INSERT INTO usuarios (nome, email, senha, perfil, semestre_atual)
SELECT * FROM (VALUES
                   ('Daniela Vasconcelos de Oliveira', 'fac-design@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Luiz Clério Duarte Júnior', 'luizduarte@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Lorena Bezerra de Sousa', 'lorenasousa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Robson Luis Trindade Lustosa', 'robsonlustosa@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Denise Lins Farias dos Santos', 'denisesantos@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Andressa Mendonça da Costa Brito', 'andressabrito@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Ameliara Freire Santos de Miranda', 'ameliaramiranda@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Over Manuel Montes Causil', 'overcausil@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1),
                   ('Bruno Felipe Novaes de Souza', 'brunonovaes@pe.senac.br', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p84PolGQ3Md5b7NJ.MtxMi', 'COORDENADOR'::varchar, 1)
              ) AS v(nome, email, senha, perfil, semestre_atual)
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = v.email);

-- ─────────────────────────────────────────
-- INSERÇÃO DE CURSOS
-- ─────────────────────────────────────────
INSERT INTO cursos (nome, total_semestres)
SELECT * FROM (VALUES
                   ('Design de Moda', 5),
                   ('Gastronomia', 5),
                   ('Estética e Cosmética', 5),
                   ('Análise e Desenvolvimento de Sistemas', 5),
                   ('Design de Interiores', 5),
                   ('Logística', 4),
                   ('Jogos Digitais', 5),
                   ('Enfermagem', 10)
              ) AS v(nome, total_semestres)
WHERE NOT EXISTS (SELECT 1 FROM cursos WHERE nome = v.nome);

-- ─────────────────────────────────────────
-- ASSOCIAÇÃO COORDENADOR <-> CURSO
-- ─────────────────────────────────────────
INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'fac-design@pe.senac.br' AND c.nome = 'Design de Moda'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'fac-design@pe.senac.br' AND c.nome = 'Design de Interiores'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'luizduarte@pe.senac.br' AND c.nome = 'Design de Moda'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'lorenasousa@pe.senac.br' AND c.nome = 'Gastronomia'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'robsonlustosa@pe.senac.br' AND c.nome = 'Gastronomia'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'denisesantos@pe.senac.br' AND c.nome = 'Gastronomia'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'andressabrito@pe.senac.br' AND c.nome = 'Estética e Cosmética'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'andressabrito@pe.senac.br' AND c.nome = 'Enfermagem'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'ameliaramiranda@pe.senac.br' AND c.nome = 'Análise e Desenvolvimento de Sistemas'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'ameliaramiranda@pe.senac.br' AND c.nome = 'Jogos Digitais'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'overcausil@pe.senac.br' AND c.nome = 'Logística'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);

INSERT INTO usuario_curso (usuario_id, curso_id)
SELECT u.id, c.id FROM usuarios u, cursos c
WHERE u.email = 'brunonovaes@pe.senac.br' AND c.nome = 'Enfermagem'
  AND NOT EXISTS (SELECT 1 FROM usuario_curso WHERE usuario_id = u.id AND curso_id = c.id);