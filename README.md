## Organisation  
Regarding the organization within the collective, we have established a structured methodology, as follows:  

* We have implemented a GIT repository.  
* We conducted consultations to develop a central idea for our project.  
* Faced with various projects and exams demanding our attention, we initiated a Proof of Concept (POC) to  
  promptly start and build strong foundations.  
* Concerning the distribution of responsibilities within the group:  
 * Lydia focused on generating CSV files, as well as writing documentation and the README file.  
 * Rayan took charge of developing the two CSV file parsing functions.  
 * Sélim led the design of the algorithm for assigning events to hospitals, taking certain constraints into account.  
 * Timothée is responsible for implementing tests exploiting the features of ZIOStream, allowing  
   the generation of events.
  
## Objective  
  
Git repository containing your code with appropriate commits and a ReadMe file providing a presentation of use cases, a functional diagram,  
instructions on how to run and test your application, as well as decisions made  
(libraries, data structure(s), algorithm, performance, ...).  
  
The application corresponds to a hospital server responsible for handling emergency events.  
The goal of our application is to reduce the calls made to emergency services every day.  
It also aims to simplify the transmission of information by patients to emergency services.  
Thus, when a patient uses the front-end application by indicating the fields corresponding to their emergency,  
the back-end application will find the nearest hospital to their location and transmit the information.  
The messages received by the application would be in the form of events indicating the type of emergency, the patient's position,  
the patient's name, and the patient's location.  
  
Therefore, an interface would be presented to users where they could fill out  
a form and find the nearest hospital to their location.  
  
## Use Case Presentation  
  
The application starts with the Main entry point.  
The prerequisites for launching the application are as follows:  
  
A CSV file containing hospital information.  
A CSV file containing event information.  
To generate these CSV files, use the CsvGenerator entry point.  
It is also possible to modify the number of generated events and hospitals by changing the corresponding constants.  
  
Once the application is launched, it will read the CSV files and parse them to create a list of hospitals and events.  
These are then used to generate a list of hospitals with associated events.  
The purpose of this list is to be able to associate an event with a hospital based on the distance between them.  
The application will then prompt event assignments gradually, indicating the characteristics of the event and the hospital.  
  
## Instructions    
  
The application is launched via the Main entry point.  
To test the application, use sbt test to run the corresponding unit tests.  
  
## Important Decisions Made  
  
We decided to use CSV files to store hospital and event information.  
This allows for easy generation of test files and the ability to modify them manually if necessary.  
  
## Documentation  
  
Documentation is available in the application's source code files above the relevant functions.  