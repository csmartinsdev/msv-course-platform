-- CREATE SEQUENCE platform.users_id_seq;
-- DROP TABLE IF EXISTS platform.users;
CREATE TABLE IF NOT EXISTS platform.users (
    id integer NOT NULL DEFAULT nextval('platform.users_id_seq'::regclass),
    external_id character varying(45) COLLATE pg_catalog."default" NOT NULL DEFAULT gen_random_uuid(),
    owner_id integer,
    cpf character varying(11) NOT NULL,
    name character varying(200) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(200) ,    
	token_email character varying(200),
	token_email_date timestamp without time zone,
	created_date date NOT NULL DEFAULT now(),
	updated_date date,
    active boolean DEFAULT true,
	
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT cpf_unique UNIQUE (cpf),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT external_id_unique UNIQUE (external_id),
    CONSTRAINT fk_owner_user FOREIGN KEY (owner_id)
        REFERENCES platform.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- DROP TABLE IF EXISTS platform.roles;
CREATE TABLE IF NOT EXISTS platform.roles (
    id integer NOT NULL DEFAULT nextval('platform.roles_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(150) COLLATE pg_catalog."default",
    label_pt character varying(300) COLLATE pg_catalog."default",
    label_es character varying(300) COLLATE pg_catalog."default",
    label_en character varying(300) COLLATE pg_catalog."default",
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS platform.privileges;
CREATE TABLE IF NOT EXISTS platform.privileges (
    id integer NOT NULL DEFAULT nextval('platform.privileges_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_privileges PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS platform.users_roles;
CREATE TABLE IF NOT EXISTS platform.users_roles(
    role_id integer NOT NULL,
    user_external_id character varying(45) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (user_external_id, role_id),
    CONSTRAINT fk_role_user FOREIGN KEY (role_id)
        REFERENCES platform.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_user_role FOREIGN KEY (user_external_id)
        REFERENCES platform.users (external_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- DROP TABLE IF EXISTS platform.roles_privileges;
CREATE TABLE IF NOT EXISTS platform.roles_privileges(
    role_id integer NOT NULL,
    privilege_id integer NOT NULL,
	
    CONSTRAINT roles_privileges_pkey PRIMARY KEY (privilege_id, role_id),
    CONSTRAINT fk_privilege_role FOREIGN KEY (privilege_id)
        REFERENCES platform.privileges (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_role_privilege FOREIGN KEY (role_id)
        REFERENCES platform.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE platform.refresh_tokens_id_seq;
-- DROP TABLE IF EXISTS platform.refresh_tokens;
CREATE TABLE IF NOT EXISTS platform.refresh_tokens (
    id integer NOT NULL DEFAULT nextval('platform.refresh_tokens_id_seq'::regclass),
    user_id integer NOT NULL,
    token character varying(100) NOT NULL,
    expiry_date timestamp without time zone,

	CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES platform.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);