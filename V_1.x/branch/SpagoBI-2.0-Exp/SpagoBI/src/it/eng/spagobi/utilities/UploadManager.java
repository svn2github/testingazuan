package it.eng.spagobi.utilities;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.httpchannel.upload.IUploadHandler;
import it.eng.spago.dispatching.service.DefaultRequestContext;

import org.apache.commons.fileupload.FileItem;

public class UploadManager extends DefaultRequestContext implements IUploadHandler {

	public void upload(FileItem item) throws Exception {
		UploadedFile uploadedFile = new UploadedFile();
		uploadedFile.setFileContent(item.get());
		uploadedFile.setFieldNameInForm(item.getFieldName());
		uploadedFile.setSizeInBytes(item.getSize());
		uploadedFile.setFileName(GeneralUtilities.getRelativeFileNames(item.getName()));
		SourceBean serviceRequest = getServiceRequest();
		serviceRequest.setAttribute("UPLOADED_FILE", uploadedFile);
	}

}
