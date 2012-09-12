package CN;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Divisao {

    private int identificao;
    private String nome;
    private Semeadora semeadora;
    private Set<AlocacaoPeca> alocacoesPeca;

    public Divisao(String nome, Semeadora semeadora) {

        this(semeadora, 0, nome, new LinkedHashSet<AlocacaoPeca>());
    }

    public Divisao(Semeadora semeadora, int codDivisao, String nome, Set<AlocacaoPeca> alocacoes) {

        this.nome = nome;
        this.identificao = codDivisao;
        this.alocacoesPeca = alocacoes;
        this.setSemeadora(semeadora);

    }

    public void addPeca(AlocacaoPeca peca, Date data) {
    }

    public boolean addPeca(AlocacaoPeca alocPeca) {

        try {

            return alocacoesPeca.add(alocPeca);
        } catch (NullPointerException npe) {

            return false;
        }

    }

    public List listarPecas() {
        return null;
    }

    public boolean excluirPeca(AlocacaoPeca alocPeca) {


        for (AlocacaoPeca peca : alocacoesPeca) {

            if (peca.equals(alocPeca)) {

                peca.setDivisao(null);
                return alocacoesPeca.remove(alocPeca);
            }
        }

        return false;

    }

    public AlocacaoPeca selecionarPeca(int cod) {
        return null;
    }

    /**
     * @return the identificao
     */
    public int getIdentificao() {
        return identificao;
    }

    /**
     * @param identificao the identificao to set
     */
    public void setIdentificao(int identificao) {
        this.identificao = identificao;
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

        if (semeadora != null) {

            semeadora.addDivisao(this);
            this.semeadora = semeadora;
        } else {

            this.semeadora = null;
        }
    }
}
