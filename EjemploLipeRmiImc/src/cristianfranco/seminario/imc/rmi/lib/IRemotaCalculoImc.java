
package cristianfranco.seminario.imc.rmi.lib;

/**
 * Interfaz remota para el cálculo del Índice de Masa Corporal (IMC).
 * Esta interfaz define un método que permite calcular el IMC basado en
 * los datos proporcionados.
 * 
 * @author Cristian
 */
public interface IRemotaCalculoImc {
    
    /**
     * Método que calcula el IMC utilizando los datos proporcionados.
     * Este método recibe un objeto de tipo `DatosImc` que contiene el peso y la altura,
     * y devuelve otro objeto `DatosImc` con el resultado del IMC y su interpretación.
     * 
     * @param datos Un objeto `DatosImc` que contiene el peso y la altura.
     * @return Un objeto `DatosImc` con el resultado del IMC y la interpretación.
     */
    public DatosImc calcularImc(DatosImc datos);
}
