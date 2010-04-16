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

<p>
Certificate details:<br/>
Subject: ${certificate.distinguishedName}<br/>
Issuer: ${certificate.issuer}<br/>
Valid from: ${certificate.validityStart?date}<br/>
Valid until: ${certificate.validityEnd?date}<br/>
</p>

<p>
Best regards,<br/>
Fedict
</p>