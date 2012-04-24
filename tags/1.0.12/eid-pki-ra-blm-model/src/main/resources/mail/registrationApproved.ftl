text/html
===
pki-ra@fedict.be
===
Your registration for ${certificateDomain.name} has been approved
===
<p>Dear ${user.name},</p>

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

<p>
Visit eID PKI RAA at <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>