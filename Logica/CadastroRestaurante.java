
package Logica;
import Conexao.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class CadastroRestaurante {
    public int idRestaurante;
    public String nome;
    public String telefone;
    public String diasAtendimento;
    public Endereco endereco;

    public CadastroRestaurante(String nome, String telefone, String diasAtendimento, Endereco endereco) {
        this.idRestaurante = gerarIdRestaurante();
        this.nome = nome;
        this.telefone = telefone;
        this.diasAtendimento = diasAtendimento;
        this.endereco = endereco;
    }

    private int gerarIdRestaurante() {
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }


    public void salvarNoBanco() {
        Connection conexao = ConexaoBanco.conectar();
        if (conexao != null) {
            try {

                String sqlRestaurante = "INSERT INTO restaurante (id_restaurante, nome, telefone, dias_alimento) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatementRestaurante = conexao.prepareStatement(sqlRestaurante);

                preparedStatementRestaurante.setInt(1, this.idRestaurante);
                preparedStatementRestaurante.setString(2, this.nome);
                preparedStatementRestaurante.setString(3, this.telefone);
                preparedStatementRestaurante.setString(4, this.diasAtendimento);

                preparedStatementRestaurante.executeUpdate();

                String sqlEndereco = "INSERT INTO endereco (rua, numero, complemento, bairro, cidade, uf, pais, id_restaurante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatementEndereco = conexao.prepareStatement(sqlEndereco);

                preparedStatementEndereco.setString(1, this.endereco.getRua());
                preparedStatementEndereco.setInt(2, this.endereco.getNumero());
                preparedStatementEndereco.setString(3, this.endereco.getComplemento());
                preparedStatementEndereco.setString(4, this.endereco.getBairro());
                preparedStatementEndereco.setString(5, this.endereco.getCidade());
                preparedStatementEndereco.setString(6, this.endereco.getUf());
                preparedStatementEndereco.setString(7, this.endereco.getPais());
                preparedStatementEndereco.setInt(8, this.idRestaurante);

                preparedStatementEndereco.executeUpdate();

                System.out.println("Restaurante e endereço salvos no banco de dados com sucesso!");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConexaoBanco.fecharConexao(conexao);
            }
        }
    }
}
