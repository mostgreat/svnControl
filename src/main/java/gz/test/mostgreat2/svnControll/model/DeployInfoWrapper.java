package gz.test.mostgreat2.svnControll.model;

import java.util.List;


public class DeployInfoWrapper {
	
	private List<DeployInfo> deploys;
	
	private String deployDir;

	public List<DeployInfo> getDeploys() {
		return deploys;
	}

	public void setDeploys(List<DeployInfo> deploys) {
		this.deploys = deploys;
	}

	public String getDeployDir() {
		return deployDir;
	}

	public void setDeployDir(String deployDir) {
		this.deployDir = deployDir;
	}
	
	
}
