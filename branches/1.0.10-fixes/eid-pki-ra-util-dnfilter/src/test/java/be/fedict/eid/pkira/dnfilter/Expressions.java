package be.fedict.eid.pkira.dnfilter;

public class Expressions {

	public static final String[] VALID_EXPRESSIONS = { 
		"C=BE,CN=Test",
		"c=be,ou=*",
		"CN=Test,ou=a,ou=b,C=be",
		"c=be,*ou=*",
		"c=be,ou=abc,ou=*,*ou=*",
		"c=be,ou=\\*",
		"c=be,ou=\\(\\*\\,\\\\=\\)\\|",
		"c=be,mail=test@test.be,a=*", 
		"*ou=*,ou=*",
		"O=Belgian Federal Government, *OU=*, L=*, C=BE, CN=*",
		"*OU=*",
		"c=be,ou=(www.fedict.be|fedict.be)",
		"c=be,ou=(*.fedict.be|fedict.be)",
	};

	public static final String[] NORMALIZED_EXPRESSIONS = { 
		"c=BE,cn=Test",
		"c=be,ou=*",
		"c=be,cn=Test,ou=a,ou=b",
		"c=be,*ou=*",
		"c=be,ou=abc,ou=*,*ou=*",
		"c=be,ou=\\*",
		"c=be,ou=\\(\\*\\,\\\\=\\)\\|",
		"c=be,mail=test@test.be,a=*",  
		"ou=*,*ou=*",
		"c=BE,o=Belgian Federal Government,cn=*,l=*,*ou=*",
		"*ou=*",
		"c=be,ou=(fedict.be|www.fedict.be)",
		"c=be,ou=(fedict.be|*.fedict.be)",
	};
	
	public static String[] INVALID_DNS = {
		"c=be, ou=abc,=ou=def", "c=be*\\", "c=be,ou=\\*", "c=be,ou=\\(\\*\\,\\\\\\=\\)|",  
	};

	public static final String[][] MATCHING_DNS = {
		{ "C=BE,CN=Test", "c=BE,cn=Test", "cn=Test,c=BE",  },
		{ "c=be,ou=abc", "c=be,ou=def", "ou=xyz,c=be",  },
		{ "CN=Test,ou=a,ou=b,C=be", "c=be,ou=b,cn=Test,ou=a",  },
		{ "c=be", "c=be,ou=a", "c=be,ou=a,ou=b",  },
		{ "c=be,ou=abc,ou=def", "ou=abc,ou=def,c=be", "ou=def,c=be,ou=ghi,ou=abc",  },
		{ "c=be,ou=*" },
		{ "c=be,ou=(*\\,\\\\=)|", },
		{ "c=be,mail=test@test.be,a=*", },
		{ "ou=def,ou=abc", "ou=def" },
		{ "O=Belgian Federal Government, OU=def, L=abc, C=BE, CN=qrs" },
		{ "ou=abc", "OU=abc,ou=def"},
		{ "c=be,ou=www.fedict.be", "c=be,ou=fedict.be"},
		{ "c=be,ou=www.fedict.be", "c=be,ou=fedict.be"},
	};
	
	public static final String[][] UNMATCHING_DNS = {
		{ "ou=abc", "c=BE,cn=test",  },
		{ "c=be", "c=be,ou=abc,ou=def", "c=xx,ou=abc",  },
		{ "CN=Test,ou=a,C=be",  },
		{ "c=be,o=b",  },
		{ "c=be", "c=be,ou=def", "c=be,ou=abc",  },
		{ "c=be,ou=abc",  },
		{ "c=be,ou=*",  },
		{ "c=be,mail=test2@test.be,a=*", },
		{ "c=be" },
		{ "c=BE" },
		{ "c=be" },
		{ "c=be,ou=bla.fedict.be"},
		{ "c=be" }
	};
	
	public static final int[] VALID__SIZES =
		{ 2,2,4,2,4,2,2,3,2,5,1,2,2 };

	public static final String[] INVALID_EXPRESSIONS =
		{ "c=b*c", "bla", "null", "x=y,", "c=be,ou=*,ou=**", "c=be,0ou=abc", "x=\\", "c=be,", "c=be\\*\\" };
}