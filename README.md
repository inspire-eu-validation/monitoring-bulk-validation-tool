## Bulk processing tool for the INSPIRE Reference Validator
This software tool allows for validation of large numbers of metadata records using the API of the [INSPIRE Reference Validator](https://inspire.ec.europa.eu/validator/about/). It was developed to support INSPIRE Monitoring & Reporting activities. The tool was built with [Pentaho Data Integration Community Edition](https://community.hitachivantara.com/s/article/data-integration-kettle) platform which is required to use it.

### Prerequisites
- One or more instances of **INSPIRE Reference Validator** [latest release](https://github.com/inspire-eu-validation/community/releases/latest).
- **Pentaho Data Integration** (PDI) **Community Edition** (CE), suggested PDI CE version is [9.0](https://sourceforge.net/projects/pentaho/files/Pentaho%209.0/client-tools/pdi-ce-9.0.0.0-423.zip/download) or 8.2, (8.3 suffers from JSON Input step performance deterioration and is not recommended). In case of slow download click "Problems downloading?" and try an alternative download mirror.
- **Apache HttpClient** components [4.5.12](https://downloads.apache.org/httpcomponents/httpclient/binary/httpcomponents-client-4.5.12-bin.zip).
- Source metadata compiled according to the **INSPIRE Technical Guidelines** (TG) version **2.0** and available as XML files with single metadata record per file.

### Installation
- Unzip PDI,
- copy all *.jar* files from Apache HttpClient to your PDI *lib* folder,
- copy [*inspire-validator.jar*](inspire-validator.jar) to your PDI *lib* folder,
- in [*validation.bat*](validation.bat) insert the path to your PDI *data-integration* folder.

### Configuration
In [*pdi/config.properties*](pdi/config.properties) update the following items:
- `endpoint` - endpoint id, used to create file- and folder- names [use only characters valid for a filename],
- `source_folder` - folder where source metadata are located (including subfolders) [use forward slashes "/" in the path],
- `results_folder` - folder where results will be written [use forward slashes "/" in the path],
- `source_suffix` - source metadata files suffix, used to filter the files to validate,
- `validator_nodes` - number of validator instances to use, `validator_url_X` needs to be provided for each instance,
- `validator_url_X` - URLs for each validator instance, up to "/v2/" [*http://.../v2/*],
- `queue_max_size` - maximum number of test runs that can be run in parallel on each validator instance.

### Usage
Run [*validation.bat*](validation.bat) script, it will perform preprocessing, validation and results generation as described below:
1. Preprocessing:
   - read all files with the given suffix in *\<source_folder\>* (including subfolders) that were not validated before;
   - identify records with missing or unknown type;
   - identify duplicate records using MD5 hash values;
   - create *\<endpoint\>.md.json* metadata summary (after completed preprocessing of all records).
2. Validation:
   - validate each record using the validator instance(s);
   - save validation reports for each record in *\<results_folder\>*/*\<endpoint\>* folder:
     - the subfolder structure of *\<source_folder\>* is preserved, 
	 - filenames correspond to those of source metadata with *\<source_suffix\>* removed, 
	 - each report is saved in two versions: *.html* and *.json*;
   - add results for each record to CSV results *\<endpoint\>.csv*, detailed [below](#results-csv-columns).
3. Results:
   - after completed validation of all source metadata the following result files are generated: *\<endpoint\>.json*, *\<endpoint\>.services.zip* and *\<endpoint\>.dataset.zip*, detailed [below](#result-files);
   - the results can be used to calculate the conformity indicators as detailed [below](#conformity-indicators).

In case the validation does not complete for all source metadata (due to errors, user interruption, etc.), when the transformation is run for the same endpoint again, it will continue processing source metadata that were not processed before, hence are not included in CSV results. To re-validate an endpoint that was validated before, the CSV results file needs to be renamed or moved out of the results folder.  

Alternatively, the procedure can be run from the PDI user interface (Spoon) which provides more control and feedback, and allows for modifications. For this purpose run *Spoon.bat*, open and run [*pdi/validation.kjb*](pdi/validation.kjb) job.

#### Result files
All result files are saved in *\<results_folder\>*:
1. *\<endpoint\>* - folder where validation reports for each metadata record are saved,
2. *\<endpoint\>.md.json* - source metadata summary,
3. *\<endpoint\>.csv* - validation results for each metadata record, detailed [below](#results-csv-columns),
4. *\<endpoint\>.json* - validation results summary and source metadata summary,
5. *\<endpoint\>.services.zip* - validation reports for service metadata records that failed validation,
6. *\<endpoint\>.dataset.zip* - validation reports for dataset, series, missing and unkown metadata records that failed validation.

File 2 is produced only after completed preprocessing of all metadata records.  
Files 4, 5 and 6 are produced only after completed validation of all metadata records.

#### Results CSV columns
- `file_id` - identifies source metadata file and validation reports,
- `md_id` - metadata identifier (from source XML),
- `type` - metadata type (from source XML),
- `result` - validation result,
- `MD5` - metadata file hash value, used to detect duplicates,
- `error_count` - number of failed assertions,
- `errors` - ids of failed assertions.

#### Conformity Indicators
The metadata Conformity Indicators **MDi1.1** and **MDi1.2** can be calculated by dividing the number of passed data sets metadata and the number of passed service metadata found in the validation results summary (JSON file) by, respectively, the total number of available datasets (indicator DSi1.1) and the total number of available services (indicator DSi1.2) retrieved from the Harvest Console (see Article 4 of ID M&R [below](#external-document-references)), i.e.:
```
MDi1.1 = dataset_metadata_passed / DSi1.1
MDi1.2 = service_metadata_passed / DSi1.2
```

### Support
If you experience any issue in the setup and/or use of the software, please open an issue in the [INSPIRE Validator helpdesk](https://github.com/inspire-eu-validation/community/issues/new/choose).

### External document references

| Abbreviation | Document name                       |
| ------------ | ----------------------------------- |
| INSPIRE | [Directive 2007/2/EC of the European Parliament and of the Council of 14 March 2007 establishing an Infrastructure for Spatial Information in the European Community (INSPIRE)](http://eur-lex.europa.eu/legal-content/EN/TXT/PDF/?uri=CELEX:32007L0002&from=EN) |
| ID M&R | [COMMISSION IMPLEMENTING DECISION (EU) 2019/1372 of 19 August 2019 implementing Directive  2007/2/EC of  the  European Parliament and  of  the  Council as  regards monitoring and reporting](https://eur-lex.europa.eu/legal-content/EN/TXT/PDF/?uri=CELEX:32019D1372&from=EN) |

### Acknowledgments
This software tool was developed with contributions by:
- [Lukasz Ziemba](https://github.com/ukiz)
- [Davide Artasensi](https://github.com/dartasensi)
- [Marco Minghini](https://github.com/MarcoMinghini)
- [Fabio Vinci](https://github.com/fabiovin)

This work was supported by the [Interoperability solutions for public administrations, businesses and citizens programme](http://ec.europa.eu/isa2) through Action 2016.10: European Location Interoperability Solutions for e-Government (ELISE).

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
