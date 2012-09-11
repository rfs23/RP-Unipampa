/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import java.util.Date;

/**
 *
 * @author rafael
 */
public class ItemPeca {
    
    private int identificacao;
    private int AnoFab;
    private Date dataAquis;
    private Peca peca;
    
    public ItemPeca(){
    
        this(0,0000, new Date(), new Peca());
    }
    
    public ItemPeca(int identificacao, int anoFab, Date dataAquis, Peca peca){
        
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
    
}
