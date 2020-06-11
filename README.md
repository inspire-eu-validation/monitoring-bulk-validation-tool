## Bulk processing tool for the INSPIRE Reference Validator
This software tool allows for validation of large numbers of metadata records using the API of the [INSPIRE Reference Validator](https://inspire.ec.europa.eu/validator/about/). It was developed to support INSPIRE Monitoring & Reporting activities. The tool was built with [Pentaho Data Integration Community Edition](https://community.hitachivantara.com/s/article/data-integration-kettle) platform which is required to use it.

### Prerequisites:
- One or more instances of **INSPIRE Reference Validator** [latest release](https://github.com/inspire-eu-validation/community/releases/latest).
- **Pentaho Data Integration** (PDI) **Community Edition** (CE), suggested PDI CE version is [9.0](https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/client-tools/pdi-ce-9.0.0.0-423.zip/download) or 8.2, (8.3 suffers from JSON Input step performance deterioration and is not recommended). In case of slow download click "Problems downloading?" and try an alternative download mirror.
- **Apache HttpClient** components [4.5.12](https://downloads.apache.org/httpcomponents/httpclient/binary/httpcomponents-client-4.5.12-bin.zip).
- Source metadata compiled according to **Technical Guidelines** (TG) version **1.3** or **2.0** and contained in xml files with single metadata record per file. Suggested TG version is 2.0. The tool will classify the TG version according to the procedure outlined [below](#metadata-tg-version-classification-procedure). 

### Installation:
- Unzip PDI,
- copy all *.jar* files from Apache HttpClient to your PDI *lib* folder,
- copy [*inspire-validator.jar*](inspire-validator.jar) to your PDI *lib* folder,
- in [*validation.bat*](validation.bat) insert the path to your PDI *data-integration* folder.

### Configuration:
In [*config.properties*](pdi/config.properties) update the following items:
- `endpoint` - endpoint id, used to create file- and folder- names [use only characters valid for a filename],
- `source_folder` - folder where source metadata are located (may contain subfolders) [use forward slashes "/" in the path],
- `results_folder` - folder where results will be written [use forward slashes "/" in the path],
- `source_suffix` - source metadata files suffix, used to filter the files to validate,
- `validator_nodes` - number of validator instances to use, `validator_url_X` needs to be provided for each instance,
- `validator_url_X` - URLs for each validator instance [*http://.../v2/*],
- `queue_max_size` - maximum number of test runs that can be run in parallel on one validator instance.

### Usage:
Run [*validation.bat*](validation.bat) script, it will read all files with given suffix in the source folder (and subfolders), [classify TG version](#metadata-tg-version-classification-procedure), validate each file using the validator instances and save validation reports and [result files](#result-files) in the results folder. The results can be used to calculate the [conformity indicators](#conformity-indicators).  
When the transformation is run for the same endpoint again, it will continue processing source files that were not processed before, hence are not included in results CSV file. To re-validate an endpoint that was validated before, the CSV file needs to be renamed or moved out of the results folder.  
Alternatively, the procedure can be run from the PDI user interface (Spoon) which provides more control and feedback. For this purpose run *Spoon.bat*, open and run [*validation.kjb*](pdi/validation.kjb) job.

#### Metadata TG version classification procedure:
1. TG version classification (1.3 vs. 2.0) based on the presence of `gmd:useLimitation` element, denoted in column *version_0* in CSV results,
2. validation against the corresponding Conformance Class(es), the corresponding validation reports end with *.html* and *.json* and columns *error_count_0*, *errors_0* in CSV results,
3. if the validation 2. is passed, the MD record is classified as initially determined in 1. and denoted in column *version* in CSV results,
4. if the validation 2. is NOT passed, the validation against the Conformance Class(es) of the other TG is run, the corresponding validation reports end with *.1.html*, *.1.json* and columns *error_count_1*, *errors_1* in CSV results,
5. if the second validation 4. is passed, the MD record is classified as compiled according to the TG tested later in 4. and denoted in column *version* in CSV results,
6. if this second validation is NOT passed, the MD record is classified back as initially determined in 1. and denoted in column *version* in CSV results.

#### Result files:
- *endpoint* - folder where validation reports are saved, the subfolder structure of the source folder is preserved,
- *endpoint.md.json* - source metadata summary,
- *endpoint.csv* - validation results for each metadata record, detailed [below](#results-csv-columns),
- *endpoint.json* - validation results summary,
- *endpoint.services.zip* - validation reports for service metadata records that failed validation,
- *endpoint.dataset.zip* - validation reports for dataset, series, missing and unkown metadata records that failed validation.

#### Results CSV columns:
- `file_id` - identifies corresponding metdata file and validation reports,
- `md_id` - metadata identifier (from source xml),
- `version` - final metadata TG version classification (1.3 vs. 2.0),
- `version_0` - initial metadata TG version classification (1.3 vs. 2.0) based on the presence of `gmd:useLimitation` element, used in the first validation,
- `type` - metadata type (from source xml),
- `result` - final validation result,
- `MD5` - metadata file hash value, used to detect duplicates,
- `error_count_0` - number of failed assertions in the first validation,
- `errors_0` - failed assertions in the first validation,
- `error_count_1` - number of failed assertions in the second validation,
- `errors_1` - failed assertions in the second validation.

#### Conformity Indicators
The metadata Conformity Indicators **MDi1.1** and **MDi1.2** can be calculated by dividing the numbers of passed datasets and services found in validation results summary json by the numbers of available datasets DSi1.1 and services DSi1.2 found in the Harvesting Console, i.e.:
```
MDi1.1 = dataset_pass / DSi1.1
MDi1.2 = service_pass / DSi1.2
```

### Acknowledgments
This software tool was developed with contributions by:
- [Davide Artasensi](https://github.com/dartasensi)
- [Marco Minghini](https://github.com/MarcoMinghini)
- [Fabio Vinci](https://github.com/fabiovin)

### Licence
Copyright 2020 EUROPEAN UNION  
Licensed under the EUPL, Version 1.2 or - as soon as they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence").  
You may not use this work except in compliance with the Licence.  
You may obtain a copy of the Licence at:

https://ec.europa.eu/isa2/solutions/european-union-public-licence-eupl_en

Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the Licence for the specific language governing permissions and limitations under the Licence.

Date: 2020/06/08  
Authors: European Commission, Joint Research Centre - jrc-inspire-support@ec.europa.eu

This work was supported by the [Interoperability solutions for public administrations, businesses and citizens programme](http://ec.europa.eu/isa2)
through Action 2016.10: European Location Interoperability Solutions for e-Government (ELISE)
