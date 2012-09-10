/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroSemeadoras;
import Exceções.InserçãoException;
import Repositório.AcessoBD;
import Repositório.AcessoPostgres;
import Repositório.DBSemeadora;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafael
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DBSemeadora dbSem = new DBSemeadora(AcessoPostgres.getInstance());
        Semeadora sem =  new Semeadora("Case 100", "Case", 2005);
        
        try{
            
            new CadastroSemeadoras(dbSem).insertSemeadora(sem);
        }catch (InserçãoException ie){
            
            ie.getRTException().printStackTrace();
        }
        
        new CadastroSemeadoras(dbSem).deleteSemeadora(2);
        
        /*try {
            new Semeadora("Case 100", "Case", 2005).saveSemeadora();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }*/
        
        /*try {
            new Semeadora().removeSemeadora(9);
        }catch(SQLException ex){
            ex.printStackTrace();
        }*/
        
    }
}
