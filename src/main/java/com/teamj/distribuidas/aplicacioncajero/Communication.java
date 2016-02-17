/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.distribuidas.aplicacioncajero;

import com.daraf.projectdarafprotocol.AppClient;
import com.daraf.projectdarafprotocol.Mensaje;
import com.daraf.projectdarafprotocol.clienteapp.MensajeRQ;
import com.daraf.projectdarafprotocol.clienteapp.MensajeRS;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaClienteRQ;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaClienteRS;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaFacturaRQ;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaFacturaRS;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaProductoRQ;
import com.daraf.projectdarafprotocol.clienteapp.consultas.ConsultaProductoRS;
import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoClienteRQ;
import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoClienteRS;
import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoFacturaRQ;
import com.daraf.projectdarafprotocol.clienteapp.ingresos.IngresoFacturaRS;
import com.daraf.projectdarafprotocol.clienteapp.seguridades.AutenticacionEmpresaRQ;
import com.daraf.projectdarafprotocol.clienteapp.seguridades.AutenticacionEmpresaRS;
import com.daraf.projectdarafprotocol.model.Cliente;
import com.daraf.projectdarafprotocol.model.DetalleFacturaAppRQ;
import com.daraf.projectdarafprotocol.model.Empresa;
import com.daraf.projectdarafprotocol.model.Factura;
import com.daraf.projectdarafprotocol.model.Producto;
import com.daraf.projectdarafutil.NetUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public static Empleado retrieveEmpleado(String usuario, String password) {
        if (usuario != null && password != null) {
            AppClient appClient = new AppClient();
            AutenticacionRQ aerq = new AutenticacionRQ();

            aerq.setClave(password);
            aerq.setUsuario(usuario);

            MensajeRQ mensajeRQ = new MensajeRQ("marco", Mensaje.ID_MENSAJE_AUTENTICACION);
            mensajeRQ.setCuerpo(aerq);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            if (mensajeRS != null) {
                AutenticacionRS aers = (AutenticacionRS) mensajeRS.getCuerpo();
                if (aers.getResultado().equals("1")) {
                    System.out.println("" + aers.getEmpleado());
                    return aers.getEmpleado();
                }
            }
        }
        return null;
    }

    public static Cuenta buscarCuenta(String datos) {
        if (datos != null && datos.length() == 10) {
            AppCajero appCajero = new AppCajero();
            CuentaRQ cueRQ = new CuentaRQ();
            cueRQ.setIdentificacion(datos);

            MensajeRQ mensajeRQ = new MensajeRQ("CONSULTACU", Mensaje.ID_MENSAJE_CONSULTACUENTA);
            mensajeRQ.setCuerpo(cueRQ);
            MensajeRS mensajeRS = appCajero.sendRequest(mensajeRQ);
            CuentaRS cueRS = (CuentaRS) mensajeRS.getCuerpo();
            if (cueRS.getResultado().equals("1")) {
                System.out.println("" + cueRS.getCliente());
                return cueRS.getCuenta();
            }

        }
        return null;
    }

    public static Cliente buscarCliente(String datos) {
        if (datos != null && datos.length() == 10) {
            AppCajero appCajero = new AppCajero();
            ConsultarClienteRQ cliRQ = new ConsultarClienteRQ();
            cliRQ.setIdentificacion(datos);

            MensajeRQ mensajeRQ = new MensajeRQ("CONSULTACL", Mensaje.ID_MENSAJE_CONSULTACLIENTE);
            mensajeRQ.setCuerpo(cliRQ);
            MensajeRS mensajeRS = appCajero.sendRequest(mensajeRQ);
            ConsultarClienteRS cliRS = (ConsultarClienteRS) mensajeRS.getCuerpo();
            if (cliRS.getResultado().equals("1")) {
                System.out.println("" + cliRS.getCliente());
                return cliRS.getCliente();
            }

        }
        return null;
    }

    public static Movimiento buscarMovimiento(String datos) {
        if (datos != null && datos.length() == 10) {
            AppCajero appCajero = new AppCajero();
            ConsultarMovimientoRQ movRQ = new ConsultarMovimientoRQ();
            movRQ.setIdentificacion(datos);

            MensajeRQ mensajeRQ = new MensajeRQ("CONSULTAMO", Mensaje.ID_MENSAJE_CONSULTAMOVIMIENTO);
            mensajeRQ.setCuerpo(movRQ);
            MensajeRS mensajeRS = appCajero.sendRequest(mensajeRQ);
            ConsultarMovimientoRS movRS = (ConsultarMovimientoRS) mensajeRS.getCuerpo();
            if (movRS.getResultado().equals("1")) {
                System.out.println("" + movRS.getMovimiento());
                return movRS.getMovimiento();
            }

        }
        return null;
    }

    public static String registrarMovimeinto(String cedula, String cuenta, String monto, String tipo, Date fecha) {

        if (monto != null && tipo != null) {
            if (tipo.equals("RE")) {
                RetiroRQ retiroRQ = new RetiroRQ();
                AppClient appClient = new AppClient();
                retiroRQ.setDocumentoCliente(cedula);
                retiroRQ.setNumeroCuenta(cuenta);
                retiroRQ.setTipoCuenta(tipo);
                retiroRQ.setValorRetiro(monto);
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                retiroRQ.setFechaRetiro(sdf.format(fecha));
                MensajeRQ mensajeRQ = new MensajeRQ(NetUtil.getLocalIPAddress(), Mensaje.ID_MENSAJE_REGISTROMOVIMIENTO);
                mensajeRQ.setCuerpo(retiroRQ);
                MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
                if (mensajeRS != null) {
                    RetiroRS retiroRS = (RetiroRS) mensajeRS.getCuerpo();
                    return retiroRS.getResultado();
                }
                
            } else {
                DepositoRQ depositoRQ = new DepositoRQ();
                AppClient appClient = new AppClient();
                depositoRQ.setDocumentoCliente(cedula);
                depositoRQ.setNumeroCuenta(cuenta);
                depositoRQ.setTipoCuenta(tipo);
                depositoRQ.setValorDeposito(monto);
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                depositoRQ.setFechaDeposito(sdf.format(fecha));
                MensajeRQ mensajeRQ = new MensajeRQ(NetUtil.getLocalIPAddress(), Mensaje.ID_MENSAJE_REGISTROMOVIMIENTO);
                mensajeRQ.setCuerpo(depositoRQ);
                MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
                if (mensajeRS != null) {
                    DepositoRS depositoRS = (DepositoRS) mensajeRS.getCuerpo();
                    return depositoRS.getResultado();
                }

            }
            return OK_RESPONSE;
        } else {
            return NULL_PARAMETERS;
        }

    }

}
