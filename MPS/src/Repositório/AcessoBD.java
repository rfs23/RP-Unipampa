/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rafael
 */
public interface AcessoBD {
    
    void setUsuario(String usuario);
    
    String getUsuario();
    
    void setSenha(String senha);
    
    String getSenha();
    
    void executeSQL(String sql) throws SQLException;
  
    ResultSet selectData(String sql) throws SQLException;
}
