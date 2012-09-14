package CN;

import Cadastro.CadastroPecas;
import Repositório.AcessoPostgres;
import Repositório.DBPeca;
import java.util.Date;
import java.util.Map;

public class Peca {

    private int identificacao;    
    private String fabricante;
    private TipoPeca tipo;
    private CalculaDesgasteAtividade calcDesgaste;

    public Peca(int identificacao, String fabricante, TipoPeca tipo) {
        
        this.identificacao = identificacao;
        this.fabricante = fabricante;
        this.tipo = tipo;
    }

    public Peca(String fabricante, TipoPeca tipo){
        
        this(new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).geraCodigoPeca(), fabricante, tipo);
    }

    public void setTipo(TipoPeca tipo) {
        
        this.tipo = tipo;
    }

    public void setCalculaDesgaste(CalculaDesgasteAtividade calcDesgaste) {
        
        this.calcDesgaste = calcDesgaste;
    }

    public int calculaDesgaste(Map fatores, Date duracao, TipoAtividade atividade) {
        
        return 0;
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
    public void setIdentificacao(int identificacao) {
        
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
    public void setFabricante(String fabricante) {
        
        this.fabricante = fabricante;
    }


    /**
     * @return the tipo
     */
    public TipoPeca getTipo() {
        
        return tipo;
    }
    
    @Override
    public int hashCode(){
        
        return this.identificacao;
    }
    
    @Override
    public boolean equals(Object obj){
        
        return ((obj instanceof Peca) && ((Peca)obj).getIdentificacao() == this.identificacao );
    }
    
    @Override
    public String toString(){
        
        return "Peca: " + this.identificacao + ", " + this.fabricante + ", " + this.tipo;
    }
}
