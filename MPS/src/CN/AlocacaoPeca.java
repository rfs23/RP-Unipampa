package CN;

import Exceções.RelaçãoPeçaDivisãoException;
import java.util.Date;
import java.util.Observable;

public class AlocacaoPeca extends Observable {

    private Date dataInclusaoItemPeca;
    private Divisao divisao;
    private ItemPeca peca;
    private Observable observable;

    public AlocacaoPeca(ItemPeca peca, Divisao divisao) throws RelaçãoPeçaDivisãoException{

        setItemPeca(peca);
        setDivisao(divisao, null);
    }

    public void alterarItemPeca(ItemPeca peca) {
    }

    public ItemPeca getItemPeca() {

        return this.peca;
    }

    public void subtraiVidaUtil(int desgaste) {

        this.peca.setTempoVidaUtilRestante(this.getItemPeca().getTempoVidaUtilRestante() - desgaste);
    }

    public int getPorcVidaUtil() {

        return Calculo.vidaUtilParaPorcVidaUtil(this);
    }

    public void setPorcVidaUtil(int porcVidaUtil) {

        this.peca.setTempoVidaUtilRestante(Calculo.porcVidaUtilParaVidaUtil(this.peca.getPeca(), porcVidaUtil));
    }

    public void setDataInclusaoItemPeca(Date data) {

        this.dataInclusaoItemPeca = data;
    }

    public void verificaVidaUtil(int vidaUtil) {
    }

    public void setItemPeca(ItemPeca peca) {

        if (peca != null) {

            if (peca.getAlocPeca() == null) {

                if (this.peca != null) {

                    this.peca.setAlocPeca(null);
                }

                this.peca = peca;
                peca.setAlocPeca(this);
            }

        }
    }

    public void verificaTipoAlocacao(Divisao div, ItemPeca peca) throws RelaçãoPeçaDivisãoException{
        
        if(!div.getTipoAloc().equals(peca.getPeca().getTipo().getTipoAlocacao())){
            
            throw new RelaçãoPeçaDivisãoException(div.getTipoAloc(), peca.getPeca().getTipo().getTipoAlocacao(), "O item de peça está sendo alocado em uma divisão a qual não é compatível com!");
        }
    }

    /**
     * @return the dataInclusaoItemPeca
     */
    public Date getDataInclusaoItemPeca() {
        return dataInclusaoItemPeca;
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
    public void setDivisao(Divisao divisao, Object obj) {

        if (divisao != null) {

            divisao.addPeca(this, new Date());
            this.divisao = divisao;
        } else {

            if (this.divisao != null) {

                if (this.divisao.equals(obj)) {

                    this.divisao.selecionarPeca(this.getItemPeca().getIdentificacao()).divisao = null;
                } else {

                    this.divisao.excluirPeca(divisao.selecionarPeca(this.getItemPeca().getIdentificacao()));
                }

            }

            this.divisao = null;
        }

    }

    @Override
    public int hashCode() {

        try {

            return this.peca.getIdentificacao();
        } catch (Exception ex) {

            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {

        return ((obj instanceof AlocacaoPeca) && ((AlocacaoPeca) obj).hashCode() == this.hashCode()) ? true : false;
    }

    @Override
    public String toString() {

        return "AlocacaoPeca: " + this.divisao + ", " + this.peca + ", " + this.dataInclusaoItemPeca;
    }
}
