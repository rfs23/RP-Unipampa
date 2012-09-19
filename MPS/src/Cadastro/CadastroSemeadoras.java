/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.Semeadora;
import CN.TipoPeca;
import Exceções.AnoInvalidoException;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Exceções.ValorNuloException;
import Exceções.SemeadoraIncompletaException;
import Repositório.RepositorioSemeadoras;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class CadastroSemeadoras {

    public static Map<Integer, Semeadora> semeadoras;
    private RepositorioSemeadoras repSemeadoras;

    public CadastroSemeadoras(RepositorioSemeadoras repSemeadoras) {

        this.repSemeadoras = repSemeadoras;

        if (semeadoras == null) {

            semeadoras = new HashMap<Integer, Semeadora>();
        }
    }

    public void insertSemeadora(Semeadora semeadora) throws InsercaoException, SemeadoraIncompletaException {

        verificaCompletudePecasSemeadora(semeadora);
        
        this.repSemeadoras.insertSemeadora(semeadora);

        System.out.println("Semeadora adicionada com sucesso!");
    }

    public void deleteSemeadora(int codSemeadora) throws DelecaoException {

        this.repSemeadoras.deleteSemeadora(codSemeadora);

        CadastroSemeadoras.semeadoras.remove(codSemeadora);
    }

    public void updateSemeadora(int codSemeadora, Semeadora semeadora) throws AtualizacaoException {

        this.repSemeadoras.updateSemeadora(codSemeadora, semeadora);
        CadastroSemeadoras.semeadoras.put(codSemeadora, semeadora);
    }

    public Semeadora selectSemeadora(int codSemeadora) throws ConsultaException {

        if (CadastroSemeadoras.semeadoras.get(codSemeadora) == null) {

            Semeadora semeadora = this.repSemeadoras.selectSemeadora(codSemeadora);

            if (semeadora != null) {

                CadastroSemeadoras.semeadoras.put(semeadora.getIdentificacao(), semeadora);
            }

            return semeadora;
        } else {

            return CadastroSemeadoras.semeadoras.get(codSemeadora);
        }

    }

    public void clearList() {

        CadastroSemeadoras.semeadoras.clear();
    }

    public void listSemeadoras() throws ConsultaException {

        CadastroSemeadoras.semeadoras = repSemeadoras.listSemeadoras();
    }

    public void selectSemeadorasByModelo(String modeloSemeadora) throws ConsultaException {

        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByModelo(modeloSemeadora);
    }

    public void selectSemeadorasByMarca(String marcaSemeadora) throws ConsultaException {

        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByMarca(marcaSemeadora);
    }

    public void selectSemeadorasByAno(int ano) throws AnoInvalidoException, ConsultaException {

        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByAno(ano);
    }

    public void selectSemeadorasByDataInclusao(Date dataInclusao) throws ConsultaException {

        CadastroSemeadoras.semeadoras = repSemeadoras.selectSemeadorasByDataInclusao(dataInclusao);
    }

    public int gerarCódigoSemeadora() throws ConsultaException {

        return this.repSemeadoras.getMaxCodSemeadora() + 1;
    }

    public int gerarCódigoDivisão(int codSem) throws ConsultaException {

        return this.repSemeadoras.getMaxCodDivisao(codSem) + 1;
    }
    
    public void verificaCompletudeRegistroSemeadora(Semeadora sem) throws ValorNuloException{
        
        if(sem.getIdentificacao() <= 0){
            
            throw new ValorNuloException("A semeadora deve ter um código válido", sem);
        }else if(sem.getAno() <= 0){
            
            throw new ValorNuloException("Deve ser informado o ano de fabrincação da semeadora", sem);
        }else if (sem.getMarca() == null || sem.getMarca().equals("")){
            
            throw new ValorNuloException("Deve ser informada a marca da semeadora", sem);
        }else if (sem.getModelo() == null || sem.getModelo().equals("")){
            
            throw new ValorNuloException("Deve ser informado o modelo da semeadora", sem);
        }else if (sem.getDataRegistro() == null){
            
            throw new ValorNuloException("Deve ser informada a data em que a semeadora está sendo registrada", sem);
        }
        
        for(Divisao div: sem.listarDivisoes()){
            
            if(div.getIdentificao() <= 0){
                
                throw new ValorNuloException("As divisões da semeadora deve possuir um código válido", sem);
            }else if (div.getNome() == null || div.getNome().equals("")){
                
                throw new ValorNuloException("Deve ser informado um nome para a divisão " + div.getIdentificao() + " da semeadora " + sem.getIdentificacao(), sem);
            }else if (div.getTipoAloc()==null){
                
                throw new ValorNuloException("Deve ser informado um tipo de alocação para a divisão " + div.getIdentificao() + " da semeadora " + sem.getIdentificacao(), sem);
            }
        }
    }

    public void verificaCompletudePecasSemeadora(Semeadora sem) throws SemeadoraIncompletaException {

        for (Divisao div : sem.listarDivisoes()) {
            
            verificaDivisaoPecas(div);
        }
    }

    private void verificaDivisaoPecas(Divisao div) throws SemeadoraIncompletaException {

        ArrayList<TipoPeca> tiposPecas = new ArrayList<TipoPeca>();
        int qtdTipoPeca = 0;

        for (TipoPeca tp : TipoPeca.values()) {

            if (div.getTipoAloc().equals(tp.getTipoAlocacao())) {

                tiposPecas.add(tp);
            }
        }

        for (TipoPeca tp : tiposPecas) {

            for (AlocacaoPeca alocPeca : div.listarPecas()) {
                
                if(alocPeca.getItemPeca().getPeca().getTipo().equals(tp)){
                    
                    qtdTipoPeca++;
                }
            }
            
            if(qtdTipoPeca == 0){
                
                throw new SemeadoraIncompletaException("A divisão de código " + div.getIdentificao() + " não possui todas alocações de peças que deveria", div);
            }
            
            qtdTipoPeca = 0;
        }


    }
}
