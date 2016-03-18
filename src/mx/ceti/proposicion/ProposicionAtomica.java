/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.proposicion;

/**
 *
 * @author oliva
 */
public class  ProposicionAtomica  {
    private int signo,reglaID;
    private String nombre;
    private boolean valor;
    private boolean agregado;
    
    public ProposicionAtomica( String valor,int signo,int reglaID) {
        this.signo = signo;
        this.nombre = valor;
        this.reglaID = reglaID;
        agregado = false;
    }


    public ProposicionAtomica( String valor,int signo) {
        this.signo = signo;
        this.nombre = valor;
        agregado = false;
    }

    ProposicionAtomica() {
    }

   
    
    
    /**
     * @return the signo
     */
    public int getSigno() {
        return signo;
    }

    /**
     * @param signo the signo to set
     */
    public void setSigno(int signo) {
        this.signo = signo;
    }

    /**
     * @return the valor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the valor to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
    return getNombre();      
 //return (signo<0?Proposicion.NOT:"") + getValor();
        //return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

   

    /**
     * @return the reglaID
     */
    public int getReglaID() {
        return reglaID;
    }

    /**
     * @param reglaID the reglaID to set
     */
    public void setReglaID(int reglaID) {
        this.reglaID = reglaID;
    }

    /**
     * @return the valor
     */
    public boolean isValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public String printWithId() {
        return (signo<0?Proposicion.NOT:"") + getNombre();
    }

    /**
     * @return the agregado
     */
    public boolean isAgregado() {
        return agregado;
    }

    /**
     * @param agregado the agregado to set
     */
    public void setAgregado(boolean agregado) {
        this.agregado = agregado;
    }


    
    
}
