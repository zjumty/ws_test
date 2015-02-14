<!DOCTYPE html PUBLIC 
	"-//W3C//DTD XHTML 1.1 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Index</title>
</head>
<body>
   <table>
       <#list fooList as foo>
           <tr>
               <td>${foo.count}</td>
               <td>${foo.name}</td>
               <td><#if foo.gender>male<#else>female</#if></td>
           </tr>
       </#list>
   </table>
</body>
</html>
	