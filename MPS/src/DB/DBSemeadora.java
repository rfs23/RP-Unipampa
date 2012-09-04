/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.ItemPeca;
import CN.Semeadora;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rafael
 */
public class DBSemeadora {

    AcessoBD sgbd;

    public DBSemeadora(AcessoBD sgbd) {

        this.sgbd = sgbd;
    }

    /**
     * Método para adcionar um objeto Semeadora em um Banco de Dados.
     *
     * @param semeadora Objeto semeadora a ser adcionado no banco de dados.
     * @throws SQLException Caso haja algum problema com o banco de dados.
     */
    public void addSemeadora(Semeadora semeadora) throws SQLException {

        String sql = "insert into semeadora(nome, marca, ano, datainclusao) values ( '"
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
    public void excluirSemeadora(int codSem) throws SQLException {

        String sql = "delete from semeadora where codsem=" + codSem;
        sgbd.executeSQL(sql);
    }

    /**
     *
     * @param codSem
     * @param semeadora
     * @throws SQLException
     */
    public void editarSemeadora(int codSem, Semeadora semeadora) throws SQLException {

        String sql = "update semeadora set nome='" + semeadora.getModelo() + "', marca='" + semeadora.getMarca() + "', ano='" + semeadora.getAno() + "', datainclusao='" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "'";

        sgbd.executeSQL(sql);
    }

    public Semeadora buscarSemeadora(int codSem) throws SQLException {

        String sql = "select * from semeadora where codsem=" + codSem;
        ResultSet result = sgbd.selectData(sql);
        Semeadora semeadora = new Semeadora(result.getString("nome"), result.getString("marca"),
                (Integer) result.getObject("ano"));

        semeadora.setDataRegistro(result.getDate("dataRegistro"));
        semeadora.setIdentificacao(result.getInt("codsem"));

        return semeadora;

    }

    private Map<Integer, Divisao> buscarDivisoesSemeadora(int codSem) throws SQLException {

        String sql = "select * from divisao where codSem=" + codSem;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, Divisao> divisoes = new HashMap<Integer, Divisao>();

        while (result.next()) {

            int codDivisao = result.getInt("codDivisao");
            Map<Integer, AlocacaoPeca> alocacoes = buscarAlocacoesPecaSemeadora(codSem, codDivisao);
            Divisao div = new Divisao(buscarSemeadora(codSem), codDivisao, result.getString("nome"), alocacoes);
            divisoes.put(div.getIdentificao(), div);
        }

        return divisoes;
    }

    private Map<Integer, AlocacaoPeca> buscarAlocacoesPecaSemeadora(int codSem, int codDivisao) throws SQLException {

        String sql = "select * from alocacaopeca where codsem=" + codSem + " and coddivisao=" + codDivisao;
        ResultSet result = sgbd.selectData(sql);

        HashMap<Integer, AlocacaoPeca> alocacoes = new HashMap<Integer, AlocacaoPeca>();

        while (result.next()) {
            
            ItemPeca itemPeca = new ItemPeca();
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
