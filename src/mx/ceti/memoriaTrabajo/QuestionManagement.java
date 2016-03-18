/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.memoriaTrabajo;

import static mx.ceti.memoriaTrabajo.Question.NO;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.ceti.motorDeduccion.ConjuntoDeduccion;
import mx.ceti.motorDeduccion.SetManagement;
import mx.ceti.proposicion.ProposicionAtomica;

/**
 *
 * @author oliva
 */
public class QuestionManagement {
   private List<ProposicionAtomica> memoriaTrabajo = new java.util.LinkedList();
   private Set<ProposicionAtomica> conjuntoProposiconesDesconocidas;
   private ConjuntoDeduccion cd;
   private boolean stopAsking;
   
   public QuestionManagement(ConjuntoDeduccion cd){
       this.cd = cd;
       stopAsking = false;
       conjuntoProposiconesDesconocidas = new java.util.HashSet();
   }
   
   public void start(){
       Set<ProposicionAtomica> cPDAux = new java.util.HashSet();
       cd.setMemoriaTrabajo(memoriaTrabajo);
       //El 0reden si importa
        asking(cd.getSa());//Solo antecedentes
        if(!stopAsking){
              /**
                 * Pregunat por proposiciones que no habian determinado 
                 */
                 
            cPDAux.addAll(getConjuntoProposiconesDesconocidas());
            cPDAux.addAll(cd.getCi());
            asking(cPDAux);
        }
        if(!stopAsking)
        asking(cd.getCf()); //Conclusiones finales
        System.out.println("No hay más proposiciones que preguntar, se ha terminado el proceso de inferencia");
       
        
   }
   
   
   
   public List<ProposicionAtomica> getResult(){
      return cd.getResult();
   }
   
  

    private void asking(Set<ProposicionAtomica> sa) {
        Iterator<ProposicionAtomica> i = sa.iterator();
        int answer;
        while(i.hasNext()){
            ProposicionAtomica element = i .next();
            //No existe el elemento en la memoria de trabajo, hay que preguntar por él
            if(!SetManagement.existe(memoriaTrabajo,element))
            {
                answer = Question.asking(element);
                if(answer == 0){
                    conjuntoProposiconesDesconocidas.add(element);
                    continue;
                }
                element.setValor(answer == 1);
                //element.setAgregado(true);
                memoriaTrabajo.add(element);
                //cd.setMemoriaTrabajo(memoriaTrabajo);
                cd.evaluarBaseConocimientos(element); // Meter en Deduccion; inferencia
                //Se encontro un elemento final
                if(cd.isfinal){
                    System.out.println("Desea conocer porqué se concluyo " +
                            memoriaTrabajo.get(memoriaTrabajo.size() - 1) +" ? [no/si]");
//                    if(!app.input().toLowerCase().contains(NO))
                        cd.printExplanation(cd.getResult().get(cd.getResult().size() - 1));
//                    System.out.println("Desea continuar ingresando valores? [no/si]");
//                    if((stopAsking = app.input().toLowerCase().contains(NO)))
//                        break;
                        
                }
            }
        }

    }

    /**
     * @return the conjuntoProposiconesDesconocidas
     */
    public Set<ProposicionAtomica> getConjuntoProposiconesDesconocidas() {
        return conjuntoProposiconesDesconocidas;
    }

    /**
     * @param conjuntoProposiconesDesconocidas the conjuntoProposiconesDesconocidas to set
     */
    public void setConjuntoProposiconesDesconocidas(Set<ProposicionAtomica> conjuntoProposiconesDesconocidas) {
        this.conjuntoProposiconesDesconocidas = conjuntoProposiconesDesconocidas;
    }
   
    
    
}
