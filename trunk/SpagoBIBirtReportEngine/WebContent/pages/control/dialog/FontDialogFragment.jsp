<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.viewer.aggregation.Fragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.viewer.aggregation.Fragment" scope="request" />

<%-----------------------------------------------------------------------------
	Font dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2px" CELLPADDING="2px" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<tr style='width:100%;height:20px'>
		<td style='width:60px' valign='top' rowspan='4'></td>
		<td width='50%' valign='top' style='font-size:8pt'>
			<b>Font:</b>
		</td>
		<td width='35%' valign='top' style='font-size:8pt'>
			<b>Style:</b>
		</td>
		<td width='15%' valign='top' style='font-size:8pt'>
			<b>Size:</b>
		</td>
		<td style='width:40px' valign='top' rowspan='4'></td>
	</tr>
	<tr style='width:100%;height:25px'>
		<td width='50%' valign='top'>
			<input id='family' name='family' type='text' value='' style='width:100%;font-size:8pt'>
		</td>
		<td width='35%' valign='top'>
			<input name='style' type='text' value='' style='width:100%;font-size:8pt'>
		</td>
		<td width='15%' valign='top'>
			<input name='size' type='text' value='' style='width:100%;font-size:8pt'>
		</td>
	</tr>
	<tr style='width:100%;height:100px' border=1>
		<td width='50%' valign='top'>
			<select 
				size='6'
				id='familys'
				style='width:100%;
					   height:100px;
					   font-size:8pt'>
				<option>Arial
				<option>Verdana
				<option>Courier
				<option>Garamond
				<option>MS Serif
			</select>
		</td>
		<td width='35%' valign='top'>
			<select	id='styles'
					size='4'
					style='width:100%;
						   height:60px;
						   font-size:8pt'>
				<option>Regular
				<option>Italic
				<option>Bold
				<option>Bold Italic
			</select>
		</td>
		<td width='15%' valign='top'>
			<select
				size='5'
				id='sizes'
				style='width:100%;
					   height:100px;
					   font-size:8pt'>
				<option>8
				<option>10
				<option>12
				<option>15
			</select>
		</td>
	</tr>
	<tr style='width:100%;height:80px'>
		<td width='50%' valign='top'>
			<TABLE CELLSPACING="0" CELLPADDING="0" style="width:100%;height:100%;font-size:8pt">
				<tr>
					<td valign='top' style='height:15px'>
						<b>Effect</b>
					</td>
				</tr>
				<tr>
					<td valign='top'>
						<TABLE CELLSPACING="0" CELLPADDING="2px"
							style="width:100%;
								   height:100%;
								   border-width:1px;
								   border-style:solid;
								   border-color:#cccccc;
								   font-size:8pt">
							<tr>
								<td valign='top'>
									<input type='checkbox' name='effect' value='underline'>Underline<br>
									<input type='checkbox' name='effect' value='strikout'>Strikout
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width='50%' valign='top' colspan='2'>
			<TABLE CELLSPACING="0" CELLPADDING="0" style="width:100%;height:100%;font-size:8pt">
				<tr>
					<td valign='top' style='height:15px'>
						<b>Super/Subscript</b>
					</td>
				</tr>
				<tr>
					<td valign='top'>
						<TABLE CELLSPACING="0" CELLPADDING="2px" valign='top'
							style="width:100%;
								   height:100%;
								   border-width:1px;
								   border-style:solid;
								   border-color:#cccccc;
								   font-size:8pt">
							<tr>
								<td valign='top'>
									<input type='radio' name='super' value='Normal' checked>Normal<br>
									<input type='radio' name='super' value='Superscript'>Superscript<br>
									<input type='radio' name='super' value='Subscript'>Subscript
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr style='height:5px'><td></td></tr>
</TABLE>