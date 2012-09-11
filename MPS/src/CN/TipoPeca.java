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
    };

    public void setEstVidaUtil(int estVidaUtil) {
    }

    public int getEstVidaUtil() {
        return 0;
    }
}
