text/html
===
pki-ra@fedict.be
===
Uw registratie voor ${certificateDomain.name} is goedgekeurd
===
<p>Beste ${user.name},</p>

<p>
U hebt onlangs geregistreerd om het certificaatdomein ${certificateDomain.name} te gebruiken. 
Onze beheerders hebben dit verzoek goedgekeurd. 
U kunt het domein onmiddellijk gebruiken.
</p>

<#if reason?has_content>
<p>Reden: ${reason}.</p>
</#if>

<p>
Met vriendelijke groeten,<br/>
Fedict
</p>

<p>
Bezoek eID PKI RAA op <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>
