/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class DeleçãoException extends RuntimeException {
    
    private String message;
    private Exception exception;
    
    public DeleçãoException(String message, Exception exception){
        
        this.message = message;
        this.exception = exception;
    }
    
    public DeleçãoException(String message){
        
        this(message, null);
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
    
    public Exception getRTException(){
        
        return exception;
    }
}
