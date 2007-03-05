- The file install_spagobi_support_classes.jar contains the classes used by the Izpack installer. When any changes occurs to the source code of the installer it is necessary to export the new jar.
- The folder spagobi contains all the contents to include into the intaller. The internal structure of the folder must remain the same. Any changes have to reflect to the source code. 

classi cambiate di IzPack:
com.izforge.izpack.compiler.CompilerConfig: cambiato il nome della classe unpacker --> compiler.jar
com.izforge.izpack.installer.WebAccessor: modificato il try catch per beccare l'eccezione ottenuta dal proxy --> installer.jar
com.izforge.izpack.installer.SpagoBIUnpacker: aggiunta --> installer.jar
com.izforge.izpack.compiler.SpagoBIPackager: aggiunta --> compiler.jar
com.izforge.izpack.compiler.PackInfo --> compiler.jar
com.izforge.izpack.Pack --> compiler.jar + installer.jar