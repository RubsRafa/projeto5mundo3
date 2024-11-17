/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author rubia
 */
public class CadastroServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroPUV2");
        ProdutoJpaController ctrlProd = new ProdutoJpaController(emf);
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        MovimentoJpaController ctrlMov = new MovimentoJpaController(emf);
        PessoaJpaController ctrlPessoa = new PessoaJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor aguardando conexões na porta 4321...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                CadastroThreadV2 thread = new CadastroThreadV2(
                        clientSocket,
                        ctrlMov,
                        ctrlProd,
                        ctrlPessoa,
                        ctrlUsu,
                        1
                );
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        // TODO code application logic here
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroPU");
//        ProdutoJpaController ctrl = new ProdutoJpaController(emf);
//        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
//        
//        try(ServerSocket serverSocket = new ServerSocket(4321)) {
//            System.out.println("Servidor aguardando conexões na porta 4321...");
//            
//            while(true) {
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
//                
//                CadastroThread thread = new CadastroThread(ctrl, ctrlUsu, clientSocket);
//                thread.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
