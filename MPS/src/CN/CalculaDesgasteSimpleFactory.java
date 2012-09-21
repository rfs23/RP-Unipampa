/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

/**
 *
 * @author rafael
 */
public abstract class CalculaDesgasteSimpleFactory {
    
    public static CalculaDesgasteAtividade createCalculaDesgasteAtividade(TipoAtividade tipoAtiv){
        
        switch(tipoAtiv){
            
            case SEMEAR_ADUBAR: 
                
                return new CalculaDesgasteSemeareAdubar();
                
            default: 
                
                return null;
        }
    }
}
