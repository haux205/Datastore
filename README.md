# Datastore - Freshworks Assignment 

## Envirionment
Operating System- Mac OS Catalina and above. 
Java JDK- 15. 

## Setup and initialisation
 The class must be initialised by running `DataStore obj= new DataStore();`.  
 When running first time `obj.setup()` must be called to setup the directories **[see note before running setup()**].  
 After setting up the directories `obj.createFile()` must be called to create a new data store.  
 `obj.selectStore` can be used to switch between mulitple datastores in the default directory. 
 
 **Note: Default path has been set to `/Users/username/Desktop/jav/`(where jav is the folder in which the data store file is present).The user has to change 'username' to his corresponding user name in the code**. 
 **If required the user can set the default path (The folder in which the data store files are present) to his/her preference**. 
 **The variable to change is `private static string defpath` in the `DataStore` class**
 
## Performing Operations in the DataStore
 Basic operatios can be performed using the following method calls.  
 -`void writeData(String key, JSONObject value, String time)` - Write data for a given key JSON object and time [Enter '0' if time-to-live property is not required].  
 -`JSONObject readData(String val)` - Read data for a given key.  
 -`void deleteData(String val)` - Delete data for a given key value.   
 
 ## Datastore file format
 Every store is a txt file in which each record(key-value pair) is stored line by line. 
 Line 1 of the file always has '0'. It represents a simple mutex lock to prevent other processes from accessing the store when it is already in access.  
 Every process when accessing the file sets line 1 to '1'. So when another process or thread accesses the same file and finds '1' in line 1, it is not allowed to access the file.  
 After completion of the operation the process removes its access by changing line 1 to '0'. 
 Therefore it maintains data consistency and establishes thread-safety. 
 
 
 
