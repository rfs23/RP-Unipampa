/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class AtualizaçãoException extends RuntimeException {

    private String message;
    private RuntimeException rtException;

    public AtualizaçãoException(String message, RuntimeException rtException) {

        this.message = message;
        this.rtException = rtException;
    }

    public AtualizaçãoException(String message) {

        this(message, null);
    }

    @Override
    public String getMessage() {

        return this.message;
    }

    public RuntimeException getRTException() {

        return rtException;
    }
}
