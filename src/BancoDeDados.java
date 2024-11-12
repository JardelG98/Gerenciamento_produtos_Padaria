import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDeDados {
    private static final String URL = "jdbc:mysql://localhost:3306/gerenciamento_produtos"; // Altere para o nome do seu banco
    private static final String USUARIO = "root"; // Usuário root
    private static final String SENHA = "1234"; // Senha root (ou a senha que você configurou para o root)

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }
}
