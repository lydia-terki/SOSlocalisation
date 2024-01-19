## Organisation  

Regarding the organization within the collective, we have established a structured methodology, as follows:  

* We have implemented a GIT repository.  
* We conducted consultations to develop a central idea for our project.  
* Faced with various projects and exams demanding our attention, we initiated a Proof of Concept (POC) to  
  promptly start and build strong foundations.  
* Concerning the distribution of responsibilities within the group:
   * Lydia focused on generating CSV files, as well as writing documentation and the README file (with Sélim).
   * Rayan took charge of developing the two CSV file parsing functions.  
   * Sélim led the design of the algorithm for assigning events to hospitals, taking certain constraints into account.  
   * Timothée is responsible for implementing tests exploiting the features of ZIOStream, allowing  
     the generation of events.


## Objective

The application serves as a hospital server responsible for handling emergency events. 
The primary goal of our application is to reduce the number of emergency calls made to the ambulance service each day. 
Additionally, we aim to streamline the transmission of information from patients to emergency services.

When a patient uses the front-end application and provides details about their emergency, the back-end application will 
identify the nearest hospital based on the patient's location and transmit the relevant information. 
The messages received by the application would be in the form of events, specifying the type of emergency, the patient's position, the patient's name, and the patient's location.

As a result, a user interface would be presented to users, allowing them to fill out a form and 
find the nearest hospital based on their current position. 
  
## Use Case Presentation  
  
The application starts with the Main entry point.  

The prerequisites for launching the application are as follows:  
  
* a CSV file containing hospital information.  
* a CSV file containing event information.  
* To generate these CSV files, use the CsvGenerator entry point. (It is also possible to modify the number of generated
events and hospitals by changing the corresponding constants.) 
  
Once the application is launched, it will read the CSV files and parse them to create a list of hospitals and events. 
These are then used to generate a list of hospitals with associated events.  

The purpose of this list is to be able to associate an event with a hospital based on the distance between them.  
The application will then prompt event assignments gradually, indicating the characteristics of the event and the hospital.  
  
## Instructions    
  
The application is launched via the Main entry point.  
To test the application, use sbt test to run the corresponding unit tests.  
  
## Important Decisions Made  
  
We decided to use CSV files to store hospital and event information.  
This allows an easiest generation of test files and the ability to modify them manually if necessary.  
  
## Documentation  
  
The documentation is available in the application's source code files above the relevant functions.  