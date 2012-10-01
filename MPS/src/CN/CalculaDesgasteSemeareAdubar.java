package CN;

import java.util.Map;
import java.util.Date;

public class CalculaDesgasteSemeareAdubar implements CalculaDesgasteAtividade {

    /**
     * @see Diagrama de
     * classes.CalculaDesgasteAtividade#calculaDesgaste(java.util.Map,
     * java.util.Date, Diagrama de classes.TipoAtividade)
     *
     *
     */
    @Override
    public int calculaDesgaste(Map<String, Fator> fatores, int tempoDuracao, TipoPeca tipoPeca) {

        float desgaste = 0f;
        int qtdFatores = 0;

        if(fatores.containsKey("operador")){
        
            desgaste += (float) fatores.get("operador").valor() * (float) tempoDuracao;
            qtdFatores++;
        }
        
        if(fatores.containsKey("solo")){
        
            desgaste += (float) fatores.get("solo").valor() * (float) tempoDuracao;
            qtdFatores++;
        }
        
        if(fatores.containsKey("velocidade de trabalho")){
        
            desgaste += (float) fatores.get("velocidade de trabalho").valor() * (float) tempoDuracao;
            qtdFatores++;
        }

        desgaste -= (qtdFatores-1) * tempoDuracao;
        desgaste *= 1.07f;
        
        return Math.round(desgaste);
    }
}
