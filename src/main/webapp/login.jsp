<%@ page contentType="text/html;" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="webjars/bootstrap/5.1.3/css/bootstrap.min.css">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-4">
	        <form class="form" id="auth-form">
	            <label for="account-number" class="form-label">Account Number</label>
	            <input id="account-number" class="form-control" type="text" name="account_number">
	            <label for="account-pin" class="form-label">PIN</label>
                <input id="account-pin" class="form-control" type="password" name="account_pin">
                <button id="btn-submit" type="submit" class="btn btn-primary form-control" >Submit</button>
	        </form>
	        <span id="spinner" class="spinner-grow" hidden="true"></span>
        </div>
    </div>
</div>

<script src="/webjars/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
$(() => {
	$("#auth-form").submit((e) => {
		e.preventDefault()
		showLoading(true)
		const accountNumber = $("#account-number").val()
		const accountPin = $("#account-pin").val()
		disableForm(true)
		$.post("/account", { account: accountNumber, pin: accountPin})
		  .done((data) => {
			  window.location.replace("/")
		  })
		  .fail(function(e) {
			  console.log(e.responseText)
		  })
		  .always(() => {
			  disableForm(false)
			  showLoading(false)
		  })
		
	})
	
	function disableForm(isShow) {
		$(".form > input, button").prop("disabled", isShow)
	}
	
	function showLoading(isLoading) {
		$("#spinner").prop("hidden", !isLoading)
	}
})
</script>