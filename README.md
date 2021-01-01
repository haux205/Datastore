# Datastore - Freshworks Assignment 

## Envirionment
Operating System- Mac OS Catalina and above. 
Java JDK- 15. 

## Setup and initialisation
 The class must be initialised by running `DataStore obj= new DataStore();`.  
 When running first time `obj.setup()` must be called to setup the directories.  
 After setting up the directories `obj.createFile()` must be called to create a new data store.  
 `obj.selectStore` can be used to switch between mulitple datastores in the default directory.  
 
## Performing Operations in the DataStore
 Basic operatios can be performed using the following method calls.  
 -`void writeData(String key, JSONObject value, String time)` - Write data for a given key JSON object and time [Enter '0' if time-to-live property is not required].  
 -`void readData(String val)` - Read data for a given key.  
 -`void deleteData(String val)` - Delete data for a given key value.   
 
 
 
