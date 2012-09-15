package CN;

import Cadastro.CadastroSemeadoras;
import Exceções.ConsultaException;
import Repositório.AcessoPostgres;
import Repositório.DBSemeadora;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.TreeMap;

public class Semeadora extends Observable {

    private int identificacao;
    private String modelo;
    private String marca;
    private int ano;
    private Date dataRegistro;
    private Map<Integer, Divisao> divisoes;
    private Map<Integer, Atividade> atividades;
    private Map<Integer, Manutencao> manutencoes;
    //private AdministradorManutencoes administradorManutencoes;

    public Semeadora(String modelo, String marca, int ano) throws ConsultaException {

        this(new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance())).gerarCódigoSemeadora(), modelo, marca, ano);
    }

    public Semeadora(int identificacao, String modelo, String marca, int ano) {

        this.identificacao = identificacao;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.dataRegistro = Calendar.getInstance().getTime();
        this.divisoes = new HashMap<Integer, Divisao>();
    }

    /*public Divisao addDivisao(Divisao divisao) {

     if(divisao != null){
     if(divisao.getSemeadora() != null)
     divisao.getSemeadora().divisoes.remove(divisao.getIdentificao());
            
     return divisoes.put(divisao.getIdentificao(), divisao);
     } else {

     return null;
     }

     }*/
    public Divisao addDivisao(String nome, TipoAlocacao tipoAloc) {

        Divisao div = new Divisao(nome, tipoAloc, this);
        Divisao divAnterior = this.divisoes.put(div.getIdentificao(), div);
        div.setSemeadora(this);
        return divAnterior;
    }

    public Divisao addDivisao(int codDivisao, String nome, TipoAlocacao tipoAloc) {

        Divisao div = new Divisao(codDivisao, nome, tipoAloc);
        Divisao divAnterior = this.divisoes.put(div.getIdentificao(), div);
        div.setSemeadora(this);
        return divAnterior;
    }

    public Divisao selecionarDivisao(String nome) {

        for (Entry<Integer, Divisao> sem : divisoes.entrySet()) {

            if (sem.getValue().getNome().equals(nome)) {

                return sem.getValue();
            }
        }

        return null;
    }

    public Divisao selecionarDivisao(int codDivisao) {

        return divisoes.get(codDivisao);
    }

    public Divisao excluirDivisao(int codDivisao) {

        try {

            Divisao div = divisoes.get(codDivisao);
            div.setSemeadora(null);
            divisoes.remove(codDivisao);

            return div;
        } catch (Exception ex) {

            return null;
        }

    }

    public List listarDivisoes() {

        return new ArrayList<Divisao>(this.divisoes.values());
    }

    public AlocacaoPeca addPeca(int codDivisao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusao) {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante, dataInclusao);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }

    }

    public AlocacaoPeca addPeca(int codDivisao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
            return alocPecaAnterior;

        } catch (NullPointerException npe) {

            return null;
        }
    }

    public AlocacaoPeca addPeca(int codDivisao, int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusaoPeca) {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante, dataInclusaoPeca);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }
    }

    public AlocacaoPeca addPeca(int codDivisao, int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }
    }

    public void realizarAtividade(Date data, Date duracao, TipoAtividade nome, Map fatores) {
    }

    public Atividade addAtividade(Atividade ativ) {

        return atividades.put(ativ.getCodigo(), ativ);
    }

    public List<Atividade> listarAtividades() {

        return new ArrayList<Atividade>(atividades.values());
    }

    public Atividade excluirAtividade(int codAtiv) {

        try {

            return atividades.remove(codAtiv);
        } catch (Exception ex) {

            return null;
        }

    }

    public Manutencao addManutencao(Manutencao manutencao) {

        return manutencoes.put(manutencao.getCodigo(), manutencao);
    }

    public void sofrerReparo(Date data, AlocacaoPeca peca, int porcVidaUtil) {
    }

    public AlocacaoPeca selecionarPeca(int cod) {
        return null;
    }

    public void sofrerSubstituicaoPeca(Date data, AlocacaoPeca pecaSubstituida, Peca pecaSubstituta, int porcVidaUtil) {
    }

    public List<Manutencao> listarManutencoes() {

        return new ArrayList<Manutencao>(manutencoes.values());
    }

    public Manutencao excluirManutencao(int codManut) {

        return manutencoes.remove(codManut);
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

    @Override
    public boolean equals(Object obj) {

        return ((obj instanceof Semeadora) && ((Semeadora) obj).getIdentificacao() == this.identificacao) ? true : false;
    }

    @Override
    public int hashCode() {

        return this.identificacao;
    }

    @Override
    public String toString() {

        return "Semeadora: " + this.identificacao + ", " + this.marca + ", " + this.modelo + ", " + this.ano + ", " + this.dataRegistro;
    }
    /*public void saveSemeadora() throws SQLException {
        
     new DBSemeadora(AcessoPostgres.getInstance()).insertSemeadora(this);
     }
    
     public void removeSemeadora() throws SQLException {
        
     new DBSemeadora(AcessoPostgres.getInstance()).deleteSemeadora(this.getIdentificacao());
     }
    
     public void removeSemeadora(int codSem) throws SQLException {
        
     new DBSemeadora(AcessoPostgres.getInstance()).deleteSemeadora(codSem);
     }*/
}
