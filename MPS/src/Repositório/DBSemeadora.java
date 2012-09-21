/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.AlocacaoPeca;
import CN.Atividade;
import CN.Divisao;
import CN.Peca;
import CN.Semeadora;
import CN.TipoAlocacao;
import CN.TipoPeca;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class DBSemeadora implements RepositorioSemeadoras {

    private AcessoBD sgbd;
    private String sql;
    private ResultSet result;
    private Semeadora semeadora;

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
    public void insertSemeadora(Semeadora semeadora) throws InsercaoException {

        sql = "insert into semeadora values ( "
                + semeadora.getIdentificacao() + ", '" + semeadora.getModelo() + "', '" + semeadora.getMarca() + "', " + semeadora.getAno()
                + ", '" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "')";

        try {

            this.sgbd.executeSQL(sql);
            insertDivisao(semeadora.listarDivisoes());
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível incluir a semeadora no banco de dados", sqle);
        }

    }

    private void insertDivisao(List<Divisao> divisoes) throws InsercaoException {

        try {

            for (Divisao div : divisoes) {

                sql = "insert into divisao values (" + div.getSemeadora().getIdentificacao() + ", " + div.getIdentificao() + ", '" + div.getNome() + "', " + div.getTipoAloc().getCodTipoAlocacao() + ")";
                sgbd.executeSQL(sql);
                insertAlocacoesPeca(div.listarPecas());
            }
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível adicionar a divisão no banco de dados", sqle);
        }
    }

    private void insertAlocacoesPeca(List<AlocacaoPeca> alocacoes) throws InsercaoException {

        try {

            for (AlocacaoPeca alocPeca : alocacoes) {

                sql = "update itempeca set datainclusao='" + alocPeca.getDataInclusaoItemPeca().getDate() + "/" + (alocPeca.getDataInclusaoItemPeca().getMonth() + 1) + "/"
                        + (alocPeca.getDataInclusaoItemPeca().getYear() + 1900) + "'"
                        + ", coddivisao=" + alocPeca.getDivisao().getIdentificao() + ", "
                        + "codsem=" + alocPeca.getDivisao().getSemeadora().getIdentificacao() + " where coditempeca=" + alocPeca.getItemPeca().getIdentificacao();
                System.out.println(sql);
                sgbd.executeSQL(sql);
            }
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível inserir a alocação de peça no banco de dados", sqle);
        }
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
    public void deleteSemeadora(int codSem) throws DelecaoException {

        sql = "update itempeca set datainclusao=null where codsem=" + codSem;

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível alterar o valor "
                    + "da data da inclusão da peça para itens de peça no banco de dados", sqle);
        }

        sql = "delete from semeadora where codsem=" + codSem;
        try {

            this.sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new DelecaoException("Não foi possível deletar a semeadora do banco de dados", sqle);
        }
    }

    /**
     *
     * @param codSem
     * @param semeadora
     * @throws SQLException
     */
    @Override
    public void updateSemeadora(int codSem, Semeadora semeadora) throws AtualizacaoException {

        sql = "update semeadora set modelo='" + semeadora.getModelo() + "', marca='" + semeadora.getMarca() + "', ano='" + semeadora.getAno() + "', datainclusao='" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "'";

        try {

            this.sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível atualizar a semeadora no banco de dados", sqle);
        }
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
        result = null;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException ex) {

            throw new ConsultaException("Não foi possível consultar a semeadora no banco de dados", ex);
        }


        try {

            if (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
            }

        } catch (SQLException ex) {

            throw new ConsultaException("Coluna modelo, marca ou ano não existe", ex);
        }

        /*for (Divisao divisao : selectDivisoesSemeadora(codSem).values()) {

         semeadora.addDivisao(divisao.getIdentificao(), divisao.getNome(), divisao.getTipoAloc());
         for (AlocacaoPeca alocPeca : selectAlocacoesPecaSemeadora(codSem, divisao.getIdentificao()).values()) {

         semeadora.addPeca(divisao.getIdentificao(), alocPeca.getItemPeca().getIdentificacao(),
         alocPeca.getItemPeca().getAnoFab(), alocPeca.getItemPeca().getDataAquis(), alocPeca.getItemPeca().getPeca(),
         alocPeca.getItemPeca().getTempoVidaUtilRestante(), alocPeca.getDataInclusaoItemPeca());
         }
         }*/

        addDivisoes(codSem);
        addPecas(codSem);

        return semeadora;

    }

    private void addDivisoes(int codsem) throws ConsultaException {

        sql = "select * from divisao where codsem=" + codsem;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException ex) {

            throw new ConsultaException("Não foi possível consultar as divisões da semeadora", ex);
        }

        try {

            while (result.next()) {

                semeadora.addDivisao((Integer) result.getObject("coddivisao"), result.getString("nome"), getTipoAlocacao(result.getInt("codtipoalocacao")));
            }
        } catch (SQLException ex) {

            throw new ConsultaException("A divisão que estás tentando acessar não consta na consulta", ex);
        }
    }

    private void addPecas(int codsem) throws ConsultaException {

        sql = "select * from divisao inner join itempeca using(codsem, coddivisao) inner join peca using (codpeca, codtipopeca) where codsem=" + codsem;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as peças alocadas à semeadora", sqle);
        }

        try {

            while (result.next()) {

                Peca p = new Peca(result.getInt("codPeca"), result.getString("fabricante"), getTipoPeca(result.getInt("codtipopeca")));
                semeadora.addPeca((Integer) result.getObject("coddivisao"), result.getInt("coditempeca"), result.getInt("anofab"),
                        result.getDate("dataaquis"), p, result.getInt("tempovidautilrestante"), result.getDate("datainclusao"));
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado da consulta para as peças alocadas à semeadora", sqle);
        }
    }

    private TipoAlocacao getTipoAlocacao(int codTipoAlocacao) {

        for (TipoAlocacao tipoAloc : TipoAlocacao.values()) {

            if (tipoAloc.getCodTipoAlocacao() == codTipoAlocacao) {

                return tipoAloc;
            }
        }

        return null;
    }

    private TipoPeca getTipoPeca(int codTipoPeca) {


        for (TipoPeca tipoPeca : TipoPeca.values()) {

            if (tipoPeca.getCodTipoPeca() == codTipoPeca) {

                return tipoPeca;
            }
        }

        return null;
    }

    /**
     *
     * @return @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> listSemeadoras() throws ConsultaException {

        sql = "select * from semeadora";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();

        try {

            while (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
                addDivisoes(semeadora.getIdentificacao());
                addPecas(semeadora.getIdentificacao());
                semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para semeadoras", sqle);
        }

        return semeadoras;
    }

    /**
     *
     * @param modeloSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {

        sql = "select * from semeadora where modelo='" + modeloSemeadora + "'";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();

        try {

            while (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
                addDivisoes(semeadora.getIdentificacao());
                addPecas(semeadora.getIdentificacao());
                semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para semeadoras", sqle);
        }

        return semeadoras;
    }

    /**
     *
     * @param marcaSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {

        sql = "select * from semeadora where marca='" + marcaSemeadora + "'";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();

        try {

            while (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
                addDivisoes(semeadora.getIdentificacao());
                addPecas(semeadora.getIdentificacao());
                semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para semeadoras", sqle);
        }

        return semeadoras;
    }

    /**
     *
     * @param ano
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByAno(int ano) throws ConsultaException {

        sql = "select * from semeadora where ano='" + ano + "'";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();

        try {

            while (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
                addDivisoes(semeadora.getIdentificacao());
                addPecas(semeadora.getIdentificacao());
                semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para semeadoras", sqle);
        }

        return semeadoras;
    }

    /**
     *
     * @param dataFab
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByDataInclusao(Date dataInclusao) throws ConsultaException {

        sql = "select * from semeadora where datainclusao='" + dataInclusao.getDate() + "/" + (dataInclusao.getMonth() + 1)
                + "/" + (dataInclusao.getYear() + 1900) + "'";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();

        try {

            while (result.next()) {

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), result.getDate("datainclusao"));
                addDivisoes(semeadora.getIdentificacao());
                addPecas(semeadora.getIdentificacao());
                semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para semeadoras", sqle);
        }

        return semeadoras;
    }

    @Override
    public int getMaxCodSemeadora() throws ConsultaException {

        sql = "select max(codsem) as codmax from semeadora";

        try {
            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o maior código da tabela semeadora", sqle);
        }

        int maxCodSem = 0;

        try {

            result.next();
            maxCodSem = result.getInt("codmax");
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado de maior código para semadoras", sqle);
        }

        return maxCodSem;
    }

    @Override
    public int getMaxCodDivisao(int codSem) throws ConsultaException {

        sql = "select max(coddivisao) as codmax from divisao where codsem=" + codSem;

        try {
            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o maior código da tabela divisão para o código de semeadora informado", sqle);
        }

        int maxCodDivisao = 0;

        try {

            result.next();
            maxCodDivisao = result.getInt("codmax");
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado de maior código para divisões", sqle);
        }

        return maxCodDivisao;
    }

    @Override
    public int getMaxCodRealizacaoAtiv() throws ConsultaException {

        sql = "select max(codrealizacaoativ) as codmax from realizacaoatividade";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o maior código para realização de atividade do banco de dados", sqle);
        }

        int maxCodRealizazaoAtiv = 0;

        try {

            if (result.next()) {

                maxCodRealizazaoAtiv = result.getInt("codmax");
            }

        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado de maior código para realização de atividade", sqle);
        }

        return maxCodRealizazaoAtiv;
    }

    @Override
    public void registrarAtividade(int codSem, Atividade atividade) throws InsercaoException {
        
        
    }
}
