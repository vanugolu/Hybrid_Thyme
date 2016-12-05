 <html>
  <head>
  
  	<link rel="stylesheet" type="text/css" href="../dependencies/js/kendo.common.min.css">
    <link rel="stylesheet" type="text/css" href="../dependencies/js/kendo.default.min.css">
    <script type="text/javascript" src="../dependencies/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../dependencies/js/kendo.all.min.js"></script>
    <script type="text/javascript" src="../dependencies/js/createChart.js"></script>
    <script type="text/javascript" src="../dependencies/js/downloadToExcel.js"></script>
    
 <title>
${moduleStats.moduleName} Detailed Reports
 </title>
 
  <style> 
 .myStyle1 
 { 
 color : #E0E0E0; 
 font-family : Arial; 
 font-size : 13px; 
 width : 150px; 
 align : left; 
 background-color : #153E7E; 
 } 
 .myStyle2 
 { 
 color : #153E7E; 
 font-family : Arial; 
 font-size : 13px; 
 width : 150px; 
 align : left; 
 } 
 </style> 
 
 </head>
 <body id="body">
 <h4> <FONT COLOR=Blue FACE=Arial SIZE=4.5><a href="./TestingLogs/${moduleStats.moduleName}.log" TARGET="_blank" title="log file">${moduleStats.moduleName} -- Detailed Report :</a></h4>
  
 <table border=1 cellspacing=1    cellpadding=1>
 <tr> 
 <td class="myStyle1"><b>StartTime</b></td> 
 <td class="myStyle2"><b>${moduleStats.durationTracker.getStartTime()} </b></td> 
 </tr> 
 <tr> 
 <td class="myStyle1"><b>EndTime</b></td> 
 <td class="myStyle2"><b>${moduleStats.durationTracker.getEndTime()}</b></td> 
 </tr>
 <tr> 
 <td class="myStyle1"><b>Browser</b></td> 
 <td class="myStyle2"><b>${browserName}</b></td> 
 </tr>
 <tr> 
 <td class="myStyle1"><b>Total</b></td> 
 <td class="myStyle2"><b>${moduleStats.totalPassCount + moduleStats.totalFailCount}</b></td> 
 </tr>
 <tr>
 <td class="myStyle1"><b>Pass</b></td> 
 <td class="myStyle2"><b>${moduleStats.totalPassCount}</b></td> 
 </tr> 
 <tr> 
 <td class="myStyle1"><b>Fail</b></td> 
 <td class="myStyle2"><b> ${moduleStats.totalFailCount} </b></td> 
 </tr>
 <tr> 
 <td class="myStyle1"><b>Duration</b></td> 
 <td class="myStyle2"><b> ${moduleStats.durationTracker.timeTakenToComplete()} </b></td> 
 </tr>
 </table> 
 
 <br/><br/>



<p align="right">
<a id="dlink"  style="display:none;"></a>
<input type="button" onclick="tableToExcel('body', 'abc')" value="Export to Excel">
</p>

 <table  border=1 cellspacing=1    cellpadding=1 width=100%>
 <tr>
 <td width=2%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Automation TC ID</b></td> 
 <td width=38% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case Name</b></td> 
 <td width=10% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Manual TC ID</b></td> 
 <td width=10% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Status</b></td> 
 <td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run Start Time</b></td> 
 <td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run End Time</b></td>
 </tr>
 <#list testcases as testcase>
 <tr> 
 <td width=2% align=center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>${testcase.sequenceId}</b></td> 
 <td width=38% align=center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b><a href="${testcase.testCaseHyperLinkName}" TARGET="_blank">${testcase.testCaseDescription}</a></b></td> 
 <td width=10% align=center ><FONT COLOR=#153E7E FACE=Arial SIZE=2><b>${testcase.manualTCId}</b></td> 
 <td width=10% align=center  <#if testcase.result == "Pass"> bgcolor=#BCE954 <#else> bgcolor=red </#if>><FONT COLOR=black FACE= Arial  SIZE=2><b>${testcase.result}</b></td> 
 <td width=20% align=center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>${testcase.durationTracker.getStartTime()}</b></td> 
 <td width=20% align=center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>${testcase.durationTracker.getEndTime()}</b></td> 
 </tr>
 </#list>
 </table>
  </body>
 </html>
