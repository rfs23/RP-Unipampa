/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import Exceções.AnoInvalidoException;
import CN.Semeadora;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author rafael
 */
public interface RepositórioSemeadoras {
    
    void insertSemeadora(Semeadora semeadora) throws InserçãoException;
    
    void deleteSemeadora(int codSemeadora) throws DeleçãoException;
    
    void updateSemeadora(int codSemeadora, Semeadora semeadora) throws AtualizaçãoException;
    
    Semeadora selectSemeadora(int codSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> listSemeadoras() throws ConsultaException;    
    
    Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByMarca (String marcaSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByAno (int ano) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByDataInclusao (Date dataInclusao) throws ConsultaException;
    
    int getMaxCodSemeadora() throws ConsultaException;
    
    int getMaxCodDivisao(int codSem) throws ConsultaException;
}
