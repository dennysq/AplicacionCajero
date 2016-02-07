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
    public static Empresa retrieveEmpresa(String usuario, String password) {
        if (usuario != null && password != null) {
            AppClient appClient = new AppClient();
            AutenticacionEmpresaRQ aerq = new AutenticacionEmpresaRQ();

            aerq.setPassword(password);
            aerq.setUserId(usuario);

            MensajeRQ mensajeRQ = new MensajeRQ("dennys", Mensaje.ID_MENSAJE_AUTENTICACIONCLIENTE);
            mensajeRQ.setCuerpo(aerq);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            if (mensajeRS != null) {
                AutenticacionEmpresaRS aers = (AutenticacionEmpresaRS) mensajeRS.getCuerpo();
                if (aers.getResultado().equals("1")) {
                    System.out.println("" + aers.getEmpresa());
                    return aers.getEmpresa();
                }
            }
        }
        return null;
    }
    
    public static boolean insertcliente(String id, String nombre, String telefono, String direccion)
    {
                if(nombre!=null && telefono!=null && direccion!=null && id!=null && id.length()>9 && id.length()<=15)
                {
                    AppClient appClient = new AppClient();
                    IngresoClienteRQ ing= new IngresoClienteRQ();
                    ing.setCliente(new Cliente(id, nombre, telefono, direccion));
                    MensajeRQ mensajeRQ =new MensajeRQ("INGRESOCLI", Mensaje.ID_MENSAJE_INGRESOCLIENTE);
                    mensajeRQ.setCuerpo(ing);
                    MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
                    IngresoClienteRS ingrs=(IngresoClienteRS)mensajeRS.getCuerpo();
                    if (ingrs.getResultado().equals("1")) {
                      return true;
                    }
                      else{
                              return false;
                          }
            }
                return false;
    }
    
   

    public static Cliente buscarcliente(String datos) {
        if (datos != null && datos.length() == 10) {
            AppClient appClient = new AppClient();
            ConsultaClienteRQ cliRQ = new ConsultaClienteRQ();
            cliRQ.setIdentificacion(datos);

            MensajeRQ mensajeRQ = new MensajeRQ("CONSULTACL", Mensaje.ID_MENSAJE_CONSULTACLIENTE);
            mensajeRQ.setCuerpo(cliRQ);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            ConsultaClienteRS cliRS = (ConsultaClienteRS) mensajeRS.getCuerpo();
            if (cliRS.getResultado().equals("1")) {
                System.out.println("" + cliRS.getCliente());
                return cliRS.getCliente();
            }

        }
        return null;
    }

    public static Producto retrieveProducto(String idProducto) {

        if (idProducto != null) {
            AppClient appClient = new AppClient();
            ConsultaProductoRQ cprq = new ConsultaProductoRQ();

            cprq.setIdProducto(idProducto);

            MensajeRQ mensajeRQ = new MensajeRQ("ale", Mensaje.ID_MENSAJE_CONSULTAPRODUCTO);
            mensajeRQ.setCuerpo(cprq);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            ConsultaProductoRS cprs = (ConsultaProductoRS) mensajeRS.getCuerpo();
            if (cprs.getResultado().equals("1")) {
                System.out.println("" + cprs.getProducto());
                return cprs.getProducto();
            }
        }
        return null;
    }

    public static String registrarFactura(String idFactura, String identificacionCliente, Date fecha, float totalFactura, List<DetalleFacturaAppRQ> detalles) {

        if (identificacionCliente != null && fecha != null && detalles != null && idFactura != null) {
            IngresoFacturaRQ ingresoFacturaRQ = new IngresoFacturaRQ();
            AppClient appClient = new AppClient();
            ingresoFacturaRQ.setDetalles(detalles);
            ingresoFacturaRQ.setIdFactura(idFactura);
            ingresoFacturaRQ.setIdentificacion(identificacionCliente);
            ingresoFacturaRQ.setNumeroDetalles(String.valueOf(detalles.size()));
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            ingresoFacturaRQ.setFecha(sdf.format(fecha));
            BigDecimal total = new BigDecimal(totalFactura).setScale(2, RoundingMode.HALF_UP);
            ingresoFacturaRQ.setTotal(total.toPlainString());
            MensajeRQ mensajeRQ = new MensajeRQ(NetUtil.getLocalIPAddress(), Mensaje.ID_MENSAJE_INGRESOFACTURA);
            mensajeRQ.setCuerpo(ingresoFacturaRQ);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            if (mensajeRS != null) {
                IngresoFacturaRS ingresoFacturaRS = (IngresoFacturaRS) mensajeRS.getCuerpo();
                return ingresoFacturaRS.getResultado();
            }
            return BAD_RESPONSE;
        } else {
            return NULL_PARAMETERS;
        }

    }
    
    public static Factura buscarFactura(String datos) {
        if (datos != null && datos.length() == 10) {
            AppClient appClient = new AppClient();
            ConsultaFacturaRQ cliRQ = new ConsultaFacturaRQ();
            cliRQ.setIdFactura(datos);

            MensajeRQ mensajeRQ = new MensajeRQ("ale", Mensaje.ID_MENSAJE_CONSULTAFACTURA);
            mensajeRQ.setCuerpo(cliRQ);
            MensajeRS mensajeRS = appClient.sendRequest(mensajeRQ);
            ConsultaFacturaRS cliRS = (ConsultaFacturaRS) mensajeRS.getCuerpo();
            if (cliRS.getResultado().equals("1")) {
                System.out.println("" + cliRS.getFactura());
                return cliRS.getFactura();
            }

        }
        return null;
    }

}
