package com.xceptance.xlt.nocoding.rebuild.parser;

public abstract class ParserTest
{

    protected final String path = "./target/test-classes/test-scripts/";

    protected final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    protected final String fileStaticSubrequests = path + "staticSubrequests.yml";

    protected final String fileComplexTestCase = path + "complexTestCase.yml";

    protected final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    protected final String fileSyntaxErrorAction = path + "syntaxErrorAction.yml";

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    protected final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    protected final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    protected final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    protected final String fileSyntaxErrorActionNameNull = path + "syntaxErrorActionNameNull.yml";

    protected final String fileSyntaxErrorUrlNull = path + "syntaxErrorUrlNull.yml";
}
