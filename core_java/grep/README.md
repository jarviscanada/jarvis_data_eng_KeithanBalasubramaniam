# Introduction
The Java Grep application is the Java implementation of the 'grep' command line function used
in Linux bash terminals. This application recursively searches a root directory for a specified regex
pattern. It finds the corresponding lines and saves them to an output file specified by the user. This application
is packaged using Docker, where user's can pull the image from DockerHub to be implemented. The technologies used to
create this application are as follows:
- Docker
- Java
- Lambda & Stream API
- Maven
- Intellij

# Quick Start
```bash
# Compile the maven project and package the program into a Jar file
mvn clean complie project

# Run the Jar file with the three arguments [regex], [rootDirectory], [outputFile] to complete the grep search
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp [regex] [rootDirectory] [outputFile]

# View the output file of the matched lines
cat [outputFile]
```
# Usage
This application takes three input arguments which are as follows:

|    Argument   |                    Description                    |
|:-------------:|:-------------------------------------------------:|
|     regex     | Regex pattern to be searched for in the directory |
| rootDirectory |        Root directory to recursively search       |
|   outputFile  |      Output file to save the matched lines to     |

#Implementation
## Pseudocode
The Java Grep application has a process method that defines the operations taken place. The pseudocode of the 'process()'
method is as follows:

```
matchedLines[]
for file in listFilesRecursively(rootDir)
    for line in readFile(file)
        if containsPattern(line)
            matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
This application has some performance issues when performed on very large files when using the original implementation, as
it stores every line being read into the memory. However, with the integration of Java Stream and Lambda functionalities, seen in the 
JavaGrepLambdaImp class, this memory issue is no longer prevalent as Java Streams support functional programming such as map-reduce transformations
on collections without storing the data.

# Test
This application was tested manually with several sample files all varying in size in order to compare results and optimize
read and write efficiency. 

# Deployment
The Java Grep application was created as a docker image and pushed to DockerHub allowing easy access for utilization of this application.
To retrieve the docker image and use the application follow the steps outlined below:

```bash
# Pull the image from DockerHub
docker pull keithan/grep

# Run the docker container
docker run --rm -v `pwd` /data:/data -v `pwd` /log:/log keithan/grep .[regex] /data /log/grep.out
```

# Improvement
Some improvements that could have been implemented are as follows:

- Implement a feature to display the filename and line number where the matching regex pattern was found
- Have the application give the user real-time feedback on the search, to inform them if the search was successful 
  or not. This would allow the user to get all the information needed from the Grep application itself and not have to open
  the output file to confirm.
- Implement a feature to display the real-time memory usage of the application and inform the user the time it will approximately
take to complete the search.
  