# 📞 API de Logins em Ramais

Este projeto é uma API desenvolvida com **Spring Boot** que gerencia logins de usuários em ramais. A API permite que você controle quais usuários estão conectados a quais ramais de forma simples e eficiente.

## 🚀 Como rodar o projeto

1. **Clonar o repositório**

   ```bash
   git clone https://github.com/rawRocha/apiLoginsEmRamais.git
   cd apiLoginsEmRamais
   ```

2. **Configurar o banco de dados**

   - Crie uma nova base de dados chamada `extensions`.
   - Certifique-se de que você tem a estrutura de banco de dados necessária. Você pode encontrar o arquivo SQL que cria o schema na raiz do projeto.

3. **Configurar o arquivo `application.properties`**

   - No projeto, vá para o arquivo `src/main/resources/application.properties`.
   - Configure a conexão com o banco de dados da seguinte maneira:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/extensions
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

   - Substitua `seu_usuario` e `sua_senha` pelos dados corretos da sua base de dados.

4. **Dependências**

   - O projeto utiliza o **MySQL/MariaDB** como banco de dados. Certifique-se de que o MySQL está instalado e rodando na sua máquina.
   - Se necessário, adicione a dependência do MySQL no arquivo `pom.xml`:

   ```xml
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
   </dependency>
   ```

5. **Rodando o projeto**

   - Com tudo configurado, execute o seguinte comando para rodar o projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

   - Se estiver no windows pode executar o arquivo start-dev.bat

6. **Acessando a API**
   - A API estará rodando na URL `http://localhost:8080`.
   - Você pode testar os endpoints com ferramentas como **Postman** ou **cURL** ou **Insomnia**.

## 📝 Endpoints Disponíveis

- **Login em Ramal**

  - `POST /extensions/login`
  - Corpo:
    ```json
    {
      "username": "usuario",
      "password": "senha",
      "extensionNumber": 101
    }
    ```

- **Logout do Ramal**

  - `DELETE /extensions/logout`
  - Corpo:

    ```json
    {
      "username": "usuario",
      "password": "senha",
      "extensionNumber": 101
    }
    ```

  - **lista dos Ramais disponíveis**

  - `GET /extensions/available`

  - **lista dos Ramais ocupados**

  - `GET /extensions/unvailable`

  - **Criar um Ramal**

  - `POST /extensions/create`
  - Corpo:

    ```json
    {
      "extensionNumber": 101
    }
    ```

  - **Criar uma range de Ramais**

  - `POST /extensions/create-range`
  - Corpo:

    ```json
    {
      "start": 101,
      "end": 110
    }
    ```

## 📝 Endpoints Disponíveis Users

- **lista usuários**

  - `GET /users`

- **Cadastrar usuário**

  - `POST /users/register`
  - Corpo:
    ```json
    {
      "username": "usuario",
      "password": "senha"
    }
    ```

## 🛠️ Tecnologias Utilizadas

- **Spring Boot**
- **MySQL**
- **JPA/Hibernate**
- **Maven**
- **Spring Security**

## ⚙️ Configurações

### Banco de Dados

- Utilizei o **MariaDB**, porém, fique a vontade para configurar o **MySql**.

- Exemplo application.properties com **MySql**:

  ```properties
  # Nome da aplicação
  spring.application.name=projectLoginInExtensionsApi

  # Configurações de conexão com o MySQL
  spring.datasource.url=jdbc:mysql://localhost:3306/extensions?useSSL=false&serverTimezone=UTC
  spring.datasource.username=seu_usuario
  spring.datasource.password=sua_senha
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

  # Configurações de JPA / Hibernate
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
  ```

### Scripts SQL

- Utilize o arquivo `createSchemaExtensions.sql` para criar as tabelas necessárias no banco de dados.

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - consulte o arquivo [LICENSE](LICENSE) para mais detalhes.
