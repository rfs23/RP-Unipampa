/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.Peca;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Repositório.RepositorioPeca;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class CadastroPecas {

    public static Map<Integer, Peca> pecas;
    RepositorioPeca repPeca;

    public CadastroPecas(RepositorioPeca repPeca) {

        this.repPeca = repPeca;
        if(pecas == null){
            
            pecas = new HashMap<Integer, Peca>();
        }
    }

    public void insertPeca(Peca peca) throws InsercaoException {
        
        if (peca != null){
            
            repPeca.insertPeca(peca);
            //pecas.put(peca.getIdentificacao(), peca);
        }
    }

    public void deletePeca(int codPeca) throws DelecaoException {
        
        repPeca.deletePeca(codPeca);
        pecas.remove(codPeca);
    }

    public void updatePeca(int codPeca, Peca peca) throws AtualizacaoException {
        
        repPeca.updatePeca(codPeca, peca);
        pecas.put(codPeca, peca);
    }

    public Peca selectPeca(int codPeca) throws ConsultaException {
        
        if(!pecas.containsKey(codPeca)){
            
            Peca peca = repPeca.selectPeca(codPeca);
            pecas.put(peca.getIdentificacao(), peca);
        }
        
        return pecas.get(codPeca);
    }

    public void selectPecaByFabricante(String fabricante) throws ConsultaException {
        
        CadastroPecas.pecas = repPeca.selectPecaByFabricante(fabricante);
    }

    public void selectPecaByTipoPeca(int codTipoPeca) throws ConsultaException {
        
        CadastroPecas.pecas = repPeca.selectPecaByTipoPeca(codTipoPeca);
    }
    
    public int geraCodigoPeca() throws ConsultaException {

        return repPeca.getMaxCodPeca() + 1;
    }
}
