package CN;

public enum Fator {

    SOLO_ARENOSO {
        @Override
        public float valor() {

            return 1.12f;
        }

        @Override
        public String toString() {

            return "Solo arenoso";
        }
        
        @Override
        public int getCodigo(){
            
            return 1;
        }
    },
    SOLO_ARGILOSO {
        @Override
        public float valor() {

            return 1;
        }

        @Override
        public String toString() {

            return "Solo argiloso";
        }
        
        @Override
        public int getCodigo(){
            
            return 2;
        }
    },
    VELOCIDADE_DE_TRABALHO_RECOMENDADA {
        @Override
        public float valor() {

            return 1;
        }

        @Override
        public String toString() {

            return "Velocidade de trabalho recomendada";
        }
        
        @Override
        public int getCodigo(){
            
            return 3;
        }
    },
    VELOCIDADE_DE_TRABALHO_FORA_DA_RECOMENDADA {
        @Override
        public float valor() {

            return 1.15f;
        }

        @Override
        public String toString() {

            return "Velocidade de trabalho fora da recomendada";
        }
        
        @Override
        public int getCodigo(){
            
            return 4;
        }
    },
    OPERADOR_TREINADO {
        @Override
        public float valor() {

            return 1;
        }

        @Override
        public String toString() {

            return "Operador treinado";
        }
        
        @Override
        public int getCodigo(){
            
            return 5;
        }
    },
    OPERADOR_DESTREINADO {
        @Override
        public float valor() {

            return 1.2f;
        }

        @Override
        public String toString() {

            return "Operador destreinado";
        }
        
        @Override
        public int getCodigo(){
            
            return 6;
        }
    };
    
    

    public float valor() {

        return 0;
    }
    
    public int getCodigo(){
        
        return 0;
    }
}
