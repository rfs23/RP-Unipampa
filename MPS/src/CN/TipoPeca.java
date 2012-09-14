package CN;

public enum TipoPeca {

    Teste {
        int estVidaUtil = 0;

        @Override
        public void setEstVidaUtil(int estVidaUtil){
            
            this.estVidaUtil = estVidaUtil;
        }
        
        @Override
        public int getEstVidaUtil(){
            
            return this.estVidaUtil;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
    };

    public void setEstVidaUtil(int estVidaUtil) {
    }

    public int getEstVidaUtil() {
        return 0;
    }
    
    public TipoAlocacao getTipoAlocacao(){
        
        return null;
    }
}
