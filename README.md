pappl_bioinfo
=============
Objective : Use the data from the database hipathDB to create files for Cytoscape and for Process Hitting

Progress of the project :
For the moment, the program creates a graph and then writes Cytoscape and PH files, for a pathway or 1 of the 4 databases.

There is not interface yet, so you have to change the parameters (path of the database, login, password, n° of the database and pathway to use) in the code, at the beginning of the main() in the MainTest class (lines 17 to 22).

There may be some inexactitudes in some pathways of base 1 (KEGG), because some nodes cannot be found in the database.

NB : After installing the HipathDB database on your computer, we recommend you run the "script_ajout_index.sql" script to make the SQL requests run much faster.

DEPENDENCIES

JGraphT
code, jars and doc downloadable : http://jgrapht.org/
(only the jgrapht-core-0.9.0.jar is needed to execute the program)

JDBC Driver for MySQL
downloadable : https://dev.mysql.com/downloads/connector/j/