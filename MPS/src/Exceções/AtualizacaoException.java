/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class AtualizacaoException extends RuntimeException {

    private String message;
    private Exception exception;

    public AtualizacaoException(String message, Exception exception) {

        super(message);
        this.message = message;
        this.exception = exception;
    }

    public AtualizacaoException(String message) {

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
