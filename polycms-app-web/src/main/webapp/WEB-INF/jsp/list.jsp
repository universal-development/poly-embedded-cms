<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>

</head>

<body>
<h1>Page ${view}</h1>
<hr/>
Categories: <br/>
<c:forEach var="category" items="${categories}" >
    ${category}
</c:forEach>

<hr/>
<h2>Tags</h2>
<a href="/tags">Tags</a>

<c:forEach var="tag" items="${tags}" >
    ${tag}
</c:forEach>


<hr/>


Items: <br/>

<c:forEach var="item" items="${items}" >
    <a href="/${item._id}">${item.label}</a> <br/>
    ${item} <br/>
    ${item.tags} <br/>
</c:forEach>


<c:if test="${view== 'tags'}"> <!-- if customer is anonymous-->
    <h2>Tags</h2>

<c:forEach var="tag" items="${tags}" >
    ${tag}
</c:forEach>

</c:if>

<hr/>

<a href="${backPage}"> Back</a>
<a href="${nextPage}">Next</a>

<c:forEach var="page" items="${pages}" >
    <a href="${page.page}" >${page.pageId}</a>
</c:forEach>

</body>

</html>