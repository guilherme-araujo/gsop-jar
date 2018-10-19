package br.ufrn.imd.bioinfo.gsopJar;

import br.ufrn.imd.bioinfo.gsop.controller.SaveGraphController;

public class SaveGraphCaller {
	
	SaveGraphController saveGraph;
	
	public SaveGraphCaller() {
		saveGraph = new SaveGraphController();
	}
	
	public void saveGraph(String saveFile) {
		saveGraph.saveGraph(saveFile);
	}

	public void restoreGraph(String saveFile) {
		saveGraph.restoreGraph(saveFile);
	}
}
