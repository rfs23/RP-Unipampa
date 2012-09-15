/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositório;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.ItemPeca;
import CN.Peca;
import CN.Semeadora;
import CN.TipoAlocacao;
import CN.TipoPeca;
import Exceções.AtualizaçãoException;
import Exceções.ConsultaException;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
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

        sql = "insert into semeadora values ( "
                + semeadora.getIdentificacao() + ", '"+ semeadora.getModelo() + "', '" + semeadora.getMarca() + "', " + semeadora.getAno()
                + ", '" + semeadora.getDataRegistro().getDate() + "/" + (semeadora.getDataRegistro().getMonth() + 1)
                + "/" + (semeadora.getDataRegistro().getYear() + 1900) + "')";

        try {

            this.sgbd.executeSQL(sql);
            insertDivisao(semeadora.listarDivisoes());
        } catch (SQLException sqle) {

            throw new InserçãoException("Não foi possível incluir a semeadora no banco de dados", sqle);
        }

    }
    
    private void insertDivisao(List<Divisao> divisoes) throws InserçãoException{
        
        try{
            
            for(Divisao div: divisoes){
                
                sql = "insert into divisao values (" + div.getSemeadora().getIdentificacao() + ", " + div.getIdentificao() + ", '" + div.getNome() + "', " + div.getTipoAloc().getCodTipoAlocacao() + ")";
                sgbd.executeSQL(sql);
                insertAlocacoesPeca(div.listarPecas());
            }
        }catch(SQLException sqle){
            
            throw new InserçãoException ("Não foi possível adicionar a divisão no banco de dados", sqle);
        }
    }
    
    private void insertAlocacoesPeca(List<AlocacaoPeca> alocacoes) throws InserçãoException{
        
        try{
            
            for(AlocacaoPeca alocPeca: alocacoes){
                
                sql = "update itempeca set datainclusao='" + alocPeca.getDataInclusaoItemPeca().getDate() + "/" + (alocPeca.getDataInclusaoItemPeca().getMonth()+1) + "/" 
                        + (alocPeca.getDataInclusaoItemPeca().getYear() + 1900) + "'"
                        + ", coddivisao=" + alocPeca.getDivisao().getIdentificao() + ", "
                        + "codsem=" + alocPeca.getDivisao().getSemeadora().getIdentificacao() + " where coditempeca=" + alocPeca.getItemPeca().getIdentificacao();
                System.out.println(sql);
                sgbd.executeSQL(sql);
            }
        }catch(SQLException sqle){
            
            throw new InserçãoException("Não foi possível inserir a alocação de peça no banco de dados",sqle);
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
    public void deleteSemeadora(int codSem) throws DeleçãoException {

        sql = "update itempeca set datainclusao=null where codsem="+ codSem;
        
        try{
            
            sgbd.executeSQL(sql);
        }catch (SQLException sqle){
            
            throw new AtualizaçãoException("Não foi possível alterar o valor "
                    + "da data da inclusão da peça para itens de peça no banco de dados",sqle);
        }
        
        sql = "delete from semeadora where codsem=" + codSem;
        try {

            this.sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new DeleçãoException("Não foi possível deletar a semeadora do banco de dados", sqle);
        }
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

        try {

            this.sgbd.executeSQL(sql);
        } catch (SQLException sqle) {

            throw new AtualizaçãoException("Não foi possível atualizar a semeadora no banco de dados", sqle);
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

        Semeadora semeadora = null;

        try {

            semeadora = new Semeadora(result.getString("nome"), result.getString("marca"),
                    (Integer) result.getObject("ano"));
            semeadora.setIdentificacao(result.getInt("codsem"));
            semeadora.setDataRegistro(result.getDate("dataRegistro"));
            
        } catch (SQLException ex) {

            throw new ConsultaException("Coluna nome, marca ou ano não existe", ex);
        }
        
        for(Divisao divisao: selectDivisoesSemeadora(codSem).values()){
            
            semeadora.addDivisao(divisao.getIdentificao(), divisao.getNome(), divisao.getTipoAloc());
            for(AlocacaoPeca alocPeca: selectAlocacoesPecaSemeadora(codSem, divisao.getIdentificao()).values()){
                
                semeadora.addPeca(divisao.getIdentificao(), alocPeca.getItemPeca().getIdentificacao(), 
                        alocPeca.getItemPeca().getAnoFab(), alocPeca.getItemPeca().getDataAquis(), alocPeca.getItemPeca().getPeca(),
                        alocPeca.getItemPeca().getTempoVidaUtilRestante(), alocPeca.getDataInclusaoItemPeca());
            }
        }
        
        return semeadora;

    }

    /**
     *
     * @param codSem
     * @return
     * @throws ConsultaException
     */
    private Map<Integer, Divisao> selectDivisoesSemeadora(int codSem) throws ConsultaException {

        sql = "select * from divisao where codSem=" + codSem;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException ex) {

            throw new ConsultaException("Não foi possível consultar as divisões da semeadora", ex);
        }

        HashMap<Integer, Divisao> divisoes = new HashMap<Integer, Divisao>();
        try {
            while (result.next()) {

                Divisao div = new Divisao(result.getInt("coddivisao"), result.getString("nome"), selectTipoAlocacao(result.getInt("codtipoalocacao")));
                divisoes.put(div.getIdentificao(), div);
            }
        } catch (SQLException ex) {

            throw new ConsultaException("A linha que está tentando acessar não consta na consulta", ex);
        }

        return divisoes;
    }

    /**
     *
     * @param codSem
     * @param codDivisao
     * @return
     * @throws ConsultaException
     */
    private Map<Integer, AlocacaoPeca> selectAlocacoesPecaSemeadora(int codSem, int codDivisao) throws ConsultaException {

        sql = "select * from itempeca where codsem=" + codSem + " and coddivisao=" + codDivisao;

         try {

         result = sgbd.selectData(sql);
         } catch (SQLException ex) {

         throw new ConsultaException("Não foi possível consultar as alocações da divisão " + codDivisao + " da semeadora " + codSem, ex);
         }

        HashMap<Integer, AlocacaoPeca> alocacoes = new HashMap<Integer, AlocacaoPeca>();
        Map<Integer, ItemPeca> itensDivisao = new HashMap<Integer, ItemPeca>();

        try {

            while (result.next()) {

                Peca peca = selectPeca(result.getInt("codtipopeca"), result.getInt("codpeca"));
                //ItemPeca itemPeca = new ItemPeca(result.getInt("coditempeca"), result.getInt("anofab"), result.getDate("dataquis"), peca);
                //itensPecas.put(itemPeca.getIdentificacao(), itemPeca);
 //correto               AlocacaoPeca alocPeca = new AlocacaoPeca();

            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado da consulta para alocações de peça", sqle);
        }

        return alocacoes;
    }

    /**
     *
     * @return
     */
    private Map<Integer, ItemPeca> selectItensPecas(int codSem, int codDivisao) throws ConsultaException {

        String sql = "select * from itempeca where codsem=" + codSem + " and codDivisao=" + codDivisao;
        ResultSet result;

        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter os itens de peças da divisão "
                    + codDivisao + " da semeadora " + codSem + " através do banco de dados", sqle);
        }

        HashMap<Integer, ItemPeca> itensPecas = new HashMap<Integer, ItemPeca>();

        try {

            while (result.next()) {

                Peca peca = selectPeca(result.getInt("codtipopeca"), result.getInt("codpeca"));
              //  ItemPeca itemPeca = new ItemPeca(result.getInt("coditempeca"), result.getInt("anofab"), result.getDate("dataquis"), peca);
              //  itensPecas.put(itemPeca.getIdentificacao(), itemPeca);
            }
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado da consulta no banco de dados para itens de peças", sqle);
        }

        return itensPecas;
    }

    /**
     *
     * @param codTipoPeca
     * @param codPeca
     * @return
     * @throws ConsultaException
     */
    private Peca selectPeca(int codTipoPeca, int codPeca) throws ConsultaException {

        sql = "select * from itempeca where codTipoPeca=" + codTipoPeca + " and codPeca=" + codPeca;

        ResultSet result;
        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter a peça associada ao item peça do banco de dados", sqle);
        }

        Peca peca = null;

        try {

            peca = new Peca(result.getInt("codpeca"), result.getString("fabricante"), selectTipoPeca(codTipoPeca));
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar o resultado da consulta no banco de dados para peça", sqle);
        }

        return peca;
    }

    /**
     *
     * @param codTipoPeca
     * @return
     * @throws ConsultaException
     */
    private TipoPeca selectTipoPeca(int codTipoPeca) throws ConsultaException {

        sql = "select nome, estvidautil from TipoPeca where codTipoPeca=" + codTipoPeca;

        ResultSet result = null;
        try {

            result = sgbd.selectData(sql);
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível obter o tipo de peça de código " + codTipoPeca, sqle);
        }

        String nomeTP = null;
        int estVidaUtilTP = 0;
        try {

            nomeTP = result.getString("nome");
            estVidaUtilTP = result.getInt("estvidautil");
        } catch (SQLException sqle) {

            throw new ConsultaException("Não foi possível acessar os dados da consulta no banco dados para tipo de peça", sqle);
        }

        TipoPeca tipoPeca = null;

        for (TipoPeca tp : TipoPeca.values()) {

            if (nomeTP.equals(tp.toString())) {

                tipoPeca = tp;
                tipoPeca.setEstVidaUtil(estVidaUtilTP);
            }
        }

        return tipoPeca;
    }
    
    private TipoAlocacao selectTipoAlocacao(int codTipoAlocacao){
        
        for(TipoAlocacao ta: TipoAlocacao.values()){
            
            if(codTipoAlocacao == ta.getCodTipoAlocacao())
                return ta;
        }
        
        return null;
    }

    /**
     *
     * @return @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> listSemeadoras() throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param codSem
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByCode(int codSem) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param modeloSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param marcaSemeadora
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param ano
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByAno(int ano) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param dataFab
     * @return
     * @throws ConsultaException
     */
    @Override
    public Map<Integer, Semeadora> selectSemeadorasByDataFab(Date dataFab) throws ConsultaException {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
