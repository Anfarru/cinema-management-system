A company runs a chain of cinemas across several cities in Bulgaria: Sofia, Plovdiv, Varna and Aprilci.
In Sofia there are 4 cinemas, in Plovdiv - 2, in Varna - 2 and in Aprilci - 1. Each of the cinemas has its name and address and contains multiple rooms. A room has multiple seats organized in rows and seat numbers.

Endpoints provided for managing the cinemas:
* Listing the cinemas (all of them and by city)
* Listing the rooms for cinemas along with room numbers and seat capacity
* Scheduling movies that will be broadcasted in a room
* Listing movies broadcasted in a room for a given period
* Listing available and taken seats in a hall for a projection
* Reserving a seat for a given movie projection in a room of a cinema

Technical requirements:
* The reserve seat endpoint should be reactive, the rest may be simple REST
* The system should handle gracefully the situation when two users try to simultaneously book the same seat: the second one should be informed that the seat is already booked and asked to book another one
* The list of cinemas and rooms as well as the list of the movies should be cached. The cache should be invalidated upon updating
* An actual database (like Postgres) should be used, running in a docker container
* The test should not mock the database layer and should use the same DB type as the original code (with test containers)

Swagger documentation link (requires project to be running): http://localhost:8080/swagger-ui.html
