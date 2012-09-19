/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.ItemPeca;
import CN.TipoPeca;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Repositório.RepositorioItemPeca;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class CadastroItensPeca {

    public static Map<Integer, ItemPeca> itensPeca;
    RepositorioItemPeca repItemPeca;

    public CadastroItensPeca(RepositorioItemPeca repItemPeca) {

        this.repItemPeca = repItemPeca;

        if (itensPeca == null) {

            itensPeca = new HashMap<Integer, ItemPeca>();
        }
    }

    public int geraCodigoItemPeca() throws ConsultaException {

        return repItemPeca.getMaxCodItemPeca() + 1;
    }

    public void insertItemPeca(ItemPeca itemPeca) throws InsercaoException {
        
        if(itemPeca != null){
            
            repItemPeca.insertItemPeca(itemPeca);
            itensPeca.put(itemPeca.getIdentificacao(), itemPeca);
        }
    }

    public void deleteItemPeca(int codItemPeca) throws DelecaoException {
        
        repItemPeca.deleteItemPeca(codItemPeca);
        itensPeca.remove(codItemPeca);
    }

    public void updateItemPeca(int codItempeca, ItemPeca itemPeca) throws AtualizacaoException {
        
        repItemPeca.updateItemPeca(codItempeca, itemPeca);
        itensPeca.put(codItempeca, itemPeca);
    }

    public ItemPeca selectItemPeca(int codItemPeca) throws ConsultaException {
        
        if(!itensPeca.containsKey(codItemPeca)){
            
            ItemPeca itemPeca = repItemPeca.selectItemPeca(codItemPeca);
            itensPeca.put(itemPeca.getIdentificacao(), itemPeca);
        }
        
        return itensPeca.get(codItemPeca);
    }

    public void selectItensPecaByTipo(TipoPeca tipoPeca) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaByTipo(tipoPeca);
    }

    public void selectItensPecaNaoAlocadosByTipo(TipoPeca tipoPeca) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaNaoAlocadosByTipo(tipoPeca);
    }

    public void selectItensPecaByFabricante(String fabricante) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaByFabricante(fabricante);
    }

    public void selectItensPecaNaoAlocadosByFabricante(String fabricante) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaNaoAlocadosByFabricante(fabricante);
    }

    public void selectItensPecaByAnoFabricacao(int anoFabricacao) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaByAnoFabricacao(anoFabricacao);
    }

    public void selectItensPecaNaoAlocadosByAnoFabricacao(int anoFabricacao) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaNaoAlocadosByAnoFabricacao(anoFabricacao);
    }

    public void selectItensPecaByDataAquisicao(Date dataAquisicao) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaByDataAquisicao(dataAquisicao);
    }

    public void selectItensPecaNaoAlocadosByDataAquisicao(Date dataAquisicao) throws ConsultaException {
        
        itensPeca = repItemPeca.selectItensPecaNaoAlocadosByDataAquisicao(dataAquisicao);
    }
}
