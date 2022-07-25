<%@ page import="org.arkaan.simpleatm.dto.response.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <title>Withdraw</title>
</head>

<link rel="stylesheet" href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" />

<div class="container">
    <div class="row">
        <div class="col my-5">
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col-4">
            <h5>Hello, ${account.getName()}!</h5>
            <p>Choose your withdraw amount</p>
            <form class="form">
                <c:forEach var="amount" items="${AMOUNT_OPTIONS}">
	                <div class="form-check my-4">
	                    <input type="radio" class="form-check-input" name="amount_input" id="${amount}" ${amount == 10 ? "checked" : ""}/>
	                    <label class="form-check-label" for="${amount}">$${amount}</label>
	                </div>
                </c:forEach>
            </form>
            <div class="row mt-5 justify-content-between">
                <div class="col d-grid">
                    <button id="btn-back" class="btn btn-warning">Back</button>
                </div>
	            <div class="col d-grid">
	                <button id="btn-withdraw" class="btn btn-success">Withdraw</button>
	            </div>
            </div>
        </div>
    </div>
</div>

<script src="/webjars/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
$(() => {
	$("#btn-back").click(() => {
		document.location.replace("/")
	})
	
    function disableForm(isShow) {
        $(".form > input, button").prop("disabled", isShow)
    }
    
    function showLoading(isLoading) {
        $("#spinner").prop("hidden", !isLoading)
    }
})
</script>