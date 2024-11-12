import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeProdutos {
    
    // Construtor que inicializa a tabela de produtos
    public GerenciadorDeProdutos() {
        criarTabela();
    }

    // Método para criar a tabela no banco de dados
    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos ("
                   + "id INT AUTO_INCREMENT PRIMARY KEY, "
                   + "nome VARCHAR(255) NOT NULL, "
                   + "preco DOUBLE NOT NULL CHECK(preco >= 0), "
                   + "quantidade INT NOT NULL CHECK(quantidade >= 0), "
                   + "status VARCHAR(255) NOT NULL, "
                   + "categoria VARCHAR(255) NOT NULL"
                   + ")";
        
        try (Connection conn = BancoDeDados.conectar(); 
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Método para adicionar produto ao banco de dados
    public void adicionarProduto(Produto produto) throws SQLException {
        validarProduto(produto);
        
        String sql = "INSERT INTO produtos (nome, preco, quantidade, status, categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = BancoDeDados.conectar(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setProdutoParameters(pstmt, produto);
            pstmt.executeUpdate();

            // Obtém o ID gerado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    // Cria um novo produto com o ID gerado
                    produto = new Produto(id, produto.getNome(), produto.getPreco(), produto.getQuantidade(), produto.getStatus(), produto.getCategoria());
                } else {
                    throw new SQLException("Erro ao adicionar produto, ID não foi gerado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
            throw e; // Propaga a exceção
        }
    }

    // Método para listar produtos do banco de dados
    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        
        try (Connection conn = BancoDeDados.conectar(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(criarProdutoDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            throw e; // Propaga a exceção
        }
        return produtos;
    }

    // Método para atualizar um produto
    public void atualizarProduto(int id, Produto produto) throws SQLException {
        validarProduto(produto);
        
        String sql = "UPDATE produtos SET nome=?, preco=?, quantidade=?, status=?, categoria=? WHERE id=?";
        try (Connection conn = BancoDeDados.conectar(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setProdutoParameters(pstmt, produto);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            throw e; // Propaga a exceção
        }
    }

    // Método para remover um produto
    public void removerProduto(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id=?";
        try (Connection conn = BancoDeDados.conectar(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao remover produto: " + e.getMessage());
            throw e; // Propaga a exceção
        }
    }

    // Método auxiliar para validar os dados do produto
    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo.");
        }
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade do produto não pode ser negativa.");
        }
    }

    // Método auxiliar para definir parâmetros do produto no PreparedStatement
    private void setProdutoParameters(PreparedStatement pstmt, Produto produto) throws SQLException {
        pstmt.setString(1, produto.getNome());
        pstmt.setDouble(2, produto.getPreco());
        pstmt.setInt(3, produto.getQuantidade());
        pstmt.setString(4, produto.getStatus().name());
        pstmt.setString(5, produto.getCategoria().name());
    }

    // Método auxiliar para criar um objeto Produto a partir de um ResultSet
    private Produto criarProdutoDoResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id"); // Obtem o ID do ResultSet
        String nome = rs.getString("nome");
        double preco = rs.getDouble("preco");
        int quantidade = rs.getInt("quantidade");
        StatusProduto status = StatusProduto.valueOf(rs.getString("status"));
        CategoriaProduto categoria = CategoriaProduto.valueOf(rs.getString("categoria"));
        return new Produto(id, nome, preco, quantidade, status, categoria); // Usa o construtor correto
    }
}
