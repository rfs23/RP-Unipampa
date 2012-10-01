/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.Atividade;
import CN.Semeadora;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author rafael
 */
public interface RepositorioSemeadoras {
    
    void insertSemeadora(Semeadora semeadora) throws InsercaoException;
    
    void deleteSemeadora(int codSemeadora) throws DelecaoException;
    
    void updateSemeadora(int codSemeadora, Semeadora semeadora) throws AtualizacaoException;
    
    Semeadora selectSemeadora(int codSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> listSemeadoras() throws ConsultaException;    
    
    Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByMarca (String marcaSemeadora) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByAno (int ano) throws ConsultaException;
    
    Map<Integer, Semeadora> selectSemeadorasByDataInclusao (Date dataInclusao) throws ConsultaException;
    
    void registrarAtividade(int codSem, Atividade atividade) throws InsercaoException;
    
    void cancelarAtividade(int codSem, int codAtiv) throws AtualizacaoException;
    
    int getMaxCodSemeadora() throws ConsultaException;
    
    int getMaxCodDivisao(int codSem) throws ConsultaException;
    
    int getMaxCodRealizacaoAtiv() throws ConsultaException;
    
}
