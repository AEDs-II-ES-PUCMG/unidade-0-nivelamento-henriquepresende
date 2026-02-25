import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto{

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade){
        super(desc, precoCusto, margemLucro);

        if(validade.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Erro: Data de validade não pode ser no passado");
        }
        this.dataDeValidade = validade;
    }

    @Override
    public double valorVenda() {
        double desconto = 0d;
        int diasValidade = LocalDate.now().until(dataDeValidade).getDays();
        if (diasValidade <= PRAZO_DESCONTO) {
            desconto = DESCONTO;
        }
        return (precoCusto * (1 + margemLucro)) * (1 - desconto);
    }

    @Override
    public String toString(){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dados = super.toString();
        dados += "\nVálido até " + formato.format(dataDeValidade);
        return dados;
    }
}


