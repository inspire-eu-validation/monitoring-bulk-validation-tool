import os, sys, csv

args = sys.argv

report_total_location = sys.argv[1]
report_error_location = sys.argv[2]
file_errors = sys.argv[3]
file_total = sys.argv[4]
file_create = './temp.csv'

def create_file():
    with open(file_create, 'w') as create:
        create.write('file_id;md_id;type;result;MD5;error_count;errors')
        

def append_line(array):
    with open(file_create, 'a') as append:
        for line in array:
            text = ''
            for element in line:
                text +=element+";"
            append.write('\n'+text[:-1])

def update_file():
    os.remove(file_total)
    os.rename(file_create, file_total)

def move_reports(folder):
    path_error = report_error_location + '/' + folder
    error_html = path_error + '.html'
    error_json = path_error + '.json'

    path_total = report_total_location + '/' + folder
    total_html = path_total + '.html'
    total_json = path_total + '.json'
    try:
        os.remove(total_html)
    except:
        None

    try:
        os.remove(total_json)
    except:
        None
        
    print('moved ', error_html,' - ', error_json, ' to main report total folder')
    os.rename(error_html, total_html)
    os.rename(error_json, total_json)


create_file()


with open(file_total, 'r') as fileTotal:
    csv_reader_total = csv.reader(fileTotal, delimiter=';')
    next(csv_reader_total, None) 
    array = []
    for total in csv_reader_total:
        with open(file_errors, 'r') as fileErrors:
            csv_reader_errors = csv.reader(fileErrors, delimiter=';')
            next(csv_reader_errors, None) 
            for error in csv_reader_errors:
                if total[0] == error[0]:
                    print(error[3])
                    if not error[3] == "FAILED":
                        total = error
                        move_reports(total[0])
                    elif "3.6" not in error[6]:
                        total = error
                        move_reports(total[0])
            fileErrors.close()
            print(total)
        array.append(total)
    append_line(array)

update_file()

