package com.xceptance.xlt.nocoding.parser;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.NoCodingScriptItem;

public interface Parser
{
    public List<NoCodingScriptItem> parse();
}
