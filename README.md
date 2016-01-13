# VS

## Getting Started

1. Install Maven https://maven.apache.org/
1. Install Docker https://www.docker.com/
1. git clone https://github.com/TorbenHaug/VS.git
1. Start docker deamon
  1. On windows you have to use the docker shell, or export docker-machine env <machinename> --shell=<shellname>
  1. On Linux all exports have to be in your shell
1. Open Folder in your shell
1. Type mvn clean install

## Import to Eclise

1. Install the http://www.eclipse.org/m2e/
1. Goto File->Import->Maven->Existing Maven Project
1. Choose the pom.xml file in the Root-Directory of the Project

## Import to Idea

1. File->Open
1. Choose the pom.xml file in the Root-Directory of the Project

## ServiceRepository config

The names of the services have to be equal in each ApplicationStartup and in the 
function de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository.getComponents

## Set Authentification

In each ApplicationStartup and ApplicationClose and in the function de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository.getService
set variable "base64Creds" to your HAW-credetials (You can do so on https://www.base64encode.org/ with <username>:<password>).

## SetUp Bank

You have to use the bank of https://github.com/Octav14n/VSP.git. 

## Running Services

Start running each service including the bank (see SetUp Bank) except of the frontend. Start frontend when all other services are started and are registered at the serviceRepository

* events: docker run de.haw-hamburg.vs-ws2015.spahl-haug.rest.events 
* games: docker run de.haw-hamburg.vs-ws2015.spahl-haug.rest.games 
* boards: docker run de.haw-hamburg.vs-ws2015.spahl-haug.rest.boards 
* broker: docker run de.haw-hamburg.vs-ws2015.spahl-haug.rest.broker 
* player/frontend: docker run -p <externalPort>:4567 monopolyrwt

