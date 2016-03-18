/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.motorDeduccion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.ceti.proposicion.Proposicion;
import mx.ceti.proposicion.ProposicionAtomica;

/**
 *
 * @author oliva
 */
public class SetManagement {
    
    public static Set<ProposicionAtomica>[] createPropositionSet(List<Proposicion>  data){
        Set<ProposicionAtomica>[]aux = 
                new java.util.HashSet[]{
                    new java.util.HashSet(),new java.util.HashSet()
                };
        
        //Set ant = new java.util.HashSet(),con = new java.util.HashSet();
        for(Proposicion i: data){
            for(ProposicionAtomica j:i.getAntecedente())
                if(!existe(aux[0],j))
                aux[0].add(j);
            if(!existe(aux[1],i.getConsecuente()))
                aux[1].add(i.getConsecuente());
        }
        return aux;
    }
   
    
    public static Set<ProposicionAtomica> intersection(Set<ProposicionAtomica> set1, Set<ProposicionAtomica>  set2){
         Set<ProposicionAtomica> set_ = new java.util.HashSet();
         Iterator<ProposicionAtomica> i = set1.iterator();
         while(i.hasNext()){
             ProposicionAtomica element = i.next();
                     Iterator<ProposicionAtomica> i2 = set2.iterator();
                    while(i2.hasNext()){
                        ProposicionAtomica element2 = i2.next();
                        if(element.getNombre().equals(element2.getNombre()))
                            set_.add(element);
                    }
             
         }
         return set_;
         
       //return  set1.stream().filter(item-> set2.contains(item)).collect(Collectors.toSet());
    }
    
     public static Set<ProposicionAtomica> subtraction(Set<ProposicionAtomica> set1, Set<ProposicionAtomica>  set2){
         Set<ProposicionAtomica> set_ = new java.util.HashSet(set1);
         Iterator<ProposicionAtomica> i = set1.iterator();
         while(i.hasNext()){
             ProposicionAtomica element = i.next();
                     Iterator<ProposicionAtomica> i2 = set2.iterator();
                    while(i2.hasNext()){
                        ProposicionAtomica element2 = i2.next();
                        if(element.getNombre().equals(element2.getNombre()))
                            set_.remove(element);
                    }
             
         }
         return set_;
         
       // Set<ProposicionAtomica> setAux = new java.util.HashSet(set1);
        //setAux.removeAll(set2);
        //return setAux; 
       //return  set1.stream().filter(item-> set2.contains(item)).collect(Collectors.toSet());
    } 
     
     
     
     
 /*
    public static Set<ProposicionAtomica> intersection(Set<ProposicionAtomica> set1, Set<ProposicionAtomica>  set2){
       return  set1.stream().filter(item-> set2.contains(item)).collect(Collectors.toSet());
    }
    */
    /*
    public static Set<ProposicionAtomica> subtraction(Set<ProposicionAtomica> set1, Set<ProposicionAtomica>  set2){
       return  set1.stream().filter(item-> !set2.contains(item)).collect(Collectors.toSet());
    }
*/
    
    public static void print(Set<ProposicionAtomica> set){
        Iterator<ProposicionAtomica> item = set.iterator();
                    while(item.hasNext()){
                        System.out.println(item.next());
                   }
    }

    public static boolean existe(Set<ProposicionAtomica> set, ProposicionAtomica j) {
          Iterator<ProposicionAtomica> i2 = set.iterator();
         while(i2.hasNext()){
                        ProposicionAtomica element = i2.next();
                        if(element.getNombre().equals(j.getNombre()))
                            return true;
         }
        return false;
    }


    public static boolean existe(List<ProposicionAtomica> set, ProposicionAtomica j) {
          Iterator<ProposicionAtomica> i2 = set.iterator();
         while(i2.hasNext()){
                        ProposicionAtomica element = i2.next();
                        if(element.getNombre().equals(j.getNombre()))
                            return true;
         }
        return false;
    }
        
    
    
}
