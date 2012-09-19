/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.Peca;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import java.util.Map;

/**
 *
 * @author rafael
 */
public interface RepositorioPeca {
    
    void insertPeca(Peca peca) throws InsercaoException;
    
    void deletePeca(int codPeca) throws DelecaoException;
    
    void updatePeca(int codPeca, Peca peca) throws AtualizacaoException;
    
    Peca selectPeca(int codPeca) throws ConsultaException;
    
    Map<Integer, Peca> selectPecaByFabricante(String fabricante) throws ConsultaException;
    
    Map<Integer, Peca> selectPecaByTipoPeca(int codTipoPeca) throws ConsultaException;
    
    int getMaxCodPeca() throws ConsultaException;
}
