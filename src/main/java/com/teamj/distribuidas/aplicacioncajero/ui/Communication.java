/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.distribuidas.aplicacioncajero.ui;

//import com.daraf.projectdarafprotocol.AppClient;
//import com.daraf.projectdarafprotocol.Mensaje;
//import com.daraf.projectdarafprotocol.clienteapp.MensajeRQ;
//import com.daraf.projectdarafprotocol.clienteapp.MensajeRS;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaClienteRQ;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaClienteRS;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaFacturaRQ;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaFacturaRS;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaProductoRQ;
//import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaProductoRS;
//import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoClienteRQ;
//import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoClienteRS;
//import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoFacturaRQ;
//import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoFacturaRS;
//import com.daraf.projectdarafprotocol.clienteapp.seguridades.AutenticacionEmpresaRQ;
//import com.daraf.projectdarafprotocol.clienteapp.seguridades.AutenticacionEmpresaRS;
//import com.daraf.projectdarafprotocol.model.Cliente;
//import com.daraf.projectdarafprotocol.model.DetalleFacturaAppRQ;
//import com.daraf.projectdarafprotocol.model.Empresa;
//import com.daraf.projectdarafprotocol.model.Factura;
//import com.daraf.projectdarafprotocol.model.Producto;
//import com.daraf.projectdarafutil.NetUtil;
import com.teamj.distribuidas.corebancario.model.Cuenta;
import com.teamj.distribuidas.integracion.protocolo.AppCajero;
import com.teamj.distribuidas.integracion.protocolo.Mensaje;
import com.teamj.distribuidas.integracion.protocolo.MensajeRQ;
import com.teamj.distribuidas.integracion.protocolo.MensajeRS;
import com.teamj.distribuidas.integracion.protocolo.consulta.CuentaRQ;
import com.teamj.distribuidas.integracion.protocolo.consulta.CuentaRS;
import com.teamj.distribuidas.integracion.protocolo.seguridad.AutenticacionRQ;
import com.teamj.distribuidas.integracion.protocolo.seguridad.AutenticacionRS;
import com.teamj.distribuidas.integracion.protocolo.transaccion.DepositoRQ;
import com.teamj.distribuidas.integracion.protocolo.transaccion.DepositoRS;
import com.teamj.distribuidas.integracion.protocolo.transaccion.RetiroRQ;
import com.teamj.distribuidas.integracion.protocolo.transaccion.RetiroRS;
import com.teamj.distribuidas.integracion.server.AppClient;
import com.teamj.distribuidas.integracion.util.NetUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Dennys
 */
public class Communication {

    public static final String OK_RESPONSE = "OK";
    public static final String BAD_RESPONSE = "BAD";
    public static final String NULL_PARAMETERS = "Uno de los campos usados para el metodo es nulo";

    /**
     *
     * @param usuario
     * @param password
     * @return nulo si no se ha encontrado la empresa en el sistema caso
     * contratio trae el objeto empresa
     */
    public static boolean loginEmpleado(String usuario, String password) {
        if (usuario != null && password != null) {
            AppClient appClient = new AppClient();
            appClient.setIp("192.168.1.115");
            AutenticacionRQ aerq = new AutenticacionRQ();

            aerq.setClave(password);
            aerq.setUsuario(usuario);

            MensajeRQ mensajeRQ = new MensajeRQ("marco", Mensaje.ID_MENSAJE_AUTENTICACION);
            mensajeRQ.setCuerpo(aerq);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            if (mensajeRS != null) {
                AutenticacionRS aers = (AutenticacionRS) mensajeRS.getCuerpo();
                if (aers.getMessage().equals("OK")) {
                    System.out.println("" + aers.getMessage());
                    return true;
                }
            }
        }
        return false;
    }

    public static Cuenta buscarCuenta(String datos,String tipo) {
        if (datos != null) {
            AppClient appClient = new AppClient();
            appClient.setIp("192.168.1.115");
            CuentaRQ cueRQ = new CuentaRQ();
            cueRQ.setCuentaCliente(datos);
            cueRQ.setTipoCuenta(tipo);

            MensajeRQ mensajeRQ = new MensajeRQ("CONSULTACU", Mensaje.ID_MENSAJE_CONSULTACUENTA);
            mensajeRQ.setCuerpo(cueRQ);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            CuentaRS cueRS = (CuentaRS) mensajeRS.getCuerpo();
            if (cueRS.getMessage().equals("OK")) {
                System.out.println("" + cueRS.getCuenta());
                return cueRS.getCuenta();
            }
        }
        return null;
    }

    public static String registrarMovimeinto(String cedula, String cuenta, String monto, String tipo, String tipoC) {

        if (monto != null && tipo != null) {
            if (tipo.equals("RE")) {
                RetiroRQ retiroRQ = new RetiroRQ();
                AppClient appClient = new AppClient();
                appClient.setIp("192.168.1.115");
                retiroRQ.setDocumentoCliente(cedula);
                retiroRQ.setNumeroCuenta(cuenta);
                retiroRQ.setTipoCuenta(tipoC);
                retiroRQ.setValorRetiro(monto);
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
                retiroRQ.setFechaRetiro(sdf.format(new Date()));
                MensajeRQ mensajeRQ = new MensajeRQ(NetUtil.getLocalIPAddress(), Mensaje.ID_MENSAJE_RETIRO);
                mensajeRQ.setCuerpo(retiroRQ);
                MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
                if (mensajeRS != null) {
                    RetiroRS retiroRS = (RetiroRS) mensajeRS.getCuerpo();
                    return retiroRS.getMessage();
                }

            } else {
                DepositoRQ depositoRQ = new DepositoRQ();
                AppClient appClient = new AppClient();
                appClient.setIp("192.168.1.115");
                depositoRQ.setDocumentoCliente(cedula);
                depositoRQ.setNumeroCuenta(cuenta);
                depositoRQ.setTipoCuenta(tipoC);
                depositoRQ.setValorDeposito(monto);
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
                depositoRQ.setFechaDeposito(sdf.format(new Date()));
                MensajeRQ mensajeRQ = new MensajeRQ(NetUtil.getLocalIPAddress(), Mensaje.ID_MENSAJE_DEPOSITO);
                mensajeRQ.setCuerpo(depositoRQ);
                MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
                if (mensajeRS != null) {
                    DepositoRS depositoRS = (DepositoRS) mensajeRS.getCuerpo();
                    return depositoRS.getMessage();
                }

            }
            return OK_RESPONSE;
        } else {
            return NULL_PARAMETERS;
        }

    }

}
