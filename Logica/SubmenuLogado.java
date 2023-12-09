
package Logica;
import Conexao.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SubmenuLogado {

    public static void menu(Usuario usuarioLogado) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        while (true) {
            System.out.println("Escolha a opção que deseja.");
            System.out.println("[1] Sobre o programa \n[2] Cadastro de Restaurante \n[3] Ver restaurantes disponíveis \n[4] Deletar um Restaurante do Sistema \n[5] Apagar seu usuário do sistema \n[6] Sair");

            try {
                if (scanner.hasNextInt()) {
                    opcao = scanner.nextInt();
                } else {
                    System.out.println("Por favor, digite um número válido.");
                    scanner.next();
                    continue;
                }

                switch (opcao) {
                    case 1:
                        System.out.println("Sobre o programa. \nEste projeto tem o intuito de cadastrar e mapear nas cidades os restaurantes que doem refeições a pessoas em situação de risco social.");
                        break;
                    case 2:
                        cadastrarRestaurante();
                        break;
                    case 3:
                        visualizarRestaurantes();
                        break;
                    case 4:
                        apagarRestaurante();
                        break;
                    case 5:
                        scanner.nextLine();
                        if (Usuario.excluirUsuario(usuarioLogado, scanner)) {
                            System.out.println("Voltando para o menu principal...");
                            return;
                        }
                        break;
                    case 6:
                        System.out.println("Estamos encerrando a sessão. Obrigado, até logo!");
                        return;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro de entrada. Por favor, digite um número válido.");
                scanner.next();
            }
        }
    }

    private static void cadastrarRestaurante() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome do restaurante:");
        String nome = scanner.nextLine();

        System.out.println("Digite o telefone do restaurante:");
        String telefone = scanner.nextLine();

        System.out.println("Digite os dias de atendimento do restaurante:");
        String diasAtendimento = scanner.nextLine();

        System.out.println("Agora, vamos adicionar o endereço do restaurante:");
        Endereco endereco = cadastrarEndereco();

        CadastroRestaurante restaurante = new CadastroRestaurante(nome, telefone, diasAtendimento, endereco);
        restaurante.salvarNoBanco();
        System.out.println("Restaurante cadastrado com sucesso!");
    }

    private static Endereco cadastrarEndereco() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite a rua:");
        String rua = scanner.nextLine();

        System.out.println("Digite o número:");
        int numero = scanner.nextInt();

        scanner.nextLine();

        System.out.println("Digite o complemento: (Caso não tenha, apenas ignore)");
        String complemento = scanner.nextLine();

        System.out.println("Digite o bairro:");
        String bairro = scanner.nextLine();

        System.out.println("Digite a cidade:");
        String cidade = scanner.nextLine();

        System.out.println("Digite o UF (Exemplo, SP):");
        String uf = scanner.nextLine();

        System.out.println("Digite o país:");
        String pais = scanner.nextLine();

        return new Endereco(rua, numero, complemento, bairro, cidade, uf, pais);
    }
    private static void apagarRestaurante() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o ID do restaurante que deseja apagar:");
        int idRestaurante = scanner.nextInt();

        Connection conexao = ConexaoBanco.conectar();
        if (conexao != null) {
            try {
                if (restauranteExiste(idRestaurante, conexao)) {
                    excluirRestaurante(idRestaurante, conexao);
                    System.out.println("Restaurante removido com sucesso!");
                } else {
                    System.out.println("Restaurante não encontrado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConexaoBanco.fecharConexao(conexao);
            }
        }
    }

    private static boolean restauranteExiste(int idRestaurante, Connection conexao) throws SQLException {
        String sql = "SELECT * FROM restaurante WHERE id_restaurante = ?";
        try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
            preparedStatement.setInt(1, idRestaurante);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    private static void excluirRestaurante(int idRestaurante, Connection conexao) throws SQLException {
        String sqlRestaurante = "DELETE FROM restaurante WHERE id_restaurante = ?";
        String sqlEndereco = "DELETE FROM endereco WHERE id_restaurante = ?";

        try (PreparedStatement preparedStatementRestaurante = conexao.prepareStatement(sqlRestaurante);
             PreparedStatement preparedStatementEndereco = conexao.prepareStatement(sqlEndereco)) {

            preparedStatementRestaurante.setInt(1, idRestaurante);
            preparedStatementEndereco.setInt(1, idRestaurante);

            preparedStatementEndereco.executeUpdate();
            preparedStatementRestaurante.executeUpdate();
        }
    }
    private static void visualizarRestaurantes() {
        Connection conexao = ConexaoBanco.conectar();
        if (conexao != null) {
            try {
                String sql = "SELECT restaurante.id_restaurante, nome, dias_alimento, rua, numero, cidade FROM restaurante INNER JOIN endereco ON restaurante.id_restaurante = endereco.id_restaurante";
                PreparedStatement preparedStatement = conexao.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int idRestaurante = resultSet.getInt("id_restaurante");
                    String nomeRestaurante = resultSet.getString("nome");
                    String diasAtendimento = resultSet.getString("dias_alimento");
                    String rua = resultSet.getString("rua");
                    int numero = resultSet.getInt("numero");
                    String cidade = resultSet.getString("cidade");

                    System.out.println("ID do Restaurante: " + idRestaurante);
                    System.out.println("Nome: " + nomeRestaurante);
                    System.out.println("Dias de Atendimento: " + diasAtendimento);
                    System.out.println("Endereço: " + rua + ", " + numero + ", " + cidade);
                    System.out.println("----------------------");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConexaoBanco.fecharConexao(conexao);
            }
        }
    }
}
