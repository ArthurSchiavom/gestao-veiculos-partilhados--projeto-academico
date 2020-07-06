# Ride-Sharing Management

## Authors:
* Arthur Silva - 1180842
* Ivo Macieira - 1170554
* Francisco Andrade - 1181477
* Diogo Silva - 1161752
* José Magalhães - 1180852
* Kevin Sousa - 1180853


# 1. Introduction

This report aims to explain our approach, work methodology and conclusion. 

The application's objective is to support a ride-sharing businesses by managing users, vehicles, parks, trips, invoices and receipts

The development languages used were Java and Oracle SQL*Plus. SQL scripts can be found inside the folder "Modelo Relacional\Script".

# 2. Resolution

## 2.1. The application
Users can pickup vehicles at designated parks and return them at a different parking locations. This application manages users, bicycles, electric scooters, parks and trips.

**Domain Model**

![DOMAIN_MODEL.png](report/modeloDominio.png)

**Relational Model**

![RELATIONAL_MODEL.png](report/ModeloRelacional.png)

## 2.2. Use Cases

![USE_CASES.png](report/UseCaseDiagram1.png)

### 2.2.1. Get the most efficient route between two parks (at least a certain number of interest points)

**Use case diagram**

![SSD.png](report/SDD_GetTheMostEnergiticallyEfficientRouteBetweenTwoPark.png)

**Sequence Diagram**

![SD.png](report/SD_GetTheMostEnergeticallyEfficientRouteBetweenTwoParks2.png))

**Class Diagram**	

![CD.png](report/CD_GetTheMostEnergeticallyEfficientRouteBetweenTwoParks.png)

**Description**

In this use case, the client is able to consult the most efficient route between two parks, passing by a certain number of points of interest. 

### 2.2.2. Fetch free slots at park by type

**Use case diagram**

![SSD.png](report/SSD_fetchFreeSlotsAtParkByType.png)

**Sequence Diagram**

![SD.png](report/SD_fetchFreeSlotsAtParkByType.png))

**Class Diagram**	

![CD.png](report/CD_fetchFreeSlotsAtParkByType.png)

**Description**

In this use case, the client is able to check if a park has free slots to a specified type of vehicle.

### 2.2.3. Get uncapable scooters

**Use case diagram**

![SSD.png](report/SSD_GetUncapableScooters.png)

**Sequence Diagram**

![SD.png](report/SD_getUncapableScooters.png))

**Class Diagram**	

![CD.png](report/CD_GetUncapableScooters.png)

**Description**

In this use case, the administrator is able to get a report stating which scooters, registered in the system, do not have enough autonomy to make a trip with a certain number of kilometers, defined by him.

### 2.2.4. Get the most energetically efficient Route between Two Parks

**Use case diagram**

![SSD.png](report/SDD_GetTheMostEnergeticallyEfficientRouteBetweenTwoParks.png)

**Sequence Diagram**

![SD.png](report/SD_GetTheMostEnergeticallyEfficientRouteBetweenTwoParks.png)

**Class Diagram**	

![CD.png](report/CD_GetTheMostEnergeticallyEfficientRouteBetweenTwoParks2.png)

**Description**

In this use case, the client is able to get the most energetically efficient route between two parks. To calculate the energy that will be spent, information about the client, the vehicle and the paths that he will pass are needed, since the physic calculation needs that information.

### 2.2.5. Get the nearest park

**Use case diagram**

![SSD.png](report/SDD_NearestPark.png)

**Sequence Diagram**

![SD.png](report/SD_NearestPark.png)

**Class Diagram**	

![CD.png](report/CD_NearestPark.png)

**Description**

In this use case, the client is able to get the nearest park to him. If not specified, it is given a nearest park inside the range of 1km.

### 2.2.6. Update park

**Use case diagram**

![SSD.png](report/SSD_updatePark.png)

**Sequence Diagram**

![SD.png](report/SD_updateParkId.png)

![](report/updateParkCapacity.png)
![](report/updateParkInputVoltage.png)
![](report/updateParkInputCurrent.png)
![](report/updateParkDescription.png)

**Class Diagram**	

![CD.png](report/CD_updatePark.png)

**Description**

In this use case, the administrator is able to update the information of a park.

### 2.2.7. Add park

**Use case diagram**

![SSD.png](report/SSD_AddNewPark.png)

**Sequence Diagram**

![SD.png](report/SD_AddNewPark.png)

**Class Diagram**	

![CD.png](report/CD_AddNewPark.png)

**Description**

In this use case, the administrator uses the application (Inserts a park) to register a park into the system.

### 2.2.8. Distance of a park by id

**Use case diagram**
![SSD.png](report/SDD_DistanceOfParkById.png)

**Sequence Diagram**

![SD.png](report/SD_DistanceOfParkById.png)

**Class Diagram**	

![CD.png](report/CD_DistanceOfParkById.png)

**Description**

In this use case, the client can obtain the distance to a given park.

### 2.2.9. Remove a park by id

**Use case diagram**

![SSD.png](report/SSD_DeletePark.png)

**Sequence Diagram**

![SD.png](report/SD_DeleteParkById.png)

**Class Diagram**	

![CD.png](report/CD_deleteParkbyId.png)

**Description**

In this use case, the administrator can remove a park from the system, by providing the id.

### 2.2.10. Load a point of interest

**Use case diagram**
![SSD.png](report/SDD_LoadPOI.png)

**Sequence Diagram**

![SD.png](report/SD_LoadPOI.png)

**Class Diagram**	

![CD.png](report/CD_LoadPoi.png)

**Description**

In this use case, the administrator is able to load a point of interest into the system, by specifying his description, latitude, longitude and altitude.

### 2.2.11. Predict burned calories

**Use case diagram**
![SSD.png](report/SSD_PredictBurntCalories.png)

**Sequence Diagram**

![SD.png](report/SD_PredictCaloriesBurnt.png)

**Class Diagram**	

![CD.png](report/CD_predictBurntCalories.png)

**Description** 
 
In this use case the client can predict the amount of calories burnt between two parks. This will only be possible if the data about the client, the bicycle in use and the start and end parks are specified, since information like the weight and average speed of the client, the frontal area of the bicycle, the kinetic coefficient of the path, etc. This calculation will be made in the class physics calculations, that has a method that calculates the energy spent in joules, and then converts it to calories.


### 2.2.12. Return vehicle to park

**Use case diagram**
![SSD.png](report/SSD_ReturnVehicleToPark.png)

**Sequence Diagram**

![SD.png](report/SD_ReturnVehicleToPark.png)

**Class Diagram**	

![CD.png](report/CD_ReturnVehicleToPark.png)

**Description**

In this use case, the client will be able to return a vehicle to a park, after making a trip. For this situation to succeed, information about the park and the vehicle description is needed. When the vehicle is successfully returned to the park, the client receives an email informing of the situation.

### 2.2.13. Get the shortest route between two parks

**Use case diagram**
![SSD.png](report/SSD_ShortestRouteBeetwenTwoParks.png)

**Sequence Diagram**

![SD.png](report/SD_ShortestRouteBetweenTwoParks.png)

**Class Diagram**	

![CD.png](report/CD_ShortestRouteBetweenTwoParks.png)

**Description**

In this use case, the client can get the shortest path between two parks. The starting and ending park must be specified for the success of this operation, along with the name of the file that will contain the information returned.

### 2.2.14. Filter scooters with autonomy

**Use case diagram**
![SSD.png](report/SSD_filterScootersWithAutonomy.png)

**Sequence Diagram**

![SD.png](report/SD_filterScootersWithAutonomy.png)

**Class Diagram**	

![CD.png](report/CD_filterScootersWithAutonomy.png)

**Description**

In this use case, the client will be able to know the scooters with enough autonomy to make a trip. The scooters must have autonomy to make the distance, plus 10% of the distance.

### 2.2.15. Get List of vehicles not available

**Use case diagram**
![SSD.png](report/SSD_getListOfVehiclesNotAvailable.png)

**Sequence Diagram**

![SD.png](report/SD_getListOfVehiclesNotAvailable.png)

**Class Diagram**	

![CD.png](report/CD_getListOfVehiclesNotAvailable.png)

**Description**

In this use case, the administrator is able to get a list of vehicles that are not available. To get this information, he must specifie a start time and an end time. The system will then verify the vehicles not available in that period of time.

### 2.2.16. Unlock a vehicle

**Use case diagram**
![SSD.png](report/SSD_UnlockVehicle.png)

**Sequence Diagram**

![SD.png](report/SD_UnlockVehicle.png)

**Class Diagram**	

![CD.png](report/CD_UnlockVehicle.png)

**Description**

In this use case, the client will be able to unlock a vehicle. The system requests his username and the vehicle description, so that the client and the vehicle are identified. Having this, if the vehicle is not already unlocked, the operation is a success. 

### 2.2.17. Register a user

**Use case diagram**
![SSD.png](report/SSD_RegisterClient.png)

**Sequence Diagram**

![SD.png](report/SD_RegisterUser.png)

**Class Diagram**	

![CD.png](report/CD_RegisterUser.png)

**Description**

In this use case, a non registered user can register on the system. He must provide information about him (name, email, gender, height, etc). If he does not insert invalid data, the operation is a success, and the person is now registered in the system.

### 2.2.18. Register vehicles

**Use case diagram**

![SSD.png](report/SSD_RegisterVehicles.png)

**Sequence Diagram**

![SD.png](report/SD_RegisterVehicles.png)

**Class Diagram**	

![CD.png](report/CD_RegisterVehicles.png)

**Description**

In this use case, an administrator can register a new vehicle in the system. This vehicles can be bicycles or electrical scooters. Some requested data will be different according to the vehicle type. If the operation is a success, a new vehicle is registered in the system.

### 2.2.19. Vehicles Available at a given park

**Use case diagram**

![SSD.png](report/SSD_VehiclesAvailableAtAGivenPark.png)

**Sequence Diagram**

![SD.png](report/SD_VehiclesAvailableAtAGivenPArk.png)

**Class Diagram**	

![CD.png](report/CD_VehiclesAvailableAtAGivenPark.png)

**Description**

In this use case, a client can consult the vehicles available in a specified park. To inform which park he wants to consult, the client can insert the park id or the coordinates that is located.

## 2.2.20. Retrieve park charging report

**Use case diagram**

![SSD.png](report/SSD_chargingVehicle.png)

**Sequence Diagram**

![SD.png](report/SD_chargingVehicle.png)

**Class Diagram**	

![CD.png](report/CD_chargingVehicle.png)

**Description**

In this use case, the administrator can obtain a report stating the charging status for each vehicle in a park and an estimate projection for how long it would take for each vehicle to reach 100% charge under the existing circumstances. 

## 2.2.21. Retrieve invoice for month

The invoices being issued are relative to the trips made during the given month.
However, initially, we supposed that the invoices being issued were for the trips from the month prior to the one specified. 
Example: The user requests an invoice to be issued for month 5. The application would issue the invoice relative to the trips that happened in month 4.
Reasoning: Realistically, invoices issued on month 5 are relative to the trips that happened during month 4. In other words, in month 5 you pay for month 4.
On the other hand, the last test scenario provided seemed to expect the trips that happened in the same month, since otherwise the invoice wouldn't be associated to any trip.
It was decided that we would opt to have optimal results in the scenario.
To achieve this, following piece of code was removed from InvoiceAPI.issueInvoice:

    if (month == 1)
        month = 12;
    else
        month--;
