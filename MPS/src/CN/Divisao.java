package CN;

import Exceções.ConsultaException;
import Exceções.DataInvalidaException;
import Exceções.TempoVidaUtilForaDosLimitesException;
import Exceções.ValorNuloException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Divisao {

    private int identificacao;
    private String nome;
    private Semeadora semeadora;
    private TipoAlocacao tipoAloc;
    private Map<Integer, AlocacaoPeca> alocacoesPeca;

    /*public Divisao(String nome, TipoAlocacao tipoAloc, Semeadora semeadora) throws ConsultaException, ValorNuloException {

     this(new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance())).gerarCódigoDivisão(semeadora.getIdentificacao()), nome, tipoAloc);
     }*/
    public Divisao(int codDivisao, String nome, TipoAlocacao tipoAloc) throws ValorNuloException {

        this(nome, tipoAloc);
        setIdentificao(codDivisao);
    }
    
    public Divisao(String nome, TipoAlocacao tipoAloc) throws ValorNuloException {

        setNome(nome);
        setTipoAloc(tipoAloc);

        this.alocacoesPeca = new HashMap<Integer, AlocacaoPeca>();
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
    /*public AlocacaoPeca addPeca(int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusao) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

     return this.addPeca(nextCodeItemPeca(), anoFab, dataAquis, peca, tempoVidaUtilRestante, dataInclusao);

     ItemPeca iPeca = new ItemPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
     AlocacaoPeca alocPeca = new AlocacaoPeca(dataInclusao);
     AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
     alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());

     return alocPecaAnterior;
     }

     public AlocacaoPeca addPeca(int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

     return this.addPeca(nextCodeItemPeca(), anoFab, dataAquis, peca, tempoVidaUtilRestante);

     ItemPeca iPeca = new ItemPeca(anoFab, dataAquis, peca, tempoVidaUtilRestante);
     AlocacaoPeca alocPeca = new AlocacaoPeca();
     AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
     alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());

     return alocPecaAnterior;
     }

     public AlocacaoPeca addPeca(int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante, Date dataInclusaoPeca) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

     ItemPeca iPeca = new ItemPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
     AlocacaoPeca alocPeca = new AlocacaoPeca(dataInclusaoPeca);
     AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
     alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());

     return alocPecaAnterior;
     }

     public AlocacaoPeca addPeca(int identificacao, int anoFab, Date dataAquis, Peca peca, int tempoVidaUtilRestante) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

     ItemPeca iPeca = new ItemPeca(identificacao, anoFab, dataAquis, peca, tempoVidaUtilRestante);
     AlocacaoPeca alocPeca = new AlocacaoPeca();
     AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
     alocPeca.alterarItemPeca(this, iPeca, alocPeca.getDataInclusaoItemPeca());

     return alocPecaAnterior;
     }*/
    public AlocacaoPeca addPeca(ItemPeca iPeca) throws ConsultaException, ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        AlocacaoPeca alocPeca = new AlocacaoPeca();
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        alocPeca.alterarItemPeca(this, iPeca, new Date());

        return alocPecaAnterior;
    }

    public AlocacaoPeca addPeca(ItemPeca iPeca, Date dataInclusao) throws ValorNuloException, DataInvalidaException, TempoVidaUtilForaDosLimitesException {

        AlocacaoPeca alocPeca = new AlocacaoPeca(dataInclusao);
        AlocacaoPeca alocPecaAnterior = this.alocacoesPeca.put(iPeca.getIdentificacao(), alocPeca);
        AlocacaoPeca aloc = alocPeca.alterarItemPeca(this, iPeca, new Date());

        return alocPecaAnterior;
    }

    public void trocarPeca(int codItemPeca, ItemPeca iPeca) {

        this.alocacoesPeca.get(codItemPeca).alterarItemPeca(this, iPeca, new Date());
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

        return this.identificacao;
    }

    public final void setIdentificao(int identificacao) throws ValorNuloException {

        if (identificacao <= 0) {

            throw new ValorNuloException("Deve ser informado um valor válido para identificação da divisão");
        }

        this.identificacao = identificacao;
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
    public final void setNome(String nome) throws ValorNuloException {

        if (nome == null || nome.equals("")) {

            throw new ValorNuloException("Deve ser informado um nome para a divisão");
        }

        this.nome = nome;
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
    public final void setTipoAloc(TipoAlocacao tipoAloc) throws ValorNuloException {

        if (tipoAloc == null) {

            throw new ValorNuloException("Deve ser informado o tipo de alocação para a divisão");
        }

        this.tipoAloc = tipoAloc;
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

        try {

            if (semeadora.selecionarDivisao(identificacao).equals(this)) {

                this.semeadora = semeadora;
            }
        } catch (NullPointerException npe) {
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

    private int nextCodeItemPeca() {

        int maxCodeItemPeca = 0;

        if (!this.alocacoesPeca.isEmpty()) {

            for (Integer i : this.alocacoesPeca.keySet()) {

                maxCodeItemPeca = Math.max(maxCodeItemPeca, i);
            }

        }

        return maxCodeItemPeca + 1;
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
        if (this.identificacao != other.identificacao) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {

        return this.identificacao;
    }

    @Override
    public String toString() {

        try {

            return "Divisão: " + this.identificacao + ", " + this.getNome() + ", " + this.getSemeadora().toString();
        } catch (Exception ex) {

            return "Divisão: " + this.identificacao + ", " + this.getNome();
        }
    }
}
