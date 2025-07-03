CREATE TABLE org_form (
                          id serial PRIMARY KEY NOT NULL,
                          name text NOT NULL,
                          is_active boolean NOT NULL DEFAULT TRUE
);
