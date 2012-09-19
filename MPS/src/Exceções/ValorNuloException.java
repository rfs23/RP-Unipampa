/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class ValorNuloException extends RuntimeException{
    
    Object obj;
    String message;
    
    public ValorNuloException(String message, Object obj){
        
        super(message);
        this.message = message;
        this.obj = obj;
    }
    
    public ValorNuloException(String message){

        this(message, null);
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
    
    public Object getObjetoRegistroIncompleto(){
        
        return this.obj;
    }
}