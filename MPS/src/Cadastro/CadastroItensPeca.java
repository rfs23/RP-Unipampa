/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import Exceções.ConsultaException;
import Repositório.RepositorioItemPeca;

/**
 *
 * @author rafael
 */
public class CadastroItensPeca {

    RepositorioItemPeca repItemPeca;

    public CadastroItensPeca(RepositorioItemPeca repItemPeca) {

        this.repItemPeca = repItemPeca;
    }

    public int geraCodigoItemPeca() throws ConsultaException {

        return repItemPeca.getMaxCodItemPeca() + 1;
    }
}
