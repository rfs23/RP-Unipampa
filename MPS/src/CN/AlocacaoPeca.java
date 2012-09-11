package CN;

import java.util.Date;
import java.util.Observable;


public class AlocacaoPeca extends Observable {

    private Date dataInclusaoItemPeca;
    private int tempoVidaUtil;
    private Divisao divisao;
    private ItemPeca peca;
    private Observable observable;

    public AlocacaoPeca(ItemPeca peca, int tempoVidaUtilRestante, Date dataInclusao) {
        
        
    }
    
    public AlocacaoPeca(){
        
        this(null, 0, new Date());
    }

    public void alterarItemPeca(ItemPeca peca, int tempoVidaUtilRestante, Date dataInclusao) {
    }

    public ItemPeca getItemPeca() {
        return null;
    }

    public void subtraiVidaUtil(int desgaste) {
    }

    public int getPorcVidaUtil() {
        return 0;
    }

    public void setPorcVidaUtil(int porcVidaUtil) {
    }

    public void setDataInclusaoItemPeca(Date data) {
    }

    public void verificaVidaUtil(int vidaUtil) {
    }

    public void setItemPeca(ItemPeca peca) {
    }

    public void verificaTipo(ItemPeca peca) {
    }

    /**
     * @return the dataInclusaoItemPeca
     */
    public Date getDataInclusaoItemPeca() {
        return dataInclusaoItemPeca;
    }

    /**
     * @return the tempoVidaUtil
     */
    public int getTempoVidaUtil() {
        return tempoVidaUtil;
    }

    /**
     * @param tempoVidaUtil the tempoVidaUtil to set
     */
    public void setTempoVidaUtil(int tempoVidaUtil) {
        this.tempoVidaUtil = tempoVidaUtil;
    }

    /**
     * @return the divisao
     */
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * @param divisao the divisao to set
     */
    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }
}
