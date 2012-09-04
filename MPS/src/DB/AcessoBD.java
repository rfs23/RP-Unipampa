/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.ResultSet;

/**
 *
 * @author rafael
 */
public interface AcessoBD {
    
    void setUsuario(String usuario);
    
    String getUsuario();
    
    void setSenha(String senha);
    
    String getSenha();
    
    boolean executeSQL(String sql);
  
    ResultSet selectData(String sql);
}
