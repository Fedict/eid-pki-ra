text/html
===
pki-ra@fedict.be
===
Votre enregistrement pour ${certificateDomain.name} a &eacute;t&eacute; rejet&eacute;
===
<p>Cher, Ch&egrave;re ${user.name},</p>

<p>
Vous vous &ecirc;tes r&eacute;cemment enregistr&eacute;(e) pour utiliser le domaine de certification ${certificateDomain.name}.
Malheureusement, cette demande a &eacute;t&eacute; rejet&eacute;e par nos administrateurs.
</p>

<#if reason?has_content>
<p>Raison: ${reason}.</p>
</#if>

<p>
Avec nos salutations les meilleures,<br/>
Fedict.
</p>  

<p>
Consultez le site eID PKI RAA &agrave; l'adresse <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>
