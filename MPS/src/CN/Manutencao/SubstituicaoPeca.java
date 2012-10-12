package CN.Manutencao;

import CN.AlocacaoPeca;
import CN.ItemPeca;
import CN.Semeadora;
import Exceções.DataInvalidaException;
import Exceções.PecasIncompativeisException;
import Exceções.RelacaoPecaDivisaoException;
import Exceções.ValorNuloException;
import java.util.Date;

public class SubstituicaoPeca extends Manutencao {

    private ItemPeca itemPeca;

    public SubstituicaoPeca(Date data) {
        super(data);
    }

    @Override
    public boolean realizarManutencao() throws RelacaoPecaDivisaoException, DataInvalidaException, ValorNuloException{
        
        super.getPeca().getDivisao().excluirPeca(super.getPeca());
        super.getPeca().getDivisao().addPeca(itemPeca, super.getDataRealizacao());
        //super.getPeca().alterarItemPeca(super.getPeca().getDivisao(), itemPeca, super.getDataRealizacao());
        this.restauro = this.itemPeca.getTempoVidaUtilRestante() - super.getPeca().getItemPeca().getTempoVidaUtilRestante();
        setTempoVidaUtil(itemPeca.getTempoVidaUtilRestante());
        
        return true;
    }
    
    public ItemPeca getPecaSubstituta(){
        
        return this.itemPeca;
    }

    @Override
    public void setSemeadora(Semeadora semeadora) {

        super.setSemeadora(semeadora);
    }

    @Override
    public void setAlocacaoPeca(AlocacaoPeca peca) {

        super.setAlocacaoPeca(peca);
    }

    public void setPecaSubstituta(ItemPeca peca) throws PecasIncompativeisException{
        
        verificaCompatibilidadePecas(peca);
        
        this.itemPeca = peca;
    }
    
 /*   @Override
    public void setTempoVidaUtil(int tempoVidaUtil){
        
        super.setTempoVidaUtil(tempoVidaUtil);
    }*/

    private void verificaCompatibilidadePecas(ItemPeca peca) throws PecasIncompativeisException {

        try {

            if (!super.getPeca().getItemPeca().getPeca().getTipo().equals(peca.getPeca().getTipo())) {

                throw new PecasIncompativeisException("Os tipos das peças não são compatíveis",
                        super.getPeca().getItemPeca().getPeca().getTipo(), peca.getPeca().getTipo());
            }

        } catch (NullPointerException npe) {

            throw new PecasIncompativeisException("Peça anterior ou peça nova não existe",
                    super.getPeca() != null ? super.getPeca().getItemPeca().getPeca().getTipo() : null,
                    peca != null ? peca.getPeca().getTipo() : null);
        }

    }
}
