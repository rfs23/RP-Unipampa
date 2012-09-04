package CN;

import DB.AcessoPostgres;
import DB.DBSemeadora;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class Semeadora extends Observable {
    
    private int identificacao;
    private String modelo;
    private String marca;
    private int ano;
    private Date dataRegistro;
    private Collection<Divisao> divisao;
    private Collection<AlocacaoPeca> alocacaoPeca;
    private Collection<Atividade> atividade;
    private Collection<Manutencao> manutencao;
    private AdministradorManutencoes administradorManutencoes;
    
    public Semeadora(String modelo, String marca, int ano) {
        
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.dataRegistro = Calendar.getInstance().getTime();
    }
    
    public Semeadora(){
        
    }
    
    public void addDivisao(Divisao divisao) {
        
    }
    
    public Divisao selecionarDivisao(String nome) {
        return null;
    }
    
    public void realizarAtividade(Date data, Date duracao, TipoAtividade nome, Map fatores) {
        
    }
    
    public void addAtividade(Atividade ativ) {
        
    }
    
    public List listarAtividades() {
        return null;
    }
    
    public void excluirAtividade(int cod) {
        
    }
    
    public List listarDivisoes() {
        return null;
    }
    
    public void addManutencao(Manutencao manutencao) {
        
    }
    
    public void sofrerReparo(Date data, AlocacaoPeca peca, int porcVidaUtil) {
        
    }
    
    public AlocacaoPeca selecionarPeca(int cod) {
        return null;
    }
    
    public void sofrerSubstituicaoPeca(Date data, AlocacaoPeca pecaSubstituida, ItemPeca pecaSubstituta, int porcVidaUtil) {
        
    }
    
    public List listarManutencoes() {
        return null;
    }
    
    public void excluirManutencao(int cod) {
        
    }
    
    public void editarReparo(int cod, Date data, int porcVidaUtil) {
        
    }
    
    public Reparo selecionarReparo(int cod) {
        return null;
    }

    /**
     *
     */
    public boolean matches(Map caracteristicas) {
        return false;
    }

    /**
     * @return the identificacao
     */
    public int getIdentificacao() {
        return identificacao;
    }

    /**
     * @return the modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @return the ano
     */
    public int getAno() {
        return ano;
    }

    /**
     * @return the dataRegistro
     */
    public Date getDataRegistro() {
        return dataRegistro;
    }
    
    public void saveSemeadora() throws SQLException {
        
        new DBSemeadora(AcessoPostgres.getInstance()).addSemeadora(this);
    }
    
    public void removeSemeadora() throws SQLException {
        
        new DBSemeadora(AcessoPostgres.getInstance()).excluirSemeadora(this.getIdentificacao());
    }
    
    public void removeSemeadora(int codSem) throws SQLException {
        
        new DBSemeadora(AcessoPostgres.getInstance()).excluirSemeadora(codSem);
    }

    /**
     * @param identificacao the identificacao to set
     */
    public void setIdentificacao(int identificacao) {
        this.identificacao = identificacao;
    }

    /**
     * @param dataRegistro the dataRegistro to set
     */
    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
