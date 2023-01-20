* Some documentation about circuitbreakers by Resilience4J: https://resilience4j.readme.io/docs/circuitbreaker

States of a circuitbreak: <br />
![img.png](img.png)

---------------------------
When running the Spring application, the request to see the endpoint protected by circuit break is: http://localhost:8080/api/circuit-breaker <br /> 
The logic behind this protection is: the first 5 requests will give error 500 because de downstream service 
is broken, moving the circuitbreak to the OPEN state. In this state the browser will receive status code 503. 
Then, after some seconds(~3) the circuit will move to half_open which will allow 3 more failed requests 
than move to OPEN state again. <br /> 
In a real scenario, the downstream service would recover eventually and when the calls succeed to the 
downstream service (after recovering from the failure threshold), the circuitbreak will move to CLOSED state.