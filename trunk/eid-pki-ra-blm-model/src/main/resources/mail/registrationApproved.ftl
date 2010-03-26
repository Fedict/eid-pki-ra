text/html
===
pki-ra@fedict.be
===
Your registration for ${certificateDomain.name} has been approved
===
<p>Dear ${user.firstName} ${user.lastName},</p>

<p>
You recently registered yourself to use the certificate domain ${certificateDomain.name}. 
This request has been approved by our administrators. 
You can start using this domain immediately.
</p>

<#if reason?has_content>
<p>Reason: ${reason}.</p>
</#if>

<p>
Best regards,<br/>
Fedict.
</p>  