text/html
===
pki-ra@fedict.be
===
Ihre Registrierung f&uuml;r ${certificateDomain.name} wurde abgelehnt 
===
<p>Sehr geehrter ${user.name},</p>

<p>
Sie haben sich k&uuml;rzlich f&uuml;r die Verwendung der Zertifikat-Domain ${certificateDomain.name} angemeldet. 
Leider wurde diese Anforderung von unseren Administratoren abgelehnt. 
</p>

<#if reason?has_content>
<p>Grund: ${reason}.</p>
</#if>

<p>
Mit freundlichen Gr&uuml;&szlig;en<br/>
Fedict.
</p>  

<p>
Besuchen Sie eID PKI RAA auf <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>