import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.text.BadLocationException; // Importando BadLocationException

public class TelaPrincipal {
    private GerenciadorDeProdutos gerenciador;
    private JFrame frame;
    private JTextArea textArea;
    private JTextField nomeField, precoField, quantidadeField;
    private JComboBox<StatusProduto> statusComboBox; // ComboBox para Status
    private JComboBox<CategoriaProduto> categoriaComboBox; // ComboBox para Categoria
    private JButton adicionarButton, editarButton, excluirButton;
    private int produtoSelecionadoId; // Para armazenar o ID do produto selecionado

    public TelaPrincipal() {
        gerenciador = new GerenciadorDeProdutos();
        criarTela();
    }

    private void criarTela() {
        frame = new JFrame("Gerenciamento de Produtos Padaria");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Adicionar MouseListener para selecionar produto ao clicar
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int linha = textArea.getLineOfOffset(e.getY());
                    if (linha >= 0) {
                        Produto produto = gerenciador.listarProdutos().get(linha);
                        preencherCampos(produto);
                    }
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao selecionar a linha: " + ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao listar produtos: " + ex.getMessage());
                } catch (IndexOutOfBoundsException ex) {
                    // Ignorar se a linha estiver fora do intervalo
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Preço:"));
        precoField = new JTextField();
        panel.add(precoField);

        panel.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        panel.add(quantidadeField);

        panel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(StatusProduto.values());
        panel.add(statusComboBox);

        panel.add(new JLabel("Categoria:"));
        categoriaComboBox = new JComboBox<>(CategoriaProduto.values());
        panel.add(categoriaComboBox);

        frame.add(panel, BorderLayout.NORTH);

        // Botões
        adicionarButton = new JButton("Adicionar Produto");
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });
        
        editarButton = new JButton("Editar Produto");
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProduto();
            }
        });
        
        excluirButton = new JButton("Excluir Produto");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(adicionarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(excluirButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        listarProdutos(); // Carregar produtos ao iniciar a tela
    }

    private void preencherCampos(Produto produto) {
        produtoSelecionadoId = produto.getId(); // Supondo que Produto tenha um método getId()
        nomeField.setText(produto.getNome());
        precoField.setText(String.valueOf(produto.getPreco()));
        quantidadeField.setText(String.valueOf(produto.getQuantidade()));
        statusComboBox.setSelectedItem(produto.getStatus());
        categoriaComboBox.setSelectedItem(produto.getCategoria());
    }

    private void adicionarProduto() {
        try {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "O nome do produto não pode estar vazio.");
                return;
            }

            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            // Verificar se preço e quantidade são válidos
            if (preco < 0 || quantidade < 0) {
                JOptionPane.showMessageDialog(frame, "Preço e quantidade devem ser positivos.");
                return;
            }

            StatusProduto status = (StatusProduto) statusComboBox.getSelectedItem();
            CategoriaProduto categoria = (CategoriaProduto) categoriaComboBox.getSelectedItem();

            if (categoria == null) {
                JOptionPane.showMessageDialog(frame, "Por favor, selecione uma categoria válida.");
                return;
            }

            Produto produto = new Produto(nome, preco, quantidade, status, categoria);
            gerenciador.adicionarProduto(produto); // Certifique-se que este método lança SQLException

            listarProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Por favor, insira valores válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao adicionar produto: " + ex.getMessage());
        }
    }

    private void editarProduto() {
        if (produtoSelecionadoId <= 0) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto para editar.");
            return;
        }

        try {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "O nome do produto não pode estar vazio.");
                return;
            }

            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            // Verificar se preço e quantidade são válidos
            if (preco < 0 || quantidade < 0) {
                JOptionPane.showMessageDialog(frame, "Preço e quantidade devem ser positivos.");
                return;
            }

            StatusProduto status = (StatusProduto) statusComboBox.getSelectedItem();
            CategoriaProduto categoria = (CategoriaProduto) categoriaComboBox.getSelectedItem();

            Produto produto = new Produto(produtoSelecionadoId, nome, preco, quantidade, status, categoria);
            gerenciador.atualizarProduto(produtoSelecionadoId, produto);

            listarProdutos();
            JOptionPane.showMessageDialog(frame, "Produto editado com sucesso.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Por favor, insira valores válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao editar produto: " + ex.getMessage());
        }
    }

    private void excluirProduto() {
        if (produtoSelecionadoId <= 0) {
            JOptionPane.showMessageDialog(frame, "Selecione um produto para excluir.");
            return;
        }
        
        try {
            gerenciador.removerProduto(produtoSelecionadoId);
            listarProdutos();
            JOptionPane.showMessageDialog(frame, "Produto excluído com sucesso.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao excluir produto: " + ex.getMessage());
        }
    }

    private void listarProdutos() {
        try {
            List<Produto> produtos = gerenciador.listarProdutos();
            textArea.setText("");
            for (Produto produto : produtos) {
                textArea.append(produto.toString() + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao listar produtos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}
