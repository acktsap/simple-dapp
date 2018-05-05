<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Definition page</title>
</head>
<body>
    <form action="/def" method="post">
        <div>
            <p>Creator private key</p>
            <input size="60" type="text" name="creator" />
        </div>
        <div>
            <p>Contract id (address)</p>
            <input size="37" type="text" name="contractId" />
        </div>
        <div>
            <p>Definition code</p>
            <textarea rows="40" cols="100" name="code"></textarea>
        </div>
        <div>
            <p></p>
            <input type="submit" value="Request">
        </div>
    </form>
</body>
</html>