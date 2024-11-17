package cadastroclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;

public class CadastroClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar login e senha
            out.writeObject("op1"); // exemplo de login
            out.writeObject("op1"); // exemplo de senha

            // Receber resposta de validação do servidor
            String response = (String) in.readObject();
            System.out.println(response);
            if (response.contains("inválidas")) {
                return;
            }

            // Enviar comando 'L' para solicitar a lista de produtos
            out.writeObject("L");

            // Receber e exibir a lista de produtos
            List<Produto> produtos = (List<Produto>) in.readObject();
            produtos.forEach(produto -> System.out.println(produto.getNome()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
