/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author rafael
 */
public class TempoVidaUtilForaDosLimitesException extends RuntimeException{
    
    private int tempoVidaUtilExcedente;
    private int tempoVidaUtilMaximo;
    String message;
    
    public TempoVidaUtilForaDosLimitesException(int tempoVidaUtilExcedente, int tempoVidaUtilMaximo, String message){
    
        super(message);
        
        this.tempoVidaUtilExcedente = tempoVidaUtilExcedente;
        this.tempoVidaUtilMaximo = tempoVidaUtilMaximo;
        this.message = message;
    }

    public int getTempoVidaUtilExcedente() {
        
        return tempoVidaUtilExcedente;
    }

    public int getTempoVidaUtilMaximo() {
        
        return tempoVidaUtilMaximo;
    }
    
    @Override
    public String getMessage(){
        
        return this.message;
    }
    
}
