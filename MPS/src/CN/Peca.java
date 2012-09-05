package CN;

import java.util.Date;
import java.util.Collection;
import java.util.Map;

public class Peca {

    private int identificacao;
    private String anoFabricacao;
    private String fabricante;
    private Date dataAquisicao;
    private TipoPeca tipo;
    private AdministradorManutencoes administradorManutencoes;

    public Peca(String anoFabricacao, String fab, Date dataAquis, TipoPeca tipo) {
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
     * @return the anoFabricacao
     */
    public String getAnoFabricacao() {
        return anoFabricacao;
    }

    /**
     * @param anoFabricacao the anoFabricacao to set
     */
    public void setAnoFabricacao(String anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
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
     * @return the dataAquisicao
     */
    public Date getDataAquisicao() {
        return dataAquisicao;
    }

    /**
     * @param dataAquisicao the dataAquisicao to set
     */
    public void setDataAquisicao(Date dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    /**
     * @return the tipo
     */
    public TipoPeca getTipo() {
        return tipo;
    }
}
