import os, shutil, sys
import csv

args = sys.argv
endpoint = args[1]
data_location = args[2]
result_location = args[3]

new_folder = "{0}/retesting-{1}".format(result_location, endpoint)
report_path = args[3]
test_path = "{0}".format(data_location, endpoint)

if os.path.isdir(new_folder):
    shutil.rmtree(new_folder)

os.mkdir(new_folder)


with open('{0}/testout_{1}.csv'.format(result_location, endpoint)) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=';')
    line_count = 0
    for row in csv_reader:
        if not line_count == 0:
            filename = row[0]
            if not filename.split("/").__len__() == 1:
                if not os.path.isdir(new_folder + '/' + filename.split("/")[0]):
                    os.mkdir(new_folder + '/' + filename.split("/")[0])
            shutil.copyfile(test_path + '/' + filename + ".xml", new_folder + '/' + filename + ".xml")

        line_count += 1

