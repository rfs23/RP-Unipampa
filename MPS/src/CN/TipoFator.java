package CN;

public enum TipoFator {

    SOLO_ARENOSO {
        @Override
        public float valor() {

            return 1.12f;
        }

        @Override
        public String toString() {

            return "Solo arenoso";
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
    };

    public float valor() {
        
        return 0;
    }
}
