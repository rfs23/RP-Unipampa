package CN;

public enum TipoPeca {

    DISCO_DE_CORTE_DE_PALHADA {
        
        @Override
        public int getEstVidaUtil(){
            
            return 150;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 1;
        }
        
        @Override
        public String toString(){
            
            return "Disco de corte de palhada";
        }
    },
    
    EJETOR_DE_SEMENTES{
        
        @Override
        public int getEstVidaUtil(){
            
            return 250;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 2;
        }
        
        @Override
        public String toString(){
            
            return "Ejetor de sementes";
        }
    },
    
    PONTEIRA{
        
        @Override
        public int getEstVidaUtil(){
            
            return 100;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 3;
        }
        
        @Override
        public String toString(){
            
            return "Ponteira";
        }
    },
    
    ROLAMENTO{
        
        @Override
        public int getEstVidaUtil(){
            
            return 150;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 4;
        }
        
        @Override
        public String toString(){
            
            return "150";
        }
    },
    
    DOSADOR_DE_FERTILIZANTE{
        
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
            
            return 5;
        }
        
        @Override
        public String toString(){
            
            return "Dosador de fertilizante";
        }
    },
    
    DISCO_DUPLO_DE_TOSADO{
        
        @Override
        public int getEstVidaUtil(){
            
            return 100;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Linha;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 6;
        }
        
        @Override
        public String toString(){
            
            return "Disco duplo de tosado";
        }
    },
    
    DISCO_DOSADOR{
        
        @Override
        public int getEstVidaUtil(){
            
            return 250;
        }
        
        @Override
        public TipoAlocacao getTipoAlocacao(){
            
            return TipoAlocacao.Semeadora;
        }
        
        @Override
        public int getCodTipoPeca(){
            
            return 7;
        }
        
        @Override
        public String toString(){
            
            return "Disco dosador";
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
