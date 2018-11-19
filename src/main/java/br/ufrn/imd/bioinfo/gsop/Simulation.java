package br.ufrn.imd.bioinfo.gsop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Collections;

import br.ufrn.imd.bioinfo.gsop.model.Eph;
import br.ufrn.imd.bioinfo.gsop.model.GsopNode;

public class Simulation {

	private static SimulationData simulationData;

	private static List<Double> partialFitnessAvg;

	public static void cycleV6(Map<String, GsopNode> nodes, double deathRate, boolean neighborhoodInheritance) {

		// Contagem de nascimentos e mortes
		Random gerador = new Random();

		
		int dieCount = (int) ((double) nodes.size() * (deathRate - 1));
		if (dieCount == 0)
			dieCount++;
		// deaths
		List<String> selectedKeys = new ArrayList<String>();
		List<String> selectedKeysDeath = new ArrayList<String>();
		List<String> keys = new ArrayList<String>(nodes.keySet());
		Collections.shuffle(keys);
		
		for (int i = 0; i < dieCount; i++) {
			selectedKeys.add(keys.get(i));
		}

		// deaths in case of neighborhood inheritance
		if (neighborhoodInheritance) {
			for (int i = dieCount; i < dieCount*2; i++) {
				selectedKeysDeath.add(keys.get(i));
			}
		}
		
		// births
		for (String key : selectedKeys) {

			GsopNode n = nodes.get(key);

			// gerar roleta baseado em vizinhança
			List<String> roleta = new ArrayList<String>();
			List<String> neighborsHashList = n.getNeighborsHashList();
			if(!neighborsHashList.contains(key)) {
				neighborsHashList.add(key);
			}
			for (String s : neighborsHashList) {

				GsopNode neighbour = nodes.get(s);

				int qtd = (int) (neighbour.getCoeff() * 100.0);
				for (int i = 0; i < qtd; i++) {
					roleta.add(s);
				}
			}

			Eph eph = null;

			//sorteado é o nó que irá propagar o seu tipo
			GsopNode sorteado;
			if(roleta.size()==0) {
				sorteado = nodes.get(key);
			}else {
			// escolher tipo propagado
				int chosen = gerador.nextInt(roleta.size());
				sorteado = nodes.get(roleta.get(chosen));
			}
			
			sorteado.setFitness(sorteado.getFitness() + 1);

			if (!neighborhoodInheritance) {
				eph = n.getEph();
				n.setEph(null);
				// substituir tipo do nó
				n.setCoeff(sorteado.getRawCoeff());
				n.setType(sorteado.getType());
				n.setFitness(0);
				// verifica se o nascido é do tipo gerador, e se vai gerar eph
				if (n.getType() == "A") {
					int sorteioGeracao = gerador.nextInt(100);
					if (sorteioGeracao < (simulationData.getEphBirthGenerationChance() * 100)) {
						n.setEph(new Eph(simulationData.getEphBonus()));
					}
				}				
			} else {
				// mata nó antigo e aproveita o uuid para o nó que vai nascer

				GsopNode dyingNode = nodes.get(selectedKeysDeath.get(0));
				String selectedUUID = dyingNode.getHash();
				eph = dyingNode.getEph();
				for (String neighborUUID : dyingNode.getNeighborsHashList()) {
					GsopNode neighbor = nodes.get(neighborUUID);

					if (!neighbor.getHash().equals(dyingNode.getHash()))
						neighbor.getNeighborsHashList().remove(selectedUUID);
				}
				
				nodes.remove(selectedUUID);
				selectedKeysDeath.remove(0);
				
				// gera novo nó, atualiza listas de vizinhança e adiciona-o à lista de nós
				GsopNode newNode = new GsopNode();
				newNode.setHash(selectedUUID);
				newNode.setCoeff(sorteado.getRawCoeff());
				newNode.setType(sorteado.getType());
				newNode.setFitness(0);
				List<String> newList = new ArrayList<String>();
				
				for(String s : sorteado.getNeighborsHashList()) {
					if (nodes.containsKey(s))
						newList.add(s);
				}
				if (nodes.containsKey(sorteado.getHash()))
					newList.add(sorteado.getHash());
				newNode.setNeighborsHashList(newList);

				if (newNode.getType() == "A") {
					int sorteioGeracao = gerador.nextInt(100);
					if (sorteioGeracao < (simulationData.getEphBirthGenerationChance() * 100)) {
						newNode.setEph(new Eph(simulationData.getEphBonus()));
					}
				}

				for (String neighborUUID : newNode.getNeighborsHashList()) {
					GsopNode neighbor = nodes.get(neighborUUID);
					if (neighbor == null)
						System.out.println(neighborUUID + " nulo");
					neighbor.getNeighborsHashList().add(selectedUUID);
				}

				nodes.put(selectedUUID, newNode);

			}

			List<String> currentKeys = new ArrayList<String>(neighborsHashList);
			Collections.shuffle(currentKeys);
			boolean pegou = false;
			for(String k : currentKeys) {
				
				if (nodes.get(k).getEph() == null && nodes.get(k).getType()=="A") {
					nodes.get(k).setEph(eph);
					pegou = true;
				} 
			}
			
			if (!pegou) {
				long seed = System.nanoTime();
				currentKeys = new ArrayList<String>(nodes.keySet());
				Collections.shuffle(currentKeys, new Random(seed));
				for (String k : currentKeys) {
					if (nodes.get(k).getEph() == null && nodes.get(k).getType()=="A") {
						nodes.get(k).setEph(eph);
						break;						
					}
				}
			}
	
		}
		
		//atualiza ephs e faz contagem
		
		for(String key : keys) {
			GsopNode n = nodes.get(key);
			if(n.getEph()!=null) {
				
				if(n.getEph().getTime()==1) {
					n.setEph(null);					
				}else {
					n.getEph().setTime(n.getEph().getTime()-1);
				}
			}
			
		}

	}
	
	public static void cycleV5(Map<String, GsopNode> nodes, double deathRate, boolean neighborhoodInheritance) {

		// Contagem de nascimentos e mortes
		Random gerador = new Random();

		
		int dieCount = (int) ((double) nodes.size() * (deathRate - 1));
		if (dieCount == 0)
			dieCount++;
		// deaths
		List<String> selectedKeys = new ArrayList<String>();
		List<String> selectedKeysDeath = new ArrayList<String>();
		List<String> keys = new ArrayList<String>(nodes.keySet());
		// System.out.println(dieCount);
		// System.out.println("Keys size "+keys.size());
		Collections.shuffle(keys);
		
		for (int i = 0; i < dieCount; i++) {
			//int chosen = gerador.nextInt(nodes.size());
			selectedKeys.add(keys.get(i));
		}

		// deaths in case of neighborhood inheritance
		if (neighborhoodInheritance) {
			for (int i = dieCount; i < dieCount*2; i++) {
				//int chosen = gerador.nextInt(nodes.size());
				selectedKeysDeath.add(keys.get(i));
			}
		}
		
		// births
		for (String key : selectedKeys) {

			GsopNode n = nodes.get(key);

			// gerar roleta baseado em vizinhança
			List<String> roleta = new ArrayList<String>();
			List<String> neighborsHashList = n.getNeighborsHashList();
			if(!neighborsHashList.contains(key)) {
				neighborsHashList.add(key);
			}
			for (String s : neighborsHashList) {

				GsopNode neighbour = nodes.get(s);

				int qtd = (int) (neighbour.getCoeff() * 100.0);
				for (int i = 0; i < qtd; i++) {
					roleta.add(s);
				}
			}

			Eph eph = null;

			//sorteado é o nó que irá propagar o seu tipo
			GsopNode sorteado;
			if(roleta.size()==0) {
				//System.out.println("Nó sem vizinhos "+key);
				sorteado = nodes.get(key);
			}else {
			// escolher tipo propagado
				int chosen = gerador.nextInt(roleta.size());
				sorteado = nodes.get(roleta.get(chosen));
			}
			
			sorteado.setFitness(sorteado.getFitness() + 1);

			if (!neighborhoodInheritance) {
				eph = n.getEph();
				n.setEph(null);
				// substituir tipo do nó
				n.setCoeff(sorteado.getRawCoeff());
				n.setType(sorteado.getType());
				n.setFitness(0);
				// verifica se o nascido é do tipo gerador, e se vai gerar eph
				if (n.getType() == "A") {
					int sorteioGeracao = gerador.nextInt(100);
					if (sorteioGeracao < (simulationData.getEphBirthGenerationChance() * 100)) {
						n.setEph(new Eph(simulationData.getEphBonus()));
					}
				}				
			} else {
				// mata nó antigo e aproveita o uuid para o nó que vai nascer

				GsopNode dyingNode = nodes.get(selectedKeysDeath.get(0));
				String selectedUUID = dyingNode.getHash();
				eph = dyingNode.getEph();
				for (String neighborUUID : dyingNode.getNeighborsHashList()) {
					GsopNode neighbor = nodes.get(neighborUUID);

					if (!neighbor.getHash().equals(dyingNode.getHash()))
						neighbor.getNeighborsHashList().remove(selectedUUID);
				}
				
				nodes.remove(selectedUUID);
				selectedKeysDeath.remove(0);
				
				// gera novo nó, atualiza listas de vizinhança e adiciona-o à lista de nós
				GsopNode newNode = new GsopNode();
				newNode.setHash(selectedUUID);
				newNode.setCoeff(sorteado.getRawCoeff());
				newNode.setType(sorteado.getType());
				newNode.setFitness(0);
				List<String> newList = new ArrayList<String>();
				
				for(String s : sorteado.getNeighborsHashList()) {
					if (nodes.containsKey(s))
						newList.add(s);
				}
				if (nodes.containsKey(sorteado.getHash()))
					newList.add(sorteado.getHash());
				newNode.setNeighborsHashList(newList);

				if (newNode.getType() == "A") {
					int sorteioGeracao = gerador.nextInt(100);
					if (sorteioGeracao < (simulationData.getEphBirthGenerationChance() * 100)) {
						newNode.setEph(new Eph(simulationData.getEphBonus()));
					}
				}

				for (String neighborUUID : newNode.getNeighborsHashList()) {
					GsopNode neighbor = nodes.get(neighborUUID);
					if (neighbor == null)
						System.out.println(neighborUUID + " nulo");
					neighbor.getNeighborsHashList().add(selectedUUID);
				}

				nodes.put(selectedUUID, newNode);
				//System.out.println(nodes.get(selectedUUID).getCoeff());

			}

//			int ephChosen = gerador.nextInt(neighborsHashList.size());
//			GsopNode recebedorEph = nodes.get(neighborsHashList.get(ephChosen));
			List<String> currentKeys = new ArrayList<String>(neighborsHashList);
			Collections.shuffle(currentKeys);
			boolean pegou = false;
			for(String k : currentKeys) {
				
				if (nodes.get(k).getEph() == null && nodes.get(k).getType()=="A") {
					nodes.get(k).setEph(eph);
					pegou = true;
				} 
			}
			
			if (!pegou) {
				long seed = System.nanoTime();
				currentKeys = new ArrayList<String>(nodes.keySet());
				Collections.shuffle(currentKeys, new Random(seed));
				for (String k : currentKeys) {
					if (nodes.get(k).getEph() == null && nodes.get(k).getType()=="A") {
						nodes.get(k).setEph(eph);
						break;						
					}
				}
			}
//			if (!neighborhoodInheritance) {
//				// sorteia vizinho para ocupar o eph
//				int ephChosen = gerador.nextInt(neighborsHashList.size());
//				// vizinho ocupa o eph caso não tenha um.
//
//				GsopNode recebedorEph = nodes.get(neighborsHashList.get(ephChosen));
//				if (recebedorEph.getEph() == null) {
//					recebedorEph.setEph(eph);
//				} else {
//					long seed = System.nanoTime();
//					Collections.shuffle(keys, new Random(seed));
//					for (String k : keys) {
//						recebedorEph = nodes.get(k);
//						if (recebedorEph.getEph() == null) {
//							recebedorEph.setEph(eph);
//							break;
//						}
//					}
//				}
//
//			}

		}

	}

	public static void cycleV4(Map<String, GsopNode> nodes, double deathRate) {

		// Contagem de nascimentos e mortes
		Random gerador = new Random();

		int dieCount = (int) ((double) nodes.size() * (deathRate - 1));
		if (dieCount == 0)
			dieCount++;
		// deaths
		List<String> selectedKeys = new ArrayList<String>();
		List<String> keys = new ArrayList<String>(nodes.keySet());
		// System.out.println(dieCount);
		// System.out.println("Keys size "+keys.size());
		for (int i = 0; i < dieCount; i++) {
			int chosen = gerador.nextInt(nodes.size());
			selectedKeys.add(keys.get(chosen));
		}
		// System.out.println("Selected Keys size"+selectedKeys.size());

		// births
		for (String key : selectedKeys) {

			GsopNode n = nodes.get(key);

			// gerar roleta baseado em vizinhança
			List<String> roleta = new ArrayList<String>();
			List<String> neighborsHashList = n.getNeighborsHashList();

			for (String s : neighborsHashList) {

				GsopNode neighbour = nodes.get(s);

				int qtd = (int) (neighbour.getCoeff() * 100.0);
				for (int i = 0; i < qtd; i++) {
					roleta.add(s);
				}
			}

			// Remove eph do individuo que morreu
			Eph eph = n.getEph();
			n.setEph(null);

			// escolher tipo propagado
			int chosen = gerador.nextInt(roleta.size());

			GsopNode sorteado = nodes.get(roleta.get(chosen));
			sorteado.setFitness(sorteado.getFitness() + 1);

			// substituir tipo do nó
			n.setCoeff(sorteado.getRawCoeff());
			n.setType(sorteado.getType());
			n.setFitness(0);

			// verifica se o nascido é do tipo gerador, e se vai gerar eph
			if (n.getType() == "A") {
				int sorteioGeracao = gerador.nextInt(100);
				if (sorteioGeracao < (simulationData.getEphBirthGenerationChance() * 100)) {
					n.setEph(new Eph(simulationData.getEphBonus()));
				}
			}

			// sorteia vizinho para ocupar o eph
			int ephChosen = gerador.nextInt(neighborsHashList.size());
			// vizinho ocupa o eph caso não tenha um.
			GsopNode recebedorEph = nodes.get(neighborsHashList.get(ephChosen));
			if (recebedorEph.getEph() == null) {
				recebedorEph.setEph(eph);
			}

		}

	}

	public static void cycleV3(Map<String, GsopNode> nodes, double deathRate) {

		// Contagem de nascimentos e mortes
		Random gerador = new Random();

		int dieCount = (int) ((double) nodes.size() * (deathRate - 1));
		if (dieCount == 0)
			dieCount++;
		// deaths
		List<String> selectedKeys = new ArrayList<String>();
		List<String> keys = new ArrayList<String>(nodes.keySet());
		// System.out.println(dieCount);
		// System.out.println("Keys size "+keys.size());
		for (int i = 0; i < dieCount; i++) {
			int chosen = gerador.nextInt(nodes.size());
			selectedKeys.add(keys.get(chosen));
		}
		// System.out.println("Selected Keys size"+selectedKeys.size());

		// births
		for (String key : selectedKeys) {
			// gerar roleta baseado em vizinhança
			List<String> roleta = new ArrayList<String>();

			GsopNode n = nodes.get(key);
			for (String s : n.getNeighborsHashList()) {

				GsopNode neighbour = nodes.get(s);

				int qtd = (int) (neighbour.getCoeff() * 100.0);
				for (int i = 0; i < qtd; i++) {
					roleta.add(s);
				}
			}

			// escolher tipo propagado
			int chosen = gerador.nextInt(roleta.size());

			GsopNode sorteado = nodes.get(roleta.get(chosen));
			sorteado.setFitness(sorteado.getFitness() + 1);

			// substituir tipo do nó
			n.setCoeff(sorteado.getCoeff());
			n.setType(sorteado.getType());
			n.setFitness(0);

		}

	}

	public static void cycle(List<GsopNode> nodes) {

		Random gerador = new Random();

		int chosen = gerador.nextInt(nodes.size());

		List<Integer> roleta = new LinkedList<>();

		for (int i = 0; i < nodes.size(); i++) {
			if (i == chosen)
				continue;
			for (int j = 0; j < nodes.get(i).getVal(); j++) {
				roleta.add(i);
			}
		}

		int chosen2 = gerador.nextInt(roleta.size());
		GsopNode n = new GsopNode();
		n.setHash(nodes.get(chosen).getHash());
		n.setType(nodes.get(roleta.get(chosen2)).getType());
		n.setVal(nodes.get(roleta.get(chosen2)).getVal());
		nodes.set(chosen, n);

	}

	public static void cycleV2(List<GsopNode> nodes, double birthRate, double deathRate) {

		Random gerador = new Random();

		int dieCount = (int) ((double) nodes.size() * (deathRate - 1));
		int birthCount = (int) ((double) nodes.size() * (birthRate - 1));

		// deaths

		// System.out.println(dieCount);
		for (int i = 0; i < dieCount; i++) {
			int chosen = gerador.nextInt(nodes.size());
			nodes.remove(chosen);
		}

		// births

		for (int a = 0; a < birthCount; a++) {

			int count_a = 0, count_b = 0;

			count_a = typeCount("A", nodes);
			count_b = typeCount("B", nodes);

			double chanceA = ((double) count_a) * simulationData.getTypes().get(0).getInitialCoeff();
			double chanceB = ((double) count_b) * simulationData.getTypes().get(1).getInitialCoeff();

			int chosen = gerador.nextInt((int) (chanceA + chanceB));
			GsopNode n = new GsopNode();
			if (chosen < (int) chanceA) {
				n.setType(simulationData.getTypes().get(0).getType());
				n.setCoeff(simulationData.getTypes().get(0).getInitialCoeff());
			} else {
				n.setType(simulationData.getTypes().get(1).getType());
				n.setCoeff(simulationData.getTypes().get(1).getInitialCoeff());
			}
			nodes.add(n);
			int pivot = gerador.nextInt(nodes.size() - 1);
			for (int i = 0; i < nodes.size(); i++) {
				if (++pivot > nodes.size() - 1)
					pivot = 0;
				if (nodes.get(pivot).getType() == n.getType()) {
					nodes.get(pivot).setFitness(nodes.get(pivot).getFitness() + 1);
					break;
				}
			}

		}

	}

	public static int typeCount(String type, List<GsopNode> nodes) {
		int count = 0;

		for (GsopNode n : nodes) {
			if (n.getType().compareTo(type) == 0) {
				count++;
			}
		}
		return count;
	}

	public static String printTypeCount(List<GsopNode> nodes) {
		int count_a = 0;
		int count_b = 0;

		for (GsopNode n : nodes) {
			if (n.getType().compareTo("A") == 0) {
				count_a++;
			} else {
				count_b++;
			}
		}

		return "A: " + count_a + " B: " + count_b;
	}

	public static double avgCoeff(List<GsopNode> nodes) {
		double sum = 0;

		for (GsopNode n : nodes) {
			sum += n.getCoeff();
		}

		return sum / (double) nodes.size();
	}

	public static String printAvgCoeff(List<GsopNode> nodes) {
		return "Avg. coefficient: " + avgCoeff(nodes);
	}

	public static double avgFitness(List<GsopNode> nodes) {
		double sum = 0;

		for (GsopNode n : nodes) {
			sum += n.getFitness();

		}

		return sum / (double) nodes.size();
	}

	public static String printAvgFitness(List<GsopNode> nodes) {
		return "Avg. fitness: " + avgFitness(nodes);
	}

	public static SimulationData getSimulationData() {
		return simulationData;
	}

	public static void setSimulationData(SimulationData simulationData) {
		Simulation.simulationData = simulationData;
	}

	public static List<Double> getPartialFitnessAvg() {
		return partialFitnessAvg;
	}

	public static void setPartialFitnessAvg(List<Double> partialFitnessAvg) {
		Simulation.partialFitnessAvg = partialFitnessAvg;
	}

	public static void addPartialFitnessAvg(Double avg) {
		if (partialFitnessAvg == null)
			partialFitnessAvg = new ArrayList<Double>();
		partialFitnessAvg.add(avg);
	}

}
