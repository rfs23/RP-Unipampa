package CN;

import java.util.Date;
import java.util.Map;
import java.util.Collection;

public class Atividade {

	private TipoAtividade nome;

	private Date dataRealizacao;

	private Date duracao;

	private Map fatores;

	private int codigo;

	private Semeadora semeadora;

	private Collection<DesgastePeca> desgastePeca;

	public Atividade(Date data, Date duracao, TipoAtividade nome, Map fatores) {

	}

	public void addDesgastePeca(DesgastePeca desgastePeca) {

	}

}
