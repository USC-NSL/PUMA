# PUMA
*Programmable UI-Automation Framework for Dynamic App Analysis*

## Project Details
PUMA was developed by Shuai Hao in collaboration with Bin Liu, William G.J. Halfond, Ramesh Govindan at [USC](http://www.usc.edu) and Suman Nath at MSR. A thorough description of PUMA can be found at the Networked Systems Lab [project page for PUMA](http://nsl.cs.usc.edu/Projects/PUMA). Any technical questions can be directed to shuai.hao@gmail.com.

## Running PUMA
First of all, environment variables like ```JAVA_HOME``` and ```ANDROID_HOME``` should be set up properly.

To run the "app under study", you need to find out the package name and text label for the app. e.g. ```com.abc.apk``` will have ```com.abc``` as package name. "label" will be the text shown after installing it on phone.

- replace ```app.info``` file with above information in two lines
- ```./setup-phone.sh``` // it will list a file "haos"
- ```./run.sh``` // start monkey execution

Note that PUMA is currently instantiated for Android platform and only tested on Ubuntu machine (12.04), Android 4.3. Users may require proper Android development environment setup before using PUMA. We are adding some automated procedure to check such environment before running PUMA.
