#!/bin/bash

# Check Input Argument Parameters
if [ "$#" -ne 5 ];
  then
    echo "Invalid syntax, please use this script with the following correct syntax: "
    echo "./host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]"
    exit 1
fi

# Input Arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_pass=$5

# Function To Pull Resource Information
vmt=$(vmstat -t)
vmd=$(vmstat -d)

# Retrieve Resource Information
timestamp=$(date "+%Y-%m-%d %T")
hostname=$(hostname -f)
memory_free=$(echo "$(vmstat --unit=M)" |tail -1 | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmt" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmt" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(echo "$vmd" | tail -1 | awk '{print $10}' | xargs)
disk_available=$(echo "$(df -BM ~)" | tail -1 | awk '{$4=substr($4,1,length($4)-1); print $4}' | xargs)

# Create Insert Statement For PSQL Database
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES ('$timestamp', (SELECT id FROM host_info WHERE host_info.hostname = '$hostname'), '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')"

# Exporting PSQL Password
export PGPASSWORD=$psql_pass

# Calling and Creating PSQL Database
psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$insert_stmt"

exit $?