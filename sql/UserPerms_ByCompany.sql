select r.name AS "Role Name", c.NAME as "Company Name", p.KEY, u.EMAIL from JOIN_USER_COMPANY_ROLE ucr
   JOIN biz_role r on r.id = ucr.ROLE_ID
   JOIN join_role_permission rp on rp.ROLE_ID = r.ID
   JOIN biz_permission p on p.id = rp.PERMISSION_ID
   JOIN join_company_member cm on cm.ID = ucr.COMPANY_MEMBER_ID
   JOIN biz_company c on c.ID = cm.COMPANY_ID
   JOIN biz_users u on cm.USER_ID = u.ID
WHERE
    c.NAME = ''
--   and r.NAME = ''
--   and u.EMAIL = ''