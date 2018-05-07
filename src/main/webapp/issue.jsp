<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Issuance page</title>
</head>
<body>
    <form action="/issue" method="post">
        <div>
            <p>Sender private key</p>
            <input size="60" type="text" name="sender" />
        </div>
        <div>
            <p>Contract id (address)</p>
            <input size="37" type="text" name="contractId" />
        </div>
        <div>
            <p>Goods name</p>
            <input size="20" type="text" name="goodsName" />
        </div>
        <div>
            <p>Price</p>
            <input size="20" type="number" name="price" />
        </div>
        <div>
            <p></p>
            <input type="submit" value="Issuance">
        </div>
    </form>
</body>
</html>