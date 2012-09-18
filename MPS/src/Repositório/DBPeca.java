/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.Peca;
import CN.TipoPeca;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class DBPeca implements RepositorioPeca {

    private AcessoBD sgbd;
    private String sql;
    private ResultSet result;

    public DBPeca(AcessoBD sgbd) {

        this.sgbd = sgbd;
    }

    @Override
    public int getMaxCodPeca() throws ConsultaException {

        sql = "select max(codpeca) as maxcod from peca";

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o código máximo para peças no banco de dados", sqle);
        }


        try {

            result.next();
            return result.getInt("maxcod");
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para máximo código de peças", sqle);
        }

    }

    @Override
    public void insertPeca(Peca peca) throws InserçãoException {

        sql = "insert into peca values (" + peca.getIdentificacao() + ", " + peca.getTipo().getCodTipoPeca() + ", '" + peca.getFabricante() + "')";

        try {

            sgbd.executeSQL(sql);

        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível inserir a peça no banco de dados", sqle);
        }
    }

    @Override
    public void deletePeca(int codPeca) throws DeleçãoException {

        sql = "delete from peca where codpeca=" + codPeca;

        try {

            sgbd.executeSQL(sql);

        } catch (SQLException sqle) {

            throw new DeleçãoException("Não foi possível deletar a peça do banco de dados", sqle);
        }
    }

    @Override
    public void updatePeca(int codPeca, Peca peca) throws AtualizaçãoException {

        sql = "update peca set codpeca=" + peca.getIdentificacao() + ", codtipopeca=" + peca.getTipo().getCodTipoPeca() + ", fabricante='" + peca.getFabricante() + "' where codpeca=" + codPeca;

        try {

            sgbd.executeSQL(sql);

        } catch (SQLException sqle) {

            throw new AtualizaçãoException("Não foi possível atualizar a peça no banco de dados", sqle);
        }
    }

    @Override
    public Peca selectPeca(int codPeca) throws ConsultaException {

        sql = "select from peca where codpeca=" + codPeca;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar a peça no banco de dados", sqle);
        }

        try {

            if (result.next()) {

                Peca peca = new Peca((Integer) result.getObject("codpeca"), result.getString("fabricante"), getTipoPeca(result.getInt("codtipopeca")));
                return peca;
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para peças", sqle);
        }

        return null;
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
    public Map<Integer, Peca> selectPecaByFabricante(String fabricante) throws ConsultaException {

        sql = "select * from peca where fabricante='" + fabricante + "'";

        Map<Integer, Peca> pecas = new HashMap<Integer, Peca>();

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as peças no banco de dados", sqle);
        }

        try {

            while (result.next()) {

                Peca peca = new Peca((Integer) result.getObject("codpeca"), result.getString("fabricante"), getTipoPeca(result.getInt("codtipopeca")));
                pecas.put(peca.getIdentificacao(), peca);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para peças", sqle);
        }

        return pecas;
    }

    @Override
    public Map<Integer, Peca> selectPecaByTipoPeca(int codTipoPeca) throws ConsultaException {

        sql = "select * from peca where codtipopeca='" + codTipoPeca + "'";

        Map<Integer, Peca> pecas = new HashMap<Integer, Peca>();

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível consultar as peças no banco de dados", sqle);
        }

        try {

            while (result.next()) {

                Peca peca = new Peca((Integer) result.getObject("codpeca"), result.getString("fabricante"), getTipoPeca(result.getInt("codtipopeca")));
                pecas.put(peca.getIdentificacao(), peca);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta para peças", sqle);
        }

        return pecas;
    }
}
