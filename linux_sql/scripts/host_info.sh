#!/bin/bash

# Check Input Argument Parameters
if [ "$#" -ne 5 ];
  then
    echo "Invalid syntax, please use this script with the following correct syntax: "
    echo "./host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]"
    exit 1
fi

# Input Arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_pass=$5

# Function To Pull Hardware Information
lscpu_out=`lscpu`
total_mem_out=`cat /proc/meminfo`

# Retrieving Hardware Information
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model:" | awk '{print $2}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{$3=substr($3,1,length($3)-1); print $3}' | xargs)
total_mem=$(echo "$total_mem_out" | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date '+%Y-%m-%d %T')

# Creating Insert Statement For PSQL Database
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)
VALUES ('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp')"

# Exporting PSQL Password
export PGPASSWORD=$psql_pass

# Calling and Creating PSQL Database
psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$insert_stmt"

exit $?