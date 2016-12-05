 <html>
 <head>
 <title>
${testCaseName} Detailed Reports
 </title>
 </head>
 <body>
 <h4> <FONT COLOR=Blue FACE=Arial SIZE=4.5>${testCaseName} -- Detailed Report :</h4>
 <table  border=1 cellspacing=1    cellpadding=1 width=100%>
 	<tr>
		 <td align=center width=10%  align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Step</b></td>
		 <td align=center width=50% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Description</b></td>
		 <td align=center width=10% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Keyword</b></td>
		 <td align=center width=15% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Result</b></td>
		 <td align=center width=15% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Screen Shot</b></td>
 	</tr>
<#list testSteps as testStep>
 	<tr>
		 <td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>${testStep.testStepId}</b> </td>
		 <td align=center width=50%><FONT COLOR=#153E7E FACE=Arial SIZE=1>
		 	<#if testStep.testStepDescription??>
		 		<b>${testStep.testStepDescription}</b>
		 	</#if>
		 </td>
		 <td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1>
		 	<#if testStep.testStepKeyword??>
		 		<b>${testStep.testStepKeyword}</b>
		 	</#if>
		 </td>
		 <#if testStep.testStepResult??>
			 <td width=20% align= center 
			 	<#if testStep.testStepResult == "Pass"> bgcolor=#BCE954 <#else> bgcolor=Red </#if>><FONT COLOR=#153E7E FACE= Arial  SIZE=2>
			 	<b>${testStep.testStepResult}</b>
			 </td>
		 </#if>
		 <td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1>
		 	<#if testStep.failureScreenShot??>
				<b><a href='${testStep.failureScreenShot}' target=_blank>Screen Shot</a></b>
			</#if>
		 </td>
 	</tr>
</#list>
</table>
</body>
</html>

