/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rafael
 */
public class AcessoPostgres implements AcessoBD{
    
    private String dataBaseURL;
    private final String JDBC_DRIVER = "org.postgresql.Driver";
    private String usuario;
    private String senha;
    private Connection con;
    private Statement stm;
    
    private static AcessoPostgres bdPostgres = new AcessoPostgres();
    
    private AcessoPostgres (){
    
        this.usuario = "postgres";
        this.senha = "unipampa";
        this.dataBaseURL = "jdbc:postgresql://localhost:5432/MPSData";
    }
    
    public static AcessoPostgres getInstance(){
        
        return bdPostgres;
    }

    @Override
    public void setUsuario(String usuario) {
        
        this.usuario = usuario;
    }

    @Override
    public String getUsuario() {
        
        return this.usuario;
    }

    @Override
    public void setSenha(String senha) {
        
        this.senha = senha;
    }

    @Override
    public String getSenha() {
        
        return this.senha;
    }
    
    /**
     * @return the dataBaseURL
     */
    public String getDataBaseURL() {
        return dataBaseURL;
    }

    /**
     * @param dataBaseURL the dataBaseURL to set
     */
    public void setDataBaseURL(String dataBaseURL) {
        this.dataBaseURL = dataBaseURL;
    }

    /**
     * Realiza conexão com um banco de dados PostGres.
     * @return true se a conexão foi realizado com sucesso, ou false caso contrário.
     */
    public void connect() throws SQLException{
        
        try{
            
            Class.forName(JDBC_DRIVER).getInterfaces();
            
            con = DriverManager.getConnection(getDataBaseURL(), usuario, senha);
            
        }catch (SQLException sqle){
            
            sqle.setNextException(new SQLException("Não foi possível a conexão com o Banco de Dados"));
            throw sqle;
        }catch (ClassNotFoundException cnfe){
            
            cnfe.printStackTrace();
        }
        
    }

    /**
     * Encerra a conexão com o banco de dados.
     * @return true se a desconexão foi concluída com sucesso, ou false caso contrário.
     */ 
    public void disconnect() throws SQLException{
        
        try{
            
            con.close();
        }catch (SQLException sqle){
            
            sqle.setNextException(new SQLException("Não foi possível encerrar a conexão"));
            //throw sqle;
        }
    }

    /**
     * Conecta-se ao banco de dados e executa uma instrução SQL de consulta ao banco de dados.
     * Concluída com sucesso ou não a consulta, a conexão com o banco de dados é encerrada.
     * @param sql - Instrução SQL de consulta ao banco de dados.
     * @return Um objeto ResultSet contendo os dados obtidos pela consulta.
     */
    @Override
    public ResultSet selectData(String sql) throws SQLException{
        
        connect();
        try{
            
            stm = con.createStatement();
            ResultSet result = stm.executeQuery(sql);
            return result;
        }catch (SQLException sqle){
            
            sqle.setNextException(new SQLException("SQL Inválida"));
            throw sqle;
        }
    }

    /**
     * Conecta-se ao banco de dados e executa instruções SQL sobre o mesmo.
     * Permite executar as instruções insert, update e delete.
     * Tendo sida executada com sucesso a instrução ou não a conexão com o banco de dados é encerrada.
     * @param sql Instrução SQL a ser executada.
     * @return true se a instrução foi executada com sucesso ou false caso contrário.
     */
    
    @Override
    public void executeSQL(String sql) throws SQLException{
        
        connect();
        try{
            
            stm = con.createStatement();
            stm.executeUpdate(sql);
        }catch (SQLException sqle){
            
            sqle.setNextException(new SQLException("SQL Inválida"));
            throw sqle;
        }finally{
            
            disconnect();
        }
    }
  
}
