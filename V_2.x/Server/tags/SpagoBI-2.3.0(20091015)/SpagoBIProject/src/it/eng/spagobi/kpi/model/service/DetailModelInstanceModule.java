package it.eng.spagobi.kpi.model.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.detail.impl.DefaultDetailModule;
import it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil;

import java.util.Collection;
import java.util.Iterator;

public class DetailModelInstanceModule extends DefaultDetailModule {

	private static final String VALIDATION_PAGE = "ModelInstanceDetailPage";
	private static final String VALIDATION_INSERT_PAGE = "ModelInstanceDetailInsertPage";

	public void service(SourceBean request, SourceBean response)
			throws Exception {
		boolean validationError = false;

		String message = (String) request.getAttribute("MESSAGE");
		if (message == null) {
			message = SpagoBIConstants.DETAIL_SELECT;
		}
		// VALIDATION
		validationError = hasValidationError(message);
		
		// DETAIL_SELECT
		if (message.equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)) {
			String idModel = (String) request.getAttribute("ID");
			DetailModelInstanceUtil.selectModelInstance(Integer
					.parseInt(idModel), response);
		}
		// DETAIL_UPDATE
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)) {
			String idModel = (String) request.getAttribute("ID");
			response.setAttribute("ID", Integer.parseInt(idModel));
			response.setAttribute("MESSAGE", SpagoBIConstants.DETAIL_SELECT);
			if (!validationError) {
				try {
					DetailModelInstanceUtil.updateModelInstanceFromRequest(
							request, response, Integer.parseInt(idModel));
				} catch (NumberFormatException e) {
					EMFErrorHandler engErrorHandler = getErrorHandler();
					engErrorHandler.addError(new EMFUserError(
							EMFErrorSeverity.WARNING, "10013",
							"component_kpi_messages"));
				} catch (EMFUserError e) {
					EMFErrorHandler engErrorHandler = getErrorHandler();
					engErrorHandler.addError(e);
				}

				DetailModelInstanceUtil.selectModelInstance(Integer
						.parseInt(idModel), response);
			} else {
				DetailModelInstanceUtil.restoreModelInstanceValue(Integer.parseInt(idModel),
						request, response);
			}
		}

		// DETAIL_INSERT
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)) {
			if (!validationError) {
				try{
				DetailModelInstanceUtil.newModelInstance(request, response, null);
				}catch (EMFUserError e) {
					EMFErrorHandler engErrorHandler = getErrorHandler();
					engErrorHandler.addError(e);
					DetailModelInstanceUtil.restoreModelInstanceValue(null,
							request, response);
				}
			} else {
				DetailModelInstanceUtil.restoreModelInstanceValue(null,
						request, response);
			}
		}
	}

	private boolean hasValidationError(String message) {
		boolean toReturn = false;
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)
		) {
			ValidationCoordinator.validate("PAGE", VALIDATION_PAGE, this);

			EMFErrorHandler errorHandler = getErrorHandler();

			Collection errors = errorHandler.getErrors();

			if (errors != null && errors.size() > 0) {
				Iterator iterator = errors.iterator();
				while (iterator.hasNext()) {
					Object error = iterator.next();
					if (error instanceof EMFValidationError) {
						toReturn = true;
					}
				}
			}
		}
		if (message.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)
				) {
					ValidationCoordinator.validate("PAGE", VALIDATION_INSERT_PAGE, this);

					EMFErrorHandler errorHandler = getErrorHandler();

					Collection errors = errorHandler.getErrors();

					if (errors != null && errors.size() > 0) {
						Iterator iterator = errors.iterator();
						while (iterator.hasNext()) {
							Object error = iterator.next();
							if (error instanceof EMFValidationError) {
								toReturn = true;
							}
						}
					}
				}
		
		return toReturn;
	}

}
