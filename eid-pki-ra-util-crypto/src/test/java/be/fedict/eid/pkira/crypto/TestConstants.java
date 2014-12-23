/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
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

	public static final String VALID_CSR_NO_ATTRIBUTES = "-----BEGIN CERTIFICATE REQUEST-----\r\n"
			+ "MIIC4zCCAcsCAQAwgZ8xCzAJBgNVBAYTAkJFMRswGQYDVQQKExJGZWRlcmFsIEdvdmVybm1lbnQxGTAXBgNVBAsTEFNTSU49NzMxMjA1MjUzNTAxGjAYBgNVBAsTEUpFUk9FTiBIT1VURVZFTFRTMSEwHwYDVQQLExhlSGVhbHRoLXBsYXRmb3JtIEJlbGdpdW0xGTAXBgNVBAMTEFNTSU49NzMxMjA1MjUzNTAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCyfkOueWwZm0mcA1LTHebd0PamEygsNNwLmePodMdwy8l8CZCMJ4D/PkMQrFCVyahX/wN4QpczR0vwfoZtSxZBmoh8YHQX+nKcYGPN1KK0Hktv2oIPXaCqNOgv1WdDWBbd0FFbJDxlG3pxojJ/0AOKZb+h5ZjrW9F1c1vDZadW3PiaCCVf+nOKRKIR/Evq9ip1ub1vDP6rL+TY8WHeaZYmtQLquH+Skg9OWSSiL/sh+uU6iXWapB12rdNA/71nMtIyVfuZq7YnNy7vf6grXp+fxSIAk0TiFLnMyOY3lr3ZS0werIo+tgjIXVvDCWGh6ZO4g7ke6JFDwFSx/0xAshVhAgMBAAEwDQYJKoZIhvcNAQEFBQADggEBAFN9E/OJ+6tHnbomoCSNKUAVLegWJBsiKCdDd4XjwwN8bku6qhr1irq9Hdmbb2lsu9vPNMolbObBzD1/H20hmYAjPHblYQpr/lKfiiYCSc7yU+rGELzOsytInldzYTYvCgY5e0312rxeFAYv+jmGwmgqYnb7D4EY697TJ85zRhPJ0xcVW79a6G/nc3z/vVdvH4TLR2t5yC4T40la5fHhXW4g6RTVaX5dKrnktwXUfjY4CINH7y/1ayDRwYF7RJKsZjG7PiT5VdPtaO4gnbRIIdiIdi7zqaLk46vIF67K2Mp1inNUXmnT7Y0TJEoxbUhnQfPne4qjIFVgj4oZa6kWo48=\r\n"
			+ "-----END CERTIFICATE REQUEST-----";
	
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

	public static final String INVALID_CSR_WILDCARD_IN_SAN = "-----BEGIN NEW CERTIFICATE REQUEST-----\r\n"
			+ "MIICnDCCAYQCAQAwMDELMAkGA1UEBhMCQkUxDTALBgNVBAoTBFRlc3QxEjAQBgNV\r\n"
			+ "BAMUCSoudGVzdC5iZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALiy\r\n"
			+ "i0FRtU+WtVp6CzGdb9r2/ODkWcr1fVJc2/fbkA2Pee86pi5dPA95WF9PcGdyAke9\r\n"
			+ "rrISNkqch+zEuToP8aU2xhgf5AacHKW3pwEEplYsKnh0A718me4k2YHg6O6i6se8\r\n"
			+ "uZKHkJsqtNbBBGHDnlXH5e6/evQ53NMB4sZ8mrCHGKAhbph7eB1vjzdahUA2iUWZ\r\n"
			+ "BHyGPLW7A39rZoj86aSW63bIuOT292y7HXHeORTBIbkPPSuF8OE0efTm/vftgDjE\r\n"
			+ "YkWzgeZu6Zi3f8od+TTW4Gv2IvkAV2vuqD2lSG/6y7soSpqYnuZK2vHk+MTNJgF1\r\n"
			+ "ilpsB3GMYs9+tyYT4+kCAwEAAaAnMCUGCSqGSIb3DQEJDjEYMBYwFAYDVR0RBA0w\r\n"
			+ "C4IJKi50ZXN0LmJlMA0GCSqGSIb3DQEBBQUAA4IBAQCA6Plgu3TVs2UYubqxrYrL\r\n"
			+ "RFyYlnmiOlz0aTIvr8Pd3dKMTO1kEzq9q40R/rWPEEKwikXbSJiV0pMrUpOQwGZu\r\n"
			+ "31kkcD4TnbBOfRcsLPMuHzRhWIZdtLUIbp9Rpiq/1D0qIs7misks6VwM+M0GR6wW\r\n"
			+ "WRWmeMFQY/BKiBjeML855MJUBSWrBvQrkupTCgHD60jYMT5iI7cJMPaTUW9B6Wkb\r\n"
			+ "B1NVGt+LZlydrJLkIiDOsZLTTRAmFby2QDA+nRulvjkjRAC+1LveJCqJ5Zp0InOv\r\n"
			+ "ta/KT0WS3I+nhki7KFH81aeZSPFOQNH7PbCBjeDsBKs4LaBImkHrqhPhmnEGHkis\r\n"
			+ "-----END NEW CERTIFICATE REQUEST-----";

	public static final String VALID_CSR_WITH_SAN = "-----BEGIN CERTIFICATE REQUEST-----\r\n"
			+ "MIICrTCCAZUCAQAwNTELMAkGA1UEBhMCQkUxDzANBgNVBAoTBkZlZGljdDEVMBMG\r\n"
			+ "A1UEAxMMdGVzdC50ZXN0LmJlMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\r\n"
			+ "AQEAz0eHwFS+91hcVQkmPO1ExDlp1WIQpjuGSfFMQlsnAue/oBEMJl4trRqb9cIc\r\n"
			+ "8l32TYoN+HFi1B3+xTwBIoWOQf8GmpsFyA27nQVj/FH+K0nh3o9RuoBbbIJgbBMF\r\n"
			+ "HVWz8lpoFFgYwsPIVsANTy5xEv0cUQGCHqzYTmRu4aqgNogMHEZjPzIRzksZ0dak\r\n"
			+ "umt9yS4B/Dd28vqzx6dzkh06uzq+33OiWtmuVPlw820kRUNcWLHiuC2Q3bp6ivK2\r\n"
			+ "dZW09AzK4i4h+T8Fpu7whKcNLkPr56fSTOTPaxptrJbo8jBHEC2uPsBrifO5DxVF\r\n"
			+ "XMF8/CaDipfGf5EC1/44V+PJDQIDAQABoDMwMQYJKoZIhvcNAQkOMSQwIjAgBgNV\r\n"
			+ "HREEGTAXggxhbHQxLnRlc3QuYmWCB3Rlc3QuYmUwDQYJKoZIhvcNAQEFBQADggEB\r\n"
			+ "AFx2+OtMHPYRiB/Ka1DD3alrNX7185myMrHwi6eWDePj2b1m+B7/WB8WQ2aFvC7f\r\n"
			+ "UBwfmjVFkZfNtObEp5pD+MApvbUk36v9VI19KWSHyxPOxuj/ms+qWWe/Bqv5rdIB\r\n"
			+ "M+OsdXrMjGO74y1FFUciwVnDxQMeOVMHpXtaGQpvKDE7ya8O7Hcped9Y+aJoGYws\r\n"
			+ "RJZnqxHP+JnCk06897rI1XxN/RxPnIyhPHYTEATV/o0R7O8yfktferWyAiRFKy78\r\n"
			+ "xeO8TBlb4nHj2SBEjcB15Ec/wikMjdpOj0KFxc7FCoIwYmGNxUY9kUYjwCcGcPZ1\r\n" + "7WObH1abpFPS0H9lccj/Qx0=\r\n"
			+ "-----END CERTIFICATE REQUEST-----";

	public static final String VALID_CSR_WITH_MULTIPLE_SAN = "-----BEGIN CERTIFICATE REQUEST-----\n" +
			"MIIC4TCCAckCAQAwTjELMAkGA1UEBhMCQkUxEDAOBgNVBAcMB0hhc3NlbHQxGTAX\n" +
			"BgNVBAsMEEFDQS1JVCBTb2x1dGlvbnMxEjAQBgNVBAMMCWFjYS1pdC5iZTCCASIw\n" +
			"DQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANFbxoS1gjSlZ+ZPRiBCY4z0DDm2\n" +
			"wrrYWeBkbVkgCt+fo7tFrMG3C/K6+1vd4xVVikPN02/4eEEl4WAK7TGdrdSUkgw/\n" +
			"D9FYb51r7i6VeP0v2OhEOVhfQc+Mafl5KnYa5nVGepNC/zaJSCQbD3SLIG5F1shS\n" +
			"6OmRN8iq0oCXnKPeDHfKxzFAcSrCmD0T9k4s5JKOcmn0VkAGtNF0lKi9fJkIwlBR\n" +
			"m+VjGXV8K68Ml8HamhdU+eoBI2qatQT6KhS1OXPwK0XgGbQf5ymJ8nOwYyvH4UBf\n" +
			"YsPt9DtVKbc/XoJoRyiHaOp4frHE3H/+wDXYtSpHnXQ4r2mBptmB+ETeMfkCAwEA\n" +
			"AaBOMEwGCSqGSIb3DQEJDjE/MD0wCQYDVR0TBAIwADALBgNVHQ8EBAMCBeAwIwYD\n" +
			"VR0RBBwwGoINd3d3LmFjYS1pdC5iZYIJYWNhLWl0LmJlMA0GCSqGSIb3DQEBBQUA\n" +
			"A4IBAQAmC4+Wx98a7uSj3wvXiSgF3DL6+CWgAjiCocvrggWr+BXz2jZSi8W9wHpW\n" +
			"OFZ5Yas2FEcLPrWymPQOV5jOZ01NhWSibi/GP8P6llNqlDJdthb8yOcN+UllOUBT\n" +
			"qduXw9qv1+Nvpjbxs0fqNyX6geAxA/U3ImIuIpaoXtmjWIxPz4p0HW7Bae9nr5mw\n" +
			"E48bk9nQu1meS6oUusv0iK3P8TZb5DUPwnT6vANZHJfzPXQlI7AAKiCSdVEdxUXt\n" +
			"2oZakRrCQoAT79cVAYsv0auRqgFXBeMCh2fZSv1+Wx1zVI2Dl/VW65Q1gIg+iD4Y\n" +
			"nDw2Z5+VFcm8ZamlfjBgYxRdO6ul\n" +
			"-----END CERTIFICATE REQUEST-----\n";

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
			+ "4w6S9K8cG5tvySx08dq/rPo8Tj+anYC8j0/YMVdAVPh6s8r1KCTBCKL6YhgHNnhb\r\n" + "dRFMtvQw9DXFiNs=\r\n"
			+ "-----END CERTIFICATE-----";

	public static final String VALID_CERTIFICATE_WITH_SAN = "-----BEGIN CERTIFICATE-----\r\n"
			+ "MIIDETCCAfmgAwIBAgIJAPjpopzCKqV3MA0GCSqGSIb3DQEBBQUAMDUxCzAJBgNV\r\n"
			+ "BAYTAkJFMQ8wDQYDVQQKEwZGZWRpY3QxFTATBgNVBAMTDHRlc3QudGVzdC5iZTAe\r\n"
			+ "Fw0xMTEwMjUxMTUwMjFaFw0xMjEwMjQxMTUwMjFaMDUxCzAJBgNVBAYTAkJFMQ8w\r\n"
			+ "DQYDVQQKEwZGZWRpY3QxFTATBgNVBAMTDHRlc3QudGVzdC5iZTCCASIwDQYJKoZI\r\n"
			+ "hvcNAQEBBQADggEPADCCAQoCggEBAM9Hh8BUvvdYXFUJJjztRMQ5adViEKY7hknx\r\n"
			+ "TEJbJwLnv6ARDCZeLa0am/XCHPJd9k2KDfhxYtQd/sU8ASKFjkH/BpqbBcgNu50F\r\n"
			+ "Y/xR/itJ4d6PUbqAW2yCYGwTBR1Vs/JaaBRYGMLDyFbADU8ucRL9HFEBgh6s2E5k\r\n"
			+ "buGqoDaIDBxGYz8yEc5LGdHWpLprfckuAfw3dvL6s8enc5IdOrs6vt9zolrZrlT5\r\n"
			+ "cPNtJEVDXFix4rgtkN26eorytnWVtPQMyuIuIfk/Babu8ISnDS5D6+en0kzkz2sa\r\n"
			+ "bayW6PIwRxAtrj7Aa4nzuQ8VRVzBfPwmg4qXxn+RAtf+OFfjyQ0CAwEAAaMkMCIw\r\n"
			+ "IAYDVR0RBBkwF4IMYWx0MS50ZXN0LmJlggd0ZXN0LmJlMA0GCSqGSIb3DQEBBQUA\r\n"
			+ "A4IBAQAqYFRM5523rj0s3uvjMIonY7AB51QnA13kDeAnDMEU3rZPVR5mOh//0yRK\r\n"
			+ "u9ej1tNiPJfctmzAa4+4vi0Fjkh/zxq6fEya9gTV5/ImKyIqvasLjAWrO8XaVFaT\r\n"
			+ "fdImQkj8lQLtxx6qLlGQ9AGoZFEYoQYYwcUOEdgW2Fvf+6YwsiS+HJp53icNFe5Z\r\n"
			+ "5tna5SLDL8HTiE/HL2e8BHvkzPTzQ0hEBNHPi0qb5bUzHha3aNisujj0W2ecOKF4\r\n"
			+ "vwURDIRLhrFRJTdGmzptF2PXVuXOD+sLdwMTtTlG6h6vGTnb9fT06IdkK6nLuyDA\r\n"
			+ "/6gk3Nl1n2kUS1WAufRh+b3ckvcf\r\n" + "-----END CERTIFICATE-----\r\n";

	public static final String CSR_SUBJECT = "C=BE,ST=Limburg,L=Hasselt,O=ACA,OU=test,CN=Jan Van den Bergh";

	public static final String CERTIFICATE_DN = "C=BE,OU=Domain Control Validated,O=*.aca-it.be,CN=*.aca-it.be";

	public static final String CERTIFICATE_ISSUER = "C=BE,OU=Domain Validation CA,O=GlobalSign nv-sa,CN=GlobalSign Domain Validation CA";

	public static final Date CERTIFICATE_START_DATE = new Date(1260912858000L);

	public static final Date CERTIFICATE_END_DATE = new Date(1418679256000L);

	public static final BigInteger SERIAL_NUMBER = new BigInteger("1208925819615890090461432");

}
