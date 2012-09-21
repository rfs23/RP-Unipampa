/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroItensPeca;
import Exceções.ConsultaException;
import Exceções.DataInvalidaException;
import Exceções.TempoVidaUtilForaDosLimitesException;
import Exceções.ValorNuloException;
import Repositório.AcessoPostgres;
import Repositório.DBItemPeca;
import java.util.Date;

/**
 *
 * @author rafael
 */
public class ItemPeca {

    private int identificacao;
    private int anoFab;
    private Date dataAquis;
    private int tempoVidaUtilRestante;
    private Peca peca;
    private AlocacaoPeca alocPeca;

    public ItemPeca(int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        this(new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance())).geraCodigoItemPeca(), anoFab, dataAquis, peca, tempoVidaUtilRestante);
    }

    public ItemPeca(int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        this.peca = peca;

        setIdentificacao(identificacao);
        setAnoFab(anoFab);
        setDataAquis(dataAquis);
        this.tempoVidaUtilRestante = 0;

        if (tempoVidaUtilRestante <= 0) {

            throw new ValorNuloException("Deve ser atribuído ao item de peça um tempo de visa útil restante", this);
        }

        acrescentarTempoVidaUtil(tempoVidaUtilRestante);

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
    public final void setIdentificacao(int identificacao) throws ValorNuloException {

        if (identificacao <= 0) {

            throw new ValorNuloException("Deve ser fornecida uma identificação válida para o item de peça");
        }

        this.identificacao = identificacao;
    }

    /**
     * @return the AnoFab
     */
    public int getAnoFab() {

        return anoFab;
    }

    /**
     * @param AnoFab the AnoFab to set
     */
    public final void setAnoFab(int anoFab) throws ValorNuloException {

        if (anoFab <= 0) {

            throw new ValorNuloException("Deve ser fornecido um ano de fabricação válido para o item de peça");
        }
        this.anoFab = anoFab;
    }

    /**
     * @return the dataAquis
     */
    public Date getDataAquis() {

        return dataAquis;
    }

    /**
     * @param dataAquis the dataAquis to set
     */
    public final void setDataAquis(Date dataAquis) throws ValorNuloException, DataInvalidaException {

        if (dataAquis == null) {

            throw new ValorNuloException("Deve ser fornecida a data de aquisição do item de peça");
        }

        if (dataAquis.after(new Date())) {

            throw new DataInvalidaException("Data informada é posterior à data atual", dataAquis);
        }

        if ((new Date().getYear() - dataAquis.getYear()) > 50) {

            throw new DataInvalidaException("Data informada muito distante da data atual", dataAquis);
        }

        this.dataAquis = dataAquis;
    }

    /**
     * @return the peca
     */
    public Peca getPeca() {

        return peca;
    }

    /**
     * @return the tempoVidaUtilRestante
     */
    public int getTempoVidaUtilRestante() {

        return tempoVidaUtilRestante;
    }

    public final void acrescentarTempoVidaUtil(int tempoVidaUtil) throws TempoVidaUtilForaDosLimitesException {

        int tempoVidaUtilRest = this.tempoVidaUtilRestante + Math.abs(tempoVidaUtil);
        
        if (tempoVidaUtilRest  > this.peca.getTipo().getEstVidaUtil()) {            

            throw new TempoVidaUtilForaDosLimitesException(tempoVidaUtilRest , this.peca.getTipo().getEstVidaUtil(), "Não é possível atribuir a um item de peça um tempo de vida útil restante maior do que ele pode ter");
        }
        
        this.tempoVidaUtilRestante += Math.abs(tempoVidaUtil);
    }

    public void subtrairTempoVidautil(int tempoVidaUtil) throws TempoVidaUtilForaDosLimitesException {

        int tempoVidaUtilRest = this.tempoVidaUtilRestante + Math.abs(tempoVidaUtil);
        
        if (tempoVidaUtilRest < 0) {

            throw new TempoVidaUtilForaDosLimitesException(tempoVidaUtilRest, 0, "Um item de peça não pode ter tempo de vida útil menor do que 0");
        }

        this.tempoVidaUtilRestante -= Math.abs(tempoVidaUtil);
    }

    protected AlocacaoPeca getAlocPeca() {

        return this.alocPeca;
    }

    /**
     * @param alocPeca the alocPeca to set
     */
    public void setAlocPeca(AlocacaoPeca alocPeca) {

        /* Exception e = new Exception();  
         StackTraceElement[] stack = e.getStackTrace();
         String nomeCompletoClasseChamadora = stack[1].getClassName().replace(".", "/");
         String[] nomeClasseChamadora = nomeCompletoClasseChamadora.split("/");
        
         if (nomeClasseChamadora[nomeClasseChamadora.length -1].equals("AlocacaoPeca")) {

         this.alocPeca = alocPeca;
         }*/

        if (alocPeca.getItemPeca().equals(this)) {

            this.alocPeca = alocPeca;
        }

    }

    @Override
    public int hashCode() {

        return this.identificacao;
    }

    @Override
    public boolean equals(Object obj) {

        return ((obj instanceof ItemPeca) && (((ItemPeca) obj).getIdentificacao() == this.identificacao));
    }

    @Override
    public String toString() {

        return "Item de Peça: " + this.identificacao + ", " + this.peca + "," + this.anoFab + ", " + this.dataAquis + ", " + this.tempoVidaUtilRestante;
    }
}
