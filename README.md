pappl_bioinfo
=============
Objective : Use the data from the database hipathDB to create files for Cytoscape and for Process Hitting

Progress of the project :
For the moment, the program creates a graph and then writes Cytoscape files, for a pathway or 1 of the 4 databases.
Process Hitting is not working and ph files are not created right now.
There is not interface yet, so you have to change the parameters (path of the database, login, password, n° of the database and pathway to use) in the code, at the beginning of the main() in the MainTest class.

There may be some inexactitudes in some pathways of base 1 (KEGG), because some nodes cannot be found in the database.

DEPENDENCIES

JGraphT
code, jars and doc downloadable : http://jgrapht.org/
(only the jgrapht-core-0.9.0.jar is needed to execute the program)

JDBC Driver for MySQL
downloadable : https://dev.mysql.com/downloads/connector/j/