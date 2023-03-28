# Cron

## Demo
![Demo](https://s10.gifyu.com/images/demo29e276dc8bb87fad.gif)

## Tech Used
- Language: Kotlin
- IDE: IntelliJ

## How to Run
The compiled package can be found in the [/release](https://github.com/LPirro/cron-task/tree/main/release) directory of this repository along with the configuration file:
- cron-1.0-SNAPSHOT
- input.txt

To run the program you need to fire the following command
```
cat input.txt | java -jar <compiled_package_jar> <simulated_current_time>
```
Example: 
```
cat input.txt | java -jar cron-1.0-SNAPSHOT.jar 11:00
```

## How to build
To build and compile the .jar package you can use the following gradle script command I've created:
```
./gradlew jar
```
The output will be created under the /build/libs folder

## What can be improved
I think that there are areas where the code can be improved, for example: 
- Move the parser and the logic for getting the next run into dedicated classes in order to improve readability.
- Unit Test coverage for the core functionality 
- Improve error handling (eg: when the config file is not present or has a wrong format)
- Code Documentation