/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cadastro;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.Manutencao.Reparo;
import CN.Manutencao.SubstituicaoPeca;
import CN.Semeadora;
import CN.TipoPeca;
import Exceções.AnoInvalidoException;
import Exceções.AtualizacaoException;
import Exceções.ConsultaException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Exceções.SemeadoraIncompletaException;
import Exceções.ValorNuloException;
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

    public void registrarAtividade(int codSem, int codAtiv) throws NullPointerException {

        Semeadora sem = selectSemeadora(codSem);

        if (sem.selecionarAtividade(codAtiv) == null) {

            throw new NullPointerException("A atividade não foi realizada pela semeadora e, portanto, não pode ser registrada");
        }

        this.repSemeadoras.registrarAtividade(codSem, sem.selecionarAtividade(codAtiv));
    }

    public void listarAtividadesRealizadas(int codSem) throws ConsultaException {

        if (semeadoras.containsKey(codSem)) {

            Semeadora sem = this.repSemeadoras.carregarAtividades(semeadoras.get(codSem));
            semeadoras.put(sem.getIdentificacao(), sem);
        }
    }
    
    public void cancelarAtividade(int codSem, int codAtividade, Date dataCancelamento){
        
        if(semeadoras.containsKey(codSem)){
            
            if(semeadoras.get(codSem).selecionarAtividade(codAtividade) != null){
                
                this.repSemeadoras.cancelarAtividade(codSem, codAtividade, dataCancelamento);
                semeadoras.get(codSem).cancelarAtividade(codAtividade);
            }
        }
    }
    
    public void registrarReparo(int codSem, Date dataReparo) throws NullPointerException, InsercaoException, AtualizacaoException{
        
        if(semeadoras.containsKey(codSem)){
            
            Semeadora sem = semeadoras.get(codSem);
            
            if(sem.selecionarManutencao(dataReparo) == null || !(sem.selecionarManutencao(dataReparo) instanceof Reparo)){
                
                throw new NullPointerException("A semeadora não realizou reparo para a data informada");
            }
            
            this.repSemeadoras.registrarReparo(codSem, (Reparo)sem.selecionarManutencao(dataReparo));
        }
    }
    
    public void registrarSubstituicao(int codSem, Date dataSubstituicao) throws NullPointerException, InsercaoException, AtualizacaoException{
        
        if(semeadoras.containsKey(codSem)){
            
            Semeadora sem = semeadoras.get(codSem);
            
            if(sem.selecionarManutencao(dataSubstituicao) == null || !(sem.selecionarManutencao(dataSubstituicao) instanceof SubstituicaoPeca)){
                
                throw new NullPointerException("A semeadora substituição de peça para a data informada");
            }
            
            this.repSemeadoras.registrarSubstituicao(codSem, (SubstituicaoPeca)sem.selecionarManutencao(dataSubstituicao));
        }
    }

    public int gerarCódigoSemeadora() throws ConsultaException {

        return this.repSemeadoras.getMaxCodSemeadora() + 1;
    }

    public int gerarCódigoDivisão(int codSem) throws ConsultaException {

        return this.repSemeadoras.getMaxCodDivisao(codSem) + 1;
    }

    public int gerarCódigoRealizaçãoAtividade() throws ConsultaException {

        return this.repSemeadoras.getMaxCodRealizacaoAtiv() + 1;
    }

    public void verificaCompletudeRegistroSemeadora(Semeadora sem) throws ValorNuloException {

        if (sem.getIdentificacao() <= 0) {

            throw new ValorNuloException("A semeadora deve ter um código válido", sem);
        } else if (sem.getAno() <= 0) {

            throw new ValorNuloException("Deve ser informado o ano de fabrincação da semeadora", sem);
        } else if (sem.getMarca() == null || sem.getMarca().equals("")) {

            throw new ValorNuloException("Deve ser informada a marca da semeadora", sem);
        } else if (sem.getModelo() == null || sem.getModelo().equals("")) {

            throw new ValorNuloException("Deve ser informado o modelo da semeadora", sem);
        } else if (sem.getDataRegistro() == null) {

            throw new ValorNuloException("Deve ser informada a data em que a semeadora está sendo registrada", sem);
        }

        for (Divisao div : sem.listarDivisoes()) {

            if (div.getIdentificao() <= 0) {

                throw new ValorNuloException("As divisões da semeadora deve possuir um código válido", sem);
            } else if (div.getNome() == null || div.getNome().equals("")) {

                throw new ValorNuloException("Deve ser informado um nome para a divisão " + div.getIdentificao() + " da semeadora " + sem.getIdentificacao(), sem);
            } else if (div.getTipoAloc() == null) {

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

        for (TipoPeca tp : TipoPeca.values()) {

            if (div.getTipoAloc().equals(tp.getTipoAlocacao())) {

                tiposPecas.add(tp);
            }
        }


        for (AlocacaoPeca alocPeca : div.listarPecas()) {

            if (!tiposPecas.contains(alocPeca.getItemPeca().getPeca().getTipo())) {

                throw new SemeadoraIncompletaException("A divisão de código " + div.getIdentificao() + " não possui todas alocações de peças que deveria", div);
            }
        }
    }
}
