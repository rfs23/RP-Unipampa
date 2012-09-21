package CN;

import java.util.Collection;
import java.util.Map;
import java.util.Date;

public interface CalculaDesgasteAtividade {

    /**
     *
     */
    public abstract int calculaDesgaste(Map<String, TipoFator> fatores, int TempoDuracao, TipoPeca tipoPeca);
}
