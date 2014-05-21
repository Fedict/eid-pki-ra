text/html
===
pki-ra@fedict.be
===
Ihr Zertifikat f&uuml;r "${certificate.distinguishedName}" luft ab 
===
<p>Sehr geehrter,</p>

<p>
Das Zertifikat mit dem Betreff "${certificate.distinguishedName}" und der Seriennummer ${certificate.serialNumber} luft am ${certificate.validityEnd?string("dd/MM/yyyy")} ab. 
Es wurde f&uuml;r die Zertifikat-Domain "${certificate.certificateDomain.name}" angefordert.
</p>

<p>
Wenn Sie Ihr Zertifikat erneuern wollen, benutzen Sie bitte die Fedict eID PKI RAA Anwendung auf <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>.

<p>
Mit freundlichen Gr&uuml;&szlig;en<br/>
Fedict.
</p>  