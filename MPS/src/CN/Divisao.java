package CN;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Divisao {

    private int identificao;
    private String nome;
    private Semeadora semeadora;
    private Collection<AlocacaoPeca> alocacaoPeca;

    public Divisao(String nome, Semeadora semeadora) {
    }

    public Divisao(Semeadora buscarSemeadora, int codDivisao, String string, Map<Integer, AlocacaoPeca> alocacoes) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addPeca(AlocacaoPeca peca, Date data) {
    }

    public List listarPecas() {
        return null;
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
        this.semeadora = semeadora;
    }
}
