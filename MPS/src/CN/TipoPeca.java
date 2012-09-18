package CN;

public enum TipoPeca {

    Mancal {
        
        @Override
        public int getEstVidaUtil(){
            
            return 200;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 1;
        }
    };

    public int getEstVidaUtil() {
        return 0;
    }
    
    public TipoAlocacao getTipoAlocacao(){
        
        return null;
    }
    
    public int getCodTipoPeca(){
        
        return 0;
    }
}
