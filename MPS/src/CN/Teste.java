/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Repositório.AcessoPostgres;
import Repositório.DBItemPeca;
import Repositório.DBPeca;
import Repositório.DBSemeadora;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author rafael
 */
public class Teste {

    private String teste;
    static CadastroSemeadoras cSem = new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance()));
    static CadastroPecas cPecas = new CadastroPecas(new DBPeca(AcessoPostgres.getInstance()));
    static CadastroItensPeca cItensPeca = new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance()));

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

            cSem.insertSemeadora(sem);
        } catch (InsercaoException ie) {

            ie.getRTException().printStackTrace();
        }
    }

    public static void testeDeleçãoSemeadora() {

        try {

            cSem.deleteSemeadora(1);
        } catch (DelecaoException de) {

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
        sem.addPeca(1, cItensPeca.selectItemPeca(1));
    }

    public static void testeInsertSemeadora() {

        Semeadora sem = new Semeadora("3000", "CASE", 2010);
        sem.addDivisao("Semeadora", TipoAlocacao.Semeadora);
        sem.addDivisao("Linha 1", TipoAlocacao.Linha);

        sem.addPeca(1, cItensPeca.selectItemPeca(1), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(2), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(3), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(4), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(5), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(6), new Date());
        sem.addPeca(2, cItensPeca.selectItemPeca(7), new Date());

        try {

            cSem.insertSemeadora(sem);
        } catch (InsercaoException ie) {

            ie.getRTException().printStackTrace();
        }

    }

    public static void addPecas() {

        Peca p = new Peca(1, "Marchesan", TipoPeca.DISCO_DOSADOR);
        Peca p2 = new Peca(2, "Marchesan", TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        Peca p3 = new Peca(3, "Marchesan", TipoPeca.EJETOR_DE_SEMENTES);
        Peca p4 = new Peca(4, "Marchesan", TipoPeca.PONTEIRA);
        Peca p5 = new Peca(5, "Marchesan", TipoPeca.ROLAMENTO);
        Peca p6 = new Peca(6, "Marchesan", TipoPeca.DOSADOR_DE_FERTILIZANTE);
        Peca p7 = new Peca(7, "Marchesan", TipoPeca.DISCO_DUPLO_DE_TOSADO);

        Peca[] pecas = {p, p2, p3, p4, p5, p6, p7};

        for (Peca peca : pecas) {

            cPecas.insertPeca(peca);
        }
    }

    public static void addItensPeca() {

        ItemPeca ip = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(1), 200);
        ItemPeca ip2 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(2), 150);
        ItemPeca ip3 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(3), 250);
        ItemPeca ip4 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(4), 100);
        ItemPeca ip5 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(5), 150);
        ItemPeca ip6 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(6), 200);
        ItemPeca ip7 = new ItemPeca(2009, new Date(2010 - 1900, 01, 01), cPecas.selectPeca(7), 100);

        ItemPeca[] iPecas = {ip, ip2, ip3, ip4, ip5, ip6, ip7};

        for (ItemPeca iPeca : iPecas) {

            try {

                cItensPeca.insertItemPeca(iPeca);
            } catch (InsercaoException ie) {

                System.out.println(ie.getRTException().getMessage());
            }

        }
    }

    public static void testeSelectSemeadora() {

        Semeadora sem = cSem.selectSemeadora(1);
    }

    public static void testeInsertPeca() {

        Peca peca = new Peca(2, "baldan", TipoPeca.DISCO_DUPLO_DE_TOSADO);

        new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).insertPeca(peca);
    }

    public static void testeDeletePeca() {

        new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).deletePeca(2);
    }

    public static void testeUpdatePeca() {

        Peca p = new Peca(3, "stara", TipoPeca.DISCO_DUPLO_DE_TOSADO);

        new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).updatePeca(2, p);
    }

    public static void testeSelectPeca() {

        new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).selectPecaByTipoPeca(TipoPeca.DISCO_DUPLO_DE_TOSADO.getCodTipoPeca());
        System.out.println(CadastroPecas.pecas);
    }

    public static void testeInsertItemPeca() {

        Peca peca = new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).selectPeca(1);
        ItemPeca iPeca = new ItemPeca(2010, new Date(), peca, peca.getTipo().getEstVidaUtil());

        new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance())).insertItemPeca(iPeca);
    }

    public static void testeDeleteItemPeca() {

        new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance())).deleteItemPeca(3);
    }

    public static void testeUpdateItemPeca() {

        Peca peca = new CadastroPecas(new DBPeca(AcessoPostgres.getInstance())).selectPeca(1);
        ItemPeca iPeca = new ItemPeca(2010, new Date(), peca, 100);

        new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance())).updateItemPeca(2, iPeca);
    }

    public static void testeRealizaAtividade() {

        Semeadora sem = cSem.selectSemeadora(1);
        cSem.listarAtividadesRealizadas(1);
        sem = cSem.selectSemeadora(1);

        Map<String, Fator> fatores = new HashMap<String, Fator>();
        fatores.put("operador", Fator.OPERADOR_DESTREINADO);
        fatores.put("solo", Fator.SOLO_ARGILOSO);
        fatores.put("velocidade de trabalho", Fator.VELOCIDADE_DE_TRABALHO_FORA_DA_RECOMENDADA);

        sem.realizarAtividade(new Date(), 5, TipoAtividade.SEMEAR_ADUBAR, fatores);

        try {

            new DBSemeadora(AcessoPostgres.getInstance()).registrarAtividade(1, sem.selecionarAtividade(1));
        } catch (InsercaoException ie) {

            System.out.println(ie.getRTException().getMessage());
        }
    }
    
    public static void testeCancelaAtividade(){
        
        cSem.selectSemeadora(1);
        cSem.listarAtividadesRealizadas(1);
        Semeadora sem = cSem.selectSemeadora(1);
        
        cSem.cancelarAtividade(1, 1, new Date());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
}
