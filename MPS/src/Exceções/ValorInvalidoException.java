/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class ValorInvalidoException extends RuntimeException {

    String message;
    int valorIncorreto;

    public ValorInvalidoException(String message, int valorIncorreto) {

        super(message);
        this.message = message;
        this.valorIncorreto = valorIncorreto;
    }

    @Override
    public String getMessage() {
        
        return message;
    }
    
    public int getValorIncorreto(){
        
        return valorIncorreto;
    }
}
