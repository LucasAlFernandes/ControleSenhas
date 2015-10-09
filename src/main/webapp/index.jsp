<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<% 
	if(session.getAttribute("startUpPath") == null)
		session.setAttribute("startUpPath", getServletContext().getRealPath("/"));
	if(session.getAttribute("Menu") == null)
	{
		String Menu = "<script type='text/javascript'>"+
				"function _click(obj)"+
				"{"+						
					"document.getElementById('paginaSolicitada').value = obj.id;"+
					"document.frmPagina.submit();"+
				"}"+			
			"</script>"+
			"<a id='Inicio' onclick='_click(this)'>Inicio</a><br>"+
			"<a id='Login' onclick='_click(this)'>Entrar</a><br>"+
			"<a id='Cadastro' onclick='_click(this)'>Cadastrar</a><br>"+
			"<form id='frmPagina' name='frmPagina' action='Cliente' method='post'>"+
			"<input type='hidden' name='paginaSolicitada' id='paginaSolicitada' value='Cadastrar'>"+ 
			"</form>";
		session.setAttribute("Menu",Menu);
		session.setAttribute("SereverError", "");
		session.setAttribute("Titulo","Inicio");
		session.setAttribute("Pagina","");
	}
	
	if(session.getAttribute("TimeToRefresh") != null)
	{
		try
		{
			//response.setIntHeader("Refresh", (Integer)session.getAttribute("TimeToRefresh"));
		}
		catch (Exception e) 
	    {
	       	session.setAttribute("SereverError", e.getMessage());
		}  
	}
	else
		response.setIntHeader("Refresh", 99999);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
	<head>
		<link rel="stylesheet" type="text/css" href="tema.css">		
	</head>
<body>	
	<div id="header">
		<h1>Fila de espera para consulta com gerentes do banco</h1>
		<h3><%=session.getAttribute("Titulo")%></h3>
	</div>
	
	<div id="nav">
		<%=session.getAttribute("Menu")%>
	</div>
		
	<div id="section">
		<%=session.getAttribute("Pagina")%>
	</div>
	
	<div id="footer">
		Copyright © Desenv Fernandes
	</div>
	<div><%=session.getAttribute("SereverError")%></div>
</body>	
<html> 	