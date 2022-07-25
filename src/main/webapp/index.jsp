<%@ page contentType="text/html;" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="webjars/bootstrap/5.1.3/css/bootstrap.min.css">

<div class="container">
    <div class="row">
        <div class="col my-5">
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col-md-2">
            <div class="card">
                <div class="card-body">
                    <p class="card-text">Get your cash.</p>
                    <a class="btn btn-primary" href="/withdraw">Withdraw</a>
                </div>
            </div>
        </div>
        <div class="col-md-2">
            <div class="card">
                <div class="card-body">
                    <p class="card-text">Send money to family or friends.</p>
                    <button class="btn btn-primary">Transfer</button>
                </div>
            </div>
        </div>
        <div class="col-md-2">
            <div class="card">
                <div class="card-body">
                    <p class="card-text">See your transaction history.</p>
                    <button class="btn btn-primary">Activities</button>
                </div>
            </div>
        </div>
        <div class="col-md-2">
            <div class="card">
                <div class="card-body">
                    <p class="card-text">Quit ATM</p>
                    <button class="btn btn-primary">Logout</button>
                </div>
            </div>
        </div>
        <span id="spinner" class="spinner-grow" hidden="true"></span>
    </div>
</div>