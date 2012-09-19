/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class InsercaoException extends RuntimeException {

    private String message;
    private Exception exception;

    public InsercaoException(String message, Exception exception) {

        super(message);
        this.message = message;
        this.exception = exception;
    }

    public InsercaoException(String message) {

        this(message, null);
    }

    @Override
    public String getMessage() {

        return this.message;
    }

    public Exception getRTException() {

        return exception;
    }
}
