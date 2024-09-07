package cristianfranco.imc.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import cristianfranco.imc.modelo.CalculoImc;
import cristianfranco.imc.vistas.VentanaPrincipal;

// Clase que representa un subproceso que maneja la comunicación con un cliente específico.
public class SubProcesoCliente extends Thread {

    private Socket cliente; // Socket de conexión con el cliente.
    private String ip; // Dirección IP del cliente.
    private VentanaPrincipal ventana; // Referencia a la ventana principal para actualizar la interfaz.

    // Constructor que inicializa el socket del cliente y la ventana principal.
    public SubProcesoCliente(Socket cliente, VentanaPrincipal v) {
        this.cliente = cliente;
        this.ip = cliente.getInetAddress().getHostAddress(); // Obtiene la dirección IP del cliente.
        this.ventana = v;
    }

    // Método que se ejecuta cuando se inicia el hilo.
    @Override
    public void run() {
        try {
            // Realiza el cálculo del IMC y envía la respuesta al cliente.
            CalculoImc.Imc imc = calcularImc();
            enviarRespuesta(imc);
        } catch (Exception ex) {
            // Captura y muestra cualquier error que ocurra durante el proceso.
            System.out.println(log() + ex.getMessage());
            ventana.getCajaLog().append(log() + ex.getMessage() + "\n");
            try {
                // Intenta cerrar la conexión con el cliente en caso de error.
                cliente.close();
            } catch (IOException ex1) {
                ServidorTcp.listaDeClientes.remove(ip);
            } finally {
                // Remueve la IP del cliente de la lista de clientes conectados.
                ServidorTcp.listaDeClientes.remove(ip);
            }
        }
    }

    // Método que captura los datos del cliente y realiza el cálculo del IMC.
    public CalculoImc.Imc calcularImc() throws Exception {
        DataInputStream input = null;
        try {
            input = new DataInputStream(cliente.getInputStream()); // Flujo de entrada para recibir datos del cliente.
            String msg = "Esperando el PESO: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n" + "\n");
            float peso = input.readFloat(); // Lee el peso enviado por el cliente.
            msg = "PESO: " + peso;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            msg = "Esperando La Altura: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            float altura = input.readFloat(); // Lee la altura enviada por el cliente.
            msg = "ALTURA: " + altura;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");

            // Calcula el IMC utilizando los datos recibidos.
            CalculoImc datosImc = new CalculoImc(peso, altura);
            System.out.println(log() + "IMC: " + datosImc.getImc().resultado);
            msg = "IMC: " + datosImc.getImc().resultado;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            System.out.println(log() + "MENSAJE: " + datosImc.getImc().mensaje);
            msg = "MENSAJE: " + datosImc.getImc().mensaje;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");

            // Devuelve el resultado del IMC.
            return datosImc.getImc();
        } catch (IOException ex) {
            // Manejo de errores en caso de problemas al recibir datos del cliente.
            String msg = "Error al capturar datos del cliente " + ip;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            throw new Exception("Error al capturar datos del cliente " + ip);
        }
    }

    // Método que envía la respuesta del cálculo del IMC al cliente.
    public void enviarRespuesta(CalculoImc.Imc imc) {
        // Se crea un nuevo hilo para manejar la respuesta al cliente.
        Thread hiloResponde = new Thread() {

            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(cliente.getOutputStream()); // Flujo de salida para enviar datos al cliente.
                    output.writeFloat(imc.resultado); // Envía el resultado del IMC.
                    output.writeUTF(imc.mensaje); // Envía el mensaje asociado al IMC.
                    String msg = "IMC: " + imc.resultado;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    msg = "MENSAJE: " + imc.mensaje;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    output.flush(); // Asegura que todos los datos sean enviados.

                    // Llama recursivamente para recibir nuevos datos del cliente.
                    enviarRespuesta(calcularImc());
                } catch (IOException ex) {
                    // Manejo de errores en caso de problemas al enviar datos al cliente.
                    String msg = "Error al enviar datos al cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    ServidorTcp.listaDeClientes.remove(ip);
                } catch (Exception ex) {
                    // Manejo de errores en caso de problemas al leer nuevos datos del cliente.
                    String msg = "Error al leer datos del cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    try {
                        cliente.close(); // Cierra la conexión con el cliente en caso de error.
                    } catch (IOException ex1) {
                        ServidorTcp.listaDeClientes.remove(ip);
                    } finally {
                        ServidorTcp.listaDeClientes.remove(ip);
                    }
                }
            }
        };
        hiloResponde.start(); // Inicia el hilo que maneja la respuesta al cliente.
    }

    // Métodos getter y setter para el socket del cliente.
    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

    // Método que genera un mensaje de log con la IP del cliente y la fecha/hora actual.
    public String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return ip + " -> " + f.format(new Date()) + " - ";
    }
}
