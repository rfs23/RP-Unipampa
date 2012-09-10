/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.Peca;
import CN.Semeadora;
import CN.TipoPeca;
import Exceções.AnoInvalidoException;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafael
 */
public class DBSemeadora implements RepositórioSemeadoras {

    private AcessoBD sgbd;
    private String sql;
    private ResultSet result;

    public DBSemeadora(AcessoBD sgbd) {

        this.sgbd = sgbd;
    }

    /**
     * Método para adcionar um objeto Semeadora em um Banco de Dados.
     *
     * @param semeadora Objeto semeadora a ser adcionado no banco de dados.
     * @throws SQLException Caso haja algum problema com o banco de dados.
     */
    @Override
    public void insertSemeadora(Semeadora semeadora) throws InserçãoException {

        sql = "insert into semeadora(nome, marca, ano, datainclusao) values ( '"
                + semeadora.getModelo() + "', '" + semeadora.getMarca() + "', " + semeadora.getAno()
                + ", '" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "')";

        this.sgbd.executeSQL(sql);
    }

    /**
     * Remove do banco de dados um registro de semeadora que tenha o código
     * passado como parâmetro.
     *
     * @param codSem Código da semeadora a ser excluída.
     * @throws SQLException Caso não exista o registro ou viole alguma restrição
     * do banco de dados.
     */
    @Override
    public void deleteSemeadora(int codSem) throws DeleçãoException {

        sql = "delete from semeadora where codsem=" + codSem;
        sgbd.executeSQL(sql);
    }

    /**
     *
     * @param codSem
     * @param semeadora
     * @throws SQLException
     */
    @Override
    public void updateSemeadora(int codSem, Semeadora semeadora) throws AtualizaçãoException {

        sql = "update semeadora set nome='" + semeadora.getModelo() + "', marca='" + semeadora.getMarca() + "', ano='" + semeadora.getAno() + "', datainclusao='" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "'";

        sgbd.executeSQL(sql);
    }

    /**
     *
     * @param codSem
     * @return
     * @throws ConsultaException
     */
    @Override
    public Semeadora selectSemeadora(int codSem) throws ConsultaException {

        sql = "select * from semeadora where codsem=" + codSem;
        ResultSet result = sgbd.selectData(sql);
        Semeadora semeadora = null;
        try {
            semeadora = new Semeadora(result.getString("nome"), result.getString("marca"),
                    (Integer) result.getObject("ano"));
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            semeadora.setDataRegistro(result.getDate("dataRegistro"));
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            semeadora.setIdentificacao(result.getInt("codsem"));
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        return semeadora;

    }

    private Map<Integer, Divisao> selectDivisoesSemeadora(int codSem) throws ConsultaException {

        sql = "select * from divisao where codSem=" + codSem;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, Divisao> divisoes = new HashMap<Integer, Divisao>();
        try {
            while (result.next()) {

                int codDivisao = result.getInt("codDivisao");
                Map<Integer, AlocacaoPeca> alocacoes = selectAlocacoesPecaSemeadora(codSem, codDivisao);
                Divisao div = new Divisao(selectSemeadora(codSem), codDivisao, result.getString("nome"), alocacoes);
                divisoes.put(div.getIdentificao(), div);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        return divisoes;
    }

    private Map<Integer, AlocacaoPeca> selectAlocacoesPecaSemeadora(int codSem, int codDivisao) throws ConsultaException {

        sql = "select * from alocacaopeca where codsem=" + codSem + " and coddivisao=" + codDivisao;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, AlocacaoPeca> alocacoes = new HashMap<Integer, AlocacaoPeca>();
        try {
            while (result.next()) {

                Peca pca = selectPeca(result.getInt("codtipopeca"), result.getInt("codpeca"));
                AlocacaoPeca alcPeca = new AlocacaoPeca(pca, result.getInt("tempovidautilrestante"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Peca selectPeca(int codTipoPeca, int codPeca) throws ConsultaException {

        sql = "select * from itempeca where codTipoPeca=" + codTipoPeca + " and codPeca=" + codPeca;

        ResultSet result = sgbd.selectData(sql);

        Peca peca = null;

        try {
            peca = new Peca(result.getString("anofab"), result.getString("fabricante"), result.getDate("dataaquis"), selectTipoPeca(codTipoPeca));
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            peca.setIdentificacao(result.getInt("codpeca"));
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }

        return peca;
    }

    private TipoPeca selectTipoPeca(int codTipoPeca) throws ConsultaException {

        sql = "select * from TipoPeca where codTipoPeca=" + codTipoPeca;
        ResultSet result = sgbd.selectData(sql);
        String nomeTP = null;
        try {
            nomeTP = result.getString("nome");
        } catch (SQLException ex) {
            Logger.getLogger(DBSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        TipoPeca tipoPeca = null;

        for (TipoPeca tp : TipoPeca.values()) {

            if (nomeTP.equals(tp.toString())) {

                tipoPeca = tp;
            }
        }

        return tipoPeca;
    }

    @Override
    public Map<Integer, Semeadora> listSemeadoras() throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Integer, Semeadora> selectSemeadorasByCode(int codSem) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Integer, Semeadora> selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Integer, Semeadora> selectSemeadorasByAno(int ano) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Integer, Semeadora> selectSemeadorasByDataFab(Date dataFab) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
