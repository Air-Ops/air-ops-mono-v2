-- Given the desire to create a new permission and map it to some role, this script will do that.

-- Phase 1: Make the Permission

DECLARE
    v_key         NVARCHAR2(100) := '';
    v_description NVARCHAR2(100) := '';
    v_domain      NVARCHAR2(100) := '';
BEGIN
    CREATE_PERMISSION(v_key, v_description, v_domain);
END;
/

-- Find Required Information

-- Paste permission key here
select * from BIZ_PERMISSION p where p.KEY = ''; /

-- Paste company name here
select * from BIZ_COMPANY c where c.name = ''; /

-- Paste role name and company id here
select * from BIZ_ROLE r where r.name = 'Company Owner' and r.COMPANY_ID = ''; /

-- Phase 2: Map the Permission
DECLARE
    v_perm_id NVARCHAR2(100) := '';
    v_role_id NVARCHAR2(100) := '';

begin
    MAP_PERM_TO_ROLE(v_perm_id, v_role_id);
end; /

select * from JOIN_ROLE_PERMISSION rp where rp.PERMISSION_ID = '' and rp.ROLE_ID = '';

select p.KEY from JOIN_ROLE_PERMISSION rp
                      join biz_role r on r.id = rp.ROLE_ID
                      join biz_permission p on p.id = rp.PERMISSION_ID
                      join join_user_company_role ucr on ucr.ROLE_ID = ''
where p.DOMAIN = ''
