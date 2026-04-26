# Backend — Sistema de Atividades Complementares

API REST desenvolvida em Java 25 com Spring Boot 4, responsável por gerenciar as atividades complementares de alunos, coordenadores e administradores.

---

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Java 25](https://jdk.java.net/25/)
- [Maven](https://maven.apache.org/) (ou use o `mvnw` incluído no projeto)
- [PostgreSQL](https://www.postgresql.org/download/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) ou qualquer IDE de sua preferência
- [Insomnia](https://insomnia.rest/) ou [Postman](https://www.postman.com/) para testar os endpoints

---

## Configuração do banco de dados

1. Instale o PostgreSQL e certifique-se de que ele está rodando localmente na porta `5432`
2. Crie o banco de dados:

```sql
CREATE DATABASE senac_pi_4;
```

3. Insira um usuário Super Admin inicial:

```sql
INSERT INTO usuarios (nome, email, senha, perfil)
VALUES (
    'Admin Teste',
    'admin@teste.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LQTOl8BKQQ2',
    'SUPER_ADMIN'
);
```

> A senha acima corresponde a `password` criptografada com BCrypt.

---

## Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (mesma pasta do `pom.xml`) com o seguinte conteúdo:

```
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres
JWT_SECRET=senac-pi-4-chave-secreta-jwt-2026-abc123
```

> O arquivo `.env` já está no `.gitignore` e nunca será enviado ao repositório.

---

## Como rodar o projeto

### Pelo IntelliJ

1. Abra o projeto no IntelliJ IDEA
2. Aguarde o Maven baixar as dependências automaticamente
3. Localize a classe `BackendApplication.java` em `src/main/java/br/edu/senac/backend/`
4. Clique no botão **Run** (▶) ou pressione **Shift+F10**
5. Aguarde a mensagem `Started BackendApplication` no console

### Pelo terminal

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## Testando os endpoints

> Em todas as requisições autenticadas adicione o header:
> `Authorization: Bearer seu_token_aqui`
>
> No Insomnia vá em **Auth → Bearer Token** e cole o token.

---

### Autenticação

#### POST /auth/login
```json
{
    "email": "admin@teste.com",
    "senha": "password"
}
```
Retorna o token JWT e o perfil do usuário.

---

### Cursos — requer perfil SUPER_ADMIN ou COORDENADOR

#### POST /cursos
```json
{
    "nome": "Análise e Desenvolvimento de Sistemas"
}
```

#### GET /cursos
Retorna a lista de todos os cursos.

#### GET /cursos/{id}
Retorna um curso pelo ID.

#### PUT /cursos/{id}
```json
{
    "nome": "Novo Nome do Curso"
}
```

#### DELETE /cursos/{id}
Remove o curso pelo ID.

---

### Usuários — requer perfil SUPER_ADMIN

#### POST /usuarios
```json
{
    "nome": "Nome do Usuário",
    "email": "email@exemplo.com",
    "senha": "senha123",
    "perfil": "COORDENADOR",
    "cursoIds": [1]
}
```
> Perfis disponíveis: `SUPER_ADMIN`, `COORDENADOR`, `ALUNO`

#### GET /usuarios
Retorna a lista de todos os usuários.

#### GET /usuarios/{id}
Retorna um usuário pelo ID.

#### PUT /usuarios/{id}
```json
{
    "nome": "Nome Atualizado",
    "email": "email@exemplo.com",
    "senha": "",
    "perfil": "COORDENADOR",
    "cursoIds": [1, 2]
}
```
> Deixe `senha` vazia para não alterar a senha atual.

#### DELETE /usuarios/{id}
Remove o usuário pelo ID.

---

### Regras de Atividade — requer perfil SUPER_ADMIN

#### POST /cursos/{cursoId}/regras
```json
{
    "area": "Extensão",
    "limiteHoras": 60
}
```

#### GET /cursos/{cursoId}/regras
Retorna todas as regras do curso.

#### PUT /cursos/{cursoId}/regras/{regraId}
```json
{
    "area": "Extensão",
    "limiteHoras": 80
}
```

#### DELETE /cursos/{cursoId}/regras/{regraId}
Remove a regra pelo ID.

---

### Solicitações

#### POST /solicitacoes/aluno/{alunoId} — requer perfil ALUNO
```json
{
    "descricao": "Participação em evento de tecnologia",
    "area": "Extensão",
    "horasSolicitadas": 10,
    "cursoId": 1
}
```

#### GET /solicitacoes/aluno/{alunoId} — requer perfil ALUNO, COORDENADOR ou SUPER_ADMIN
Retorna todas as solicitações do aluno.

#### GET /solicitacoes/curso/{cursoId} — requer perfil COORDENADOR ou SUPER_ADMIN
Retorna todas as solicitações do curso.

#### PATCH /solicitacoes/{id}/status?status=APROVADA — requer perfil COORDENADOR ou SUPER_ADMIN
> Status disponíveis: `PENDENTE`, `APROVADA`, `REPROVADA`

---

## Estrutura do projeto

```
src/main/java/br/edu/senac/backend/
├── controller/        # Endpoints REST
├── service/           # Regras de negócio
├── repository/        # Interfaces JPA
├── model/             # Entidades do banco
├── dto/               # Objetos de entrada e saída das APIs
├── security/          # JWT, filtros e configuração do Spring Security
└── exception/         # Tratamento global de erros
```

---

## Tecnologias utilizadas

- Java 25
- Spring Boot 4
- Spring Security + JWT (JJWT 0.12.3)
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway (migrations)
- Lombok
- Bean Validation
