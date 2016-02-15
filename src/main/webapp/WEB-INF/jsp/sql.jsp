<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<h1>Create SQL for request</h1>

<c:url var="saveUrl" value="/javarush/main/users/setSQL"/>
<form:form modelAttribute="userSQLAttribute" method="POST" action="${saveUrl}">
    <table>
        <tr>
            <td><form:label path="firstName">First Name:</form:label></td>
            <td><form:input path="firstName"></form:input></td>
        </tr>
    </table>

    <input type="submit" value="Save"/>
</form:form>

</body>
</html>
