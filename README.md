# Backend — Sistema de Atividades Complementares

API REST desenvolvida em Java 21 com Spring Boot 4, responsável por gerenciar as atividades complementares de alunos, coordenadores e administradores.

---

## 🚀 Deploy

A API está disponível em produção no Railway:

```
https://backend-production-a784.up.railway.app
```

---

## Pré-requisitos

Antes de rodar o projeto localmente, certifique-se de ter instalado:

- [Java 21](https://jdk.java.net/21/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) ou qualquer IDE de sua preferência
- Caso já não venha com o IntelliJ: [Maven](https://maven.apache.org/) (ou use o `mvnw` incluído no projeto)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Insomnia](https://insomnia.rest/) ou [Postman](https://www.postman.com/) para testar os endpoints

---

## Configuração do banco de dados

1. Instale o PostgreSQL e certifique-se de que ele está rodando localmente na porta `5432`
2. Crie o banco de dados:

```sql
CREATE DATABASE senac_pi_4;
```

3. Suba o projeto uma vez — o Flyway criará as tabelas e populará automaticamente com os dados do seed (migrations V1 a V7)

4. O seed já inclui um super admin com as seguintes credenciais:

```json
{
    "email": "admin@senac.com",
    "senha": "password"
}
```

> Caso precise inserir o admin manualmente, use o hash abaixo (válido para a senha `password`):
> ```sql
> INSERT INTO usuarios (nome, email, senha, perfil)
> VALUES (
>     'Admin',
>     'admin@senac.com',
>     '$2b$10$vSqYzehzYDJ99xjLYjYP5ehg3/k4r/OeBRmOZzISEVfauAeDPdzIe',
>     'SUPER_ADMIN'
> );
> ```

---

## Configuração do Mailtrap (e-mail para desenvolvimento)

O projeto usa o [Mailtrap](https://mailtrap.io) para simular o envio de e-mails em desenvolvimento. Nenhum e-mail real é enviado.

1. Crie uma conta gratuita em [mailtrap.io](https://mailtrap.io)
2. Acesse **Sandboxes → My Sandbox → SMTP Settings**
3. Copie o **Username** e o **Password** exibidos
4. Adicione essas credenciais no arquivo `.env` conforme instruções abaixo

---

## Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (mesma pasta do `pom.xml`) com o seguinte conteúdo:

```
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres
JWT_SECRET=sua_chave_secreta_com_minimo_32_caracteres
MAIL_USERNAME=seu_username_mailtrap
MAIL_PASSWORD=sua_password_mailtrap
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

## Perfis de acesso

| Perfil | Descrição |
|---|---|
| `SUPER_ADMIN` | Acesso total ao sistema |
| `COORDENADOR` | Gerencia alunos e solicitações dos seus cursos |
| `ALUNO` | Acessa apenas seus próprios dados e solicitações |

> Um aluno autenticado só consegue visualizar e criar dados relacionados ao seu próprio ID.

---

## Testando os endpoints

> Em todas as requisições autenticadas adicione o header:
> `Authorization: Bearer seu_token_aqui`
>
> No Insomnia vá em **Auth → Bearer Token** e cole o token.
>
> Substitua `http://localhost:8080` pelo link de produção quando necessário:
> `https://backend-production-a784.up.railway.app`

---

### Autenticação

#### POST /auth/login
```json
{
    "email": "admin@senac.com",
    "senha": "password"
}
```
Retorna o token JWT e o perfil do usuário.

---

### Cursos — requer perfil SUPER_ADMIN, COORDENADOR ou ALUNO (somente leitura para ALUNO)

#### POST /cursos — SUPER_ADMIN
```json
{
    "nome": "Engenharia de Software"
}
```

#### GET /cursos — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna a lista de todos os cursos.

#### GET /cursos/{id} — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna um curso pelo ID.

#### PUT /cursos/{id} — SUPER_ADMIN
```json
{
    "nome": "Novo Nome do Curso"
}
```

#### DELETE /cursos/{id} — SUPER_ADMIN
Remove o curso pelo ID.

---

### Usuários — requer perfil SUPER_ADMIN ou COORDENADOR

#### POST /usuarios — SUPER_ADMIN, COORDENADOR
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

#### GET /usuarios — SUPER_ADMIN, COORDENADOR
Retorna a lista de todos os usuários incluindo os cursos vinculados.

#### GET /usuarios/alunos — SUPER_ADMIN, COORDENADOR
Retorna apenas os usuários com perfil `ALUNO`.

#### GET /usuarios/coordenadores — SUPER_ADMIN
Retorna apenas os usuários com perfil `COORDENADOR`.

#### GET /usuarios/{id} — SUPER_ADMIN, COORDENADOR
Retorna um usuário pelo ID incluindo os cursos vinculados.

#### PUT /usuarios/{id} — SUPER_ADMIN
Edição geral de qualquer usuário (coordenadores, admin).
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

#### PUT /usuarios/alunos/{id} — SUPER_ADMIN
Edição específica de um aluno pelo ID.
```json
{
    "nome": "Nome Atualizado",
    "email": "email@exemplo.com",
    "senha": "",
    "perfil": "ALUNO",
    "cursoIds": [1]
}
```
> Deixe `senha` vazia para não alterar a senha atual.

#### DELETE /usuarios/{id} — SUPER_ADMIN
Remove o usuário pelo ID.

#### POST /usuarios/{usuarioId}/cursos/{cursoId} — SUPER_ADMIN
Associa um usuário a um curso.

#### DELETE /usuarios/{usuarioId}/cursos/{cursoId} — SUPER_ADMIN
Desassocia um usuário de um curso.

---

### Regras de Atividade

#### POST /cursos/{cursoId}/regras — SUPER_ADMIN
```json
{
    "area": "Extensão",
    "limiteHoras": 60
}
```

#### GET /cursos/{cursoId}/regras — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna todas as regras do curso.

#### PUT /cursos/{cursoId}/regras/{regraId} — SUPER_ADMIN
```json
{
    "area": "Extensão",
    "limiteHoras": 80
}
```

#### DELETE /cursos/{cursoId}/regras/{regraId} — SUPER_ADMIN
Remove a regra pelo ID.

---

### Solicitações

#### POST /solicitacoes/aluno/{alunoId} — ALUNO
```json
{
    "descricao": "Participação em evento de tecnologia",
    "area": "Extensão",
    "horasSolicitadas": 10,
    "cursoId": 1,
    "urlCertificado": "https://i.imgur.com/exemplo.jpg"
}
```
> O campo `urlCertificado` deve conter o link público da imagem do certificado (ex: Imgur).
> Ao criar uma solicitação, o sistema envia automaticamente um e-mail para os coordenadores do curso.

#### GET /solicitacoes/aluno/{alunoId} — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna todas as solicitações do aluno incluindo a `urlArquivo` do certificado.

#### GET /solicitacoes/aluno/{alunoId}/curso/{cursoId} — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna todas as solicitações do aluno em um curso específico.

#### GET /solicitacoes/aluno/{alunoId}/filtro?status=PENDENTE — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna as solicitações do aluno filtradas por status.

#### GET /solicitacoes/curso/{cursoId} — COORDENADOR, SUPER_ADMIN
Retorna todas as solicitações do curso.

#### GET /solicitacoes/curso/{cursoId}/filtro?status=PENDENTE — COORDENADOR, SUPER_ADMIN
Retorna as solicitações do curso filtradas por status.

#### PATCH /solicitacoes/{id}/status?status=APROVADA — COORDENADOR, SUPER_ADMIN
> Status disponíveis: `PENDENTE`, `APROVADA`, `REPROVADA`
>
> Ao atualizar o status, o sistema envia automaticamente um e-mail para o aluno informando o resultado.

---

### Dashboard — ALUNO, COORDENADOR, SUPER_ADMIN

#### GET /dashboard/aluno/{alunoId}/curso/{cursoId}

Retorna o progresso do aluno no curso com horas aprovadas por área.

**Resposta esperada:**
```json
{
    "nomeAluno": "Ana Paula Silva",
    "nomeCurso": "Análise e Desenvolvimento de Sistemas",
    "areas": [
        {
            "area": "Extensão",
            "horasAprovadas": 20,
            "limiteHoras": 60,
            "concluido": false
        },
        {
            "area": "Pesquisa",
            "horasAprovadas": 15,
            "limiteHoras": 40,
            "concluido": false
        },
        {
            "area": "Ensino",
            "horasAprovadas": 10,
            "limiteHoras": 30,
            "concluido": false
        }
    ],
    "totalHorasAprovadas": 45,
    "totalHorasExigidas": 130
}
```

> O campo `concluido` será `true` quando o aluno atingir o limite de horas da área.

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

- Java 21
- Spring Boot 4
- Spring Security + JWT (JJWT 0.12.3)
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway (migrations + seed)
- Lombok
- Bean Validation
- Mailtrap (e-mail sandbox para desenvolvimento)
- Railway (deploy em produção)
