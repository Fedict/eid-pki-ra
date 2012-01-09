text/html
===
pki-ra@fedict.be
===
Your certificate
===
<p>Dear ${user.name},</p>

<p>
Please find attached the certificate you requested earlier.
</p>

<p>Certificate details:
<ul>
<li><b>Certificate type</b>: ${certificate.certificateType}<br/></li>
<li><b>Subject</b>: ${certificate.distinguishedName}<br/></li>
<li><b>Issuer</b>: ${certificate.issuer}<br/></li>
<li><b>Valid from</b>: ${certificate.validityStart?date}<br/></li>
<li><b>Valid until</b>: ${certificate.validityEnd?date}<br/></li>
</ul>
</p>

<p>
Best regards,<br/>
Fedict
</p>

<p>
Visit eID PKI RAA at <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>