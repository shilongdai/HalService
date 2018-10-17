<%--
  Created by IntelliJ IDEA.
  User: sdai
  Date: 10/5/18
  Time: 4:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<title>
		Crawl Page
	</title>
</head>
<body>

<form method="post" action="<c:url value="/submit" />">
	<label for="url">URL</label>
	<input type="text" placeholder="URL to crawl" name="url" id="url">

	<button type="submit">submit</button>
</form>
</body>

</html>
