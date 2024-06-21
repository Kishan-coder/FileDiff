## To Run
#### Docker
1. Download Docker
2. Keep two input files handy in any folder (say Desktop)
3. use cmd to pull image: ```docker pull kishancoder/turebora-file-diff:v1.0```
4. If you named files as T1.csv and T2.csv then directly use cmd to run image: ```docker run -v ~/Desktop/:/data kishancoder/turebora-file-diff:v1.0``` \
OR If not use this cmd: ```docker run -e SYS_PROPS="-Dfile.old=<filename1>.csv -Dfile.new=<filename2>.csv" -v ~/Desktop/:/data kishancoder/turebora-file-diff:v1.0``` \
<Note: here ```~/Desktop/``` is the absolute path to the folder containing input files, keep ``:/data`` part intact>
6. You can see the output file(s) in say Desktop folder only. 
#### jar
1. Git Clone Project
2. Download JDK 17
3. Go to Project
4. Use: ```./gradlew build``` to build jar
5. Switch dir with: ``cd build/libs`` and Create ``data`` folder
6. Copy Input Files to ``data``
7. if Files are named T1.csv and T2.csv then directly run jar in `build/libs`: ``java -jar filediff-1.0.0.jar``
8. Else run jar with: ``java -jar -Dfile.old=<filename1>.csv -Dfile.new=<filename2>.csv filediff-1.0.0.jar  ``  

## File Diff Calculator between 2 User Record Files
# Problem Statement
There are two csv files containing User Data of an Organization, one generated at Time T1 and another generated at Time T2. The csv files contain latest information
about ALL users in the Organization. Let us assume that there is data for 25 different properties for each User record in the two csv files. The goal is to find out at T2
what new user records were added, what user records were modified and what user records were deleted.
Write a program that compares the two files and creates a 3rd file with 26 columns (25 columns containing user-record-data and 26th column that tells us whether
the record is new, or modified or deleted).

# Approaches
Since we need to compare the files:
## Approach 1:
Linearly Scan all records of file 1 (say N count) to search against all records in file 2 (say M count) with T.C = O(M*N) & S.C = O(1)
## Aproach 2:
Store all file 1 records in memory either in Cache or HashMap and iterate all records in file 2 checking if it exists in memory Map: <br>
   2.1 if Yes then check if any attribute's been modified => Changed <br>
       2.1.1 Remove it from Map <br>
   2.2 if No => Added <br>
   2.3 Finally all remaining Map records => Deleted <br><br>
T.C = O(N) S.C = O(M) <br>
An optimstic assumption: N~M
## A Catch
There might be some records in both files which repeats (same ID), a pre-processing step is to remove all such later found duplicate occurences from both these files.
<br> In order to keep track of duplicate records we need to read other file into memory as well.

## Back of the evelope estimation
Max Records Per File = 500,000\
Total Records = 1,000,000\
Max attributes = 26\
Size of An attribute = ~10B\
Size of a record = 10*26 + OverHead Bytes = ~500B\
Size of all records = 500*1,000,000 = 500 MB

Since modern day computers have enough RAM, I assume that the running service won't chock with even ~1 GB memory in-use so I implemented via HashMap.\
A better consideration could be to use Redis as Persistent Cache which would contribute higher latency due to Disk I/O but might be the only solution we file can't be loaded in-memory.

## Following Domain Driven Design
Application is structured into: \
#### Domain: Contains logic to compare files, domain models (VO & Entities), ports/ interfaces, domain exceptions
#### Infrastructure: Bean Configurations and Adapters to domain ports
#### Application: Class mainting driving logic 
#### FailSafe Module: Contains logic to handle errored records, 
possible errors: [Duplicate Records, Incompatibe Files, UnModified Records, Invalid User Records]

##### Built on Spring Boot

### Extra Features Provided
1. Files can have custom headers
2. Errored Records appended in a discarded file with proper error reason and code

