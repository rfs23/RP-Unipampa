package CN.Manutencao;

import CN.AlocacaoPeca;
import CN.Semeadora;
import java.util.Date;

public abstract class Manutencao {

    private Date dataRealizacao;
    private Semeadora semeadora;
    private AlocacaoPeca alocPeca;
    protected int restauro;
    private int tempoVidaUtil;

    protected Manutencao(Date dataRealizacao) {

        this.dataRealizacao = dataRealizacao;
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
    protected void setDataRealizacao(Date dataRealizacao) {

        this.dataRealizacao = dataRealizacao;
    }

    /**
     * @return the codigo
     */
    /*public int getCodigo() {
     return codigo;
     }*/
    public AlocacaoPeca getPeca() {

        return this.alocPeca;
    }

    public Semeadora getSemeadora() {

        return semeadora;
    }

    protected void setSemeadora(Semeadora semeadora) {

        try {

            if (semeadora.selecionarManutencao(this.dataRealizacao).equals(this)) {

                this.semeadora = semeadora;
            }
        } catch (Exception ex) {
        }
    }

    public int getRestauro() {

        return restauro;
    }

    protected void setAlocacaoPeca(AlocacaoPeca alocPeca) {

        try {

            if (semeadora.selecionarDivisao(alocPeca.getDivisao().getIdentificao()).selecionarPeca(alocPeca.getItemPeca().getIdentificacao()) != null) {

                this.alocPeca = alocPeca;
            }
        } catch (Exception ex) {
        }
    }

    public void setTempoVidaUtil(int tempoVidaUtil) {

        this.tempoVidaUtil = tempoVidaUtil;
    }

    public int getTempoVidaUtil() {

        return tempoVidaUtil;
    }

    public abstract boolean realizarManutencao();

    @Override
    public String toString() {

        return "Manutencao " + this.getDataRealizacao() + ", " + this.semeadora + ", " 
                + this.alocPeca + ", " + this.tempoVidaUtil + ", " + this.restauro;
    }
    
    @Override
    public int hashCode(){
        
        try{
            
            String hc = "" + this.dataRealizacao.getDate() + this.dataRealizacao.getMonth() + this.dataRealizacao.getYear() + this.semeadora.getIdentificacao();
            return Integer.parseInt(hc);
        }catch(Exception ex){
            
            return 0;
        }
    }
    
    @Override
    public boolean equals(Object obj){
        
        return (obj instanceof Manutencao && ((Manutencao)obj).hashCode() == this.hashCode());
    }
}
