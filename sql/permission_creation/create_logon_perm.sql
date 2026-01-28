-- Given the desire to create a new permission and map it to some role, this script will do that.

-- Phase 1: Make the Permission

DECLARE
    v_key         NVARCHAR2(100) := 'LOGON';
    v_description NVARCHAR2(100) := 'Log on to the site';
    v_domain      NVARCHAR2(100) := 'SYSTEM';
BEGIN
    CREATE_PERMISSION(v_key, v_description, v_domain);
END;
/

-- Find Required Information

-- Paste permission key here
select * from BIZ_PERMISSION p where p.KEY = 'LOGON'; /

-- Paste company name here
select * from BIZ_COMPANY c where c.name = 'Test Company One'; /

-- Paste role name and company id here
select * from BIZ_ROLE r where r.name = 'Company Owner' and r.COMPANY_ID = '25EC814DFA9448EF9B0E1F45DD982FE3'; /

-- Phase 2: Map the Permission
DECLARE
    v_perm_id NVARCHAR2(100) := '497896860F40AA24E063AD62000AA5C4';
    v_role_id NVARCHAR2(100) := '4725E90DB58B4169B73F2A203FD501A3';

begin
    MAP_PERM_TO_ROLE(v_perm_id, v_role_id);
end; /

select * from JOIN_ROLE_PERMISSION rp where rp.PERMISSION_ID = '497896860F40AA24E063AD62000AA5C4' and rp.ROLE_ID = '4725E90DB58B4169B73F2A203FD501A3';

select p.KEY from JOIN_ROLE_PERMISSION rp
                      join biz_role r on r.id = rp.ROLE_ID
                      join biz_permission p on p.id = rp.PERMISSION_ID
                      join join_user_company_role ucr on ucr.ROLE_ID = '4725E90DB58B4169B73F2A203FD501A3'
where p.DOMAIN = 'SYSTEM';

select r.NAME, p.KEY from JOIN_ROLE_PERMISSION rp
    JOIN biz_role r on r.id = rp.ROLE_ID
    JOIN BIZ_PERMISSION p on p.id = rp.PERMISSION_ID

select r.name, c.NAME, p.KEY, u.EMAIL from JOIN_USER_COMPANY_ROLE ucr
    JOIN biz_role r on r.id = ucr.ROLE_ID
    JOIN join_role_permission rp on rp.ROLE_ID = r.ID
    JOIN biz_permission p on p.id = rp.PERMISSION_ID
    JOIN join_company_member cm on cm.ID = ucr.COMPANY_MEMBER_ID
    JOIN biz_company c on c.ID = cm.COMPANY_ID
    JOIN biz_users u on cm.USER_ID = u.ID
-- Where