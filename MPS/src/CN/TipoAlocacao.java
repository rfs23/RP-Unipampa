/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CN;

/**
 *
 * @author rafael
 */
public enum TipoAlocacao {

    Semeadora {
        @Override
        public int getCodTipoAlocacao() {

            return 1;
        }
    },
    Linha {
        @Override
        public int getCodTipoAlocacao() {

            return 2;
        }
    };

    public int getCodTipoAlocacao() {

        return 0;
    }
    
}
