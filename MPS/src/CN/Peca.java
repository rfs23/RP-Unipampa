package CN;

import java.util.Date;
import java.util.Collection;
import java.util.Map;

public class Peca {

	private int identificacao;

	private Date dataFabricacao;

	private String fabricante;

	private Date dataAquisicao;

	private TipoPeca tipo;

	private Collection<ItemPeca> itemPeca;

	private CalculaDesgasteAtividade calculaDesgasteAtividade;

	private AdministradorManutencoes administradorManutencoes;

	public Peca(Date dataFab, String fab, Date dataAquis, TipoPeca tipo) {

	}

	public void setTipo(TipoPeca tipo) {

	}

	public void setCalculaDesgaste(CalculaDesgasteAtividade calcDesgaste) {

	}

	public int calculaDesgaste(Map fatores, Date duracao, TipoAtividade atividade) {
		return 0;
	}

	public TipoPeca getTipoPeca() {
		return null;
	}

	public boolean matches(int Map) {
		return false;
	}

}
