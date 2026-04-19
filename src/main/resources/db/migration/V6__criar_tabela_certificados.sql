CREATE TABLE certificados (
                              id BIGSERIAL PRIMARY KEY,
                              nome_arquivo VARCHAR(255) NOT NULL,
                              url_arquivo VARCHAR(255) NOT NULL,
                              tipo_arquivo VARCHAR(50) NOT NULL,
                              solicitacao_id BIGINT NOT NULL REFERENCES solicitacoes(id)
);