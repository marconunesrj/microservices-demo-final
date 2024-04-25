DROP TABLE IF EXISTS public.users CASCADE;

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default",
    firstname character varying COLLATE pg_catalog."default",
    lastname character varying COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

DROP TABLE IF EXISTS public.documents CASCADE;

CREATE TABLE IF NOT EXISTS public.documents
(
    id uuid NOT NULL,
    document_id character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT documents_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.documents
    OWNER to postgres;

DROP TABLE IF EXISTS public.user_permissions CASCADE;

CREATE TABLE IF NOT EXISTS public.user_permissions
(
    user_id uuid NOT NULL,
    document_id uuid NOT NULL,
    user_permission_id uuid NOT NULL,
    permission_type character varying COLLATE pg_catalog."default",
    CONSTRAINT document_fk FOREIGN KEY (document_id)
        REFERENCES public.documents (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_permissions
    OWNER to postgres;

CREATE INDEX IF NOT EXISTS "fki_USER_FK"
    ON public.user_permissions USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS fki_document_fk
    ON public.user_permissions USING btree
    (document_id ASC NULLS LAST)
    TABLESPACE pg_default;
