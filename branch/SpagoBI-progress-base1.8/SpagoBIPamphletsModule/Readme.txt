

Template openoffice:

- The template must be created using the impress tool.
- Slides which should contain spagobi docuement have to contain a rectangle which will be substitute with the image of the document
- To each rectangle must be assigned a name built in the following manner:
spagobi_placeholder_<<logical name>> where logical name is the name assigned to the document into the spagobi pamphlet configuration
- The definition of the workflow must follow a template (the file SpagoBI.xpdl) and it's possible to change only the users, 
 and activity for the edit node task. 
- The user defined into shark must be the same as the ones defined into SpagoBI and the password must be the same as the user name
- Workflow activity to edit note should contain the extended attribute form and indexPart as the example SpagoBI.xpdl