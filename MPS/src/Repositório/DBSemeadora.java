/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.AlocacaoPeca;
import CN.Atividade;
import CN.DesgastePeca;
import CN.Divisao;
import CN.Fator;
import CN.ItemPeca;
import CN.Manutencao.Manutencao;
import CN.Manutencao.Reparo;
import CN.Manutencao.SubstituicaoPeca;
import CN.Peca;
import CN.Semeadora;
import CN.TipoAlocacao;
import CN.TipoAtividade;
import CN.TipoPeca;
import Exceções.AtualizacaoException;
import Exceções.CodigoInvalidoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Utilitários.DateConversion;
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
                + ", '" + DateConversion.dateToString(semeadora.getDataRegistro()) + "')";

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

                sql = "update itempeca set datainclusao='" + DateConversion.dateToString(alocPeca.getDataInclusaoItemPeca()) + "'"
                        + ", coddivisao=" + alocPeca.getDivisao().getIdentificao() + ", "
                        + "codsem=" + alocPeca.getDivisao().getSemeadora().getIdentificacao() + " where coditempeca=" + alocPeca.getItemPeca().getIdentificacao();

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

            sqle.getErrorCode();
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

        sql = "update semeadora set modelo='" + semeadora.getModelo() + "', marca='" + semeadora.getMarca() + "', ano='" + semeadora.getAno() + "', datainclusao='" + DateConversion.dateToString(semeadora.getDataRegistro()) + "'";

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

                semeadora = new Semeadora((Integer) result.getObject("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"),  DateConversion.dataBancoToDate(result.getString("dataInclusao"))/*result.getDate("datainclusao")*/);
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

        
        ResultSet resultDivs;
        try {

            resultDivs = sgbd.selectData(sql);
        } catch (SQLException ex) {

            throw new ConsultaException("Não foi possível consultar as divisões da semeadora", ex);
        }

        try {

            while (resultDivs.next()) {

                semeadora.addDivisao((Integer) resultDivs.getObject("coddivisao"), resultDivs.getString("nome"), getTipoAlocacao(resultDivs.getInt("codtipoalocacao")));
            }
        } catch (SQLException ex) {

            throw new ConsultaException("A divisão que estás tentando acessar não consta na consulta" + ex.getMessage(), ex);
        }
    }

    private void addPecas(int codsem) throws ConsultaException {

        sql = "select * from divisao inner join itempeca using(codsem, coddivisao) inner join peca using (codpeca, codtipopeca) where codsem=" + codsem;

        ResultSet resultPecas;
        try {

            resultPecas = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            System.out.println(sqle.getMessage());
            throw new ConsultaException("Não foi possível consultar as peças alocadas à semeadora", sqle);
        }

        try {

            while (resultPecas.next()) {

                Peca p = new Peca(resultPecas.getInt("codPeca"), resultPecas.getString("fabricante"), getTipoPeca(resultPecas.getInt("codtipopeca")));
                ItemPeca ip = new ItemPeca(resultPecas.getInt("coditempeca"), resultPecas.getInt("anofab"), DateConversion.dataBancoToDate(resultPecas.getString("dataaquis")), p, resultPecas.getInt("tempovidautilrestante"));
                semeadora.addPeca((Integer) resultPecas.getObject("coddivisao"), ip, DateConversion.dataBancoToDate(resultPecas.getString("datainclusao")));

                /*semeadora.addPeca((Integer) result.getObject("coddivisao"), result.getInt("coditempeca"), result.getInt("anofab"),
                 result.getDate("dataaquis"), p, result.getInt("tempovidautilrestante"), result.getDate("datainclusao"));*/
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

    private Map<Integer, Semeadora> performQuery(String sql) {

        Map<Integer, Semeadora> semeadoras = new HashMap<Integer, Semeadora>();
        this.sql = sql;
        
        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as semeadoras no banco de dados", sqle);
        }

        try {
            
            while (result.next()) {

                semeadora = new Semeadora(result.getInt("codsem"), result.getString("modelo"), result.getString("marca"), result.getInt("ano"), DateConversion.dataBancoToDate(result.getString("datainclusao")));
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
     * @return @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> listSemeadoras() throws ConsultaException {

        sql = "select * from semeadora";

        return performQuery(sql);
    }

    /**
     *
     * @param modeloSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {

        sql = "select * from semeadora where modelo like '%" + modeloSemeadora + "%'";

        return performQuery(sql);
    }

    /**
     *
     * @param marcaSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {

        sql = "select * from semeadora where marca like'%" + marcaSemeadora + "%'";

        return performQuery(sql);
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

        return performQuery(sql);
    }

    /**
     *
     * @param dataFab
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByDataInclusao(Date dataInclusao) throws ConsultaException {

        sql = "select * from semeadora where datainclusao='" + DateConversion.dateToString(dataInclusao) + "'";

        return performQuery(sql);
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

        sql = "insert into realizacaoatividade values (" + atividade.getCodigo() + ", " + atividade.getNome().getCodTipoAtividade() + ", " + codSem + ", '"
                + DateConversion.dateToString(atividade.getDataRealizacao()) + " " + atividade.getDataRealizacao().getHours() + ":"
                + atividade.getDataRealizacao().getMinutes() + ":" + atividade.getDataRealizacao().getSeconds() + "', "
                + atividade.getDuracao() + ", " + atividade.getFatores().get("solo").getCodigo() + ", "
                + atividade.getFatores().get("operador").getCodigo() + ", " + atividade.getFatores().get("velocidade de trabalho").getCodigo() + ")";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível registrar a atividade para a semeadora " + codSem + " no banco de dados", sqle);
        }

        registrarDesgasteItensPeca(atividade);
    }

    private void registrarDesgasteItensPeca(Atividade ativ) throws InsercaoException {

        for (DesgastePeca dp : ativ.listarDesgastePecas()) {

            sql = "insert into desgastepecaatividade values (" + ativ.getCodigo() + ", " + dp.getAlocacaoPeca().getItemPeca().getIdentificacao() + "," + dp.getAlocacaoPeca().getItemPeca().getTempoVidaUtilRestante() + ", " + dp.getDesgaste() + ")";

            try {

                sgbd.executeSQL(sql);
            } catch (SQLException sqle) {

                throw new InsercaoException("Não foi possível registrar o desgaste do item de peça " + dp.getAlocacaoPeca().getItemPeca() + " para a atividade " + ativ.getCodigo(), sqle);
            }

            sql = "update itempeca set tempovidautilrestante=" + dp.getAlocacaoPeca().getItemPeca().getTempoVidaUtilRestante() + "where coditempeca=" + dp.getAlocacaoPeca().getItemPeca().getIdentificacao();

            try {

                sgbd.executeSQL(sql);
            } catch (SQLException sqle) {

                throw new InsercaoException("Não foi possível atualizar o tempo de vida util restante para o item de peça " + dp.getAlocacaoPeca().getItemPeca(), sqle);
            }
        }
    }

    @Override
    public void cancelarAtividade(int codSem, int codAtiv, Date dataCancelamento) throws AtualizacaoException {

        sql = "select coditempeca from desgastepecaatividade where codrealizacaoativ=" + codAtiv;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar o desgaste das peças para a atividade " + codAtiv);
        }

        int coditempeca = 0;

        try {

            while (result.next()) {

                coditempeca = result.getInt("coditempeca");
                sql = "update itempeca set tempovidautilrestante=tempovidautilrestante+(select desgaste from desgastepecaatividade where coditempeca=" + coditempeca + " and codrealizacaoativ=" + codAtiv + ") where coditempeca=" + coditempeca;
                sgbd.executeSQL(sql);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível atualizar o tempo de vida util restante para o item de peça " + coditempeca, sqle);
        }

        sql = "update realizacaoatividade set datacancelamento='" + DateConversion.dateToString(dataCancelamento) + "'";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível atualizar a data de cancelamento para a atividade " + codAtiv, sqle);
        }

    }

    @Override
    public Semeadora carregarAtividades(Semeadora semeadora) throws ConsultaException {

        Map<String, Fator> fatores = new HashMap<String, Fator>();
        sql = "select * from realizacaoatividade where codsem=" + semeadora.getIdentificacao() + "and datacancelamento is null";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as atividades da semeadora " + semeadora, sqle);
        }

        try {

            while (result.next()) {

                fatores.put("solo", getFator(result.getInt("codsolo")));
                fatores.put("operador", getFator(result.getInt("codoperador")));
                fatores.put("velocidade de trabalho", getFator(result.getInt("codvelocidade")));

                try {

                    semeadora.realizarAtividade((Integer) result.getObject("codrealizacaoativ"), DateConversion.dataBancoToDate(result.getString("datahora")), result.getInt("duracao"), getTipoAtividade(result.getInt("codativ")), fatores);
                } catch (CodigoInvalidoException cie) {
                }
            }


        } catch (SQLException sqle) {

            System.out.println(sqle.getMessage());
            throw new AtualizacaoException("Não foi possível acessar os dados da consulta para a realização de atividades da semeadora", sqle);
        }

        return semeadora;
    }

    private Fator getFator(int codFator) {

        for (Fator f : Fator.values()) {

            if (f.getCodigo() == codFator) {

                return f;
            }
        }

        return null;
    }

    private TipoAtividade getTipoAtividade(int codTipoAtividade) {

        for (TipoAtividade ta : TipoAtividade.values()) {

            if (ta.getCodTipoAtividade() == codTipoAtividade) {

                return ta;
            }
        }

        return null;
    }

    private void insertManutencao(int codSem, Manutencao manutencao) {

        sql = "insert into manutencao values ('" + DateConversion.dateToString(manutencao.getDataRealizacao()) + " " + manutencao.getDataRealizacao().getHours() + ":" + manutencao.getDataRealizacao().getMinutes() + ":" + manutencao.getDataRealizacao().getSeconds() + "', " + codSem + ")";

        System.out.println(sql);
        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            System.out.println(sqle.getMessage());
            throw new InsercaoException("Não foi possível adcionar a manutencao " + manutencao + " no banco de dados", sqle);
        }

    }

    @Override
    public void registrarReparo(int codSem, Reparo reparo) throws InsercaoException, AtualizacaoException {

        insertManutencao(codSem, reparo);

        sql = "insert into reparo values ('" + DateConversion.dateToString(reparo.getDataRealizacao()) + " " + reparo.getDataRealizacao().getHours() + ":" + reparo.getDataRealizacao().getMinutes() + ":" + reparo.getDataRealizacao().getSeconds() + "')";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível adcionar o reparo " + reparo + " no banco de dados", sqle);
        }

        sql = "insert into reparopeca values ('" + DateConversion.dateToString(reparo.getDataRealizacao()) + " " + reparo.getDataRealizacao().getHours() + ":" + reparo.getDataRealizacao().getMinutes() + ":" + reparo.getDataRealizacao().getSeconds() + "', " + reparo.getPeca().getItemPeca().getIdentificacao() + ", " + reparo.getTempoVidaUtil() + ", " + reparo.getRestauro() + ")";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível adcionar o reparo a peca " + reparo.getPeca().getItemPeca() + " no banco de dados", sqle);
        }

        atualizaVidaUtilPeca(reparo);
    }

    private void atualizaVidaUtilPeca(Manutencao manut) throws AtualizacaoException {

        sql = "update itempeca set tempovidautilrestante=" + manut.getTempoVidaUtil() + " where coditempeca=" + manut.getPeca().getItemPeca().getIdentificacao();

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível atualizar o tempo de vida util da peca " + manut.getPeca().getItemPeca(), sqle);
        }
    }

    @Override
    public void registrarSubstituicao(int codSem, SubstituicaoPeca sub) throws InsercaoException, AtualizacaoException {

        insertManutencao(codSem, sub);

        sql = "insert into substituicao values ('" + DateConversion.dateToString(sub.getDataRealizacao()) + " " + sub.getDataRealizacao().getHours() + ":" + sub.getDataRealizacao().getMinutes() + ":" + sub.getDataRealizacao().getSeconds() + "')";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            System.out.println(sqle.getMessage());
            throw new InsercaoException("Não foi possível adcionar a substituição " + sub + " no banco de dados", sqle);
        }

        sql = "insert into substituicaopeca values ('" + DateConversion.dateToString(sub.getDataRealizacao()) + " " + sub.getDataRealizacao().getHours() + ":" + sub.getDataRealizacao().getMinutes()
                + ":" + sub.getDataRealizacao().getSeconds() + "', "  + sub.getPeca().getItemPeca().getIdentificacao() + ", " 
                + sub.getPecaSubstituta().getIdentificacao() + ", " + sub.getTempoVidaUtil() + ", " + sub.getRestauro() + ")";

        System.out.println(sql);
        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {
            System.out.println(sqle);

            throw new InsercaoException("Não foinull possível adcionar o a substituição da peça " + sub.getPeca().getItemPeca().getIdentificacao()
                    + "pela peça " + sub.getPecaSubstituta().getIdentificacao() + " no banco de dados", sqle);
        }

        trocaPecas(sub);
    }

    private void trocaPecas(SubstituicaoPeca sub) throws AtualizacaoException {

        sql = "update itempeca set datainclusao=null, codsem=null, coddivisao=null where coditempeca=" + sub.getPeca().getItemPeca().getIdentificacao();

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível desalocar a peça " + sub.getPeca().getItemPeca().getIdentificacao(), sqle);
        }

        sql = "update itempeca set datainclusao='" + DateConversion.dateToString(sub.getDataRealizacao()) + "' , codsem=" + sub.getSemeadora().getIdentificacao() + ", coddivisao="
                + sub.getPeca().getDivisao().getIdentificao() + "where coditempeca=" + sub.getPecaSubstituta().getIdentificacao();

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível alocar a peça " + sub.getPecaSubstituta().getIdentificacao(), sqle);
        }
    }
}
