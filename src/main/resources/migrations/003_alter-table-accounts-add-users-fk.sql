ALTER TABLE T_FIN_ACCOUNT
    ADD CONSTRAINT T_FIN_ACCOUNT_TBL_FIN_USER_FK FOREIGN KEY
    (
     user_id
    )
    REFERENCES T_FIN_USER
    (
     id
    )
    ON DELETE CASCADE
;