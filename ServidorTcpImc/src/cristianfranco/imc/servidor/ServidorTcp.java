package cristianfranco.imc.servidor;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import cristianfranco.imc.vistas.VentanaPrincipal;

/**
 * Servidor TCP que maneja conexiones de clientes.
 */
public class ServidorTcp extends Thread {

    private volatile boolean estado; // Estado del servidor (encendido o apagado).
    public static Map<String, SubProcesoCliente> listaDeClientes; // Lista concurrente de clientes conectados.
    private int puerto; // Puerto en el que escucha el servidor.
    private ServerSocket servicio; // Socket del servidor para aceptar conexiones.
    private VentanaPrincipal ventana; // Referencia a la interfaz gráfica principal.

    // Constructor que inicializa el servidor con un puerto y una referencia a la interfaz gráfica.
    public ServidorTcp(Integer puerto, VentanaPrincipal v) {
        this.puerto = (puerto != null && puerto > 0) ? puerto : 9007; // Asigna el puerto o usa el puerto 9007 por defecto.
        this.ventana = v;
        listaDeClientes = new ConcurrentHashMap<>(); // Inicializa la lista de clientes como un mapa concurrente.
    }

    @Override
    public void run() {
        iniciarServicio(); // Inicia el servicio del servidor.
    }

    /**
     * Inicializa el servicio de servidor y acepta conexiones de clientes.
     */
    public void iniciarServicio() {
        try {
            servicio = new ServerSocket(puerto); // Crea un socket del servidor en el puerto especificado.
            estado = true; // Cambia el estado del servidor a encendido.
            actualizarUI(true); // Actualiza la interfaz de usuario para reflejar el estado online.
            logAndAppend("Servidor disponible en el Puerto " + puerto); // Registra el inicio del servidor.

            // Bucle principal para aceptar conexiones de clientes mientras el servidor esté en línea.
            while (estado) {
                Socket cliente = servicio.accept(); // Acepta una nueva conexión de un cliente.
                String ip = cliente.getInetAddress().getHostAddress(); // Obtiene la IP del cliente.
                logAndAppend("Cliente " + ip + " conectado"); // Registra la conexión del cliente.

                // Crea un nuevo subproceso para manejar la conexión con el cliente.
                SubProcesoCliente atencion = new SubProcesoCliente(cliente, ventana);
                listaDeClientes.put(ip, atencion); // Añade el cliente a la lista de clientes conectados.
                atencion.start(); // Inicia el subproceso para atender al cliente.
            }
        } catch (IOException ex) {
            // Manejo de errores al abrir el puerto del servidor.
            logAndAppend("ERROR al abrir el puerto " + puerto);
            actualizarUI(false); // Actualiza la interfaz de usuario para reflejar el estado offline.
        }
    }

    /**
     * Detiene el servicio del servidor y desconecta a todos los clientes.
     */
    public void detenerServicio() {
        if (estado) {
            estado = false; // Cambia el estado del servidor a apagado.
            actualizarUI(false); // Actualiza la interfaz de usuario para reflejar el estado offline.

            // Desconectar a todos los clientes conectados.
            listaDeClientes.forEach((ip, cliente) -> {
                logAndAppend("Desconectando cliente " + ip); // Registra la desconexión del cliente.
                // Puedes añadir aquí el código necesario para desconectar efectivamente al cliente.
            });

            // Limpiar la lista de clientes y cerrar el servidor.
            listaDeClientes.clear(); // Limpia la lista de clientes.
            try {
                servicio.close(); // Cierra el socket del servidor.
            } catch (IOException ex) {
                logAndAppend("ERROR al cerrar el puerto " + puerto); // Manejo de errores al cerrar el servidor.
            }
        }
    }

    /**
     * Actualiza la interfaz de usuario según el estado del servidor.
     * @param enLinea Indica si el servidor está en línea o no.
     */
    private void actualizarUI(boolean enLinea) {
        ventana.getBtnIniciar().setText(enLinea ? "DETENER" : "INICIAR"); // Cambia el texto del botón de inicio.
        ventana.getTxtEstado().setText(enLinea ? "ONLINE" : "OFF LINE"); // Actualiza el estado del servidor en la interfaz.
        ventana.getTxtEstado().setForeground(enLinea ? Color.GREEN : Color.RED); // Cambia el color del texto del estado.
        ventana.getBtnIniciar().setForeground(enLinea ? Color.RED : Color.GREEN); // Cambia el color del texto del botón.
    }

    /**
     * Registra el mensaje y lo añade a la caja de log.
     * @param message El mensaje a registrar.
     */
    private void logAndAppend(String message) {
        String logMessage = log() + message; // Formatea el mensaje con la fecha y hora actual.
        System.out.println(logMessage); // Muestra el mensaje en la consola.
        ventana.getCajaLog().append(logMessage + "\n"); // Añade el mensaje al log de la interfaz gráfica.
    }

    /**
     * Obtiene la cadena de formato para los logs.
     * @return Cadena de log formateada con fecha y hora actual.
     */
    private String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"); // Formato de la fecha y hora para los logs.
        return f.format(new Date()) + " - "; // Devuelve la fecha y hora actual formateada.
    }
}
