<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<title>RESTHub - Authentication</title>
        <link rel="stylesheet" href="../css/graphikawwa/k-reset.css" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="../css/graphikawwa/k-structure.css" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="../css/graphikawwa/k-theme2.css" type="text/css" media="screen, projection">
 		<link rel="stylesheet" href="../lib/themes/base/jquery-ui.css" type="text/css">
        <link rel="stylesheet" href="../css/style.css" type="text/css" media="screen, projection">
      
		<!-- Javascript -->
	 	<script data-main="./app" src="../lib/jquery.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<div id="container">
	    	<header id="header"><span id="title">RESTHub</span></header>
		    <div id="content">
			    <div id="wrapper">
					<div id="formLogin">
						<form action="/identity/api/authorize/authenticate" method="POST">
							<div><label>User name:</label><input type="text" name="username"/></div>
							<div><label>Password:</label><input type="password" name="password"/></div>
							<input type="hidden" name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>"/>
							<input type="hidden" name="state" value="<%= request.getParameter("state") %>"/>
							<div class="hCenter"><input type="submit" value="Connect !"/></div>
						</form>
					</div>  
			    </div>
		    </div>
	    	<footer id="footer"></footer>
    	</div>
	</body>
</html>