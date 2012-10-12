package CN;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        receitas();
        //despesas();
    }

    public static void receitas() {

        Map<String, Object> parametros = new HashMap<String, Object>();
        ArrayList<Parametro> receitas = new ArrayList<Parametro>();        

        parametros.put("dataAtual", new Date());
        parametros.put("dataFim", new Date(2012, 9, 3));
        parametros.put("dataInicio", new Date(2010, 9, 1));
        
        try {
            
            geraRelatorio.geraRelatorio("relatorio testeMPS sem where", parametros);
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void despesas() {

        ArrayList<Parametro> despesas = new ArrayList<Parametro>();
        Parametro classParam = new Parametro("pessoa_login", "matheus");//passar o usuario logado.

        despesas.add(classParam);

      //  geraRelatorio rel = new geraRelatorio("relatorio despesas", despesas);

    }
}
