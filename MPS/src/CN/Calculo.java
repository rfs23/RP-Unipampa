package CN;

import Exceções.ValorInvalidoException;

public abstract class Calculo {

    /**
     * Converte a porcetagem de vida util restante de uma peça para tempo de
     * vida util restante.
     *
     * @param peca Peça que terá sua porcentagem de vida útil convertida.
     * @param porcVidaUtilRestante Porcentagem de vida útil restante à peça.
     * @return Tempo de vida útil restante à peça.
     * @throws ValorInvalidoException Lança a exceção caso a porcentagem de vida
     * útil informada for maior que 100 ou menor que 0.
     */
    public static int porcVidaUtilParaVidaUtil(Peca peca, int porcVidaUtilRestante) throws ValorInvalidoException {

        if (porcVidaUtilRestante > peca.getTipoPeca().estVidaUtil()) {

            throw new ValorInvalidoException("A porcentagem de vida útil restante da peça não pode ser maior do que 100.", porcVidaUtilRestante);
        } else if (porcVidaUtilRestante < 0) {

            throw new ValorInvalidoException("A porcentagem de vida útil restante da peça não pode ser menor do que 0.", porcVidaUtilRestante);
        } else {

            return (peca.getTipoPeca().estVidaUtil() * porcVidaUtilRestante) / 100;
        }

    }

    /**
     * Converte o tempo de vida útil restante da peça para porcentagem.
     *
     * @param peca Peça de uma semeadora que terá seu tempo de vida útil
     * restante convertido.
     * @return Porcentagem do tempo de vida útil restante à peça.
     */
    public static int vidaUtilParaPorcVidaUtil(AlocacaoPeca peca) {

        try {

            return (peca.getTempoVidaUtil() * 100) / peca.getPeca().getTipoPeca().estVidaUtil();
        } catch (ArithmeticException ex) {

            return 0;
        }
    }
}
