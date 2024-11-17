/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.MovimentoJpaController;
import controller.ProdutoJpaController;
import controller.PessoaJpaController;
import controller.UsuarioJpaController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import model.Movimento;
import model.Produto;
import model.Pessoa;
import model.Usuario;

/**
 *
 * @author rubia
 */
public class CadastroThreadV2 extends Thread {

    private final ObjectInputStream E;
    private final ObjectOutputStream S;
    private final MovimentoJpaController ctrlMov;
    private final ProdutoJpaController ctrlProd;
    private final PessoaJpaController ctrlPessoa;
    private final UsuarioJpaController ctrlUsu;
    private Integer idUsuario;

    public CadastroThreadV2(Socket s1,
            MovimentoJpaController ctrlMov,
            ProdutoJpaController ctrlProd,
            PessoaJpaController ctrlPessoa,
            UsuarioJpaController ctrlUsu,
            Integer idUsuario
    ) throws IOException {
        this.E = new ObjectInputStream(s1.getInputStream());
        this.S = new ObjectOutputStream(s1.getOutputStream());
        this.ctrlMov = ctrlMov;
        this.ctrlProd = ctrlProd;
        this.ctrlPessoa = ctrlPessoa;
        this.ctrlUsu = ctrlUsu;
        this.idUsuario = idUsuario;
    }
    
    enum Estado {
        AGUARDANDO_LOGIN,
        LOGADO
    }    
    
    private Estado estadoAtual = Estado.AGUARDANDO_LOGIN;

    @Override
    public void run() {
        try {
            while (true) {
                String comando = (String) E.readObject();
                System.out.println("Comando recebido: " + comando);

                switch (estadoAtual) {
                    case AGUARDANDO_LOGIN -> processarLogin(comando);
                    case LOGADO -> processarComandoLogado(comando);
                    default -> enviarMensagem("Estado inválido!");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            tratarErro("Erro ao processar comando: " + e.getMessage());
        } finally {
            encerrarConexao();
        }
    }

    private void processarLogin(String comando) throws IOException, ClassNotFoundException {
        if (!comando.equals("LOGIN")) {
            enviarMensagem("Você deve realizar login primeiro.");
            return;
        }

        String login = (String) E.readObject();
        String senha = (String) E.readObject();

        Usuario usuario = ctrlUsu.findUsuario(login, senha);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            this.idUsuario = usuario.getIdUsuario();
            estadoAtual = Estado.LOGADO;
            enviarMensagem("sucesso");
        } else {
            enviarMensagem("Falha no login. Verifique suas credenciais.");
        }
    }

    private void processarComandoLogado(String comando) throws IOException, ClassNotFoundException {
        switch (comando) {
            case "L" -> listarProdutos();
            case "E", "S" -> processarEntradaSaida(comando);
            case "X" -> {
                enviarMensagem("Conexão encerrada.");
                encerrarConexao();
            }
            default -> enviarMensagem("Comando inválido: " + comando);
        }
    }

    private void listarProdutos() throws IOException {
        List<Produto> listaProdutos = ctrlProd.findAll();
        System.out.println("Lista Produtos: " + listaProdutos);
        if (listaProdutos == null || listaProdutos.isEmpty()) {
            enviarMensagem("Nenhum produto encontrado.");
        } else {
            S.writeObject(listaProdutos);
            S.flush();
        }
    }

    private void processarEntradaSaida(String comando) throws IOException, ClassNotFoundException {
        Integer idPessoa = (Integer) E.readObject();
        Integer idProduto = (Integer) E.readObject();
        int quantidade = (Integer) E.readObject();
        float valorUnitario = (Float) E.readObject();

        Pessoa pessoa = ctrlPessoa.findOne(idPessoa);
        Produto produto = ctrlProd.findOne(idProduto);

        if (pessoa == null || produto == null) {
            enviarMensagem("Pessoa ou produto não encontrado!");
            return;
        }

        Usuario usuario = ctrlUsu.findOne(idUsuario);

        Movimento movimento = new Movimento();
        movimento.setTipo(comando.charAt(0));
        movimento.setIdUsuario(usuario);
        movimento.setIdPessoa(pessoa);
        movimento.setIdProduto(produto);
        movimento.setQuantidade(quantidade);
        movimento.setValorUnitario(valorUnitario);

        ctrlMov.createV2(movimento);

        atualizarQuantidadeProduto(produto, quantidade, comando.equals("E"));
    }

    private void atualizarQuantidadeProduto(Produto produto, int quantidade, boolean isEntrada) throws IOException {
        int novaQuantidade = produto.getQuantidade();

        if (isEntrada) {
            novaQuantidade += quantidade;
        } else if (novaQuantidade >= quantidade) {
            novaQuantidade -= quantidade;
        } else {
            enviarMensagem("Quantidade insuficiente para saída.");
            return;
        }

        produto.setQuantidade(novaQuantidade);
        ctrlProd.edit(produto);
        enviarMensagem("Movimento realizado com sucesso!");
    }

    private void enviarMensagem(String mensagem) throws IOException {
        S.writeObject(mensagem);
        S.flush();
    }

    private void tratarErro(String mensagem) {
        System.err.println(mensagem);
        try {
            enviarMensagem(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encerrarConexao() {
        try {
            E.close();
            S.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
