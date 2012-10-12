/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class ConjuntoDeFatoresIncompletosException extends RuntimeException{
    
    String [] fatoresFaltando;
    String message;
    
    public ConjuntoDeFatoresIncompletosException(String message, String [] fatoresFaltando){
        
        this.message = message;
        this.fatoresFaltando = fatoresFaltando;
    }
    
    public String[] getFatoresFaltando(){
        
        return fatoresFaltando;
    }
    
    @Override
    public String getMessage(){
        
        return message;
    }
}
