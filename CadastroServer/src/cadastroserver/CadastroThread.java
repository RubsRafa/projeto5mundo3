/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import model.Produto;
import model.Usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author rubia
 */
public class CadastroThread extends Thread {
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;
    private Socket s1;
    
    public CadastroThread(
            ProdutoJpaController ctrl,
            UsuarioJpaController ctrlUsu,
            Socket s1
    ) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }
    
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {
            
            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            
            Usuario usuario = this.ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                out.writeObject("Credenciais inválidas. Conexão encerrada.");
                s1.close();
                return;
            } else {
                out.writeObject("Credenciais válidas. Conectando ao servidor.");
            }
            
            while (true) {
                String comando = (String) in.readObject();
                if ("L".equalsIgnoreCase(comando)) {
                    List<Produto> produtos = this.ctrl.findAll();
                    out.writeObject(produtos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
