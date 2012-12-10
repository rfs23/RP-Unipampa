package CN;

import Cadastro.CadastroPecas;
import Exceções.ValorNuloException;
import Repositório.AcessoPostgres;
import Repositório.DBPeca;
import java.util.Date;
import java.util.Map;

public class Peca {

    private static int codPeca;
    
    private int identificacao;
    private String fabricante;
    private TipoPeca tipo;
    private CalculaDesgasteAtividade calcDesgaste;
    
    static{
        
        codPeca = new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).geraCodigoPeca();
    }

    public Peca(int identificacao, String fabricante, TipoPeca tipo) throws ValorNuloException {

        setIdentificacao(identificacao);
        setFabricante(fabricante);
        setTipo(tipo);
    }

    public Peca(String fabricante, TipoPeca tipo) throws ValorNuloException  {

        this(codPeca+1, fabricante, tipo);
        this.identificacao = nextCodePeca();
    }

    public void setCalculaDesgaste(CalculaDesgasteAtividade calcDesgaste) {

        this.calcDesgaste = calcDesgaste;
    }

    public int calculaDesgaste(Map<String, Fator> fatores, int tempoDuracao, TipoAtividade atividade) {

       return calcDesgaste.calculaDesgaste(fatores, tempoDuracao, this.tipo);
    }

    public boolean matches(int Map) {

        return false;
    }

    /**
     * @return the identificacao
     */
    public int getIdentificacao() {

        return identificacao;
    }

    /**
     * @param identificacao the identificacao to set
     */
    public final void setIdentificacao(int identificacao) throws ValorNuloException {

        if (identificacao <= 0) {

            throw new ValorNuloException("Deve ser fornecido um código válido para a peça");
        }

        this.identificacao = identificacao;
    }

    /**
     * @return the fabricante
     */
    public String getFabricante() {

        return fabricante;
    }

    /**
     * @param fabricante the fabricante to set
     */
    public final void setFabricante(String fabricante) throws ValorNuloException {

        if (fabricante == null || fabricante.equals("")) {

            throw new ValorNuloException("Deve ser informado o fabricante da peça");
        }

        this.fabricante = fabricante;
    }

    public final void setTipo(TipoPeca tipo) throws ValorNuloException {

        if(tipo == null){
            
            throw new ValorNuloException("Deve ser fornecido um tipo para peça");
        }

        this.tipo = tipo;
    }

    /**
     * @return the tipo
     */
    public TipoPeca getTipo() {

        return tipo;
    }
    
    private int nextCodePeca(){
        
        int maxCodePeca = codPeca - 1;
        codPeca++;
        
        return ++maxCodePeca;
    }

    @Override
    public int hashCode() {

        return this.identificacao;
    }

    @Override
    public boolean equals(Object obj) {

        return ((obj instanceof Peca) && ((Peca) obj).getIdentificacao() == this.identificacao);
    }

    @Override
    public String toString() {

        return "Peca: " + this.identificacao + ", " + this.fabricante + ", " + this.tipo;
    }
}
