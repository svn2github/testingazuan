In order to use the booklet feature you have to know some concepts:

- The aim is to produce a presentation document containing the images of some jasper reports and the notes
  about the reports written by the users.
- The booklet needs a template of an openoffice presentation document which is filled with the images 
  of the documents and the notes of the users
- The booklet is a particular kind of SpagoBI document. Before execute it you must configure it.
- When you exec a booklet document the system execs the reports and produces a set of report images 
  which will be used to fill the presentation template
- Subsequently a workflow process is run. The workflow process definition is part of the booklet configuration.
  The workflow engine assign to different users the activity to write some notes about different report images
- Once every allowed user has complete the notes insertion the system takes all the images and notes, 
  fills the presentation template, and produces the final document


How to write the template for a booklet ?  
  
- The template is a power point document (ppt) but you can produce it using openoffice 2.x.   
  If you want that a slide of the document will be filled with the image of a particular report 
  you have to place inside the slide a rectangle containing a text starting with "spagobi_placeholder_" 
  and ending with a free choice logical name.
- An example template is contained into this package. 
  Each template is a set of slides of a presentation document. 

  
How to change the workflow process ?

- The workflow process has some steps that are fixed and cannot be changed and other which can be personalized
- An example file of workflow process is contained into this package. 
  You can open it using jbpm workflow designer or a simple xml editor.
  (in the following we will refer to the content of this file).
- The steps that you can change are those with name starting with 'AddNote'. Each one of these nodes represents 
  the user note insertion. The swimlane assigned is the SpagoBI role associated to the activity. 
  Only the user which has the role associate to the task can do the activity.
- You can add other 'AddNote' activities, remove one of them or simply changes the values of the existing ones.
- Remember that if you add or remove an activity you have to change also the links into the previous 
  fork node and into the next join node.  
- You can change the swimlane names (each swimlane is a SpagoBI role) and assign the swimlane to the 
  'AddNote' task.
- Each 'AddNote' task has two attribute. Only one of them is modifiable (spagobi_booklet_pageindex). 
  The value of this attribute is the index of the template slide (starting form 1) where the notes and the images will
  be placed. Each activity assigned to a role means that a user with the role haa to write some notes 
  about the content of the slide identified from the index attribute.
- You can also change the role (the swimlane) assigned to the 'ValidateFinalDocument' task. This activity 
  represent the validation of the final document and you can assign it to the more adapt role 

How to configure a booklet ?
  
- A booklet is a kind of SpagoBI document.
- Once created the booklet document can be configured. Into the document detail page there is a button that 
  allow you to access the configuration
- Into the configuration mask you have to load the open office presentation template and the jbpm 
  workflow process definition. 
- Each document that must be included into the booklet must be added to the booklet configuration.
  For this release it's possible to add only Jasper Report documents
- For each document added you must provide a value for all its parameters and also a logical 
  name which must match one of the template place holders. 
  (spagobi_placeholder_<<logical name>>)
- Once configured you can exec the booklet

How the user know when they have to write notes ?

- Once exec a booklet document the workflow process is started. The process does some automatic ativity and 
  then wait until all the add notes task have been done.
- Based on the roles configured into the workflow process the users find into their 
  SpagoBI WorkList portlet a new activity.
- Once accepted the activity the users can view the images and store the notes. At the end of the note 
  insertion each user ends the activity. 
- Once all the workflow configured roles have write the notes the system automatically 
  produce the final Document.
- Once the final document has been produced it has to be validated from a SpagoBI role.
- When a user validate the final document then he can deploy the resulting presentation document into the
  SpagoBI document tree.
  
