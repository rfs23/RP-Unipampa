package CN;

public enum TipoAtividade {

    SEMEAR_ADUBAR {
        
        @Override
        public String toString() {

            return "Semear e adubar";
        }
        
        @Override
        public int getCodTipoAtividade(){
            
            return 1;
        }
    };

    public int getCodTipoAtividade() {

        return 0;
    }
}
