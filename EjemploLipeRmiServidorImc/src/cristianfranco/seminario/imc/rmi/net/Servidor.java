/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cristianfranco.seminario.imc.rmi.net;

import javax.print.attribute.standard.Severity;
import cristianfranco.seminario.imc.rmi.lib.IRemotaCalculoImc;
import javax.imageio.IIOException;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

/**
 * Clase que representa un servidor RMI para el cálculo del Índice de Masa Corporal (IMC).
 * Esta clase configura y maneja un servidor que permite la invocación remota de métodos 
 * para calcular el IMC.
 * 
 * @author Cristian
 */
public class Servidor {
    // Puerto en el que el servidor escuchará conexiones
    private int puerto = 9007;
    
    // Handler para manejar las invocaciones de métodos remotos
    private CallHandler invocador;
    
    // Objeto del servidor que maneja las conexiones RMI
    private Server servidor;
    
    // Implementación del cálculo del IMC
    private CalculoRmiImcImplem calculoImc;
    
    // Interfaz remota para el cálculo del IMC
    private IRemotaCalculoImc calculoImcRemoto;
    
    /**
     * Constructor que inicializa los componentes necesarios para el servidor RMI.
     */
    public Servidor() {
        invocador = new CallHandler();
        servidor = new Server();
        calculoImc = new CalculoRmiImcImplem();
    }
    
    /**
     * Método que inicia el servidor RMI y lo pone en estado de escucha para conexiones entrantes.
     * @throws Exception Si ocurre un error al iniciar el servidor, se lanza una excepción con un mensaje descriptivo.
     */
    public void iniciar() throws Exception {
        try {
            // Registrar la implementación del IMC para que esté disponible remotamente
            invocador.registerGlobal(IRemotaCalculoImc.class, calculoImc);
            
            // Iniciar el servidor en el puerto especificado
            servidor.bind(puerto, invocador);
        } catch (LipeRMIException e) {
            throw new Exception("Error: No es posible invocar métodos remotos");
        } catch (IIOException ex) {
            throw new Exception("Error I/O");
        }
    }
    
    /**
     * Método que detiene el servidor RMI, cerrando todas las conexiones y liberando recursos.
     */
    public void detener() {
        servidor.close();
    }
}
