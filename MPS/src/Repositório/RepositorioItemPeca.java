/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.ItemPeca;
import CN.TipoPeca;
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
public interface RepositorioItemPeca {
    
    void insertItemPeca(ItemPeca itemPeca) throws InsercaoException;
    
    void deleteItemPeca(int codItemPeca) throws DelecaoException;
    
    void updateItemPeca(int codItempeca, ItemPeca itemPeca) throws AtualizacaoException;
    
    ItemPeca selectItemPeca(int codItemPeca) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaByTipo(TipoPeca tipoPeca) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByTipo(TipoPeca tipoPeca) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaByFabricante(String fabricante) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByFabricante(String fabricante) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaByAnoFabricacao(int anoFabricacao) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByAnoFabricacao(int anoFabricacao) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaByDataAquisicao (Date dataAquisicao) throws ConsultaException;
    
    Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByDataAquisicao (Date dataAquisicao) throws ConsultaException;
    
    int getMaxCodItemPeca() throws ConsultaException;
}
