package cristianfranco.imc.modelo;

import java.io.Serializable;

/**
 * @author Cristian
 * 
 * Clase para calcular el Índice de Masa Corporal (IMC).
 */
public class CalculoImc implements Serializable {

    // Atributos de la clase
    private float peso;
    private float altura;

    // Clase interna que encapsula el resultado y el mensaje del cálculo de IMC
    public static class Imc {
        public float resultado; // Resultado del cálculo de IMC
        public String mensaje;  // Mensaje asociado al resultado del IMC
    }

    // Atributo de la clase para guardar la instancia de Imc
    private Imc imc;

    // Constructor por defecto
    public CalculoImc() {
    }

    // Constructor que inicializa peso y altura
    public CalculoImc(float peso, float altura) {
        this.peso = peso;
        this.altura = altura;
    }

    // Método que calcula y devuelve una instancia de la clase interna Imc
    public Imc getImc() {
        imc = new Imc(); // Crea una nueva instancia de Imc
        
        // Verifica que el peso y la altura sean mayores que 0
        if (peso <= 0 || altura <= 0) {
            imc.mensaje = "ERROR: El peso y la altura deben ser mayores que 0"; // Error en caso de valores inválidos
            return imc;
        } else {
            // Calcula el IMC usando la fórmula peso / altura^2
            imc.resultado = peso / (altura * altura);
            
            // Define el mensaje según el resultado del IMC
            if (imc.resultado < 18.5) {
                imc.mensaje = "Debes consultar un Medico, tu peso es muy bajo";
            } else if (imc.resultado >= 18.5 && imc.resultado <= 24.9) {
                imc.mensaje = "Estas bien de peso";
            } else if (imc.resultado > 24.9 && imc.resultado <= 29.9) {
                imc.mensaje = "Debes bajar un poco de peso";
            } else {
                imc.mensaje = "Debes consultar un Medico, tu peso es muy alto";
            }
            return imc; // Devuelve el resultado y el mensaje correspondiente
        }
    }

    // Getters y Setters para los atributos peso y altura
    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }
}
