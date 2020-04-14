# CQRS-ES-Examples

To run the project, you need maven 3.6.3+(Run mvn -version to check), this can be checked by giving the following command from the root project directory. 

Run the following command:
**mvn lagom:runAll**

Once the server is up, you can run the command:
**curl -i http://127.0.0.1:9000/api/product/test**

A simple product can be posted via REST endpoint

**curl -H "Content-Type: application/json" -X POST -d '{"message": "Hi"}' http://127.0.0.1:9000/api/product/132**



