package CN;

import Cadastro.CadastroSemeadoras;
import Exceções.CodigoInvalidoException;
import Exceções.ConsultaException;
import Exceções.DataInvalidaException;
import Exceções.TempoVidaUtilForaDosLimitesException;
import Exceções.ValorNuloException;
import Repositório.AcessoPostgres;
import Repositório.DBSemeadora;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

public class Semeadora extends Observable {

    private int identificacao;
    private String modelo;
    private String marca;
    private int ano;
    private Date dataRegistro;
    private Map<Integer, Divisao> divisoes;
    private Map<Integer, Atividade> atividades;
    private Map<Integer, Manutencao> manutencoes;

    public Semeadora(String modelo, String marca, int ano) throws ConsultaException, ValorNuloException, DataInvalidaException {

        this(new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance())).gerarCódigoSemeadora(), modelo, marca, ano, new Date());
    }

    public Semeadora(int identificacao, String modelo, String marca, int ano) throws ValorNuloException {

        this(identificacao, modelo, marca, ano, new Date());
    }

    public Semeadora(int identificacao, String modelo, String marca, int ano, Date dataRegistro) throws ValorNuloException, DataInvalidaException {

        setIdentificacao(identificacao);
        setModelo(modelo);
        setMarca(marca);
        setAno(ano);
        setDataRegistro(dataRegistro);

        this.divisoes = new HashMap<Integer, Divisao>();
        this.atividades = new HashMap<Integer, Atividade>();
        this.manutencoes = new HashMap<Integer, Manutencao>();
    }

    public final void setModelo(String modelo) throws ValorNuloException {

        if (modelo == null || modelo.equals("")) {

            throw new ValorNuloException("Deve ser informado o modelo da semeadora");
        }

        this.modelo = modelo;
    }

    public final void setMarca(String marca) throws ValorNuloException {

        if (marca == null || marca.equals("")) {

            throw new ValorNuloException("Deve ser informada a marca da semeadora");
        }

        this.marca = marca;
    }

    public final void setAno(int ano) throws ValorNuloException {

        if (ano <= 0) {

            throw new ValorNuloException("Deve ser informado o ano de fabrincação da semeadora");
        }
        this.ano = ano;
    }

    /**
     * @param identificacao the identificacao to set
     */
    public final void setIdentificacao(int identificacao) throws ValorNuloException {

        if (identificacao <= 0) {

            throw new ValorNuloException("A semeadora deve ter um código válido");
        }

        this.identificacao = identificacao;
    }

    /**
     * @param dataRegistro the dataRegistro to set
     */
    public final void setDataRegistro(Date dataRegistro) throws ValorNuloException, DataInvalidaException {

        if (dataRegistro == null) {

            throw new ValorNuloException("Deve ser informada a data em que a semeadora está sendo registrada");
        }
        
        if(dataRegistro.after(new Date())){
            
            throw new DataInvalidaException("Data informada é posterior à data atual", dataRegistro);
        }
        
        if((new Date().getYear() - dataRegistro.getYear()) > 50){
            
            throw new DataInvalidaException("Data informada muito distante da data atual", dataRegistro);
        }

        this.dataRegistro = dataRegistro;
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

    /*public Divisao addDivisao(Divisao divisao) {

     if(divisao != null){
     if(divisao.getSemeadora() != null)
     divisao.getSemeadora().divisoes.remove(divisao.getIdentificao());
            
     return divisoes.put(divisao.getIdentificao(), divisao);
     } else {

     return null;
     }

     }*/
    public Divisao addDivisao(String nome, TipoAlocacao tipoAloc) throws ConsultaException, ValorNuloException{

        Divisao div = new Divisao(nome, tipoAloc, this);
        Divisao divAnterior = this.divisoes.put(div.getIdentificao(), div);
        div.setSemeadora(this);
        return divAnterior;
    }

    public Divisao addDivisao(int codDivisao, String nome, TipoAlocacao tipoAloc) throws ValorNuloException{

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

    public List<Divisao> listarDivisoes() {

        return new ArrayList<Divisao>(this.divisoes.values());
    }

    public AlocacaoPeca addPeca(int codDivisao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusao) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante, dataInclusao);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }

    }

    public AlocacaoPeca addPeca(int codDivisao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException{

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
            return alocPecaAnterior;

        } catch (NullPointerException npe) {

            return null;
        }
    }

    public AlocacaoPeca addPeca(int codDivisao, int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusaoPeca) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException{

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante, dataInclusaoPeca);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }
    }

    public AlocacaoPeca addPeca(int codDivisao, int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        try {

            AlocacaoPeca alocPecaAnterior = this.divisoes.get(codDivisao).addPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
            return alocPecaAnterior;
        } catch (NullPointerException npe) {

            return null;
        }
    }

    
    public Atividade realizarAtividade(int codAtividade, Date data, int tempoDuracao, TipoAtividade nome, Map<String, TipoFator> fatores) throws CodigoInvalidoException{
        
        if(this.atividades.containsKey(codAtividade)){
            
            throw new CodigoInvalidoException("A semeadora já possui uma atividade com esse código", codAtividade);
        }
        
        Atividade atividade = new Atividade(codAtividade, data, tempoDuracao, nome, fatores);
        
        CalculaDesgasteAtividade calc = CalculaDesgasteSimpleFactory.createCalculaDesgasteAtividade(nome);
        
        for(Divisao div: this.divisoes.values()){
            
           for(AlocacaoPeca alocPeca: div.listarPecas()){
               
               alocPeca.getItemPeca().getPeca().setCalculaDesgaste(calc);
               alocPeca.getItemPeca().subtrairTempoVidautil(atividade.calculaDesgastePeça(alocPeca));
           }
        }
        
        this.atividades.put(atividade.getCodigo(), atividade);
        atividade.setSemeadora(this);
        
        return this.atividades.get(atividade.getCodigo());
    }
    

    public Atividade selecionarAtividade(int codAtividade){
        
        return this.atividades.get(codAtividade);
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
