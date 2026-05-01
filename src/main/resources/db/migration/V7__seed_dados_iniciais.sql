-- ========================
-- SUPER ADMIN (senha: password)
-- ========================
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Admin', 'admin@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'SUPER_ADMIN');

-- ========================
-- CURSOS
-- ========================
INSERT INTO cursos (nome) VALUES ('Análise e Desenvolvimento de Sistemas');
INSERT INTO cursos (nome) VALUES ('Gestão de Tecnologia da Informação');
INSERT INTO cursos (nome) VALUES ('Redes de Computadores');
INSERT INTO cursos (nome) VALUES ('Ciência de Dados');
INSERT INTO cursos (nome) VALUES ('Segurança da Informação');

-- ========================
-- COORDENADORES (senha: password)
-- ========================
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Carlos Mendes', 'carlos.mendes@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'COORDENADOR');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Fernanda Lima', 'fernanda.lima@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'COORDENADOR');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Ricardo Souza', 'ricardo.souza@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'COORDENADOR');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Patricia Oliveira', 'patricia.oliveira@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'COORDENADOR');

-- ========================
-- ALUNOS (senha: password)
-- ========================
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Ana Paula Silva', 'ana.paula@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Bruno Costa', 'bruno.costa@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Camila Rocha', 'camila.rocha@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Diego Ferreira', 'diego.ferreira@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Eduarda Martins', 'eduarda.martins@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Felipe Santos', 'felipe.santos@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Gabriela Alves', 'gabriela.alves@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Henrique Nunes', 'henrique.nunes@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Isabella Pereira', 'isabella.pereira@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('João Vitor Lima', 'joao.vitor@senac.com', '$2b$10$gNL47.kCG6sW1TOGFvBlue4KEaKn7t4yvl8BI2gcYftCXybC98Ceq', 'ALUNO');

-- ========================
-- ASSOCIAÇÃO COORDENADOR -> CURSO
-- Carlos Mendes -> ADS
-- Fernanda Lima -> GTI
-- Ricardo Souza -> Redes e Ciência de Dados (coordenador de 2 cursos)
-- Patricia Oliveira -> Segurança
-- ========================
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'carlos.mendes@senac.com'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'fernanda.lima@senac.com'), (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'ricardo.souza@senac.com'), (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'ricardo.souza@senac.com'), (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'patricia.oliveira@senac.com'), (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

-- ========================
-- ASSOCIAÇÃO ALUNO -> CURSO
-- 2 alunos por curso
-- João Vitor matriculado em ADS e Segurança (2 cursos simultâneos)
-- ========================

-- ADS
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'ana.paula@senac.com'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'bruno.costa@senac.com'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

-- GTI
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'camila.rocha@senac.com'), (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'diego.ferreira@senac.com'), (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

-- Redes
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'eduarda.martins@senac.com'), (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'felipe.santos@senac.com'), (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

-- Ciência de Dados
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'gabriela.alves@senac.com'), (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'henrique.nunes@senac.com'), (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

-- Segurança da Informação
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'isabella.pereira@senac.com'), (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'joao.vitor@senac.com'), (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

-- João Vitor também matriculado em ADS (aluno em 2 cursos simultâneos)
INSERT INTO usuario_curso (usuario_id, curso_id) VALUES
((SELECT id FROM usuarios WHERE email = 'joao.vitor@senac.com'), (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

-- ========================
-- REGRAS DE ATIVIDADE POR CURSO E CATEGORIA
-- ========================

-- ADS
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Extensão', 60, (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Pesquisa', 40, (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Ensino', 30, (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Cultura', 20, (SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

-- GTI
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Extensão', 50, (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Pesquisa', 40, (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Ensino', 30, (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Cultura', 20, (SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

-- Redes
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Extensão', 60, (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Pesquisa', 30, (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Ensino', 30, (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Cultura', 20, (SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

-- Ciência de Dados
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Extensão', 50, (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Pesquisa', 50, (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Ensino', 30, (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Cultura', 20, (SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

-- Segurança da Informação
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Extensão', 60, (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Pesquisa', 40, (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Ensino', 20, (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));
INSERT INTO regras_atividade (area, limite_horas, curso_id) VALUES ('Cultura', 20, (SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

-- ========================
-- SOLICITAÇÕES E CERTIFICADOS
-- ========================

-- Ana Paula Silva - ADS
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Participação no evento TechDay 2024', 'Extensão', 20, '2024-03-10 09:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'ana.paula@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_ana_techday.jpg', 'https://i.imgur.com/5ns4S5y.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Participação no evento TechDay 2024'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Monitoria em Algoritmos', 'Ensino', 15, '2024-04-05 10:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'ana.paula@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_ana_monitoria.jpg', 'https://i.imgur.com/M3ltIRd.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Monitoria em Algoritmos'));

-- Bruno Costa - ADS
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Artigo publicado na revista acadêmica', 'Pesquisa', 30, '2024-02-15 14:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'bruno.costa@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_bruno_artigo.jpg', 'https://i.imgur.com/JIUIpHK.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Artigo publicado na revista acadêmica'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Participação em hackathon universitário', 'Extensão', 10, '2024-05-20 08:00:00', 'REPROVADA',
(SELECT id FROM usuarios WHERE email = 'bruno.costa@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_bruno_hackathon.jpg', 'https://i.imgur.com/HXbqRZq.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Participação em hackathon universitário'));

-- Camila Rocha - GTI
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Workshop de Gestão Ágil', 'Extensão', 16, '2024-03-22 09:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'camila.rocha@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_camila_workshop.jpg', 'https://i.imgur.com/tGuTLUR.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Workshop de Gestão Ágil'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Curso online de ITIL Foundation', 'Ensino', 20, '2024-06-01 11:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'camila.rocha@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_camila_itil.jpg', 'https://i.imgur.com/9dU3zZM.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Curso online de ITIL Foundation'));

-- Diego Ferreira - GTI
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Visita técnica à empresa de TI', 'Extensão', 8, '2024-04-10 13:00:00', 'REPROVADA',
(SELECT id FROM usuarios WHERE email = 'diego.ferreira@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_diego_visita.jpg', 'https://i.imgur.com/ZtH21Cc.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Visita técnica à empresa de TI'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Pesquisa sobre cloud computing', 'Pesquisa', 25, '2024-05-15 10:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'diego.ferreira@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Gestão de Tecnologia da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_diego_cloud.jpg', 'https://i.imgur.com/mFBzvc1.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Pesquisa sobre cloud computing'));

-- Eduarda Martins - Redes
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Certificação CompTIA Network+', 'Extensão', 40, '2024-02-20 09:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'eduarda.martins@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_eduarda_comptia.jpg', 'https://i.imgur.com/PRa6Ogu.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Certificação CompTIA Network+'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Palestra sobre infraestrutura de redes', 'Ensino', 10, '2024-06-10 15:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'eduarda.martins@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_eduarda_palestra.jpg', 'https://i.imgur.com/1AZ9CGH.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Palestra sobre infraestrutura de redes'));

-- Felipe Santos - Redes
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Projeto de extensão em escola pública', 'Extensão', 20, '2024-03-05 08:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'felipe.santos@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_felipe_extensao.jpg', 'https://i.imgur.com/ZjIfSNq.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Projeto de extensão em escola pública'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Iniciação científica em protocolos', 'Pesquisa', 15, '2024-05-25 09:00:00', 'REPROVADA',
(SELECT id FROM usuarios WHERE email = 'felipe.santos@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Redes de Computadores'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_felipe_ic.jpg', 'https://i.imgur.com/dNxKPiO.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Iniciação científica em protocolos'));

-- Gabriela Alves - Ciência de Dados
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Kaggle competition participation', 'Pesquisa', 30, '2024-04-01 10:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'gabriela.alves@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_gabriela_kaggle.jpg', 'https://i.imgur.com/MD7zuN6.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Kaggle competition participation'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Curso de Machine Learning aplicado', 'Extensão', 20, '2024-06-05 14:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'gabriela.alves@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_gabriela_ml.jpg', 'https://i.imgur.com/BNidwNf.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Curso de Machine Learning aplicado'));

-- Henrique Nunes - Ciência de Dados
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Apresentação em conferência de dados', 'Extensão', 10, '2024-03-18 11:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'henrique.nunes@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_henrique_conferencia.jpg', 'https://i.imgur.com/t8vDhCd.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Apresentação em conferência de dados'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Tutoria em estatística para colegas', 'Ensino', 12, '2024-05-30 09:00:00', 'REPROVADA',
(SELECT id FROM usuarios WHERE email = 'henrique.nunes@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Ciência de Dados'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_henrique_tutoria.jpg', 'https://i.imgur.com/dL0eMLX.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Tutoria em estatística para colegas'));

-- Isabella Pereira - Segurança
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('CTF competition - Capture The Flag', 'Extensão', 20, '2024-04-15 10:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'isabella.pereira@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_isabella_ctf.jpg', 'https://i.imgur.com/YThpjzV.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'CTF competition - Capture The Flag'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Pesquisa sobre criptografia moderna', 'Pesquisa', 25, '2024-06-08 13:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'isabella.pereira@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_isabella_cripto.jpg', 'https://i.imgur.com/uCcsjG3.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Pesquisa sobre criptografia moderna'));

-- João Vitor Lima - Segurança e ADS (2 cursos)
INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Palestra sobre ethical hacking', 'Extensão', 15, '2024-03-28 09:00:00', 'APROVADA',
(SELECT id FROM usuarios WHERE email = 'joao.vitor@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Segurança da Informação'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_joao_hacking.jpg', 'https://i.imgur.com/DQQCiwY.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Palestra sobre ethical hacking'));

INSERT INTO solicitacoes (descricao, area, horas_solicitadas, data_criacao, status, aluno_id, curso_id) VALUES
('Desenvolvimento de app mobile', 'Extensão', 20, '2024-05-10 10:00:00', 'PENDENTE',
(SELECT id FROM usuarios WHERE email = 'joao.vitor@senac.com'),
(SELECT id FROM cursos WHERE nome = 'Análise e Desenvolvimento de Sistemas'));

INSERT INTO certificados (nome_arquivo, url_arquivo, tipo_arquivo, solicitacao_id) VALUES
('certificado_joao_mobile.jpg', 'https://i.imgur.com/85pOYuO.png', 'IMAGEM',
(SELECT id FROM solicitacoes WHERE descricao = 'Desenvolvimento de app mobile'));