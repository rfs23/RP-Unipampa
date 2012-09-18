/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.Peca;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
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

    public void insertPeca(Peca peca) throws InserçãoException {
        
        if (peca != null){
            
            repPeca.insertPeca(peca);
            pecas.put(peca.getIdentificacao(), peca);
        }
    }

    public void deletePeca(int codPeca) throws DeleçãoException {
        
        repPeca.deletePeca(codPeca);
        pecas.remove(codPeca);
    }

    public void updatePeca(int codPeca, Peca peca) throws AtualizaçãoException {
        
        repPeca.updatePeca(codPeca, peca);
        pecas.put(codPeca, peca);
    }

    public Peca selectPeca(int codPeca) throws ConsultaException {
        
        if(pecas.containsKey(codPeca)){
            
            return pecas.get(codPeca);
        }else{
            
            return repPeca.selectPeca(codPeca);
        }
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
