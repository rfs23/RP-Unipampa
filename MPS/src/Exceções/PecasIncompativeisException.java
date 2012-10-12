/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

import CN.TipoPeca;

/**
 *
 * @author rafael
 */
public class PecasIncompativeisException extends RuntimeException {
    
    private TipoPeca tipoPecaAnterior;
    private TipoPeca tipoPecaNova;
    private String message;
    
    public PecasIncompativeisException(String message, TipoPeca tipoPecaAnterior, TipoPeca tipoPecaNova){
        
        super(message);
        this.tipoPecaAnterior = tipoPecaAnterior;
        this.tipoPecaNova = tipoPecaNova;
        this.message = message;
    }
    
    public PecasIncompativeisException(String message){
        
        super(message);
        this.message = message;
    }
    
    public TipoPeca getTipoPecaAnterior(){
        
        return this.tipoPecaAnterior;
    }
    
    public TipoPeca getTipoPecaNova(){
        
        return this.tipoPecaNova;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
    
    
}
