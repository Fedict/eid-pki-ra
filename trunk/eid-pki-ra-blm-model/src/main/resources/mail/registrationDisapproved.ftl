text/html
===
pki-ra@fedict.be
===
Your registration for ${certificateDomain.name} has been disapproved
===
<p>Dear ${user.name},</p>

<p>
You recently registered yourself to use the certificate domain ${certificateDomain.name}. 
Unfortunately, this request has been disapproved by our administrators. 
</p>

<#if reason?has_content>
<p>Reason: ${reason}.</p>
</#if>

<p>
Best regards,<br/>
Fedict.
</p>  