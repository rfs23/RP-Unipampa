package CN;

import Cadastro.CadastroSemeadoras;
import Repositório.AcessoPostgres;
import Repositório.DBSemeadora;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Atividade {

    private static int codAtiv;
    
    private int codigo;
    private TipoAtividade nome;
    private Date dataRealizacao;
    private int tempoDuracao;
    private Map<String, Fator> fatores;
    private ArrayList<DesgastePeca> desgastePecas;
    private Semeadora semeadora;

    static {

        codAtiv = new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance())).gerarCódigoRealizaçãoAtividade();
        System.out.println("Teste");
    }

    public Atividade(Date data, int tempoDuracao, TipoAtividade nome, Map<String, Fator> fatores) {

        this(0, data, tempoDuracao, nome, fatores);
        this.codigo = nextCodeAtiv();
    }

    public Atividade(int codigo, Date data, int tempoDuracao, TipoAtividade nome, Map<String, Fator> fatores) {

        this.codigo = codigo;
        this.nome = nome;
        this.dataRealizacao = data;
        this.tempoDuracao = tempoDuracao;
        this.fatores = fatores;

        this.desgastePecas = new ArrayList<DesgastePeca>();
    }

    public int calculaDesgastePeça(AlocacaoPeca alocPeca) {

        DesgastePeca dgPeca = new DesgastePeca(alocPeca, alocPeca.getItemPeca().getPeca().calculaDesgaste(fatores, tempoDuracao, nome));
        this.desgastePecas.add(dgPeca);

        return dgPeca.getDesgaste();
    }

    public DesgastePeca selecionarDesgastePeca(DesgastePeca desgastePeca) {

        if (desgastePecas.indexOf(desgastePeca) != -1) {

            return desgastePecas.get(desgastePecas.indexOf(desgastePeca));
        }

        return null;
    }

    public ArrayList<DesgastePeca> listarDesgastePecas() {

        return this.desgastePecas;
    }

    /**
     * @return the nome
     */
    public TipoAtividade getNome() {

        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(TipoAtividade nome) {

        this.nome = nome;
    }

    /**
     * @return the dataRealizacao
     */
    public Date getDataRealizacao() {

        return dataRealizacao;
    }

    /**
     * @param dataRealizacao the dataRealizacao to set
     */
    public void setDataRealizacao(Date dataRealizacao) {

        this.dataRealizacao = dataRealizacao;
    }

    /**
     * @return the duracao
     */
    public int getDuracao() {

        return tempoDuracao;
    }

    /**
     * @param duracao the duracao to set
     */
    public void setDuracao(int tempoDuracao) {

        this.tempoDuracao = tempoDuracao;
    }

    /**
     * @return the fatores
     */
    public Map<String, Fator> getFatores() {

        return fatores;
    }

    /**
     * @param fatores the fatores to set
     */
    public void setFatores(Map<String, Fator> fatores) {

        this.fatores = fatores;
    }

    public Fator selecionarFator(String fator) {

        return this.fatores.get(fator);
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {

        return this.codigo;
    }

    public void setSemeadora(Semeadora semeadora) {

        if (semeadora.selecionarAtividade(this.codigo).equals(this)) {

            this.semeadora = semeadora;
        }
    }

    private int nextCodeAtiv() {

        int maxCodeAtiv = codAtiv - 1;
        codAtiv++;
        
        return ++maxCodeAtiv;
    }
}
