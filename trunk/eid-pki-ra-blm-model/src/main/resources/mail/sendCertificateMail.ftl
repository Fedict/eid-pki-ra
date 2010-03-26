text/html
===
pki-ra@fedict.be
===
Your certificate
===
<p>Dear ${certificate.requesterName}</p>,

Please find attached the certificate you requested earlier.

Certificate details:
Subject: ${certificate.distinguishedName}
Issuer: ${certificate.issuer}
Valid from: ${certificate.validityStart?date}
Valid until: ${certificate.validityEnd?date}

Best regards,

Fedict