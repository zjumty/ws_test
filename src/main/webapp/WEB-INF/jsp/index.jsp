<!DOCTYPE html PUBLIC 
	"-//W3C//DTD XHTML 1.1 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Index</title>
</head>
<body>
   <table>
       <c:forEach items="${fooList}" var="foo">
       <tr>
           <td>${foo.count}</td>
           <td>${foo.name}</td>
           <td><c:choose><c:when test="${foo.gender}">Male</c:when><c:otherwise>Female</c:otherwise></c:choose></td>
       </tr>

       </c:forEach>

   </table>
</body>
</html>
	