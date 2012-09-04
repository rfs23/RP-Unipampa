package CN;

import DB.AcessoBD;
import DB.AcessoPostgres;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AdministradorManutencoes {

	private String nome;

	private Collection<Semeadora> semeadora;

	private Collection<Peca> peca;
        
        public boolean addSemeadora (Semeadora semeadora){
            
            AcessoBD adb = AcessoPostgres.getInstance();
            adb.executeSQL("insert into semeadora values");
            return true;
        }

	public Peca selecionarPeca(int indent) {
		return null;
	}

	public int selecionarSemeadora(int ident) {
		return 0;
	}

	public List listarSemeadoras() {
		return null;
	}

	public List listarPecas() {
		return null;
	}

	public void excluirSemeadora(int ident) {

	}

	public void addPeca(Peca peca) {

	}

	public void excluirPeca(Peca ident) {

	}

	/**
	 *  
	 */
	public List buscarSemeadoras(Map caracteristicas) {
		return null;
	}

	public List buscarPecas(Map caracteristicas) {
		return null;
	}

}
