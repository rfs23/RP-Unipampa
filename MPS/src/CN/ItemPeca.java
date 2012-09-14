/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroItensPeca;
import Repositório.AcessoPostgres;
import Repositório.DBItemPeca;
import java.util.Date;

/**
 *
 * @author rafael
 */
public class ItemPeca {

    private int identificacao;
    private int AnoFab;
    private Date dataAquis;
    private int tempoVidaUtilRestante;
    private Peca peca;
    private AlocacaoPeca alocPeca;


    public ItemPeca(int anoFab, Date dataAquis, Peca peca) {

        this(new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance())).geraCodigoItemPeca(), anoFab, dataAquis, peca);
    }

    public ItemPeca(int identificacao, int anoFab, Date dataAquis, Peca peca) {

        this.identificacao = identificacao;
        this.AnoFab = anoFab;
        this.dataAquis = dataAquis;
        this.peca = peca;
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
     * @return the AnoFab
     */
    public int getAnoFab() {

        return AnoFab;
    }

    /**
     * @param AnoFab the AnoFab to set
     */
    public void setAnoFab(int AnoFab) {

        this.AnoFab = AnoFab;
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
    public void setDataAquis(Date dataAquis) {

        this.dataAquis = dataAquis;
    }

    /**
     * @return the peca
     */
    public Peca getPeca() {

        return peca;
    }

    /**
     * @param peca the peca to set
     */
    public void setPeca(Peca peca) {

        this.peca = peca;
    }

    /**
     * @return the tempoVidaUtilRestante
     */
    public int getTempoVidaUtilRestante() {

        return tempoVidaUtilRestante;
    }

    /**
     * @param tempoVidaUtilRestante the tempoVidaUtilRestante to set
     */
    public void setTempoVidaUtilRestante(int tempoVidaUtilRestante) {

        this.tempoVidaUtilRestante = tempoVidaUtilRestante;
    }

    protected AlocacaoPeca getAlocPeca(){
        
        return this.alocPeca;
    }

    /**
     * @param alocPeca the alocPeca to set
     */
    protected void setAlocPeca(AlocacaoPeca alocPeca) {
        
        Exception e = new Exception();  
        StackTraceElement[] stack = e.getStackTrace();
        String nomeCompletoClasseChamadora = stack[1].getClassName().replace(".", "/");
        String[] nomeClasseChamadora = nomeCompletoClasseChamadora.split("/");
        
        if (nomeClasseChamadora[nomeClasseChamadora.length -1].equals("AlocacaoPeca")) {

            this.alocPeca = alocPeca;
        }

    }
}
