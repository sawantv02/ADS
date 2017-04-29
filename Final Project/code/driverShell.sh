#!/bin/bash

    echo "Working on "
    python3 /src/webScrapingCleaning.py mergeData --local-scheduler 
    mv /src/MergedFile.csv /src/Output/

find .

if [ $? -eq 0 ]
then
  echo "Successfully created the files now going to upload them in S3"
  sh /src/awsS3Upload.sh
else
  echo "Could not create file" >&2
fi
