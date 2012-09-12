/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroSemeadoras;
import Exceções.DeleçãoException;
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
        
        Semeadora sem1 = new Semeadora("sem1", "teste1", 1990);
        Semeadora sem2 = new Semeadora("sem2", "teste2", 1992);
        
        Divisao div = new Divisao("Div1", sem1);
        
        div.setIdentificao(1);
        
        System.out.println(div.getSemeadora().getAno());
        
        div.setSemeadora(sem1);
        div.setSemeadora(sem1);
        div.setSemeadora(sem2);
        
        System.out.println(div.getSemeadora().getAno());
        
        System.out.println(sem1.listarDivisoes());
        System.out.println(sem2.listarDivisoes());
        /*DBSemeadora dbSem = new DBSemeadora(AcessoPostgres.getInstance());
        Semeadora sem =  new Semeadora("Case 100", "Case", 2005);
        
        try{
            
            new CadastroSemeadoras(dbSem).insertSemeadora(sem);
        }catch (InserçãoException ie){
            
            ie.getRTException().printStackTrace();
        }
        try {
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
        /*} catch (DeleçãoException ex) {
            
            System.err.println(ex.getMessage());
        }*/
        
    }
}
