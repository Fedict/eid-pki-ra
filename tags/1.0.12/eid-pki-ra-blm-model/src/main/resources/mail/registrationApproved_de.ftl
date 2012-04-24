text/html
===
pki-ra@fedict.be
===
Ihre Registrierung f&uuml;r ${certificateDomain.name} wurde genehmigt 
===
<p>Sehr geehrter ${user.name},</p>

<p>
Sie haben sich k&uuml;rzlich f&uuml;r die Verwendung der Zertifikat-Domain ${certificateDomain.name} angemeldet. 
Diese Anforderung wurde von unseren Administratoren genehmigt. 
Sie k&ouml;nnen diese Domain ab sofort benutzen.
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