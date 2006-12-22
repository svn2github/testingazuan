/**
 * 
 */
package it.eng.spagobi.util;

/**
 * @author Gioia
 *
 */
public class ParameterSetter {
	public static String setParameters(String query, String pname, String pvalue) {
		String newQuery = query;
		int index = -1;
		int ptr = 0;
		while( (index = newQuery.indexOf("Parameter", ptr)) != -1 ) {
			ptr = newQuery.indexOf("(", index);
			String firstArg = newQuery.substring(newQuery.indexOf("(", ptr) + 1, newQuery.indexOf(",", ptr));	
			if(!firstArg.trim().equalsIgnoreCase("\""+pname+"\"")) continue;
			
			ptr = newQuery.indexOf(",", ptr) + 1; // 2 arg
			ptr = newQuery.indexOf(",", ptr) + 1; // 3 arg
			String thirdArg = newQuery.substring(ptr, newQuery.indexOf(",", ptr));	
			newQuery = newQuery.substring(0, ptr) + pvalue + newQuery.substring(newQuery.indexOf(",", ptr+1), newQuery.length());		
		}
		return newQuery;
	}
}

/*
SELECT 
{[Measures].[Previsione], [Measures].[Assestato], [Measures].[Accertato], [Measures].[Riscosso], [Measures].[Previsione-Assestato], [Measures].[Assestato-Accertato], [Measures].[Accertato-Riscosso]} ON columns, 
{(Parameter('Direzione',[Direzione],[Direzione].[All Direzione].[117 - POLIZ. MUNIC-PROT.CIV-FUNZ.SPE]), [Esercizio].[All Esercizio], [Centro di Costo].[All Centro di Costo], [TipoRisorsa].[All TipoRisorsa], [Titolo Categoria Risorsa].[All Titolo Categoria Risorsa], [Voci Economiche Entrate].[All Voci Economiche Entrate])} ON rows FROM [EntrateProva]
<parameter name='Dir' as='Direzione' />
*/

/*
<olap>
  	<connection type='XMLA' url='http://192.168.14.4/xmla/msxisapi.dll'/>"
	<cube name='FoodMart 2000'/>

	<MDXquery>
	 	select 
	 	{[Measures].[Unit Sales], [Measures].[Store Cost], [Measures].[Store Sales]} on columns, 
	 	{Parameter("ProductMember", [Product], [Product].[All Products].[Food], "wat willste?").children} ON rows 
	 	from Sales where ([Time].[1997])
   	
		<parameter name='prdCd' as='ProductMember' />
	</MDXquery>
</olap>
*/