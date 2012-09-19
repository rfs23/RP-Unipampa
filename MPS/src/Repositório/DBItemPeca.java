/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.ItemPeca;
import CN.Peca;
import CN.TipoPeca;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o código máximo para itens de peça no banco de dados", sqle);
        }

        try {

            result.next();
            return result.getInt("maxcod");
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para máximo código de itens de peça", sqle);
        }

    }

    @Override
    public void insertItemPeca(ItemPeca itemPeca) throws InsercaoException {

        sql = "insert into itempeca(coditempeca, codpeca, codtipopeca, anofab, dataaquis, tempovidautilrestante) values ("
                + itemPeca.getIdentificacao() + ", " + itemPeca.getPeca().getIdentificacao() + ", "
                + itemPeca.getPeca().getTipo().getCodTipoPeca() + ", " + itemPeca.getAnoFab() + ", '" + itemPeca.getDataAquis().getDate()
                + "/" + itemPeca.getDataAquis().getMonth() + "/" + (itemPeca.getDataAquis().getYear() + 1900) + "', " + itemPeca.getTempoVidaUtilRestante() + ")";

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new InsercaoException("Não foi possível inserir o item de peça no banco de dados", sqle);
        }
    }

    @Override
    public void deleteItemPeca(int codItemPeca) throws DelecaoException {

        sql = "delete from itempeca where coditempeca=" + codItemPeca;

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new DelecaoException("Não foi possível deletar o item de peça do banco de dados", sqle);
        }
    }

    @Override
    public void updateItemPeca(int codItempeca, ItemPeca itemPeca) throws AtualizacaoException {

        sql = "update itempeca set coditempeca=" + itemPeca.getIdentificacao() + ", codpeca=" + itemPeca.getPeca().getIdentificacao() + ", codtipopeca="
                + itemPeca.getPeca().getTipo().getCodTipoPeca() + ", anofab=" + itemPeca.getAnoFab() + ", dataaquis='" + itemPeca.getDataAquis().getDate()
                + "/" + itemPeca.getDataAquis().getMonth() + "/" + (itemPeca.getDataAquis().getYear() + 1900) + "', tempovidautilrestante=" + itemPeca.getTempoVidaUtilRestante() + " where coditempeca=" + codItempeca;

        try {

            sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizacaoException("Não foi possível atualizar o item de peca de código " + codItempeca + " no banco de dados", sqle);
        }
    }

    private Map<Integer, ItemPeca> performQuery(String sql) {

        Map<Integer, ItemPeca> itensPeca = new HashMap<Integer, ItemPeca>();

        this.sql = sql;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar o item de peça no banco de dados", sqle);
        }

        try {

            while (result.next()) {

                Peca peca = new Peca(result.getInt("codpeca"), result.getString("fabricante"), getTipoPeca(result.getInt("codtipopeca")));
                Date dataAquis = result.getDate("dataaquis");
                //dataAquis.setYear(dataAquis.getDate() - 1900);
                ItemPeca itemPeca = new ItemPeca((Integer) result.getInt("coditempeca"), Integer.parseInt(result.getString("anofab")), dataAquis, peca, result.getInt("tempovidautilrestante"));
                itensPeca.put(itemPeca.getIdentificacao(), itemPeca);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para item de peça", sqle);
        }

        return itensPeca;
    }

    private TipoPeca getTipoPeca(int codTipoPeca) {

        for (TipoPeca tp : TipoPeca.values()) {

            if (tp.getCodTipoPeca() == codTipoPeca) {

                return tp;
            }
        }

        return null;
    }

    @Override
    public ItemPeca selectItemPeca(int codItemPeca) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where coditempeca=" + codItemPeca;

        return performQuery(sql).get(codItemPeca);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaByTipo(TipoPeca tipoPeca) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where codtipopeca=" + tipoPeca.getCodTipoPeca();

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByTipo(TipoPeca tipoPeca) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where codtipopeca=" + tipoPeca.getCodTipoPeca() + "and codsem is null";

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaByFabricante(String fabricante) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where fabricante='" + fabricante + "'";

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByFabricante(String fabricante) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where fabricante='" + fabricante + "' and codsem is null";

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaByAnoFabricacao(int anoFabricacao) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where anofab=" + anoFabricacao;

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByAnoFabricacao(int anoFabricacao) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where anofab=" + anoFabricacao + "and codsem is null";

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaByDataAquisicao(Date dataAquisicao) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where dataaquis='" + dataAquisicao.getDate() + "/" + dataAquisicao.getMonth() + "/" + (dataAquisicao.getYear() + 1900) + "'";

        return performQuery(sql);
    }

    @Override
    public Map<Integer, ItemPeca> selectItensPecaNaoAlocadosByDataAquisicao(Date dataAquisicao) throws ConsultaException {

        sql = "select * from itempeca natural join peca natural join tipopeca where dataaquis='" + dataAquisicao.getDate() + "/" + dataAquisicao.getMonth() + "/" + (dataAquisicao.getYear() + 1900) + "' and cosem is null";

        return performQuery(sql);
    }
}
