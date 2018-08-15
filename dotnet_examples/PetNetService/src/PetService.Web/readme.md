To get started with the database just open up a console and type:
-  dotnet ef database update

This will create the database and execute each migration step.

To connect to the database open up Microsoft Sql Server Management Studio and connect to the server name specified in appsettings.json, which happens to be localdb:
    (localdb)\\mssqllocaldb
Use Windows Authentication and you are good to connect.


The application path is configured in appsettings.json. Currently it is set to "/pet-app".
To see the application visit https://localhost:5001/pet-app