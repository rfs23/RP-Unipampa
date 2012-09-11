/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.Semeadora;
import Exceções.AnoInvalidoException;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import Repositório.RepositórioSemeadoras;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class CadastroSemeadoras{

    public static Map<Integer, Semeadora> semeadoras;
    private RepositórioSemeadoras repSemeadoras;
    
    public CadastroSemeadoras (RepositórioSemeadoras repSemeadoras){
        
        this.repSemeadoras = repSemeadoras;
        
        if(semeadoras==null){
            
            semeadoras = new HashMap<Integer, Semeadora>();
        }
    }
    
    public void insertSemeadora(Semeadora semeadora) throws InserçãoException {
        
        this.repSemeadoras.insertSemeadora(semeadora);
    }

    public void deleteSemeadora(int codSemeadora) throws DeleçãoException{
        
        this.repSemeadoras.deleteSemeadora(codSemeadora);
        
        CadastroSemeadoras.semeadoras.remove(codSemeadora);
    }

    public void updateSemeadora(int codSemeadora, Semeadora semeadora) throws AtualizaçãoException {
        
        this.repSemeadoras.updateSemeadora(codSemeadora, semeadora);
        CadastroSemeadoras.semeadoras.put(codSemeadora, semeadora);
    }

    public Semeadora selectSemeadora(int codSemeadora) throws ConsultaException {
        
        if(CadastroSemeadoras.semeadoras.get(codSemeadora)==null){
            
            Semeadora semeadora = this.repSemeadoras.selectSemeadora(codSemeadora);
            CadastroSemeadoras.semeadoras.put(semeadora.getIdentificacao(), semeadora);
            
            return semeadora;
        }else{
            
            return CadastroSemeadoras.semeadoras.get(codSemeadora);
        }
        
    }
    
    public void clearList(){
        
        CadastroSemeadoras.semeadoras.clear();
    }

    
    public void listSemeadoras() throws ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.listSemeadoras();
    }

    
    public void selectSemeadorasByCode(int codSem) throws ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByCode(codSem);
    }

    
    public void selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByModelo(modeloSemeadora);
    }

    
    public void selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByMarca(marcaSemeadora);
    }

    
    public void selectSemeadorasByAno(int ano) throws AnoInvalidoException, ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByAno(ano);
    }

    
    public void selectSemeadorasByDataFab(Date dataFab) throws ConsultaException {
        
        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByDataFab(dataFab);
    }
}
