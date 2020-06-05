## Tool for bulk validation of metadata for INSPIRE Monitoring & Reporting
This software tool allows for validation of large numbers of metadata records using the API of the [INSPIRE Reference Validator](https://inspire.ec.europa.eu/validator/about/). It was built with [Pentaho Data Integration Community Edition](https://community.hitachivantara.com/s/article/data-integration-kettle) platform which is required to use it.

### Prerequisites:
- One or more instances of [INSPIRE Reference Validator](https://github.com/inspire-eu-validation/community/releases/latest).
- Pentaho Data Integration (PDI) Community Edition (CE), suggested PDI CE version is [9.0](https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/client-tools/pdi-ce-9.0.0.0-423.zip/download) or 8.2, (8.3 suffers from JSON Input step performance deterioration and is not recommended).
- Apache HttpClient components [4.5.12](https://downloads.apache.org/httpcomponents/httpclient/binary/httpcomponents-client-4.5.12-bin.zip).

### Installation:
- Unzip PDI,
- copy all *.jar* files from Apache HttpClient to PDI *lib* folder,
- copy *inspire-validator.jar* to PDI *lib* folder,
- in *validate.bat* insert the path to your PDI *data-integration* folder.

### Configuration:
In *config.properties* update the following items:
- *endpoint* - endpoint id, used to create file- and folder- names [use only characters valid for a filename],
- *source_folder* - folder where source metadata are located (with subfolders) [use forward slashes "/" in the path],
- *results_folder* - folder where results will be written [use forward slashes "/" in the path],
- *source_suffix* - source metadata suffix, used to filter the files to validate
- *validator_nodes* - number of validator instances to use, *validator_url_X* needs to be provided for each instance,
- *validator_url_X* - URLs for each validator instance,
- *queue_max_size* - maximum number of test runs that can be run in parallel on one validator instance.

### Usage:
Run *validate.bat* script, it will read all files with given suffix in the source folder (and subfolders), validate each file using the validator instances and save validation reports and result files in the results folder.  
When the transformation is run for the same endpoint again, it will continue processing source files that were not processed before, hence are not included in results CSV file. To re-validate an endpoint that was validated before, the CSV file needs to be renamed or moved out of the results folder.  
Alternatively, the procedure can be run from the PDI user interface (Spoon) which provides more control and feedback. For this purpose run *Spoon.bat*, open and run *validation.kjb* job.
