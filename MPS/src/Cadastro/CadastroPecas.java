/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import Exceções.ConsultaException;
import Repositório.RepositorioPeca;

/**
 *
 * @author rafael
 */
public class CadastroPecas {

    RepositorioPeca repPeca;

    public CadastroPecas(RepositorioPeca repPeca) {

        this.repPeca = repPeca;
    }

    public int geraCodigoPeca() throws ConsultaException {

        return repPeca.getMaxCodPeca() + 1;
    }
}
