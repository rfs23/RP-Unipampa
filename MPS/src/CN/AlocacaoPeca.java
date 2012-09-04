package CN;

import java.util.Observable;
import java.util.Date;
import java.util.Collection;

public class AlocacaoPeca extends Observable {

	private Date dataInclusaoPeca;

	private int tempoVidaUtil;

	private int codigo;

	private Semeadora semeadora;

	private Divisao divisao;

	private ItemPeca itemPeca;

	private Collection<DesgastePeca> desgastePeca;

	private SubstituicaoPeca substituicaoPeca;

	private Collection<Manutencao> manutencao;

	private Collection<Reparo> reparo;

	private Observable observable;

	public AlocacaoPeca(ItemPeca itemPeca, int porcVidaUtil) {

	}

	public void alterarPeca(ItemPeca peca, int porcVidaUtil, Date data) {

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

	public void setPeca(ItemPeca peca) {

	}

	public void verificaTipo(ItemPeca peca) {

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
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
