package inferenceEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hibernate.models.entities.RulesHeaderEntity;
import hibernate.models.entities.RulesItemEntity;
import main.ConsoleController;
import mx.ceti.memoriaTrabajo.Question;
import mx.ceti.motorDeduccion.SetManagement;
import mx.ceti.proposicion.ProposicionAtomica;

public class InferenceEngine {
	/*
	 * SA = Solo antecedentes = antecedente - consecuente CI = Conclusiones
	 * intermedias = antecedente ^ consecuente CF = Conclusiones finales =
	 * consecuente - antecedente
	 */
	private Set<InferenceEngineModel> justAntecedens, finalConclusions, intermediateConclusions;
	private List<InferenceEngineModel> workMemory = new ArrayList<>();
	private Set<InferenceEngineModel> unkwownElements;
	private boolean stopAsking;

	private List<InferenceEngineModel> result;
	public boolean isfinal;
	private boolean isInflicted;
	private Set<String> proposicionesMostradasResultado;
	
	public void start(){
	       Set<ProposicionAtomica> cPDAux = new java.util.HashSet();
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

	public void fillElements() {
		InferenceEngineModel temp;
		Set<InferenceEngineModel> antecedents = new HashSet<InferenceEngineModel>(),
				consecuents = new HashSet<InferenceEngineModel>();
		for (RulesHeaderEntity rulesHeaderEntity : ConsoleController.rulesController.getRules()) {
			temp = new InferenceEngineModel(rulesHeaderEntity.getConsecuent());
			if (!exist(consecuents, temp))
				consecuents.add(temp);
			for (RulesItemEntity rulesItemEntity : rulesHeaderEntity.getAntecedents()) {
				temp = new InferenceEngineModel(rulesItemEntity.getAntecedent());
				if (!exist(antecedents, temp))
					antecedents.add(temp);
			}
		}
		justAntecedens = subtraction(antecedents, consecuents);
		intermediateConclusions = intersection(antecedents, consecuents);
		finalConclusions = subtraction(consecuents, antecedents);

		result = new LinkedList<InferenceEngineModel>();
		proposicionesMostradasResultado = new HashSet<String>();
		isInflicted = false;

		System.out.println(justAntecedens.toString());
		System.out.println(intermediateConclusions.toString());
		System.out.println(finalConclusions.toString());

	}

	public static Set<InferenceEngineModel> intersection(Set<InferenceEngineModel> set1,
			Set<InferenceEngineModel> set2) {
		Set<InferenceEngineModel> res = new HashSet<InferenceEngineModel>();
		Iterator<InferenceEngineModel> i = set1.iterator();
		while (i.hasNext()) {
			InferenceEngineModel element = i.next();
			Iterator<InferenceEngineModel> i2 = set2.iterator();
			while (i2.hasNext()) {
				InferenceEngineModel element2 = i2.next();
				if (element.equals(element2))
					res.add(element);
			}
		}
		return res;
	}

	public static Set<InferenceEngineModel> subtraction(Set<InferenceEngineModel> set1,
			Set<InferenceEngineModel> set2) {
		Set<InferenceEngineModel> res = new HashSet<InferenceEngineModel>(set1);
		Iterator<InferenceEngineModel> i = set1.iterator();
		while (i.hasNext()) {
			InferenceEngineModel element = i.next();
			Iterator<InferenceEngineModel> i2 = set2.iterator();
			while (i2.hasNext()) {
				InferenceEngineModel element2 = i2.next();
				if (element.equals(element2))
					res.remove(element);
			}
		}
		return res;
	}

	public boolean exist(Set<InferenceEngineModel> set, InferenceEngineModel inferenceEngineModel) {
		for (InferenceEngineModel inferenceEngineModel1 : set)
			if (inferenceEngineModel.getCode() == inferenceEngineModel1.getCode())
				return true;
		return false;
	}
	
	private void asking(Set<InferenceEngineModel> sa) {
        Iterator<InferenceEngineModel> i = sa.iterator();
        int answer;
        while(i.hasNext()){
        	InferenceEngineModel element = i .next();
            //No existe el elemento en la memoria de trabajo, hay que preguntar por él
            if(!exist((Set<InferenceEngineModel>) workMemory,element))
            {
                answer = element.ask();
                if(answer == 0){
                    unkwownElements.add(element);
                    continue;
                }
                workMemory.add(element);
                evaluarBaseConocimientos(element); // Meter en Deduccion; inferencia
                //Se encontro un elemento final
                if(isfinal){
                    System.out.println("Desea conocer porqué se concluyo " +
                    		workMemory.get(workMemory.size() - 1) +" ? [no/si]");
//                    if(!app.input().toLowerCase().contains(NO))
                        printExplanation(getResult().get(getResult().size() - 1));
//                    System.out.println("Desea continuar ingresando valores? [no/si]");
//                    if((stopAsking = app.input().toLowerCase().contains(NO)))
//                        break;
                        
                }
            }
        }

    }

}
