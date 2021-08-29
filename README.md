# tokopedia-scrapper

## How to Install

- Make sure the computer already have maven. If not, please follow [this instruction from baeldung](https://www.baeldung.com/install-maven-on-windows-linux-mac)
- In the root directory, please do `mvn install` to download required dependencies 
- Compile the project using command
```mvn clean compile assembly:single```
- Run the app by command 
```java -cp target/tokopedia-scrapper-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.tokopedia.Main```
- You can see the result in file `parse.csv` in folder `target`
