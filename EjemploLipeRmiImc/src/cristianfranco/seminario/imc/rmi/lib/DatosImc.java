/*
 * Clase DatosImc que implementa Serializable para permitir que sus instancias
 * puedan ser enviadas a través de la red o almacenadas en un archivo.
 */
package cristianfranco.seminario.imc.rmi.lib;

import java.io.Serializable;

/**
 * Clase que representa los datos necesarios para calcular el Índice de Masa Corporal (IMC).
 * Implementa la interfaz Serializable para que los objetos de esta clase puedan ser
 * serializados y transferidos a través de la red.
 * 
 * @autor Cristian
 */
public class DatosImc implements Serializable {
    
    // Variables para almacenar el peso, altura, resultado del IMC y la interpretación del resultado
    private float peso;
    private float altura;
    private float resultado;
    private String interpretacion;
    
    /**
     * Constructor por defecto, necesario para la serialización.
     */
    public DatosImc(){}
    
    /**
     * Constructor que inicializa el peso y la altura.
     * 
     * @param peso El peso de la persona
     * @param altura La altura de la persona
     */
    public DatosImc(float peso, float altura){
        this.peso = peso;
        this.altura = altura;
    }

    /**
     * Obtiene el peso de la persona.
     * 
     * @return El peso como un valor flotante
     */
    public float getPeso() {
        return peso;
    }

    /**
     * Establece el peso de la persona.
     * 
     * @param peso El nuevo peso a establecer
     */
    public void setPeso(float peso) {
        this.peso = peso;
    }

    /**
     * Obtiene la altura de la persona.
     * 
     * @return La altura como un valor flotante
     */
    public float getAltura() {
        return altura;
    }

    /**
     * Establece la altura de la persona.
     * 
     * @param altura La nueva altura a establecer
     */
    public void setAltura(float altura) {
        this.altura = altura;
    }

    /**
     * Obtiene el resultado del cálculo del IMC.
     * 
     * @return El resultado del IMC como un valor flotante
     */
    public float getResultado() {
        return resultado;
    }

    /**
     * Establece el resultado del cálculo del IMC.
     * 
     * @param resultado El nuevo resultado del IMC a establecer
     */
    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    /**
     * Obtiene la interpretación del resultado del IMC.
     * 
     * @return La interpretación del IMC como una cadena de texto
     */
    public String getInterpretacion() {
        return interpretacion;
    }

    /**
     * Establece la interpretación del resultado del IMC.
     * 
     * @param interpretacion La nueva interpretación a establecer
     */
    public void setInterpretacion(String interpretacion) {
        this.interpretacion = interpretacion;
    }
}
