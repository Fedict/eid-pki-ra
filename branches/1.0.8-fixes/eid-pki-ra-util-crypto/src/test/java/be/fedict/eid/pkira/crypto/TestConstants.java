/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.crypto;

import java.math.BigInteger;
import java.util.Date;


/**
 * @author Jan Van den Bergh
 *
 */
public class TestConstants {

	public static final String VALID_CSR = "-----BEGIN NEW CERTIFICATE REQUEST-----\r\n"
	+ "MIICbzCCAi0CAQAwajELMAkGA1UEBhMCQkUxEDAOBgNVBAgTB0xpbWJ1cmcxEDAOBgNVBAcTB0hh\r\n"
	+ "c3NlbHQxDDAKBgNVBAoTA0FDQTENMAsGA1UECxMEdGVzdDEaMBgGA1UEAxMRSmFuIFZhbiBkZW4g\r\n"
	+ "QmVyZ2gwggG4MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YRt1I870QAwx4/\r\n"
	+ "gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZUKWkn5/oBHsQIsJPu6nX/rfG\r\n"
	+ "G/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/yIgMZndFIAccCFQCXYFCPFSMLzLKS\r\n"
	+ "uYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZV4661FlP5nEH\r\n"
	+ "EIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7YnoBJDvMpPG+\r\n"
	+ "qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBhQACgYEA+86jKc18tmTaU44RdbeQIkBi5R4q\r\n"
	+ "KGvWiuoIcoKaQswraNkLzlGLlJbsfIGA+aZbqaZkvNKpRU+7OVwW1FBuPCaXuhDL315XvLQ/kz4/\r\n"
	+ "Ft5x70OccrJqzTxecvUyjwTrhehyxURBZ4e+oCrYp9py3zMmy2qDDWIN1IYTdF+VzxSgADALBgcq\r\n"
	+ "hkjOOAQDBQADLwAwLAIUcQtBbLV6WliL6xr6yFg5IMYMjfsCFB4D9BUGyFYRNvHFms7ySKKdg+Md\r\n"
	+ "-----END NEW CERTIFICATE REQUEST-----";
	
	public static final String INVALID_CSR = "-----BEGIN NEW CERTIFICATE REQUEST-----\r\n"
		+ "MIICbzCCAi0CAQAwajELMAkGA1UEBhMCQkUxEDAOBgNVBAgTB0xpbWJ1cmcxEDAOBgNVBAcTB0hh\r\n"
		+ "c3NlbHQxDDAKBgNVBAoTA0FDQTENMAsGA1UECxMEdGVzdDEaMBgGA1UEAxMRSmFuIFZhbiBkZW4g\r\n"
		+ "QmVyZ2gwggG4MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YRt1I870QAwx4/\r\n"
		+ "gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZUKWkn5/oBHsQIsJPu6nX/rfG\r\n"
		+ "G/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/yIgMZndFIAccCFQCXYFCPFSMLzLKS\r\n"
		+ "uYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZV4661FlP5nEH\r\n"
		+ "EIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7YnoBJDvMpPG+\r\n"
		+ "qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBhQACgYEA+86jKc18tmTaU44RdbeQIkBi5R4q\r\n"
		+ "KGvWiuoIcoKaQswraNkLzlGLlJbsfIGA+aZbqaZkvNKpRU+7OVwW1FBuPCaXuhDL315XvLQ/kz4/\r\n"
		+ "Ft5x70OccrJqzTxecvUyjwTrhehyxURBZ4e+oCrYp9py3zMmy2qDDWIN1IYTdF+VzxSgADALBgcq\r\n"
		+ "hkjOOAQDBQADLwAwLAIUcQtBbLV6WliL6xr6yXg5IMYMjfsCFB4D9BUGyFYRNvHFms7ySKKdg+Md\r\n"
		+ "-----END NEW CERTIFICATE REQUEST-----";
	
	public static final String VALID_CERTIFICATE = "-----BEGIN CERTIFICATE-----\r\n"
	+ "MIIE5zCCA8+gAwIBAgILAQAAAAABJZRviPgwDQYJKoZIhvcNAQEFBQAwcTELMAkG\r\n"
	+ "A1UEBhMCQkUxHTAbBgNVBAsTFERvbWFpbiBWYWxpZGF0aW9uIENBMRkwFwYDVQQK\r\n"
	+ "ExBHbG9iYWxTaWduIG52LXNhMSgwJgYDVQQDEx9HbG9iYWxTaWduIERvbWFpbiBW\r\n"
	+ "YWxpZGF0aW9uIENBMB4XDTA5MTIxNTIxMzQxOFoXDTE0MTIxNTIxMzQxNlowXDEL\r\n"
	+ "MAkGA1UEBhMCQkUxITAfBgNVBAsTGERvbWFpbiBDb250cm9sIFZhbGlkYXRlZDEU\r\n"
	+ "MBIGA1UEChQLKi5hY2EtaXQuYmUxFDASBgNVBAMUCyouYWNhLWl0LmJlMIIBIjAN\r\n"
	+ "BgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmdQ3kASyk7gL21Au0NBN2t/cMcUO\r\n"
	+ "i5RF18eDG/kab1JFMczd2dlfmdhkAjubvnNRAj4nq9XdRrTwen7+bXzFeePhPLIU\r\n"
	+ "UrtiM88MB+QcMJjP85XC+MMXohZZ6oGZ2+3GUiVTNHeWWgzaM7rGi6uN/9KRv0fy\r\n"
	+ "f1IcOUzHeD5ruGZCOPahLDVh+xIagsA5QQtJldScXlAeowwNi4AXQE3QBTFepL4Q\r\n"
	+ "n1OmixlRt3YP+YxK53jcva1DZ1pW+CsCTESLmnZLQmxsW5ujQy37rW8Z0B76zive\r\n"
	+ "YqOEKQfmnqqsmfQ2UoSKCgrmYtfe72kyPiahbKybUCAJmBkOndYRfqNRcwIDAQAB\r\n"
	+ "o4IBkzCCAY8wHwYDVR0jBBgwFoAUNhJOnnHEJkHx+vEpTL8XpFMotuswSQYIKwYB\r\n"
	+ "BQUHAQEEPTA7MDkGCCsGAQUFBzAChi1odHRwOi8vc2VjdXJlLmdsb2JhbHNpZ24u\r\n"
	+ "bmV0L2NhY2VydC9kdmhlMS5jcnQwOQYDVR0fBDIwMDAuoCygKoYoaHR0cDovL2Ny\r\n"
	+ "bC5nbG9iYWxzaWduLm5ldC9Eb21haW5WYWwxLmNybDAdBgNVHQ4EFgQUmtC/xsVt\r\n"
	+ "QvUq5HtzvLa0RO32LmkwCQYDVR0TBAIwADAOBgNVHQ8BAf8EBAMCBPAwKQYDVR0l\r\n"
	+ "BCIwIAYIKwYBBQUHAwEGCCsGAQUFBwMCBgorBgEEAYI3CgMDMEsGA1UdIAREMEIw\r\n"
	+ "QAYJKwYBBAGgMgEKMDMwMQYIKwYBBQUHAgEWJWh0dHA6Ly93d3cuZ2xvYmFsc2ln\r\n"
	+ "bi5uZXQvcmVwb3NpdG9yeS8wEQYJYIZIAYb4QgEBBAQDAgbAMCEGA1UdEQQaMBiC\r\n"
	+ "CyouYWNhLWl0LmJlgglhY2EtaXQuYmUwDQYJKoZIhvcNAQEFBQADggEBADRt1M7D\r\n"
	+ "qkvB+2UcMe6xAT8l8PdCbSSYWiis16JCHzDsFiui0j7nuoERdP2/f72fCMfmhmEv\r\n"
	+ "phcg4Ozm4QYxNYnZtqmxdPhq4p9nvzNhKHzWzgoq981BCCYJfrAjAiYAYD9wwC9c\r\n"
	+ "2gC/19SQVfJtXf3WOqVLfJLioJE5dPacjpMaJwqVoClIuPSUK/nsx/nGp7XupJYe\r\n"
	+ "fkcbzcNcqCP8jWF6b+ZlnEGnfN4LcEWsSZs5qNeL55JNpCyXtS8P48QPV28KS2vS\r\n"
	+ "4w6S9K8cG5tvySx08dq/rPo8Tj+anYC8j0/YMVdAVPh6s8r1KCTBCKL6YhgHNnhb\r\n"
	+ "dRFMtvQw9DXFiNs=\r\n"
	+ "-----END CERTIFICATE-----";
	
	public static final String CSR_SUBJECT = "C=BE,ST=Limburg,L=Hasselt,O=ACA,OU=test,CN=Jan Van den Bergh";
	
	public static final String CERTIFICATE_DN = "C=BE,OU=Domain Control Validated,O=*.aca-it.be,CN=*.aca-it.be";

	public static final String CERTIFICATE_ISSUER = "C=BE,OU=Domain Validation CA,O=GlobalSign nv-sa,CN=GlobalSign Domain Validation CA";

	public static final Date CERTIFICATE_START_DATE = new Date(1260912858000L);
	
	public static final Date CERTIFICATE_END_DATE = new Date(1418679256000L);

	public static final BigInteger SERIAL_NUMBER =  new BigInteger("1208925819615890090461432");

}
