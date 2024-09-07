/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cristianfranco.seminario.imc.rmi;

import cristianfranco.seminario.imc.rmi.net.Servidor;

/**
 *
 * @author Cristian
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try {
            servicio.iniciar();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    
}
