/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

import CN.TipoAlocacao;

/**
 *
 * @author rafael
 */
public class RelacaoPecaDivisaoException extends RuntimeException{
    
    private TipoAlocacao tipoAlocDivisao;
    private TipoAlocacao tipoAlocPeca;
    String message;
    
    public RelacaoPecaDivisaoException(TipoAlocacao tipoAlocDivisao, TipoAlocacao tipoAlocPeca, String message){
        
        super(message);
        this.tipoAlocDivisao = tipoAlocDivisao;
        this.tipoAlocPeca = tipoAlocPeca;
        this.message = message;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }

    /**
     * @return the tipoAlocDivisao
     */
    public TipoAlocacao getTipoAlocDivisao() {
        return tipoAlocDivisao;
    }

    /**
     * @return the tipoAlocPeca
     */
    public TipoAlocacao getTipoAlocPeca() {
        return tipoAlocPeca;
    }
}
