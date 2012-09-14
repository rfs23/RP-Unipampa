package CN;

import java.util.Date;
import java.util.Collection;

public class Manutencao {

	private Date dataRealizacao;

	private int codigo;

	private Collection<SubstituicaoPeca> substituicaoPeca;

	private Semeadora semeadora;

	private Collection<AlocacaoPeca> alocacaoPeca;

	public void setData(Date data) {

	}

    /**
     * @return the dataRealizacao
     */
    public Date getDataRealizacao() {
        return dataRealizacao;
    }

    /**
     * @param dataRealizacao the dataRealizacao to set
     */
    public void setDataRealizacao(Date dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

}
