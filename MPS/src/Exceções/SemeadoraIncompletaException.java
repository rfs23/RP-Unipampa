/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

import CN.Divisao;

/**
 *
 * @author rafael
 */
public class SemeadoraIncompletaException extends RuntimeException{
    
    Divisao divisaoIncompleta;
    String message;
    
    public SemeadoraIncompletaException(String message, Divisao divisaoIncompleta){
        
        super(message);
        this.message = message;
        this.divisaoIncompleta = divisaoIncompleta;
    }
    
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
    
    public Divisao getDivisaoIncompleta(){
        
        return this.divisaoIncompleta;
    }
    
}
