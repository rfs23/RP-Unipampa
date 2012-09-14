/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import Exceções.ConsultaException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rafael
 */
public class DBItemPeca implements RepositorioItemPeca {

    private AcessoBD sgbd;
    private String sql;
    private ResultSet result;

    public DBItemPeca(AcessoBD sgbd) {

        this.sgbd = sgbd;
    }

    @Override
    public int getMaxCodItemPeca() throws ConsultaException {
        
        sql = "select max(coditempeca) as maxcod from itempeca";
        
        try{
            
            result = sgbd.selectData(sql);
        }catch(SQLException sqle){
            
            throw new ConsultaException("Não foi possível obter o código máximo para itens de peça no banco de dados", sqle);
        }
        
        
        try{
           
            result.next();
            return result.getInt("maxcod");
        }catch(SQLException sqle){
            
            throw new ConsultaException("Não foi possível acessar os dados da consulta para máximo código de itens de peça", sqle);
        }
        
    }
}
