package CN;

import Cadastro.CadastroSemeadoras;
import Exceções.ConsultaException;
import Repositório.AcessoPostgres;
import Repositório.DBSemeadora;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Divisao {

    private int identificao;
    private String nome;
    private Semeadora semeadora;
    private TipoAlocacao tipoAloc;
    private Map<Integer, AlocacaoPeca> alocacoesPeca;

    public Divisao(String nome, TipoAlocacao tipoAloc, Semeadora semeadora) throws ConsultaException{

        this(new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance())).gerarCódigoDivisão(semeadora.getIdentificacao()), nome, tipoAloc);
    }

    public Divisao(int codDivisao, String nome, TipoAlocacao tipoAloc) {

        this.nome = nome;
        this.identificao = codDivisao;
        this.alocacoesPeca = new HashMap<Integer, AlocacaoPeca>();
        this.tipoAloc = tipoAloc;
    }

    /*public AlocacaoPeca addPeca(AlocacaoPeca peca, Date data) {

        try {

            if (peca.getDivisao() != null) {

                peca.setDataInclusaoItemPeca(data);
                peca.getDivisao().alocacoesPeca.remove(peca.getItemPeca().getIdentificacao());
                return alocacoesPeca.put(peca.getItemPeca().getIdentificacao(), peca);
            } else {

                peca.setDataInclusaoItemPeca(data);
                return alocacoesPeca.put(peca.getItemPeca().getIdentificacao(), peca);
            }
        } catch (Exception npe) {

            return null;
        }


    }*/
    
    public AlocacaoPeca addPeca(int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusao){
        
        ItemPeca iPeca= new ItemPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
        AlocacaoPeca alocPeca = new AlocacaoPeca(dataInclusao);
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());
        
        return alocPecaAnterior;
    }
    
    public AlocacaoPeca addPeca(int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante){
        
        ItemPeca iPeca= new ItemPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
        AlocacaoPeca alocPeca = new AlocacaoPeca();
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());
        
        return alocPecaAnterior;
    }
    
    public AlocacaoPeca addPeca(int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusaoPeca){
        
        ItemPeca iPeca= new ItemPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
        AlocacaoPeca alocPeca = new AlocacaoPeca(dataInclusaoPeca);
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());
        
        return alocPecaAnterior;
    }
    
    public AlocacaoPeca addPeca(int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante){
        
        ItemPeca iPeca= new ItemPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
        AlocacaoPeca alocPeca = new AlocacaoPeca();
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());
        
        return alocPecaAnterior;
    }
    

    /*public AlocacaoPeca addPeca(AlocacaoPeca peca) {

        try {

            if (peca.getDivisao() != null) {

                peca.getDivisao().alocacoesPeca.remove(peca.getItemPeca().getIdentificacao());
                return alocacoesPeca.put(peca.getItemPeca().getIdentificacao(), peca);
            }

            return null;
        } catch (Exception npe) {

            return null;
        }
    }*/

    public List<AlocacaoPeca> listarPecas() {

        return new ArrayList<AlocacaoPeca>(alocacoesPeca.values());
    }

    public AlocacaoPeca excluirPeca(AlocacaoPeca alocPeca) {

        try {

            AlocacaoPeca peca = alocacoesPeca.get(alocPeca.getItemPeca().getIdentificacao());
            peca.alterarItemPeca(null, peca.getItemPeca(), peca.getDataInclusaoItemPeca());
            alocacoesPeca.remove(alocPeca.getItemPeca().getIdentificacao());

            return peca;
        } catch (Exception ex) {

            return null;
        }
    }

    public AlocacaoPeca selecionarPeca(int codItemPeca) {

        return alocacoesPeca.get(codItemPeca);
    }

    /**
     * @return the identificao
     */
    public int getIdentificao() {
        return this.identificao;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the semeadora
     */
    public Semeadora getSemeadora() {
        return semeadora;
    }

    /**
     * @param semeadora the semeadora to set
     */
    public void setSemeadora(Semeadora semeadora) {

        if(semeadora.selecionarDivisao(identificao).equals(this)){
            
            this.semeadora = semeadora;
        }
        /*if (semeadora != null) {

            semeadora.addDivisao(this);
            this.semeadora = semeadora;
        } else {

            if (this.semeadora != null) {

                if (this.semeadora.equals(obj)) {

                    this.semeadora.selecionarDivisao(this.identificao).semeadora = null;
                } else {

                    this.semeadora.excluirDivisao(identificao);
                }

            }

            this.semeadora = null;
        }*/
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Divisao other = (Divisao) obj;
        if (this.identificao != other.identificao) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {

        return this.identificao;
    }

    @Override
    public String toString() {

        try {

            return "Divisão: " + this.identificao + ", " + this.getNome() + ", " + this.getSemeadora().toString();
        } catch (Exception ex) {

            return "Divisão: " + this.identificao + ", " + this.getNome();
        }

    }

    /**
     * @return the tipoAloc
     */
    public TipoAlocacao getTipoAloc() {
        return tipoAloc;
    }

    /**
     * @param tipoAloc the tipoAloc to set
     */
    public void setTipoAloc(TipoAlocacao tipoAloc) {
        this.tipoAloc = tipoAloc;
    }
}
