This installer was developed starting from IzPack 3.10

- The folder spagobi contains all the contents to include into the intaller. The internal structure of the folder must remain the same. Any changes have to reflect to the source code. 

Per creare l'installer:
1. Compilare i jar dei pannelli e metterli dentro la cartella IzPack_Home\bin\panels. I jar devono contenere:

SpagoBIFinishPanel: basta la classe com\izforge\izpack\panels\SpagoBIFinishPanel;

SpagoBITargetPanel: deve contenere le classi 
	com\izforge\izpack\panels\PathInputPanel
	com\izforge\izpack\panels\PathSelectionPanel
	com\izforge\izpack\panels\SpagoBITargetPanel
	com\izforge\izpack\panels\TargetPanel
	com\izforge\izpack\panels\TargetPanelAutomationHelper

SpagoBIRepositoryPathPanel: deve contenere le classi 
	com\izforge\izpack\panels\PathInputPanel
	com\izforge\izpack\panels\PathSelectionPanel
	com\izforge\izpack\panels\SpagoBIRepositoryPathPanel

SpagoBIPerlPathPanel: deve contenere le classi 
	com\izforge\izpack\panels\PathInputPanel
	com\izforge\izpack\panels\PathSelectionPanel
	com\izforge\izpack\panels\SpagoBIPerlPathPanel
	
2. Ricreare le librerie compiler.jar e installer.jar presenti dentro IzPack_Home\lib con le seguenti classi modificate:

com.izforge.izpack.compiler.CompilerConfig: cambiato il nome della classe unpacker --> compiler.jar
com.izforge.izpack.installer.WebAccessor: modificato il try catch per beccare l'eccezione ottenuta dal proxy --> installer.jar
com.izforge.izpack.installer.SpagoBIUnpacker: aggiunta --> installer.jar
com.izforge.izpack.compiler.SpagoBIPackager: aggiunta --> compiler.jar
com.izforge.izpack.compiler.PackInfo --> compiler.jar
com.izforge.izpack.Pack --> compiler.jar + installer.jar

3. Lanciare il file build.xml come script ant