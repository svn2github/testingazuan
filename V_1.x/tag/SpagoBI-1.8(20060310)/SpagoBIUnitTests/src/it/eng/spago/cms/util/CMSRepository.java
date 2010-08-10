package it.eng.spago.cms.util;

public class CMSRepository {

	private String repositorPath = null;

	private String zipRepositoryFileForRecovery = null;

	public String getRepositorPath() {
		return repositorPath;
	}

	public String getZipRepositoryFileForRecovery() {
		return zipRepositoryFileForRecovery;
	}

	public void setRepositorPath(String repositorPath) {
		this.repositorPath = repositorPath;
	}

	public void setZipRepositoryFileForRecovery(String zipRepositoryFileForRecovery) {
		this.zipRepositoryFileForRecovery = zipRepositoryFileForRecovery;
	}
}
