/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

import java.util.Date;

/**
 *
 * @author rafael
 */
public class DataInvalidaException extends RuntimeException{
    
    Date dataInvalida;
    String message;
    
    public DataInvalidaException (String message, Date dataInvalida){
        
        super(message);
        this.dataInvalida = dataInvalida;
        this.message = message;
    }
    
    public Date getDataInvalida(){
        
        return this.dataInvalida;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
}
