public class Produto {
    private int id; // ID do produto
    private String nome; // Nome do produto
    private double preco; // Preço do produto
    private int quantidade; // Quantidade disponível
    private StatusProduto status; // Status do produto
    private CategoriaProduto categoria; // Categoria do produto

    // Construtor que inclui o ID
    public Produto(int id, String nome, double preco, int quantidade, StatusProduto status, CategoriaProduto categoria) {
        this.id = id; // Inicializa o ID
        this.nome = nome; // Inicializa o nome
        this.preco = preco; // Inicializa o preço
        this.quantidade = quantidade; // Inicializa a quantidade
        this.status = status; // Inicializa o status
        this.categoria = categoria; // Inicializa a categoria
    }

    // Construtor sem ID, para uso antes da persistência
    public Produto(String nome, double preco, int quantidade, StatusProduto status, CategoriaProduto categoria) {
        this.nome = nome; // Inicializa o nome
        this.preco = preco; // Inicializa o preço
        this.quantidade = quantidade; // Inicializa a quantidade
        this.status = status; // Inicializa o status
        this.categoria = categoria; // Inicializa a categoria
    }

    // Getters
    public int getId() {
        return id; // Retorna o ID do produto
    }

    public String getNome() {
        return nome; // Retorna o nome do produto
    }

    public double getPreco() {
        return preco; // Retorna o preço do produto
    }

    public int getQuantidade() {
        return quantidade; // Retorna a quantidade disponível
    }

    public StatusProduto getStatus() {
        return status; // Retorna o status do produto
    }

    public CategoriaProduto getCategoria() {
        return categoria; // Retorna a categoria do produto
    }

    // Setters (opcional, caso você precise alterar os valores depois da criação)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setStatus(StatusProduto status) {
        this.status = status;
    }

    public void setCategoria(CategoriaProduto categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id + // Inclui o ID na representação do produto
                ", nome='" + nome + '\'' + // Inclui o nome
                ", preco=" + preco + // Inclui o preço
                ", quantidade=" + quantidade + // Inclui a quantidade
                ", status=" + status + // Inclui o status
                ", categoria=" + categoria + // Inclui a categoria
                '}'; // Formatação final
    }
}
