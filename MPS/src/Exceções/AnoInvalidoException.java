/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class AnoInvalidoException extends RuntimeException {
    
    private String message;
    private int ano;
    
    public AnoInvalidoException(String message){
        
        this(message, 0);
    }
    
    public AnoInvalidoException(String message, int anoInvalido){
        
        super(message);
        this.message = message;
        this.ano = anoInvalido;
    }
    
    public int getAnoInválido(){
        
        return this.ano;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
}
