# SC2002_CAMS
Java Project for a Camp Application and Management System to demonstrate OOP Principles.

## See the following directories for the relevant info.
- data
  - Within here you can find the CSV datastore, and the sample student and staff files given.
- docs
  - This folder contains the generated java docs. View index.html in browser to browse.
- src
  - This folder contains all the source java code.
  - It is split up into packages, such as boundary,control,entity.
  - util contains logical classes that do not fit into control
  - CAMSApp.java is the object created in Main.java.
  - Main.java is the main function and hence the entry point.
- uml_diagram
  - This folder contains the UML class diagrams drawn using draw.io 

## Running the app:
- We developed on VSCode as our environment. Install Java Extension Pack by Microsoft and simply run the Main.java file with F5.
- Otherwise, use javac to compile all .java files and run the generated Main.class.

## About CSV DataStore
- Meant to abstract a SQL / relational database
- Uses our own Serializable (SerializeToCSV) interface implementation for learning.
- Stored in data/
- sample/ contains given sample files for staff and student
- users/ contains generated staff and student csvs
- camps/ contains generated camps, enquiries, suggestions csvs
- CSVs are loaded into memory on initalise, and updated in memory
- CSVs are only updated on app terminate, with some exceptions (changing user password)