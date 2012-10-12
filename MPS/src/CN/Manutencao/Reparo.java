package CN.Manutencao;

import CN.AlocacaoPeca;
import CN.Semeadora;
import Exceções.TempoVidaUtilForaDosLimitesException;
import java.util.Date;

public class Reparo extends Manutencao {

    public Reparo(Date data) {
        
        super(data);
    }

    @Override
    public boolean realizarManutencao() throws TempoVidaUtilForaDosLimitesException {

        int valRestauro = super.getTempoVidaUtil() - super.getPeca().getTempoVidaUtil();
        super.getPeca().acrescentarVidaUtil(valRestauro);
        this.restauro = valRestauro;

        //return super.getRestauro();
        
        return true;
    }
    
    @Override
    public void setSemeadora(Semeadora sem){
        
        super.setSemeadora(sem);
    }
    
    @Override
    public void setAlocacaoPeca(AlocacaoPeca peca){
        
        super.setAlocacaoPeca(peca);
    }
    
    @Override
     public void setTempoVidaUtil(int tempoVidaUtil) {
        
        super.setTempoVidaUtil(tempoVidaUtil);
    }
    
    @Override
    public String toString(){
        
        return "Reparo: " + super.toString();
    }
    
    @Override
    public int hashCode(){
        
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj){
        
        return super.equals(obj);
    }
}
