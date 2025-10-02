//  Le e escreve a lista de Pessoa em formato JSON. Ele é o único que lida diretamente com os arquivos no disco

package poo2_main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File; 
import java.util.List;

public class PessoaIO {
    private static final ObjectMapper OM = new ObjectMapper();

    // Recebe 'File' para ser consistente com o JFileChooser
    public static List<Pessoa> carregar(File arquivo) throws Exception {
        return OM.readValue(arquivo, new TypeReference<List<Pessoa>>() {});
    }

    public static void salvar(File arquivo, List<Pessoa> dados) throws Exception {
        OM.writerWithDefaultPrettyPrinter().writeValue(arquivo, dados);
    }
}