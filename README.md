## Bulk processing tool for the INSPIRE Reference Validator
This software tool allows for validation of large numbers of metadata records using the API of the [INSPIRE Reference Validator](https://inspire.ec.europa.eu/validator/about/). It was developed to support INSPIRE Monitoring & Reporting activities. The tool was built with [Pentaho Data Integration] platform which is required to use it.

### Prerequisites
- One or more instances of **INSPIRE Reference Validator** [latest release](https://github.com/inspire-eu-validation/community/releases/latest). 
- JDK Version 11 (or newer) (https://jdk.java.net/archive/)
- Pentaho Data Integration Community (PDI) v.10.2.0.0-222 (https://pentaho.com/pentaho-developer-edition/ you have to register yourself in order to download it)
- Apache HttpClient  v.4.5.14 (https://hc.apache.org/downloads.cgi binary tar.gz)
- Python 3

### Installation 
- Set JAVA_HOME variable in order to point to the jdk dowloaded before 
- Unzip PDI folder 
- Copy the inspire-validator.jar in the lib folder of the PDI.
- Unzip HttpClient file and copy from the lib folder the httpmime-4.5.14.jar in the lib folder of the PDI.
- Launch from the Terminal the spoon.sh (Linux env) or spoon.bat (Windows env) script in order to open the Tool (don't worry if you obtain some warning regarding a library to install: it is not strictly required).


### Configuration
In [*pdi/config.properties*](pdi/config.properties) update the following items:
- `endpoint` - endpoint id, used to create file- and folder- names [use only characters valid for a filename],
- `source_folder` - folder where source metadata are located (including subfolders) [use forward slashes "/" in the path, also in Windows env],
- `results_folder` - folder where results will be written [use forward slashes "/" in the path, also in Windows env],
- `source_suffix` - source metadata files suffix, used to filter the files to validate,
- `validator_nodes` - number of validator instances to use, `validator_url_X` needs to be provided for each instance,
- `validator_url_X` - URLs for each validator instance, up to "/v2/" [*http://.../v2/*],
- `authorization_token` - authorization token to include in the header of "TestRuns" validator API POST request,
- `queue_max_size` - maximum number of test runs that can be run in parallel on each validator instance.

### Usage
- On your local machine create a new dedicated folder, called for example 'BULK_VALIDATOR' and two sub-folder 'INPUT' and 'OUTPUT'. 

- In the INPUT folder put the folder and the files of the countries used for the validation process.

- Open from the scripts folder, the file 'Job asier.kjb' inside the folder InspireTeam-scripts and modify the variables that contain link to other files with your local path (3 occurrences) (In order to find them, try so search '/home/user/Documents').

- From spoon, open the process 'Job asier.ktr' from the InspireTeam-scripts folder (no input parameters have to be inserted because the process reads them from the config.properties). 

- At the end of the process, check if inside the OUTPUT folder, a new folder with the country code as name has been created. It should contain some files. 

- If in the OUTPUT folder a new folder named 'resteting-endpoint' and 'testout_endpoint' have been created, some errors 3.6 are present.

- (If this folder is not present, go to step (*))

- Launch again the process 'Job asier.ktr' modifying the config.properties file putting as source_folder the restesting folder and as results_folder a folder named with the endpoint in the retesting folder. 

- Go to 'monitoring-bulk-validation-tool/saparated_failed', open a terminal and launch this command:
  
  python3 intersect.py /home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/<endpoint>/ /home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/retesting-<endpoint>/ /home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/testout_<endpoint>.csv /home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/<endpoint>.csv
  
  where <countrycode> and <endpoint> will be substituted by the country code and the endpoint of the country.

  In this command, there are four variables (starting from '/home') that are:
  1) report total location (folder where html and json file created are)
  2) report error location (new retesting folder)
  3) file errors (new testout file created)
  4) file total (csv created after the first process)

  Repeat these last two steps for 3 times in order to check multiples time metadata that give 3.6 errors.

- (*) Open with spoon the process 'summarize_error.ktr' from the InspireTeam-scripts folder. In the input parameters, configure:
  [*results_folder*] with path of the csv called with the endpoint generated from the process before.
  [*path_folder*] with the path that contains all the files.

  For example:
  results_folder=/home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/<endpoint>.csv
  path_folder=/home/<user>/Documents/BULK_VALIDATION/OUTPUT/<coutrycode>/<endpoint>/

- At the end of the process, verify that in the results_folder has been created the file 'summarize_error.xlsx'. 

- Open the process 'filter_failed_csv_2.ktr' from the InspireTeam-scripts folder. In the input parameters, configure:
  [*in_csv*] with the the same value used for the variable 'results_folder' before.

- At the end of the process, in the OUTPUT folder, the file <endpoint>-error.csv will contain the errors. It this file is not present, no errors have been detected.


#### Result files
All result files are saved in *\<results_folder\>*:
1. *\<endpoint\>* - folder where validation reports for each metadata record are saved,
2. *\<endpoint\>.md.json* - source metadata summary,
3. *\<endpoint\>.csv* - validation results for each metadata record, detailed [below](#results-csv-columns),
4. *\<endpoint\>.json* - validation results summary and source metadata summary,
5. *\<endpoint\>.services.zip* - validation reports for service metadata records that failed validation,
6. *\<endpoint\>.dataset.zip* - validation reports for dataset, series, missing and unkown metadata records that failed validation,
7. *validation.csv* - validation results summary and source metadata summary for each validation run.

File 2 is produced only after completed preprocessing of all metadata records.  
Files 4, 5, 6 and 7 are produced/updated only after completed validation of all metadata records.

#### Results CSV columns
- `file_id` - identifies source metadata file and validation reports,
- `md_id` - metadata identifier (from source XML),
- `type` - metadata type (from source XML),
- `result` - validation result,
- `MD5` - metadata file hash value, used to detect duplicates,
- `error_count` - number of failed assertions,
- `errors` - ids of failed assertions.

#### Conformity Indicators
The metadata Conformity Indicators **MDi1.1** and **MDi1.2** can be calculated by dividing the number of passed data sets metadata and the number of passed service metadata found in the validation results summary (JSON file) by, respectively, the total number of available data sets (indicator DSi1.1) and the total number of available services (indicator DSi1.2) retrieved from the Harvest Console (see Article 4 of ID M&R [below](#external-document-references)), i.e.:
```
MDi1.1 = dataset_metadata_passed / DSi1.1
MDi1.2 = service_metadata_passed / DSi1.2
```

### Support
If you experience any issue in the setup and/or use of the software, please open an issue in the [INSPIRE Validator helpdesk](https://github.com/inspire-eu-validation/community/issues/new/choose).


Date: 2024/12/11
