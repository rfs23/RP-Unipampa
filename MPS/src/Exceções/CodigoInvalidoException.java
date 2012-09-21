/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class CodigoInvalidoException extends RuntimeException{
    
    private int codInvalido;
    private String message;
    
    public CodigoInvalidoException(String message, int codInvalido){
        
        super(message);
        this.message = message;
        this.codInvalido = codInvalido;
    }
    
    public int getCodigoInvalido(){
        
        return this.codInvalido;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
}
