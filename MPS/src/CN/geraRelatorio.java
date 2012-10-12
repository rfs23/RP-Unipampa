package CN;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import Repositório.AcessoPostgres;
import java.sql.SQLException;
import java.util.Map;

public class geraRelatorio {

    static JasperPrint rel = null;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void geraRelatorio(String nomeRel, Map<String,Object> par) throws SQLException {

        //HashMap par = new HashMap();
        Connection con = AcessoPostgres.getInstance().connect();
        
        String arquivoJasper = "src/CN/" + nomeRel + ".jasper";

       /* for (Parametro param : parametros) {

            String nome = param.getNome();
            String valor = param.getValor();
            par.put(nome, valor);

        }*/
        try {
            rel = JasperFillManager.fillReport(arquivoJasper, par, con);
        } catch (JRException e) {

            e.printStackTrace();
        }

        JasperViewer.viewReport(rel, false);

    }
}
