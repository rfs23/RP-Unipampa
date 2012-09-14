/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import Exceções.ConsultaException;

/**
 *
 * @author rafael
 */
public interface RepositorioItemPeca {
    
    /**
     *
     * @return
     * @throws ConsultaException
     */
    int getMaxCodItemPeca() throws ConsultaException;
}
