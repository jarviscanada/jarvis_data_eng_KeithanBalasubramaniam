
-- Group hosts by CPU number and sort by their memory size in descending order
-- within each CPU number group.
SELECT
    cpu_number,
    id,
    total_mem
FROM host_info
ORDER BY total_mem DESC;

-- Function to round the timestamp to a 5 minute interval
CREATE FUNCTION rounder(times timestamp) RETURNS timestamp AS $$
    BEGIN
        RETURN date_trunc('hour', times) + date_part('minute', times):: int/5 * interval '5 min';
    END;
$$ LANGUAGE plpgsql;

-- Find the average used memory in percentage over 5 minute intervals for each host:
-- used memory = total memory - free memory
SELECT
    usages.host_id,
    infos.hostname,
    rounder(usages.timestamp) AS times,
    AVG((infos.total_mem - usages.memory_free)/infos.total_mem*100) AS avg_used_mem
FROM host_info AS infos INNER JOIN host_usage AS usages ON infos.id = usages.host_id
GROUP BY
    usages.host_id,
    infos.hostname,
    rounder(usages.timestamp)
ORDER BY
    usages.host_id ASC,
    timestamp ASC;

-- Detect if a host has failed during a crontab job. Failure is classified if a host
-- fails to insert less than three data points into the host_usage table within a
-- 5 minute interval.
SELECT * FROM
    (
        SELECT host_usage.host_id, rounder(host_usage.timestamp) as times, count(*) as total_data
        FROM host_usage
        GROUP BY times, host_usage.host_id
        ORDER BY times
    ) perfive
WHERE perfive.total_data < 3;