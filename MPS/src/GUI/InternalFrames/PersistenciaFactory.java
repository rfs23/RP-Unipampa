/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;

/**
 *
 * @author rafael
 */
public interface PersistenciaFactory {
    
    CadastroSemeadoras getPersistenciaSemeadora();
    
    CadastroItensPeca getPersistenciaItensPeca();
    
    CadastroPecas getPersistenciaPeca();
}
