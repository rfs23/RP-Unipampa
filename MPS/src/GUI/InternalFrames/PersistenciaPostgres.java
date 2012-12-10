/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import Repositório.AcessoPostgres;
import Repositório.DBItemPeca;
import Repositório.DBPeca;
import Repositório.DBSemeadora;
import java.sql.SQLException;


/**
 *
 * @author rafael
 */
public class PersistenciaPostgres implements PersistenciaFactory {

    @Override
    public CadastroSemeadoras getPersistenciaSemeadora() {

        try {
            
            AcessoPostgres.getInstance().connect();
            return new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance()));
        } catch (SQLException ex) {

            return null;
        }
    }

    @Override
    public CadastroItensPeca getPersistenciaItensPeca() {
        
        try {
            
            AcessoPostgres.getInstance().connect();
            return new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance()));
        } catch (SQLException ex) {

            return null;
        }
    }

    @Override
    public CadastroPecas getPersistenciaPeca() {
        
        try {
            
            AcessoPostgres.getInstance().connect();
            return new CadastroPecas(new DBPeca(AcessoPostgres.getInstance()));
        } catch (SQLException ex) {

            return null;
        }
    }
}
