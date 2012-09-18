/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.Peca;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import java.util.Map;

/**
 *
 * @author rafael
 */
public interface RepositorioPeca {
    
    void insertPeca(Peca peca) throws InserçãoException;
    
    void deletePeca(int codPeca) throws DeleçãoException;
    
    void updatePeca(int codPeca, Peca peca) throws AtualizaçãoException;
    
    Peca selectPeca(int codPeca) throws ConsultaException;
    
    Map<Integer, Peca> selectPecaByFabricante(String fabricante) throws ConsultaException;
    
    Map<Integer, Peca> selectPecaByTipoPeca(int codTipoPeca) throws ConsultaException;
    
    int getMaxCodPeca() throws ConsultaException;
}
