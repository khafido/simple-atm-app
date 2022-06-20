<h1>ATM Simulation Console App</h1>
<p>Very simple ATM app</p>
<h3>User able to</h3>
<ol>
    <li>Withdraw money</li>
    <li>Transfer money</li>
    <li>Deposit money</li>
    <li>Check balance</li>
    <li>See transaction history</li>
</ol>

<p>Default accounts can be found in <code>resources/accounts.csv</code></p>
<p>Formatted as: <small>[Name,PIN,Balance,Account Number]</small><p>
<p>I.e.</e>
<p>Carree,123456,798,776643</p>
<p>Merrie,844831,891,774921</p>
<p>Caressa,968661,604,777106</p>
<p>...</p>

<h5>Or pass a CSV file as argument</h5>

<h3>Run</h3>
<code>mvn clean package</code>
<br>
<br>
<code>java -jar target/simple-atm-app.jar [path-to-csv]</code>
