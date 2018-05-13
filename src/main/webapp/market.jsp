<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Market page</title>
</head>
<body>
    <form action="/market" method="post">
        <div>
            <p>Sender private key</p>
            <input size="60" type="text" name="sender" />
        </div>
        <div>
            <p>Contract id (address)</p>
            <input size="37" type="text" name="contractId" />
        </div>
        <div>
            <p>Goods uuid</p>
            <input size="40" type="text" name="uuid" />
        </div>
        <div>
            <p></p>
            <input type="submit" name="buttonType" value="Buy">
            <input type="submit" name="buttonType" value="Confirm">
        </div>
    </form>
    <div>
        <pre>${market}</pre>
    </div>
</body>
</html>