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
<p>I.e.</p>
<p>Carree,123456,798,776643</p>
<p>Merrie,844831,891,774921</p>
<p>Caressa,968661,604,777106</p>
<p>...</p>

<h3>Compile</h3>
<code>mvn clean package spring-boot:repackage</code>
<br>
<h3>Run</h3>
<code>java -jar target/simple-atm-app.jar --DATASOURCE_URL=your_postgresql_jdbc_url --DATASOURCE_USER=your_postgresql_user --DATASOURCE_PASSWORD=your_postgresql_password</code>
