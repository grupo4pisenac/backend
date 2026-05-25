# Backend — Sistema de Atividades Complementares

API REST desenvolvida em Java 21 com Spring Boot 4, responsável por gerenciar as atividades complementares de alunos, coordenadores e administradores.

---

## 🚀 Deploy

A API está disponível em produção no Render:

```
https://backend-5v4v.onrender.com
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

3. Suba o projeto uma vez — o Flyway criará as tabelas e populará automaticamente com os dados do seed (migrations V1 a V12)

4. O seed já inclui um super admin com as seguintes credenciais:

```json
{
  "email": "admin@senac.com",
  "senha": "password"
}
```

---

## Configuração de e-mail (Mailtrap)

O projeto usa o SMTP do [Mailtrap](https://mailtrap.io) para envio de e-mails.

1. Crie uma conta gratuita em [mailtrap.io](https://mailtrap.io)
2. Acesse **Sandboxes → My Sandbox → Integration → SMTP**
3. Copie o **Username** e o **Password**
4. Adicione as credenciais no arquivo `.env` conforme instruções abaixo

> ⚠️ O e-mail remetente está configurado diretamente no código em `EmailService.java`. Altere o campo `FROM_EMAIL` se necessário.

---

## Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (mesma pasta do `pom.xml`) com o seguinte conteúdo:

```
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres
JWT_SECRET=sua_chave_secreta_com_minimo_32_caracteres
MAIL_USERNAME=seu_username_mailtrap
MAIL_PASSWORD=sua_senha_mailtrap
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/senac_pi_4
```

> ⚠️ O arquivo `.env` está no `.gitignore` e **nunca deve ser commitado**. Ele contém credenciais sensíveis.

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

## Documentação interativa — Swagger UI

O projeto possui documentação interativa gerada automaticamente via SpringDoc OpenAPI.

Acesse após subir o projeto:

```
http://localhost:8080/swagger-ui/index.html
```

Em produção:

```
https://backend-5v4v.onrender.com/swagger-ui/index.html
```

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
> `https://backend-5v4v.onrender.com`

---

### Autenticação

#### POST /auth/login
```json
{
    "email": "email@exemplo.com",
    "senha": "senha"
}
```
Retorna o token JWT, o perfil e o nome do usuário.

---

### Cursos

#### POST /cursos — SUPER_ADMIN
```json
{
    "nome": "Engenharia de Software",
    "coordenadorId": 2,
    "totalSemestres": 4
}
```

#### GET /cursos — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna a lista de todos os cursos com coordenador e áreas.

#### GET /cursos/{id} — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna um curso pelo ID.

#### PUT /cursos/{id} — SUPER_ADMIN
```json
{
    "nome": "Novo nome do curso",
    "coordenadorId": 3,
    "totalSemestres": 4
}
```

#### DELETE /cursos/{id} — SUPER_ADMIN

Remove o curso pelo ID.

> ⚠️ **Atenção:** ao deletar um curso, todas as regras de atividade e solicitações vinculadas serão removidas permanentemente em cascata.

---

### Usuários

#### POST /usuarios — SUPER_ADMIN, COORDENADOR
```json
{
    "nome": "Nome do usuário",
    "email": "email@exemplo.com",
    "senha": "senha",
    "perfil": "ALUNO",
    "cursoIds": [1],
    "semestreAtual": 1
}
```
> Perfis disponíveis: `SUPER_ADMIN`, `COORDENADOR`, `ALUNO`

#### GET /usuarios — SUPER_ADMIN, COORDENADOR
Retorna todos os usuários com cursos vinculados e semestre atual.

#### GET /usuarios/alunos — SUPER_ADMIN, COORDENADOR
Retorna apenas os usuários com perfil `ALUNO`.

#### GET /usuarios/coordenadores — SUPER_ADMIN
Retorna apenas os usuários com perfil `COORDENADOR`.

#### GET /usuarios/{id} — SUPER_ADMIN, COORDENADOR
Retorna um usuário pelo ID.

#### PUT /usuarios/{id} — SUPER_ADMIN
```json
{
    "nome": "Nome atualizado",
    "email": "email@exemplo.com",
    "senha": "",
    "perfil": "ALUNO",
    "cursoIds": [1],
    "semestreAtual": 2
}
```
> Deixe `senha` vazia para não alterar a senha atual.

#### DELETE /usuarios/{id} — SUPER_ADMIN

Remove o usuário pelo ID.

> ⚠️ **Atenção:** ao deletar um aluno, todas as solicitações vinculadas a ele serão removidas permanentemente em cascata.

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
    "limiteHoras": 60,
    "limiteSemestral": 20
}
```

#### GET /cursos/{cursoId}/regras — SUPER_ADMIN, COORDENADOR, ALUNO
Retorna todas as regras do curso com `limiteHoras` e `limiteSemestral`.

#### PUT /cursos/{cursoId}/regras/{regraId} — SUPER_ADMIN
```json
{
    "area": "Extensão",
    "limiteHoras": 80,
    "limiteSemestral": 25
}
```

#### DELETE /cursos/{cursoId}/regras/{regraId} — SUPER_ADMIN
Remove a regra pelo ID diretamente, sem necessidade de desacoplamento.

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
> O semestre é preenchido automaticamente com o `semestreAtual` do aluno.
> Ao criar uma solicitação, o sistema envia automaticamente um e-mail para os coordenadores do curso.

#### GET /solicitacoes/aluno/{alunoId} — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna todas as solicitações do aluno incluindo `urlArquivo` e `semestre`.

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
> Ao aprovar, o sistema valida o limite semestral da área. Se ultrapassado, as horas são ajustadas automaticamente e um e-mail é enviado ao aluno.
> O aluno também recebe um e-mail informando o resultado independente do status.

#### DELETE /solicitacoes/{id} — SUPER_ADMIN
Remove uma solicitação pelo ID.

---

### Dashboard

#### GET /dashboard/meus-cursos — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna a lista de cursos disponíveis para o usuário autenticado (usado para montar o dropdown).

#### GET /dashboard/meu/curso/{cursoId} — ALUNO
Retorna o dashboard do próprio aluno no curso selecionado.

#### GET /dashboard/coordenador/curso/{cursoId} — COORDENADOR
Retorna o dashboard de todos os alunos do curso.

#### GET /dashboard/admin/curso/{cursoId} — SUPER_ADMIN
Retorna o dashboard de todos os alunos do curso.

#### GET /dashboard/aluno/{alunoId}/curso/{cursoId} — ALUNO, COORDENADOR, SUPER_ADMIN
Retorna o progresso do aluno no curso com horas aprovadas por área, incluindo progresso semestral.

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
            "concluido": false,
            "horasAprovadasSemestre": 10,
            "limiteSemestral": 20,
            "concluidoSemestre": false
        }
    ],
    "totalHorasAprovadas": 20,
    "totalHorasExigidas": 130
}
```

> `concluido` indica se o aluno atingiu o limite total da área no curso.
> `concluidoSemestre` indica se o aluno atingiu o limite semestral da área.

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
- Spring Security + JWT (JJWT 0.12.6)
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway (migrations + seed)
- Lombok
- Bean Validation
- SpringDoc OpenAPI (Swagger UI)
- Mailtrap SMTP (envio de e-mails)
- Render (deploy em produção)