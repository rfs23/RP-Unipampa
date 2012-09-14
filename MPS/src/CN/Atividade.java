package CN;

import java.util.Date;
import java.util.Map;
import java.util.Collection;

public class Atividade {

    private TipoAtividade nome;
    private Date dataRealizacao;
    private Date duracao;
    private Map fatores;
    private int codigo;
    private Semeadora semeadora;
    private Collection<DesgastePeca> desgastePeca;

    public Atividade(Date data, Date duracao, TipoAtividade nome, Map fatores) {
    }

    public void addDesgastePeca(DesgastePeca desgastePeca) {
    }

    /**
     * @return the nome
     */
    public TipoAtividade getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(TipoAtividade nome) {
        this.nome = nome;
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
     * @return the duracao
     */
    public Date getDuracao() {
        return duracao;
    }

    /**
     * @param duracao the duracao to set
     */
    public void setDuracao(Date duracao) {
        this.duracao = duracao;
    }

    /**
     * @return the fatores
     */
    public Map getFatores() {
        return fatores;
    }

    /**
     * @param fatores the fatores to set
     */
    public void setFatores(Map fatores) {
        this.fatores = fatores;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }
}
