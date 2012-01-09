text/html
===
pki-ra@fedict.be
===
Uw certificaat voor "${certificate.distinguishedName}" zal binnenkort vervallen
===
<p>Beste,</p>

<p>
Het certificaat met het onderwerp "${certificate.distinguishedName}" en het serienummer ${certificate.serialNumber} zal vervallen op ${certificate.validityEnd?string("dd/MM/yyyy")}.
Het werd aangevraagd voor het certificaatdomein "${certificate.certificateDomain.name}".
</p>

<p>
Als u uw certificaat wilt vernieuwen, gebruikt u de eID-toepassing PKI RAA van Fedict op <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>.

<p>
Met vriendelijke groeten,<br/>
Fedict
</p>  
