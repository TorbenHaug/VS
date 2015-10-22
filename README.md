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
