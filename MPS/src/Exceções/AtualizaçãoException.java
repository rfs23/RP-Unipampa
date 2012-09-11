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
    private Exception exception;

    public AtualizaçãoException(String message, Exception exception) {

        this.message = message;
        this.exception = exception;
    }

    public AtualizaçãoException(String message) {

        this(message, null);
    }

    @Override
    public String getMessage() {

        return this.message;
    }

    public Exception getException() {

        return exception;
    }
}
