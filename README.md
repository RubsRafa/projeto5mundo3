# Cadastro System - Servidor e Cliente Assíncrono

Este projeto consiste em um sistema de cadastro de produtos com comunicação entre cliente e servidor, implementado em Java. O sistema oferece funcionalidades de login, listagem de produtos, entrada e saída de produtos no estoque. O servidor utiliza threads para responder a múltiplas conexões de forma assíncrona. A comunicação entre o cliente e o servidor ocorre por meio de sockets e objetos serializados.

O [relatório de práticas](https://github.com/user-attachments/files/17788763/projeto5mundo3.pdf.pdf) foi confeccionado em formato PDF e produzido em conjunto com o desenvolvimento do projeto. O relatório também está disponível [via web]([https://docs.google.com/presentation/d/1t_4H5_nxv9f1gfibxAW2UkW1gZIl3dxpLMXvMWMpVFo/edit#slide=id.g4dfce81f19_0_45](https://docs.google.com/presentation/d/1t_4H5_nxv9f1gfibxAW2UkW1gZIl3dxpLMXvMWMpVFo/edit?usp=sharing)), para visualização dos GIFS, que apresentam os testes feitos.

## Funcionalidades
- **Login**: O cliente se autentica com login e senha. Caso as credenciais sejam válidas, o cliente pode realizar operações no sistema.
- **Listagem de Produtos**: O cliente pode listar todos os produtos no estoque.
- **Entrada e Saída de Produtos**: O cliente pode registrar a entrada e saída de produtos, atualizando as quantidades no estoque.
- **Assincronia no Cliente**: O cliente é assíncrono, permitindo a execução de múltiplas operações enquanto aguarda respostas do servidor.
- **Persistência**: O servidor utiliza o JPA (Java Persistence API) para persistir dados em um banco SQL Server.

## Tecnologias
- Java 8 ou superior
- JPA (Java Persistence API) com EclipseLink
- SQL Server para persistência de dados
- Sockets para comunicação entre cliente e servidor
- Java Swing para a interface gráfica do cliente

## Arquitetura
### Servidor (CadastroServer)
- **Thread de Comunicação**: Cada cliente é atendido por uma thread (`CadastroThread`), permitindo que o servidor atenda múltiplos clientes simultaneamente.
- **Persistência**: Utiliza o JPA para persistir dados das entidades `Produto`, `Movimento`, `Usuario` e `Pessoa`.
- **Controle de Entidades**: Controladores como `ProdutoJpaController`, `MovimentoJpaController` e `UsuarioJpaController` gerenciam as operações no banco de dados.

### Cliente (CadastroClient e CadastroClientV2)
- **CadastroClient**: Cliente básico para conectar ao servidor e listar os produtos disponíveis.
- **CadastroClientV2**: Cliente assíncrono com funcionalidades adicionais, como entrada e saída de produtos, onde o cliente pode enviar comandos e aguardar respostas enquanto interage com o servidor.

## Instruções de Instalação e Execução

### Requisitos
- Java 8 ou superior
- SQL Server configurado e conectado ao NetBeans
- EclipseLink (JPA 2.1)
- Conector JDBC para SQL Server

### Configuração do Servidor
1. Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/cadastro-system.git
    ```

2. Configure a conexão com o banco de dados SQL Server no NetBeans, conforme os passos da documentação.

3. Compile e execute o servidor:
    ```bash
    cd CadastroServer
    mvn clean install
    java CadastroServer.MainServer
    ```

   O servidor ficará aguardando conexões na porta `4321`.

### Configuração do Cliente
1. Compile e execute o cliente básico:
    ```bash
    cd CadastroClient
    mvn clean install
    java CadastroClient.MainClient
    ```

2. Para o cliente assíncrono:
    ```bash
    cd CadastroClient
    mvn clean install
    java CadastroClient.MainClientV2
    ```

### Como Usar

#### Para o Cliente
1. **Login**: Insira o login e a senha configurados no banco de dados.
    - Exemplo: `op1/op1`

2. **Comandos**:
    - **L**: Lista todos os produtos no estoque.
    - **E**: Registra a entrada de um produto, pedindo o `ID` do produto, `quantidade` e `valor unitário`.
    - **S**: Registra a saída de um produto, pedindo o `ID` do produto, `quantidade` e `valor unitário`.
    - **X**: Finaliza a conexão com o servidor.

3. **Interface Gráfica**: O cliente assíncrono apresenta uma janela (`SaidaFrame`) que exibe mensagens enviadas pelo servidor, como a lista de produtos e o status das operações.

#### Para o Servidor
- O servidor gerencia as conexões dos clientes e utiliza threads para lidar com múltiplas requisições simultâneas.
- Ele valida as credenciais de login e, se válidas, permite a interação com os produtos (entrada/saída).

## Testes e Validação
1. Execute o servidor.
2. Execute o cliente e teste os comandos:
    - **L** para listar os produtos.
    - **E** e **S** para realizar operações de entrada e saída de produtos.
    - **X** para finalizar a conexão.
