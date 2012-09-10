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
    
}
