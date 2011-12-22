text/html
===
pki-ra@fedict.be
===
Ihr Zertifikat 
===
<p>Sehr geehrter ${user.name},</p>

<p>
Anbei finden Sie das von Ihnen beantragte Zertifikat.
</p>

<p>Zertifikatdetails:
<ul>
<li><b>Zertifikattyp</b>: ${certificate.certificateType}<br/></li>
<li><b>Betreff</b>: ${certificate.distinguishedName}<br/></li>
<li><b>Aussteller</b>: ${certificate.issuer}<br/></li>
<li><b>G&uuml;ltig von</b>: ${certificate.validityStart?date}<br/></li>
<li><b>G&uuml;ltig bis</b>: ${certificate.validityEnd?date}<br/></li>
</ul>
</p>

<p>
Mit freundlichen Gr&uuml;&szlig;en<br/>
Fedict
</p>

<p>
Besuchen Sie eID PKI RAA auf <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>