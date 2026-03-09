import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Comercio {

    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;

    /** Scanner teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução*/
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI; Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("============================");
    }

    /** Imprime o meu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
     */
    static int menu(){
        cabecalho();
        System.out.println("1- Listar todos os produtos");
        System.out.println("2- Procurar e listar um produto");
        System.out.println("3- Cadastrar novo produto");
        System.out.println("0- Sair");
        System.out.println("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /** Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N (quantidade de produtos) <br/>
     * tipo; descrição,preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     *  Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados){
        Produto[] vetorProdutos = null;
        quantosProdutos = 0;

        try {
            File arquivo = new File(nomeArquivoDados);
            if (!arquivo.exists()) {
                return new Produto[MAX_NOVOS_PRODUTOS];
            }

            Scanner leitorArquivo = new Scanner(arquivo);
            if (leitorArquivo.hasNext()) {
                int n = Integer.parseInt(leitorArquivo.nextLine());
                vetorProdutos = new Produto[n + MAX_NOVOS_PRODUTOS];

                for (int i = 0; i < n; i++) {
                    String linha = leitorArquivo.nextLine();
                    vetorProdutos[i] = Produto.criarDoTexto(linha);
                    quantosProdutos++;
                }
            }
            leitorArquivo.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        }

        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS ---");
        if (quantosProdutos == 0) {
            System.out.println("Nenhum produto cadastrado no momento.");
            return;
        }

        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println((i + 1) + ". " + produtosCadastrados[i].toString());
        }
    }
    /** Localiza um produto no vetor de cadastrados, a partir do nome (descrição), e imprime seus dados.
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime mensagem padrão */

        static void localizarProdutos() {
            System.out.print("\nDigite o nome do produto que deseja buscar: ");
            String busca = teclado.nextLine();

            // Criamos um produto "falso" apenas com a descrição para usar o equals()
            // Colocamos 1.0 de preço só para o construtor não dar erro
            Produto produtoFalso = new ProdutoNaoPerecivel(busca, 1.0);
            boolean encontrou = false;

            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i].equals(produtoFalso)) {
                    System.out.println("\nProduto Encontrado:");
                    System.out.println(produtosCadastrados[i].toString());
                    encontrou = true;
                    break; // Achou, pode parar de procurar
                }
            }

            if (!encontrou) {
                System.out.println("\nProduto não encontrado.");
            }
        }
    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos.
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos
     objetos.
     */
        static void cadastrarProduto() {
            if (quantosProdutos >= produtosCadastrados.length) {
                System.out.println("Vetor cheio! Não há mais espaço para novos cadastros.");
                return;
            }

            System.out.println("\n--- NOVO CADASTRO ---");
            System.out.println("1 - Produto Não Perecível");
            System.out.println("2 - Produto Perecível");
            System.out.print("Escolha o tipo: ");
            int tipo = Integer.parseInt(teclado.nextLine());

            System.out.print("Descrição: ");
            String desc = teclado.nextLine();

            System.out.print("Preço de custo (ex: 15.50): ");
            double preco = Double.parseDouble(teclado.nextLine());

            System.out.print("Margem de lucro (ex: 0.2 para 20%): ");
            double margem = Double.parseDouble(teclado.nextLine());

            if (tipo == 1) {
                produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(desc, preco, margem);
                quantosProdutos++; // Aumenta a contagem
                System.out.println("Produto não perecível cadastrado com sucesso!");

            } else if (tipo == 2) {
                System.out.print("Data de validade (dd/MM/yyyy): ");
                String dataStr = teclado.nextLine();

                // Converte a string digitada para LocalDate
                java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate validade = java.time.LocalDate.parse(dataStr, formato);

                produtosCadastrados[quantosProdutos] = new ProdutoPerecivel(desc, preco, margem, validade);
                quantosProdutos++; // Aumenta a contagem
                System.out.println("Produto perecível cadastrado com sucesso!");

            } else {
                System.out.println("Tipo inválido. Operação cancelada.");
            }
        }
    public static void salvarProdutos(String nomeArquivo) {
        try {
            // PrintWriter cria o arquivo (ou apaga o antigo para reescrever)
            PrintWriter escritor = new PrintWriter(new File(nomeArquivo));

            // 1º passo: escrever a quantidade total de produtos na primeira linha
            escritor.println(quantosProdutos);

            // 2º passo: percorrer o vetor gravando linha por linha
            for (int i = 0; i < quantosProdutos; i++) {
                // Usa o método que implementamos nas classes filhas (Polimorfismo!)
                String linhaCSV = produtosCadastrados[i].gerarDadosTexto();
                escritor.println(linhaCSV);
            }

            escritor.close();
            System.out.println("Dados salvos com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);
        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}

