
package Logica;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        int opcao;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Bem vindo ao nosso sistema!");
            System.out.println("Por favor, escolha uma opção.");
            System.out.println("[1] Cadastro \n[2] Login \n[3] Sair");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        Usuario novoUsuario = Usuario.cadastrarNovoUsuario();
                        novoUsuario.salvarNoBanco();
                        System.out.println("Cadastro realizado com sucesso!");
                        break;
                    case 2:
                        Usuario usuarioLogado = Usuario.realizarLogin();
                        if (usuarioLogado != null) {
                            System.out.println("Login realizado com sucesso!");
                            SubmenuLogado.menu(usuarioLogado);
                        }
                        break;
                    case 3:
                        System.out.println("Obrigado por utilizar nosso sistema.");
                        break;
                    default:
                        System.out.println("Opção inválida, por favor, escolha uma opção válida.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
                scanner.next();
                opcao = 0;
            }
        } while (opcao != 3);

        scanner.close();
    }
}
