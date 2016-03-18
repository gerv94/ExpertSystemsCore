/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.motorDeduccion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import mx.ceti.proposicion.Proposicion;
import mx.ceti.proposicion.ProposicionAtomica;

/**
 *
 * @author oliva
 */
public class ConjuntoDeduccion {
    /*
    SA = Solo antecedentes = antecedente - consecuente
    CI = Conclusiones intermedias = antecedente ^ consecuente
    CF = Conclusiones finales = consecuente - antecedente
    
    */
    private Set<ProposicionAtomica> sa,cf,ci;
    /**
     * 
     * Lista completa de las proposiciones a evaluar durante 
     * el proceso de inferencia
     * 
     * lluve ^ #cubierto -> Me mojo
     * 
     */     
    private List<Proposicion> proposiciones, proposicionesResplado;
    
    private List<ProposicionAtomica> result;
    public boolean isfinal;
    private boolean isInflicted;
    private List<ProposicionAtomica> memoriaDeTrabajo;
    private Set<String> proposicionesMostradasResultado;
    
    public ConjuntoDeduccion(Set<ProposicionAtomica> ant, Set<ProposicionAtomica> con,List<Proposicion> proposiciones,List<Proposicion> proposicionesRespaldo){
        sa = SetManagement.subtraction(ant, con);
        ci = SetManagement.intersection(ant, con);
        cf = SetManagement.subtraction(con, ant);
        this.proposiciones = proposiciones;
        this.proposicionesResplado = proposicionesRespaldo;
        this.result = new java.util.LinkedList();
        proposicionesMostradasResultado = new java.util.HashSet();
        isInflicted = false;
    }
    
    public void evaluarBaseConocimientos(ProposicionAtomica pa){
       //E inferir cambios; si hay cambios agregarlos a mt
       //Si algúna proposiciona atómica es cero, deshabilitar la proposicion
       Iterator<Proposicion> iterador = proposiciones.iterator();
       while(iterador.hasNext()){
           Proposicion item = iterador.next();
           if(item.isHabilidato()){
               encontrarElemento(item,pa);
           }
       }
       
       
    }
    
    
    private boolean cambiarValorProposicionAtomica(ProposicionAtomica element, ProposicionAtomica pa){
        // false && true -> f
        // false && falso
        return element.getSigno() > 0 && pa.isValor() || !pa.isValor() && element.getSigno() < 0;

    }
    
  
    /**
     * En esta funcion se busca la ProposicionAtomica en la Proposicion <br>
     * Si encuenta el elementos en la Proposicion se le evalua respectoa a PA<br>
     * es decir, si el valor es falso de PA, se deshabilita la proposicion<br>
     * en cambion, si el valor es verdadero se elimina de la proposicion para <br>
     * ahorrar procesamento, ya que X ^ 1 = X
     * 
     * @param item Proposicion en el cual se buscará la ProposiconAtomica
     * @param pa Proposicion atómica con un valor ya definido por el usuario <br>
     *              en el proceso de preguntas y respuestas
     * 
     */
    private void encontrarElemento(Proposicion item, ProposicionAtomica  pa){
        try{
         Iterator<ProposicionAtomica> i = item.getAntecedente().iterator();
         int index2 = 0;
         while(i.hasNext()){
             ProposicionAtomica element = i.next();
                        if(element.getNombre().equals(pa.getNombre())){
                            System.out.println("El valor de " + element.printWithId() + " es " + cambiarValorProposicionAtomica(element,pa));
                            ///Aplicar logica de cambio de valor
                            if(cambiarValorProposicionAtomica(element,pa)){
                                System.out.print("Se eliminar la proposición atomica [Encontrar Elemento]: [" + element + "] de: " + item);
                                item.getAntecedente().remove(index2);
                                System.out.println(" ---->  " + item);
                                //evaluarBaseConocimientos();//????
                            } else {// En caso que el valor sea ceros
                                System.out.println("Se deshabilitica la proposición: " + item + ", por el valor de " + element.printWithId());
                                item.setHabilidato(false);
                                break;
                            }                           
                        }
                        index2 ++;
         }
         
                         
         
         if(item.getAntecedente().isEmpty()){
                    // <EMPTY> -> ch; esto implica que ch es verdadero y la proposicion de deshabilita
                    System.out.println("Se infiere la proposicion: " + item.getConsecuente());
                    item.setHabilidato(false);
                    item.getConsecuente().setValor(true);  //////""""
                    
                    item.getConsecuente().setAgregado(true);
                    memoriaDeTrabajo.add(item.getConsecuente()); 
                    if(esTerminal(item.getConsecuente())){
                        System.out.println("Se encontro un elemento final [evaluarConocimiento]: \n" + item.getConsecuente() );
                    }
                    //isInflicted = true;
                    evaluarBaseConocimientos(item.getConsecuente());
                }
        }catch(Exception e){;}
        
    }
    
    
    private boolean esTerminal(ProposicionAtomica pa){
         Iterator<ProposicionAtomica> i = getCf().iterator();
         while(i.hasNext()){
             ProposicionAtomica element = i.next();
                        if(element.getNombre().equals(pa.getNombre())){
                            getResult().add(element);
                            isfinal = true;
                            return true;
                        }
                                
         }
         return false;
    }
 
  
    public void printExplanations(){
        
        for(ProposicionAtomica item : result){
            proposicionesMostradasResultado.clear();
            System.out.println("El resultado de [" + item + "] es por:");
            proposicionesResplado.stream().filter( 
                    P -> P.getReglaID() == item.getReglaID()                  
                ).forEach(P -> printExplanationAux(P));
            
        }
    }
    
    public void printExplanation(ProposicionAtomica pa){        
            proposicionesMostradasResultado.clear();
            System.out.println("El resultado de [" + pa + "] es por:");
            proposicionesResplado.stream().filter( 
                    P -> P.getReglaID() == pa.getReglaID()                  
                ).forEach(P -> printExplanationAux(P));
            
    }
    
    private void printExplanationAux(Proposicion p){
         
        for(ProposicionAtomica item : p.getAntecedente()){
            if(SetManagement.existe(sa, item)){
                
                ProposicionAtomica po = memoriaDeTrabajo.stream().filter(P-> P.getNombre().equals(item.getNombre())).collect(Collectors.toList()).get(0);
                  
                if(!proposicionesMostradasResultado.contains(item.getNombre())){
                    System.out.println( po + " es " + (po.isValor()?"si":"no"));
                    proposicionesMostradasResultado.add(po.getNombre());
                  }
            }
            else{
               
                  ProposicionAtomica po = memoriaDeTrabajo.stream()
                        .filter(P-> P.getNombre().equals(item.getNombre()))
                        .collect(Collectors.toList()).get(0);
                if(!proposicionesMostradasResultado.contains(po.getNombre())){
               
                  
                        //.forEach(E -> System.out.println("\t"+ E.getNombre() + " es " + (E.isValor()?"si":"no")));
                    System.out.println("\t"+ po.getNombre() + " es " + (po.isValor()?"si":"no"));
                }
                
               proposicionesResplado.stream()
                        .filter(P -> P.getConsecuente().getNombre().equals(po.getNombre()) && !proposicionesMostradasResultado.contains(po.getNombre()))
                        .forEach(E -> printExplanationAux(E));
               proposicionesMostradasResultado.add(po.getNombre());

            }
        }
    }

    
    
    /**
     * @return the sa
     */
    public Set<ProposicionAtomica> getSa() {
        return sa;
    }

    /**
     * @param sa the sa to set
     */
    public void setSa(Set<ProposicionAtomica> sa) {
        this.sa = sa;
    }

    /**
     * @return the cf
     */
    public Set<ProposicionAtomica> getCf() {
        return cf;
    }

    /**
     * @param cf the cf to set
     */
    public void setCf(Set<ProposicionAtomica> cf) {
        this.cf = cf;
    }

    /**
     * @return the ci
     */
    public Set<ProposicionAtomica> getCi() {
        return ci;
    }

    /**
     * @param ci the ci to set
     */
    public void setCi(Set<ProposicionAtomica> ci) {
        this.ci = ci;
    }

    /**
     * @return the proposiciones
     */
    public List<Proposicion> getProposiciones() {
        return proposiciones;
    }

    /**
     * @param proposiciones the proposiciones to set
     */
    public void setProposiciones(List<Proposicion> proposiciones) {
        this.proposiciones = proposiciones;
    }

    /**
     * @return the result
     */
    public List<ProposicionAtomica> getResult() {
        return result;
    }

    public void setMemoriaTrabajo(List<ProposicionAtomica> memoriaTrabajo) {
        this.memoriaDeTrabajo = memoriaTrabajo;
    }

    /**
     * @return the isInflicted
     */
    public boolean isIsInflicted() {
        return isInflicted;
    }

    /**
     * @param isInflicted the isInflicted to set
     */
    public void setIsInflicted(boolean isInflicted) {
        this.isInflicted = isInflicted;
    }

   
    
    
}
