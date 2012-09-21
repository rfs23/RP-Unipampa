package CN;

public class DesgastePeca {

    private int desgaste;
    private AlocacaoPeca alocacaoPeca;
    private Atividade atividade;

    public DesgastePeca(AlocacaoPeca peca, int desgaste) {
        
        this.alocacaoPeca = peca;
        this.desgaste = desgaste;
    }

    public int getDesgaste() {
    
        return desgaste;
    }

    public AlocacaoPeca getAlocacaoPeca() {
        return alocacaoPeca;
    }

    public void setAtividade(Atividade atividade) {
        
        if(atividade.selecionarDesgastePeca(this) != null){
            
            this.atividade = atividade;
        }
        
    }
    
    
}
