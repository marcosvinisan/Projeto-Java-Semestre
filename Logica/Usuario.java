
package Logica;
import Conexao.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;


public class Usuario {
    private String nome;
    private String email;
    private String username;
    private String senha;

    public Usuario(String nome, String email, String username, String senha) {
        this.nome = nome;
        this.email = email;
        this.username = username;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public static Usuario cadastrarNovoUsuario() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite seu nome:");
        String nome = validarInputString(scanner);

        System.out.println("Digite seu e-mail:");
        String email = validarInputEmail(scanner);

        System.out.println("Digite seu username:");
        String username = validarInputString(scanner);

        System.out.println("Digite sua senha:");
        String senha = validarInputSenha(scanner);

        return new Usuario(nome, email, username, senha);
    }

    private static String validarInputString(Scanner scanner) {
        String input = scanner.nextLine();
        while (input.trim().isEmpty()) {
            System.out.println("Entrada inválida. Por favor, digite novamente:");
            input = scanner.nextLine();
        }
        return input;
    }

    private static String validarInputEmail(Scanner scanner) {
        String email = validarInputString(scanner);
        while (!email.contains("@")) {
            System.out.println("E-mail inválido. Por favor, digite novamente:");
            email = validarInputString(scanner);
        }
        return email;
    }


    private static String validarInputSenha(Scanner scanner) {
        String senha = validarInputString(scanner);
        while (senha.length() < 8) {
            System.out.println("Senha deve ter pelo menos 8 caracteres. Por favor, digite novamente:");
            senha = validarInputString(scanner);
        }
        return senha;
    }
    static boolean excluirUsuario(Usuario usuarioLogado, Scanner scanner) {
        System.out.println("Para confirmar a exclusão, digite sua senha:");
        String senhaConfirmacao = scanner.nextLine().trim();

        if (verificarCredenciais(usuarioLogado.getUsername(), senhaConfirmacao)) {
            usuarioLogado.excluirDoBanco();
            System.out.println("Usuário removido com sucesso!");
            return true;
        } else {
            System.out.println("Senha incorreta. Não é possível apagar o usuário.");
            return false;
        }
    }



    public void salvarNoBanco() {
        try {
            Connection conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                String query = "INSERT INTO usuario (nome, email, username, senha) VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                    preparedStatement.setString(1, nome);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, username);
                    preparedStatement.setString(4, senha);

                    preparedStatement.executeUpdate();
                }

                ConexaoBanco.fecharConexao(conexao);

            } else {
                System.out.println("Não foi possível conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void excluirDoBanco() {
        try {
            Connection conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                String query = "DELETE FROM usuario WHERE username = ?";

                try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Usuário removido do banco de dados com sucesso!");
                    } else {
                        System.out.println("Nenhum usuário encontrado para remoção.");
                    }
                }

                ConexaoBanco.fecharConexao(conexao);
            } else {
                System.out.println("Não foi possível conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static Usuario realizarLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite seu username:");
        String username = validarInputString(scanner);

        System.out.println("Digite sua senha:");
        String senha = validarInputSenha(scanner);

        if (verificarCredenciais(username, senha)) {
            System.out.println("Login bem-sucedido!");
            return obterUsuario(username);
        } else {
            System.out.println("Usuário ou senha incorretos. Tente novamente.");
            return null;
        }
    }

    private static boolean verificarCredenciais(String username, String senha) {
        try {
            Connection conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                String query = "SELECT * FROM usuario WHERE username = ? AND senha = ?";
                try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, senha);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                }
            } else {
                System.out.println("Não foi possível conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Usuario obterUsuario(String username) {
        try {
            Connection conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                String query = "SELECT * FROM usuario WHERE username = ?";
                try (PreparedStatement preparedStatement = conexao.prepareStatement(query)) {
                    preparedStatement.setString(1, username);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {

                        return new Usuario(
                                resultSet.getString("nome"),
                                resultSet.getString("email"),
                                resultSet.getString("username"),
                                resultSet.getString("senha")
                        );
                    }
                }
            } else {
                System.out.println("Não foi possível conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}