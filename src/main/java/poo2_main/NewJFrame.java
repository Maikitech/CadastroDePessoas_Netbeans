/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package poo2_main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class NewJFrame extends javax.swing.JFrame {

    private final PessoaTableModel tableModel;
    private File arquivoAtual; // Começa como nulo, será definido ao carregar/salvar
    private final JFileChooser fileChooser;

    public NewJFrame() {
        initComponents();
        
        // Inicializa componentes e configurações
        this.tableModel = new PessoaTableModel();
        this.fileChooser = new JFileChooser();
        
        // Executa os métodos de configuração
        setupTable();
        setupFileChooser();
        setupListeners(); // Conecta os eventos aos botões e menus
        
        // Tenta carregar um arquivo padrão ao iniciar
        carregarArquivoPadrao();
        
        // Centraliza a janela
        setLocationRelativeTo(null);
    }

    // --- MÉTODOS (CHAMADOS NO CONSTRUTOR) ---

    private void setupTable() {
        jTable1.setModel(tableModel);
        jTable1.setAutoCreateRowSorter(true); // Ótima adição!
    }

    private void setupFileChooser() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos JSON", "json"));
        // Define o diretório inicial para a pasta do projeto
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    }
    
    private void setupListeners() {
    
    // Ações do Menu Arquivo
    menuCarregar.addActionListener(e -> carregarDados());
    menuSalvar.addActionListener(e -> salvarDados());
    menuSair.addActionListener(e -> System.exit(0));
}
    private void carregarArquivoPadrao() {
        File arquivoPadrao = new File("pessoas.json");
        if (arquivoPadrao.exists()) {
            try {
                List<Pessoa> pessoas = PessoaIO.carregar(arquivoPadrao);
                tableModel.setLinhas(pessoas);
                this.arquivoAtual = arquivoPadrao;
                setTitle("Editor JSON - " + arquivoAtual.getName());
                jLabel1.setText("Arquivo padrão '" + arquivoPadrao.getName() + "' carregado.");
            } catch (Exception ex) {
                jLabel1.setText("Falha ao carregar arquivo padrão.");
            }
        } else {
            jLabel1.setText("Pronto. Crie dados ou carregue um arquivo.");
        }
    }

    // --- MÉTODOS DE AÇÃO (LÓGICA DOS BOTÕES E MENUS) ---

    private void carregarDados() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            try {
                List<Pessoa> pessoas = PessoaIO.carregar(arquivoSelecionado);
                tableModel.setLinhas(pessoas);
                
                // ATUALIZA o arquivo de trabalho atual
                this.arquivoAtual = arquivoSelecionado;
                setTitle("Editor JSON - " + arquivoAtual.getName()); // Atualiza o título da janela
                jLabel1.setText("Arquivo '" + arquivoAtual.getName() + "' carregado com sucesso.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar o arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                jLabel1.setText("Falha ao carregar o arquivo.");
            }
        }
    }
 //Lógica para o botão salvar
    private void salvarDados() {
        if (arquivoAtual == null) {
            // Se nenhum arquivo foi aberto ainda, o "Salvar" funciona como "Salvar Como"
            salvarComo();
        } else {
            // Salva diretamente no arquivo atual
            try {
                PessoaIO.salvar(arquivoAtual, tableModel.getLinhas());
                jLabel1.setText("Alterações salvas em '" + arquivoAtual.getName() + "'.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                jLabel1.setText("Falha ao salvar o arquivo.");
            }
        }
    }

    //  Lógica de "Salvar Como..." sempre abre o diálogo para o usuário.
     
    private void salvarComo() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();

            if (!arquivoSelecionado.getName().toLowerCase().endsWith(".json")) {
                arquivoSelecionado = new File(arquivoSelecionado.getParentFile(), arquivoSelecionado.getName() + ".json");
            }
            
            try {
                PessoaIO.salvar(arquivoSelecionado, tableModel.getLinhas());
                
                // ATUALIZA o arquivo de trabalho atual
                this.arquivoAtual = arquivoSelecionado;
                setTitle("Editor JSON - " + arquivoAtual.getName()); // Atualiza o título da janela
                jLabel1.setText("Dados salvos em '" + arquivoAtual.getName() + "'.");
                JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                jLabel1.setText("Falha ao salvar o arquivo.");
            }
        }
    }

  private void adicionarNovaPessoa() {
    // 1. Perguntar o nome
    String nome = JOptionPane.showInputDialog(this, "Digite o nome:", "Adicionar Pessoa", JOptionPane.QUESTION_MESSAGE);
    // Se o usuário clicar em "Cancelar" ou fechar a janela, o nome será nulo.
    if (nome == null) {
        return; // Interrompe a operação
    }

    // 2. Perguntar a idade
    int idade = 0;
    boolean idadeValida = false;
    while (!idadeValida) {
        String idadeStr = JOptionPane.showInputDialog(this, "Digite a idade:", "Adicionar Pessoa", JOptionPane.QUESTION_MESSAGE);
        if (idadeStr == null) {
            return; // Interrompe a operação
        }
        try {
            idade = Integer.parseInt(idadeStr);
            idadeValida = true; // Se a conversão funcionou, a idade é válida
        } catch (NumberFormatException e) {
            // Se o usuário digitou um texto que não é um número
            JOptionPane.showMessageDialog(this, "Por favor, digite um número válido para a idade.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3. Perguntar o e-mail
    String email = JOptionPane.showInputDialog(this, "Digite o e-mail:", "Adicionar Pessoa", JOptionPane.QUESTION_MESSAGE);
    if (email == null) {
        return; // Interrompe a operação
    }

    // 4. Com todos os dados coletados, criar a nova pessoa
    int proximoId = tableModel.getProximoId();
    Pessoa novaPessoa = new Pessoa(proximoId, nome, idade, email);
    tableModel.adicionar(novaPessoa);

    jLabel1.setText("Nova pessoa '" + nome + "' adicionada com sucesso.");
}
    
  
    // Método remover pessoas selecionadas
private void removerPessoasSelecionadas() {
    // 1. Pega as linhas selecionadas UMA VEZ e guarda na variável.
    int[] linhasSelecionadasNaView = jTable1.getSelectedRows();
    
    // 2. VERIFICA a variável que acabamos de criar.
    if (linhasSelecionadasNaView.length == 0) {
        // 3. Se não houver seleção, mostra o aviso E PARA A EXECUÇÃO AQUI.
        JOptionPane.showMessageDialog(this, "Selecione uma ou mais pessoas para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return; // Esta linha é a correção!
    }
    
    // 4. Se havia seleção, o código continua e usa a MESMA variável que já pegamos.
    int[] linhasSelecionadasNoModel = java.util.Arrays.stream(linhasSelecionadasNaView)
            .map(jTable1::convertRowIndexToModel)
            .sorted()
            .toArray();
    
    for (int i = linhasSelecionadasNoModel.length - 1; i >= 0; i--) {
        tableModel.removerIndice(linhasSelecionadasNoModel[i]);
    }
    
    jLabel1.setText(linhasSelecionadasNaView.length + " pessoa(s) removida(s).");
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToolBar = new javax.swing.JToolBar();
        btnAdicionar = new javax.swing.JButton();
        btnCarregar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuArquivo = new javax.swing.JMenu();
        menuCarregar = new javax.swing.JMenuItem();
        menuSalvar = new javax.swing.JMenuItem();
        menuSalvarComo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuSair = new javax.swing.JMenuItem();

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(20, 10));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable1.setBackground(new java.awt.Color(255, 255, 204));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Idade", "E-Mail"
            }
        ));
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Pronto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel2.setText("Software Maikitech");

        jToolBar.setRollover(true);

        btnAdicionar.setBackground(new java.awt.Color(0, 0, 0));
        btnAdicionar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdicionar.setForeground(new java.awt.Color(51, 255, 51));
        btnAdicionar.setText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        jToolBar.add(btnAdicionar);

        btnCarregar.setBackground(new java.awt.Color(0, 0, 0));
        btnCarregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCarregar.setForeground(new java.awt.Color(255, 153, 255));
        btnCarregar.setText("Carregar");
        btnCarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarregarActionPerformed(evt);
            }
        });
        jToolBar.add(btnCarregar);

        btnRemover.setBackground(new java.awt.Color(0, 0, 0));
        btnRemover.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRemover.setForeground(new java.awt.Color(255, 51, 51));
        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        jToolBar.add(btnRemover);

        btnSalvar.setBackground(new java.awt.Color(0, 0, 0));
        btnSalvar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSalvar.setForeground(new java.awt.Color(255, 255, 102));
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        jToolBar.add(btnSalvar);

        jMenuBar.setToolTipText("");
        jMenuBar.setAlignmentX(2.0F);
        jMenuBar.setAlignmentY(2.0F);
        jMenuBar.setBorderPainted(false);
        jMenuBar.setMaximumSize(new java.awt.Dimension(6032768, 6032768));

        jMenuArquivo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jMenuArquivo.setText("Arquivo");
        jMenuArquivo.setToolTipText("");
        jMenuArquivo.setAlignmentX(2.0F);
        jMenuArquivo.setAutoscrolls(true);
        jMenuArquivo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        menuCarregar.setText("Carregar");
        menuCarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCarregarActionPerformed(evt);
            }
        });
        jMenuArquivo.add(menuCarregar);

        menuSalvar.setText("Salvar");
        jMenuArquivo.add(menuSalvar);

        menuSalvarComo.setText("Salvar como");
        menuSalvarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalvarComoActionPerformed(evt);
            }
        });
        jMenuArquivo.add(menuSalvarComo);
        jMenuArquivo.add(jSeparator1);

        menuSair.setText("Sair");
        menuSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSairActionPerformed(evt);
            }
        });
        jMenuArquivo.add(menuSair);

        jMenuBar.add(jMenuArquivo);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(259, 259, 259)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(275, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuCarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCarregarActionPerformed
        // acabou sobrando
    }//GEN-LAST:event_menuCarregarActionPerformed

    private void menuSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuSairActionPerformed

    private void btnCarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarregarActionPerformed
        carregarDados();
    }//GEN-LAST:event_btnCarregarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvarDados();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionarNovaPessoa();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        removerPessoasSelecionadas();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void menuSalvarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalvarComoActionPerformed
        salvarComo();
    }//GEN-LAST:event_menuSalvarComoActionPerformed

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1PropertyChange

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCarregar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenuArquivo;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JMenuItem menuCarregar;
    private javax.swing.JMenuItem menuSair;
    private javax.swing.JMenuItem menuSalvar;
    private javax.swing.JMenuItem menuSalvarComo;
    // End of variables declaration//GEN-END:variables
}