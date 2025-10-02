//  Traduz as tabelas, faz a ponte entre a sua lista de objetos Pessoa e o componente visual JTable



package poo2_main;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PessoaTableModel extends AbstractTableModel {
    private final List<Pessoa> linhas = new ArrayList<>();
    private final String[] colunas = {"ID", "Nome", "Idade", "E-mail"};

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pessoa p = linhas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getId();
            case 1 -> p.getNome();
            case 2 -> p.getIdade();
            case 3 -> p.getEmail();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Pessoa p = linhas.get(rowIndex);
        switch (columnIndex) {
            case 0 -> p.setId((Integer) value);
            case 1 -> p.setNome((String) value);
            case 2 -> p.setIdade((Integer) value);
            case 3 -> p.setEmail((String) value);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void adicionar(Pessoa p) {
        linhas.add(p);
        fireTableRowsInserted(linhas.size() - 1, linhas.size() - 1);
    }

    public void removerIndice(int index) {
        linhas.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public List<Pessoa> getLinhas() {
        return new ArrayList<>(linhas);
    }

    public void setLinhas(List<Pessoa> novasLinhas) {
        linhas.clear();
        linhas.addAll(novasLinhas);
        fireTableDataChanged();
    }
    
    public int getProximoId() {
        return linhas.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
    }
}