/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import Exceções.DeleçãoException;
import Exceções.InserçãoException;
import Repositório.AcessoPostgres;
import Repositório.DBPeca;
import Repositório.DBSemeadora;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author rafael
 */
public class Teste {

    private String teste;
    static DBSemeadora dbSem = new DBSemeadora(AcessoPostgres.getInstance());

    public Teste(String teste) {

        this.teste = teste;
    }

    public void setTeste(String teste) {

        this.teste = teste;
    }

    public String getTeste() {

        return this.teste;
    }

    public static void testeHashSet() {

        HashSet<Teste> testes = new HashSet<Teste>();

        Teste t1 = new Teste("teste1");
        testes.add(t1);

        for (Teste t : testes) {

            if (t.equals(t1)) {

                Teste t2 = t;
                t2.setTeste("teste2");

            }
        }

        for (Teste t : testes) {

            System.out.println(t.getTeste());
        }
    }

    public static void testeNavegabilidadeBidirecional() {

        Semeadora sem1 = new Semeadora("sem1", "teste1", 1990);
        Semeadora sem2 = new Semeadora("sem2", "teste2", 1992);

        /*Divisao div = new Divisao("Div1", sem1, TipoAlocacao.Semeadora);

         System.out.println(div.getSemeadora().getAno());


         div.setSemeadora(sem1, new Teste(""));
         div.setSemeadora(sem1, new Teste(""));
         div.setSemeadora(sem2, new Teste(""));
         div.setSemeadora(null, new Teste(""));

         sem1.excluirDivisao(1);

         System.out.println(div.getSemeadora());
         div.setSemeadora(sem2, new Teste(""));
         System.out.println(div.getSemeadora());

         System.out.println(div.getSemeadora().getAno());*/


        System.out.println(sem1.listarDivisoes());
        System.out.println(sem2.listarDivisoes());
    }

    public static void testeInserçãoSemeadora() {

        Semeadora sem = new Semeadora("Case 100", "Case", 2005);

        try {

            new CadastroSemeadoras(dbSem).insertSemeadora(sem);
        } catch (InserçãoException ie) {

            ie.getRTException().printStackTrace();
        }
    }

    public static void testeDeleçãoSemeadora() {

        try {

            new CadastroSemeadoras(dbSem).deleteSemeadora(1);
        } catch (DeleçãoException de) {

            de.getRTException().printStackTrace();
        }
    }

    /* public static void testeIntegridadeItemPeca() {
     Semeadora sem = new Semeadora("sem", "sem", 1990);
     sem.addDivisao("Div", TipoAlocacao.Semeadora);
     ItemPeca itemPeca = new ItemPeca(1, new Date(), new Peca("", TipoPeca.Teste), 100);
     ItemPeca itemPeca2 = new ItemPeca(2, new Date(), new Peca("", TipoPeca.Teste), 100);
     AlocacaoPeca alocPeca = new AlocacaoPeca(itemPeca, div);
     AlocacaoPeca alocPeca2 = new AlocacaoPeca(itemPeca, div);
     alocPeca.setItemPeca(itemPeca2);
     }*/
    
    public static void testeGeraCodigoPeca() {

        Peca p = new Peca("teste", TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        System.out.println(p.getIdentificacao());

        ItemPeca itemPeca = new ItemPeca(1990, new Date(), p, 100);
        System.out.println(itemPeca.getIdentificacao());
    }

    public static void testeTipoAlocacao() {

        Peca p = new Peca(1, "marchesan", TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        ItemPeca iPeca = new ItemPeca(2011, new Date(), p, 200);
        Semeadora sem = new Semeadora("3000", "CASE", 2010);
        sem.addDivisao("linha 1", TipoAlocacao.Linha);
        sem.addPeca(1, 1, 2011, new Date(), p, 200);
    }

    public static void testeInsertSemeadora() {

        Peca p = new Peca(1, "marchesan", TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        ItemPeca iPeca = new ItemPeca(1, 2011, new Date(), p, 200);
        Semeadora sem = new Semeadora("3000", "CASE", 2010);
        sem.addDivisao("linha 1", TipoAlocacao.Linha);
        sem.addPeca(1, 1, 2011, new Date(), p, 200);

        System.out.println(sem.selecionarDivisao(1).selecionarPeca(1).getItemPeca().getAlocPeca());
        //   AlocacaoPeca alocPeca = new AlocacaoPeca(iPeca, div);

        try {

            new CadastroSemeadoras(dbSem).insertSemeadora(sem);
        } catch (InserçãoException ie) {

            ie.getRTException().printStackTrace();
        }

    }

    public static void testeSelectSemeadora() {

        Semeadora sem = new CadastroSemeadoras(dbSem).selectSemeadora(1);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Peca peca = new Peca(2, "baldan", TipoPeca.DISCO_DUPLO_DE_TOSADO);
        
        //new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).insertPeca(peca);
        
        //new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).deletePeca(2);
        
        Peca p = new Peca(3, "stara", TipoPeca.DISCO_DUPLO_DE_TOSADO);
        
        //new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).updatePeca(2, p);
        
        new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).selectPecaByTipoPeca(TipoPeca.DISCO_DUPLO_DE_TOSADO.getCodTipoPeca());
        System.out.println(CadastroPecas.pecas);
    }
}
