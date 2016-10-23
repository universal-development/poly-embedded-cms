<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>

</head>

<body>
<h1>Poly</h1>
<h1>View ${view}</h1>
<hr/>
Categories: <br/>
<c:forEach var="category" items="${categories}" >
    ${category}
</c:forEach>

<hr/>


${poly} <br/>
data: ${poly.data} <br/>
jsonData: ${poly.jsonData} <br/>

</body>

</html>