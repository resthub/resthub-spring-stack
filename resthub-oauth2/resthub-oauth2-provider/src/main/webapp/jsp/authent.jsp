<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head><title>Authentication</title></head>
	<body>
		<h1>Hello</h1>
		<p>You need to authenticate to go further.</p>
		<form action="/api/authorize/authenticate" method="POST">
			<div>User name: <input type="text" name="username"/></div>
			<div>Password: <input type="password" name="password"/></div>
			<input type="hidden" name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>"/>
			<input type="hidden" name="state" value="<%= request.getParameter("state") %>"/>
			<input type="submit" value="Login"/>
		</form>
	</body>
</html>