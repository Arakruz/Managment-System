## Milestone 1 ##

Project Description: 

The domain of the application will lie primarily in living quarters management (examples would include apartments and condos). Things that our database will aim to address are tracking the rooms that people are currently renting, maintenance requests, tools and supplies on hand, and available staff. In doing so, we can try to demonstrate what a database for a communal property would look like and contain (within reason to the scope of CPSC 304). 

### Database Specifications: ###

Ideally, our database will aim to provide information that property managers and maintainers will need to keep operations running smoothly. This would include (but is not limited to) logging and referencing maintenance requests, current renters in their respective units, mail services and tracking of such, supplies on hand, and room statuses. Authorized people using our database will be able to view this data and add to the database.

### Application Platform: ###

For this project, we currently intend to use the department Oracle database as it seems like the simplest option (especially for students without a background in database management and development). As for the programming language, we are planning to use Java so as to have something everyone is familiar with. Additionally, this allows us to get support from the CPSC 304 teaching team when necessary and can make the process of development a lot easier than if we use tech stacks not covered in class. 

### Extra Comments: ###

As per the milestone document, we may revisit the tech stack as we learn more in tutorials and how different languages, tools and libraries fit into our project

### Assumptions made for ER Diagram: ###

All renters are different people (not all the names need be unique, only the SIN)
All room numbers are unique, as are maintenance requests
The buildings in which rooms are in are also unique (they have a unique way of numbering them for different buildings like different identifiers)
Staff members may have the same name but all have a unique staff ID
The front desk will receive packages and mail one at a time 
All mail will have a unique mail ID and be sent to unique room numbers and will be addressed to a specific tenant
Equipment is uniquely identified by their equipment ID
Multiple equipments can be loaned to the same person, and there can several of the same equipment
Both tenants and staff are able to borrow and return equipments on some specified date
Authorized users can add maintenance requests and new packages for residents
Front desk has a unique address with some member of staff working there 
Staff ‘name’ attribute should not be unique, staff members can have the same name 
Requests have to be narrowed down to a specific room in a specific building to go in and fix so we have building, room, and request id all underlined. This allows for easier tracking per issue

<img width="626" alt="image" src="https://media.github.students.cs.ubc.ca/user/15265/files/01063e96-4549-43d9-92ca-8a6a8faf7cf9">

