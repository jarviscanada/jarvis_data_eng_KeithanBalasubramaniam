-- Switch to 'host_agent'

-- Create 'host_info' to store all hardware information
CREATE TABLE IF NOT EXISTS public.host_info
(
    id                  SERIAL NOT NULL,
    hostname            VARCHAR NOT NULL UNIQUE,
    cpu_number          INT NOT NULL,
    cpu_architecture    VARCHAR NOT NULL,
    cpu_model           VARCHAR NOT NULL,
    cpu_mhz             FLOAT NOT NULL,
    L2_cache            INT NOT NULL,
    total_mem           INT NOT NULL,
    timestamp           TIMESTAMP NOT NULL,
    CONSTRAINT host_id PRIMARY KEY (id)
);

-- Create 'host_usage' to store all resource usage information
CREATE TABLE IF NOT EXISTS public.host_usage
(
    "timestamp"         TIMESTAMP NOT NULL,
    host_id             SERIAL NOT NULL,
    memory_free         INT NOT NULL,
    cpu_idle            INT NOT NULL,
    cpu_kernel          INT NOT NULL,
    disk_io             INT NOT NULL,
    disk_available      INT NOT NULL,
    CONSTRAINT host_usage_id FOREIGN KEY (host_id) REFERENCES public.host_info (id)
);