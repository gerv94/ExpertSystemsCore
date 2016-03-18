/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.proposicion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author oliva
 */
public class Proposicion {
    
    public static String IMPLICACION = "->", AND = "\\^", NOT = "#", OR = "\\%";
    public static char  AND_CHAR = '^', NOT_CHAR = '#', OR_CHAR = '%';
    private ProposicionAtomica consecuente ;
    private List<ProposicionAtomica>  antecedente;
    private boolean habilidato;
    private int reglaID;
    
    public Proposicion(){
        this.habilidato = true;
         antecedente = new java.util.LinkedList();
          consecuente =new ProposicionAtomica();
    }
    
    public Proposicion(int reglaID){
        this.habilidato = true;
        this.reglaID = reglaID;
         antecedente = new java.util.LinkedList();
          consecuente =new ProposicionAtomica();
    }
                       
    
    public Proposicion(String p){
        this.habilidato = true;
       //antecedente = new PreposicionAtomica[p.split(IMPLICACION)[0].split(AND).length];
        antecedente = new java.util.LinkedList();
        for (int i = 0; i < p.split(IMPLICACION)[0].split(AND).length; i++) {
           // antecedente[i]=
             antecedente.add(new ProposicionAtomica(
                p.split(IMPLICACION)[0].split(AND)[i].replace(NOT,"").trim(), 
                p.split(IMPLICACION)[0].split(AND)[i].contains(NOT)?-1:1)
            );
        }
            consecuente =new ProposicionAtomica(
                p.split(IMPLICACION)[1].replace(NOT,"").trim(), 
                p.split(IMPLICACION)[1].contains(NOT)?-1:1);
        
        
    }

    public Proposicion(String p, int reglaID){
        this.reglaID = reglaID;
        this.habilidato = true;
       //antecedente = new PreposicionAtomica[p.split(IMPLICACION)[0].split(AND).length];
        antecedente = new java.util.LinkedList();
        for (int i = 0; i < p.split(IMPLICACION)[0].split(AND).length; i++) {
           // antecedente[i]=
             antecedente.add(new ProposicionAtomica(
                p.split(IMPLICACION)[0].split(AND)[i].replace(NOT,"").trim(), 
                p.split(IMPLICACION)[0].split(AND)[i].contains(NOT)?-1:1)
            );
        }
            consecuente =new ProposicionAtomica(
                p.split(IMPLICACION)[1].replace(NOT,"").trim(), 
                p.split(IMPLICACION)[1].contains(NOT)?-1:1);
        
        
    }
    
    @Override
    public String toString() {
        String s = "";
     
        
        
        for (int i = 0; i < getAntecedente().size(); i++) {
           s += (getAntecedente().get(i).getSigno()<0?NOT:"") + getAntecedente().get(i).getNombre();
           if( i < getAntecedente().size() - 1)
               s += " ^ ";
        }
        s += " -> ";
        s += (getConsecuente().getSigno()<0?NOT:"") + getConsecuente().getNombre();
     
        return s;
        //return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the consecuente
     */
    public ProposicionAtomica getConsecuente() {
        return consecuente;
    }

    /**
     * @param consecuente the consecuente to set
     */
    public void setConsecuente(ProposicionAtomica consecuente) {
        this.consecuente = consecuente;
    }

    /**
     * @return the antecedente
     */
    public List<ProposicionAtomica> getAntecedente() {
        return antecedente;
    }

    /**
     * @param antecedente the antecedente to set
     */
    public void setAntecedente( List antecedente) {
        this.antecedente = antecedente;
    }

    public String printWithId() {
        String s = "";
        for (int i = 0; i < getAntecedente().size(); i++) {
           s += (getAntecedente().get(i).getSigno()<0?NOT:"") + getAntecedente().get(i).getNombre() + "["+ getAntecedente().get(i).getSigno()  + "]";
           if( i < getAntecedente().size() - 1)
               s += " ^ ";
        }
        s += " -> ";
        s +=  (getConsecuente().getSigno()<0?NOT:"") + getConsecuente().getNombre()  + "[" + getConsecuente().getSigno() + "]";
        return s;
    }

    /**
     * @return the habilidato
     */
    public boolean isHabilidato() {
        return habilidato;
    }

    /**
     * @param habilidato the habilidato to set
     */
    public void setHabilidato(boolean habilidato) {
        this.habilidato = habilidato;
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

    
    
}
