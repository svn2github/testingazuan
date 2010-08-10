In order to use the booklet feature you have to know some concept:

- The aim is to produce a presentation document containing the images of some analitical document
- The booklet needs a template of an openoffice presentation document which is filled with the images of the documents and the
  notes of the users
- Two example of templates are contained into this package. Each template is a set of slides of a presentation document. 
  If you want that a slide will be filled with the image of a particular report you have to place a rectangle and give it a 
  name starting with "spagobi_placeholder_" and ending with a logical name that you can choose.
  
- Once configured and execute, the system produces the set of images which will be used to fill the presentation template
- Every user allowed can see the images and write some notes about them 
- Once every allowed users have complete the notes insertion the system take all the images and notes, fill the template 
  presentation, and produce the final document
- The whole process is guided from a workflow system
- The workflow process has some steps that are fixed and cannot be changed and other which can be personalized
- An example file of workflow process is contained into this package (SpagoBI.xpdl). You can open it using jawe application.
- From the xpdl definition you can change the roles (but not the system role) and assign to them the "Edit Note Activity".
- Each "Edit Note Activity" has two extedend attribute, form and indexPart, and you can change the value of the second one.
  The value is the index of a slide of the presentation document.  Each activity assigned to a role means that the corresponding 
  role have to write some notes about the content of the slide identified from the index attribute.
- Once changed the process definition yuo have to load it into Shark.
- For this release it's important that the user defined into shark are the same of SpagoBI and that the password of each 
  user is equal to his name

- Into the configuration mask of the pamphlet you have to load the open office presentation template, the name of the package 
  and workflow process loaded into shark. Each document included into the booklet must be added to the booklet configuration.
  For this release it's possible ot add only Jasper Report documents
- For each document added you must provide a value for all its parameters and also a logical name which must be the same of the 
  template place_holder final part of the name. (spagobi_palceholder_<<logical name>>)
- Once configured you can run the booklet
- then the set of report images is produced
- Then, based on the roles configured into the workflow process, the users find into their SpagoBI WorkList portlet a new activity 
  which give them the possibility to view the images and store the notes
- Once all the workflow configured roles have write the notes the system automatically produce the final Document and assign to a
  final role the task to check the document and to terminate the process.
  
