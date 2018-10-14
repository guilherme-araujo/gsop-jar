package br.ufrn.imd.bioinfo.gsopJar;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.ufrn.imd.bioinfo.gsop.Simulation;
import br.ufrn.imd.bioinfo.gsop.SimulationResults;
import br.ufrn.imd.bioinfo.gsop.database.Neo4jBootstrapper;

/**
 * Hello world!
 *
 */
public class App {
	public static String csv;

	public static void main(String[] args) {

		Neo4jBootstrapper.initialize();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				Neo4jBootstrapper.destroy();
			}
		}, "Shutdown-thread"));

		String operation = "";
		Integer samples = 1;
		Integer cycles = 1000;
		Integer numNodes = 100;
		Integer numEdges = 4;
		Double ephBonus = 0.05;
		Integer threads = 1;
		String saveFile = "";
		String graphtype = "";
		Double ephStartRatio = 0.5;

		// Parsing arguments
		for (String arg : args) {
			if (arg.contains("--operation")) {
				String op[] = arg.split("=");
				operation = op[1];

			} else if (arg.contains("--savefile")){
				String savef[] = arg.split("=");
				saveFile = savef[1];
			}else if (arg.contains("--samples")) {
				String smp[] = arg.split("=");
				samples = Integer.parseInt(smp[1]);
			} else if (arg.contains("--cycles")) {
				String cyc[] = arg.split("=");
				cycles = Integer.parseInt(cyc[1]);
			} else if (arg.contains("--numNodes")) {
				String num[] = arg.split("=");
				numNodes = Integer.parseInt(num[1]);
			} else if (arg.contains("--numEdges")) {
				String num[] = arg.split("=");
				numEdges = Integer.parseInt(num[1]);
			} else if (arg.contains("--ephBonus")) {
				String num[] = arg.split("=");
				ephBonus = Double.parseDouble(num[1]);
			} else if (arg.contains("--threads")) {
				String thr[] = arg.split("=");
				threads = Integer.parseInt(thr[1]);
			} else if (arg.contains("--graphtype")) {
				String gtype[] = arg.split("=");
				graphtype = gtype[1];
			} else if (arg.contains("--ephstartratio")) {
				String esr[] = arg.split("=");
				ephStartRatio = Double.parseDouble(esr[1]);
			}

		}

		if (operation.equals("newgraph")) {
			NewGraphCaller newGraph = new NewGraphCaller();
			newGraph.setNumNodes(numNodes);
			newGraph.setNumEdges(numEdges);
			
			if(graphtype.equals("ba")) {
				newGraph.generateba();
			} else if (graphtype.equals("complete")) {
				newGraph.generateComplete();
			} else if (graphtype.equals("simple")) {
				newGraph.generateSimple();
			}else {
				newGraph.generateba();
			}
			

			for (String s : newGraph.generatedNodes)
				System.out.println(s);

		} else if (operation.equals("simulation")) {

			//ArrayList<SimulationResults> resultList = new ArrayList<SimulationResults>();
			SimulationCaller simulation = new SimulationCaller();
			simulation.getSimulationData().setCycles(cycles);
			simulation.getSimulationData().setEphBonus(ephBonus);
			simulation.getSimulationData().setEphStartRatio(ephStartRatio);			
			System.out.println(samples + " samples will be executed");
			long tStart = System.currentTimeMillis();

			csv = "";

			ExecutorService executorService = Executors.newFixedThreadPool(threads);

			//ArrayList<Runnable> tasks = new ArrayList<Runnable>();

			CountDownLatch latch = new CountDownLatch(samples);
			
			Runnable task = () -> {
				//final int count = i;
				//System.out.println("Sample " + count);
				simulation.runSimV5();
				SimulationResults results = simulation.getSimulationResults();
				//resultList.add(results);
				int typeACount = Simulation.typeCount("A", results.finalNodes);
				int typeBCount = Simulation.typeCount("B", results.finalNodes);
				//String out = /*"Sample " + count + */" A: " + typeACount + " B: " + typeBCount + " Avg. coeff: "
				//		+ results.avgCoeff + " Avg. fitness: " + results.avgFitness + " time: "
				//		+ results.elapsedSeconds+" fixation cycles: "+results.fixationCycles;
				csv += typeACount + ";" + typeBCount + ";" + results.fixationCycles + "\n";

				//System.out.println(out);
				latch.countDown();

			};
			
			
			
			for (int i = 0; i < samples; i++) {
				
				
				executorService.execute(task);
				//tasks.add(task);
			}
			executorService.shutdown();
			try {
				while(!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
					System.out.println(latch.getCount()+" left...");
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			/*for(Runnable task : tasks) {
				executorService.execute(task);
			}*/

			try {
				DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");				
				String tag = formatter.format(new Date());
				FileWriter fileWriter = new FileWriter(tag+".csv");
				PrintWriter printWriter = new PrintWriter(fileWriter);				
				printWriter.println(csv);
				printWriter.close();
				String info = "";
				info += "samples: "+samples+" cycles: "+cycles+" numNodes: "+numNodes+" numEdges: "+numEdges
						+" ephBonus: "+ephBonus+" threads: "+threads;
				fileWriter = new FileWriter(tag+".txt");
				printWriter = new PrintWriter(fileWriter);
				printWriter.println(info);
				printWriter.close();
			} catch (Exception e) {
				System.out.println("File error");
			}

			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			double elapsedSeconds = tDelta / 1000.0;

			System.out.println(elapsedSeconds + " seconds elapsed");

		} else if(operation.equals("backup")) {
			SaveGraphCaller saveGraphCaller = new SaveGraphCaller();
			saveGraphCaller.saveGraph(saveFile);
		}

	}

}
