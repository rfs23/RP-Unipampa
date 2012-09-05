package CN;

import java.util.Observable;
import java.util.Date;
import java.util.Collection;

public class AlocacaoPeca extends Observable {

    private Date dataInclusaoPeca;
    private int tempoVidaUtil;
    private Divisao divisao;
    private Peca Peca;
    private Observable observable;

    public AlocacaoPeca(Peca Peca, int tempoVidaUtilRestante) {
    }

    public void alterarPeca(Peca peca, int tempoVidaUtilRestante, Date dataInclusao) {
    }

    public Peca getPeca() {
        return null;
    }

    public void subtraiVidaUtil(int desgaste) {
    }

    public int getPorcVidaUtil() {
        return 0;
    }

    public void setPorcVidaUtil(int porcVidaUtil) {
    }

    public void setDataInclusaoPeca(Date data) {
    }

    public void verificaVidaUtil(int vidaUtil) {
    }

    public void setPeca(Peca peca) {
    }

    public void verificaTipo(Peca peca) {
    }

    /**
     * @return the dataInclusaoPeca
     */
    public Date getDataInclusaoPeca() {
        return dataInclusaoPeca;
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
