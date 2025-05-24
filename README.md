# API da Plataforma de Cursos

Esta é uma API backend para uma plataforma educacional. Com ela, é possível cadastrar e inativar cursos, realizar matrículas de alunos e gerar relatórios dos cursos mais acessados. O sistema foi desenvolvido com Java e Spring Boot, seguindo os princípios de arquitetura RESTful.

---

## 🧰 Tecnologias Utilizadas

- Java 18+
- Spring Boot
- Spring Data JPA
- MySQL
- Flyway (migrações manuais de banco de dados)

---

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/plataforma-cursos-api.git
```
### 2. Configure o banco de dados
Crie um banco de dados MySQL e atualize as configurações no arquivo application.properties ou application.yml.

3. Execute o projeto
```bash
./mvnw spring-boot:run
```

---

## 📚 Funcionalidades
### 📌 Cadastro de Cursos
Atributos do curso:
 - Nome
 - Código (único, de 4 a 10 caracteres, letras minúsculas e hífen)
 - Instrutor
 - Descrição
 - Status (ACTIVE, INACTIVE)
 - Data de inativação (preenchida apenas se o curso for inativado)

Regras:
- Apenas usuários do tipo instrutor podem criar cursos.
- Todo curso novo é criado com status ACTIVE.
- A data de inativação é registrada apenas ao inativar o curso.

📍 Endpoint: POST /course/new

### 🛑 Inativação de Cursos
- Permite inativar cursos existentes pelo código.
- O status do curso é alterado para INACTIVE e a data de inativação é registrada com o horário atual.

📍 Endpoint: PATCH /course/{code}/inactive

### 🎓 Matrícula de Alunos
- Permite que alunos se matriculem em cursos ativos.
- Garante que um aluno não se matricule mais de uma vez no mesmo curso.
- Impede matrícula em cursos inativos.

📍 Endpoint: POST /registration

### 📊 Relatório de Cursos Mais Acessados
- Gera um relatório dos cursos com mais matrículas.
- Os dados são ordenados pela quantidade de inscrições.
- Implementado com SQL nativo para melhor desempenho em grandes volumes de dados.

📍 Endpoint: GET /registration/report
