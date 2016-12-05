<HTML>
<HEAD>
 <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">

    <link rel="stylesheet" type="text/css" href="../dependencies/js/kendo.common.min.css">
    <link rel="stylesheet" type="text/css" href="../dependencies/js/kendo.default.min.css">
    <script type="text/javascript" src="../dependencies/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../dependencies/js/kendo.all.min.js"></script>
    <script type="text/javascript" src="../dependencies/js/createChart.js"></script>
    <script type="text/javascript" src="../dependencies/js/downloadToExcel.js"></script>
    
<TITLE FONT COLOR=#E0E0E0>AUTOMATION TEST EXECUTION RESULTS</TITLE>
<style>#table1{	width : 320px;	float : left;}#table2{	width : 320px;	float : left;	margin-left : 100px;}
tr.myStyle1 td{	color : #E0E0E0;font-family : Arial;font-size : 13px;width : 150px;text-align : center;background-color : #153E7E;}
tr.myStyle2 td{	color : black;font-family : Arial;font-size : 13px;width : 150px;text-align : center;background-color : white;}
td.myStyle2 {	color : #E0E0E0;font-family : Arial;font-size : 13px;width : 150px;text-align : center;background-color : #153E7E;}
</style>
</HEAD>
<BODY bgcolor=white id="body">
	<div id="example" class="k-content">
            <div class="chart-wrapper">
                <div id="chart"></div>
            </div>
    </div>
    
<!-- <h2 align=center><FONT COLOR=black FACE=AriaL SIZE=3><b>AUTOMATION TEST EXECUTION RESULTS</b></h2> -->
<table  align=center border=1 cellspacing=1    cellpadding=1 width=20%>
<tr class="myStyle1">
<td><b>AUTOMATION TEST EXECUTION RESULTS</b></td>
</tr>
</table>
<br><br>
<table  border=1 cellspacing=1    cellpadding=1 width=12.5%>
<tr class="myStyle1">
<td><b>SUITE   DETAILS</b></td>
</tr>
</table>

<table  id ="myTable" border=1 cellspacing=1 cellpadding=1 width=100%>
<!--<caption style="text-align:left"><FONT COLOR=maroon FACE=AriaL SIZE=2><b>SUITE DETAILS</b></caption>-->
<tr class="myStyle1">
<td><b>Run Start Time</b></td>
<td><b>Run End Time</b></td>
<td><b>Duration</b></td>
<td><b>Environment</b></td>
<td><b>Suite</b></td>
<td><b>Browser</b></td>
<td><b>Total</b></td>
<td><b>Pass</b></td>
<td><b>Fail</b></td>
</tr>

<tr class="myStyle2">
<td><b> ${suiteDurationTracker.getStartTime()! " "} </b></td>
<td><b> ${suiteDurationTracker.getEndTime()! " "} </b></td>
<td><b> ${suiteDurationTracker.timeTakenToComplete()} </b></td>
<td><b><a href='${envLink}' TARGET="_blank"> ${environment} </a></b></td>
<td><b>${suiteName}</b></td>
<td><b>${browserName}</b></td>

<#assign suitePassCount = 0>
<#assign suiteFailCount = 0>

<#list indexFileData as module>
	<#assign suitePassCount = suitePassCount + module.totalPassCount>
	<#assign suiteFailCount = suiteFailCount + module.totalFailCount>
</#list>

<td><b> ${suitePassCount+suiteFailCount} </b></td>
<td><b> ${suitePassCount} <b></td>
<td><b> ${suiteFailCount} </b></td>
</tr>
</table>


<input type="button" onclick="getCount()" value="View Pie Chart" style="float: right;>

<p align="right">
<a id="dlink"  style="display:none;"></a>
<input type="button" onclick="tableToExcel('body', 'Automation Run Report','Automation_Run_Report.xls')" value="Export to Excel">
</p>

<table  border=1 cellspacing=1    cellpadding=1 width=20%>
<tr class="myStyle1">
<td><b>MODULES   DETAILS</b></td>
</tr>
</table>


<table  border=1 cellspacing=1    cellpadding=1 width=100%>
<!--<caption style="text-align:left"><FONT COLOR=maroon FACE=AriaL SIZE=2><b>MODULES  DETAILS</b></caption>-->
<tr class="myStyle1">
<td><b>MODULE NAME</b></td>
<td><b>MANUAL COUNT</b></td>
<td><b>Total</b></td>
<td><b>Pass</b></td>
<td><b>Fail</b></td>
<td><b>Start Time</b></td>
<td><b>End Time</b></td>
<td><b>Duration</b></td>
</tr>

<#list indexFileData as module>
<tr>
<td width=15% align=center ><FONT COLOR=blue FACE= Arial  SIZE=2><b><a href='${module.moduleName}.html' TARGET="_blank"> ${module.moduleName} </a></b></td>
<td width=5% align=center ><FONT COLOR=black FACE= Arial  SIZE=2><b>${module.totalManualMappingCount}</b></td>
<td width=10% align=center ><FONT COLOR=black FACE= Arial  SIZE=2><b>${module.totalPassCount+module.totalFailCount} </b></td>
<td width=10% align=center ><#if module.totalPassCount &gt; 0> <FONT  COLOR=green >  <#else> <FONT  COLOR=black > </#if><b>${module.totalPassCount}</b></FONT></td>
<td width=10% align=center ><#if module.totalFailCount &gt; 0> <FONT  COLOR=red >  <#else> <FONT  COLOR=black > </#if><b>${module.totalFailCount}</b></FONT></td>
<td width=20% align=center ><FONT COLOR=black FACE= Arial  SIZE=2><b>${module.durationTracker.getStartTime()!" "}</b></td>
<td width=20% align=center ><FONT COLOR=black FACE= Arial  SIZE=2><b>${module.durationTracker.getEndTime()! " "}</b></td>
<td width=10% align=center ><FONT COLOR=black FACE= Arial  SIZE=2><b>${module.durationTracker.timeTakenToComplete()! " "} </b></td>
</tr>
</#list>
</table>

</BODY>
</HTML>