# MSG File Reader

This is a Java application that reads all `.msg` files from a specified folder and prints their content to the console.

## Prerequisites

*   Java 8 or higher
*   Apache Maven

## How to Build

1.  Clone the repository or download the source code.
2.  Open a terminal or command prompt and navigate to the project's root directory.
3.  Run the following command to build the project:

    ```bash
    mvn package
    ```

    This will compile the code and create a JAR file in the `target` directory named `msg-reader-1.0-SNAPSHOT.jar`.

## How to Run

1.  After building the project, you can run the application from the command line.
2.  Use the following command, replacing `<folder_path>` with the actual path to the folder containing your `.msg` files:

    ```bash
    java -jar target/msg-reader-1.0-SNAPSHOT.jar <folder_path>
    ```

### Example

If your `.msg` files are in a folder named `my_messages`, you would run:

```bash
java -jar target/msg-reader-1.0-SNAPSHOT.jar my_messages
```

The application will then print the content of each `.msg` file to the console.

## Logging

This application uses SLF4J for logging. By default, the log level is set to `INFO`. You can change the log level by setting a system property when running the application.

For example, to set the log level to `DEBUG`, use the following command:

```bash
java -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG -jar target/msg-reader-1.0-SNAPSHOT.jar <folder_path>
```

Supported log levels are: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`.
