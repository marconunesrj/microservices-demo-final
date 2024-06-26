-- SCHEMA: analytics

-- DROP SCHEMA IF EXISTS analytics ;

--CREATE SCHEMA IF NOT EXISTS analytics
--    AUTHORIZATION postgres;

--SET search_path TO pg_catalog,public,analytics;
SET search_path=analytics,pg_catalog,public;

-- Table: analytics.twitter_analytics

-- DROP TABLE IF EXISTS analytics.twitter_analytics;

CREATE TABLE IF NOT EXISTS analytics.twitter_analytics
(
    id uuid NOT NULL,
    word character varying COLLATE pg_catalog."default" NOT NULL,
    word_count bigint NOT NULL,
    record_date time with time zone,
    CONSTRAINT twitter_analytics_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS analytics.twitter_analytics
    OWNER to postgres;
-- Index: INDX_WORD_BY_DATE

-- DROP INDEX IF EXISTS analytics."INDX_WORD_BY_DATE";

CREATE INDEX IF NOT EXISTS "INDX_WORD_BY_DATE"
    ON analytics.twitter_analytics USING btree
    (word COLLATE pg_catalog."default" ASC NULLS LAST, record_date DESC NULLS LAST)
    TABLESPACE pg_default;