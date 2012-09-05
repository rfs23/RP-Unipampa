/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.Peca;
import CN.Semeadora;
import CN.TipoPeca;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author rafael
 */
public class DBSemeadora {

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
    public void insertSemeadora(Semeadora semeadora) throws SQLException {

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
    public void deleteSemeadora(int codSem) throws SQLException {

        sql = "delete from semeadora where codsem=" + codSem;
        sgbd.executeSQL(sql);
    }

    /**
     *
     * @param codSem
     * @param semeadora
     * @throws SQLException
     */
    public void updateSemeadora(int codSem, Semeadora semeadora) throws SQLException {

        sql = "update semeadora set nome='" + semeadora.getModelo() + "', marca='" + semeadora.getMarca() + "', ano='" + semeadora.getAno() + "', datainclusao='" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "'";

        sgbd.executeSQL(sql);
    }

    public Semeadora selectSemeadora(int codSem) throws SQLException {

        sql = "select * from semeadora where codsem=" + codSem;
        ResultSet result = sgbd.selectData(sql);
        Semeadora semeadora = new Semeadora(result.getString("nome"), result.getString("marca"),
                (Integer) result.getObject("ano"));

        semeadora.setDataRegistro(result.getDate("dataRegistro"));
        semeadora.setIdentificacao(result.getInt("codsem"));

        return semeadora;

    }

    private Map<Integer, Divisao> selectDivisoesSemeadora(int codSem) throws SQLException {

        sql = "select * from divisao where codSem=" + codSem;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, Divisao> divisoes = new HashMap<Integer, Divisao>();

        while (result.next()) {

            int codDivisao = result.getInt("codDivisao");
            Map<Integer, AlocacaoPeca> alocacoes = selectAlocacoesPecaSemeadora(codSem, codDivisao);
            Divisao div = new Divisao(selectSemeadora(codSem), codDivisao, result.getString("nome"), alocacoes);
            divisoes.put(div.getIdentificao(), div);
        }

        return divisoes;
    }

    private Map<Integer, AlocacaoPeca> selectAlocacoesPecaSemeadora(int codSem, int codDivisao) throws SQLException {

        sql = "select * from alocacaopeca where codsem=" + codSem + " and coddivisao=" + codDivisao;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, AlocacaoPeca> alocacoes = new HashMap<Integer, AlocacaoPeca>();

        while (result.next()) {
            
            Peca pca = selectPeca(result.getInt("codtipopeca"), result.getInt("codpeca"));
            AlocacaoPeca alcPeca = new AlocacaoPeca(pca, result.getInt("tempovidautilrestante"));
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Peca selectPeca(int codTipoPeca, int codPeca) throws SQLException {

        sql = "select * from itempeca where codTipoPeca=" + codTipoPeca + " and codPeca=" + codPeca;

        ResultSet result = sgbd.selectData(sql);

        Peca peca = new Peca(result.getString("anofab"), result.getString("fabricante"), result.getDate("dataaquis"), selectTipoPeca(codTipoPeca));
        peca.setIdentificacao(result.getInt("codpeca"));

        return peca;
    }

    private TipoPeca selectTipoPeca(int codTipoPeca) throws SQLException {

        sql = "select * from TipoPeca where codTipoPeca=" + codTipoPeca;
        ResultSet result = sgbd.selectData(sql);
        String nomeTP = result.getString("nome");
        TipoPeca tipoPeca = null;

        for (TipoPeca tp : TipoPeca.values()) {

            if (nomeTP.equals(tp.toString())) {

                tipoPeca = tp;
            }
        }

        return tipoPeca;
    }
}
