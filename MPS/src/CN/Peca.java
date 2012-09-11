package CN;

import java.util.Date;
import java.util.Collection;
import java.util.Map;

public class Peca {

    private int identificacao;    
    private String fabricante;
    private TipoPeca tipo;
    private AdministradorManutencoes administradorManutencoes;

    public Peca(int identificacao, String fabricante, TipoPeca tipo) {
        
        this.identificacao = identificacao;
        this.fabricante = fabricante;
        this.tipo = tipo;
    }

    Peca() {
        
        this(0,"",null);
    }

    public void setTipo(TipoPeca tipo) {
    }

    public void setCalculaDesgaste(CalculaDesgasteAtividade calcDesgaste) {
    }

    public int calculaDesgaste(Map fatores, Date duracao, TipoAtividade atividade) {
        return 0;
    }

    public TipoPeca getTipoPeca() {
        return null;
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
}
